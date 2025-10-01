import React, { createContext, useCallback, useContext, useEffect, useMemo, useRef, useState } from "react";
import http from "@/services/http";
import { loginApi } from "@/services/endpoints";
import type { LoginResponse as AuthSession, LoginRequest, Menu, Pagina } from "@/types";

type AuthContextValue = {
  isAuthenticated: boolean;
  loading: boolean;
  user: string | null;
  roles: string[];
  token: string | null;
  menu: Menu[];
  webMenu: Menu[];
  /** Helpers */
  hasRole: (roleName: string) => boolean;
  canSeePage: (paginaNombre: string) => boolean;
  /** Acciones */
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextValue | undefined>(undefined);
const LS_KEY = "pm_auth";

/** Decodifica JWT (sin librerías) para leer 'exp' */
function parseJwtExp(token: string): number | undefined {
  try {
    const base = token.split(".")[1];
    const json = JSON.parse(atob(base.replace(/-/g, "+").replace(/_/g, "/")));
    return typeof json?.exp === "number" ? json.exp : undefined; // epoch seconds
  } catch {
    return undefined;
  }
}

/** Guarda/carga sesión en localStorage */
function saveSession(session: AuthSession) {
  localStorage.setItem(LS_KEY, JSON.stringify(session));
}
function loadSession(): AuthSession | null {
  const raw = localStorage.getItem(LS_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as AuthSession;
  } catch {
    localStorage.removeItem(LS_KEY);
    return null;
  }
}

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState<string | null>(null);
  const [user, setUser] = useState<string | null>(null);
  const [roles, setRoles] = useState<string[]>([]);
  const [menu, setMenu] = useState<Menu[]>([]);
  const logoutTimerRef = useRef<number | null>(null);

  /** Programa auto-logout según exp */
  const scheduleAutoLogout = useCallback((exp?: number) => {
    if (logoutTimerRef.current) {
      window.clearTimeout(logoutTimerRef.current);
      logoutTimerRef.current = null;
    }
    if (!exp) return;
    const ms = exp * 1000 - Date.now();
    if (ms > 0) {
      logoutTimerRef.current = window.setTimeout(() => {
        logout();
      }, ms);
    }
  }, []);

  /** Establece auth en memoria + headers + storage */
  const setAuth = useCallback((session: AuthSession) => {
    setToken(session.token);
    setUser(session.usuario);
    setRoles(session.roles || []);
    setMenu(session.menu || []);
    // Header Authorization inmediato
    http.defaults.headers.common["Authorization"] = `Bearer ${session.token}`;
    // Persistencia
    saveSession(session);
    // Auto-logout
    scheduleAutoLogout(session.exp);
  }, [scheduleAutoLogout]);

  const clearAuth = useCallback(() => {
    setToken(null);
    setUser(null);
    setRoles([]);
    setMenu([]);
    delete http.defaults.headers.common["Authorization"];
    localStorage.removeItem(LS_KEY);
    if (logoutTimerRef.current) {
      window.clearTimeout(logoutTimerRef.current);
      logoutTimerRef.current = null;
    }
  }, []);

  const login = useCallback(async (credentials: LoginRequest) => {
    setLoading(true);
    try {
      const res = await loginApi(credentials);
      const exp = parseJwtExp(res.token);
      const session: AuthSession = {
        token: res.token,
        usuario: res.usuario,
        roles: res.roles ?? [],
        menu: res.menu ?? [],
        admin: res.admin ?? false,
        exp,
      };
      setAuth(session);
    } finally {
      setLoading(false);
    }
  }, [setAuth]);

  const logout = useCallback(() => {
    clearAuth();
  }, [clearAuth]);

  /** Restaurar sesión al cargar la app */
  useEffect(() => {
    const stored = loadSession();
    if (stored?.token) {
      const stillValid = !stored.exp || stored.exp * 1000 > Date.now();
      if (stillValid) {
        http.defaults.headers.common["Authorization"] = `Bearer ${stored.token}`;
        setAuth(stored);
        setLoading(false);
        return;
      }
    }
    clearAuth();
    setLoading(false);
  }, [setAuth, clearAuth]);

  /** Sincronizar logout/login entre pestañas */
  useEffect(() => {
    const onStorage = (e: StorageEvent) => {
      if (e.key === LS_KEY) {
        const next = loadSession();
        if (!next?.token) {
          clearAuth();
        } else {
          setAuth(next);
        }
      }
    };
    window.addEventListener("storage", onStorage);
    return () => window.removeEventListener("storage", onStorage);
  }, [setAuth, clearAuth]);

  const hasRole = useCallback(
    (roleName: string) =>
      roles?.some(r => r.toUpperCase() === roleName.toUpperCase()),
    [roles]
  );

  const canSeePage = useCallback(
    (paginaNombre: string) =>
      menu?.some(m => m.paginas?.some(p => p.nombre?.toUpperCase() === paginaNombre.toUpperCase())),
    [menu]
  );

  const webMenu = useMemo<Menu[]>(
    () =>
      (menu || []).map(m => ({
        ...m,
        paginas: (m.paginas || []).filter((p: Pagina) => p.movil === false),
      })),
    [menu]
  );

  const value: AuthContextValue = {
    isAuthenticated: !!token,
    loading,
    user,
    roles,
    token,
    menu,
    webMenu,
    hasRole,
    canSeePage,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth debe usarse dentro de <AuthProvider>");
  return ctx;
}
