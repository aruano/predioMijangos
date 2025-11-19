/**
 * @file AuthContext.tsx
 * @description Contexto de autenticación mejorado con refresh automático
 * 
 * Este contexto maneja:
 * - Estado de autenticación del usuario
 * - Login/Logout
 * - Refresh automático de tokens
 * - Restauración de sesión desde localStorage
 * - Sincronización entre pestañas
 * - Verificación de permisos y roles
 * 
 * Mejoras vs versión anterior:
 * - ✅ Refresh automático cuando el token está próximo a expirar
 * - ✅ Mejor tipado con TypeScript
 * - ✅ Separación de concerns (storage, service, context)
 * - ✅ Manejo robusto de errores
 * - ✅ Sincronización 100% con backend
 * - ✅ Sin navegación (responsabilidad de los hooks/componentes)
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.1.0
 */

import React, { 
  createContext, 
  useContext, 
  useState, 
  useEffect, 
  useCallback, 
  useMemo,
  useRef,
} from 'react';
import * as authService from '@/services/endpoints/auth.service';
import { storageService } from '@/utils/storage';
import { JWT_CONFIG, STORAGE_KEYS } from '@/constants/auth.constants';
import type {
  AuthContextValue,
  LoginRequest,
  UsuarioInfo,
  Menu,
} from '@/types/auth.types';

// ============================================
// CONTEXTO
// ============================================

/**
 * Contexto de autenticación
 * Undefined inicialmente, se crea en el Provider
 */
const AuthContext = createContext<AuthContextValue | undefined>(undefined);

// ============================================
// PROVIDER
// ============================================

/**
 * Props del AuthProvider
 */
interface AuthProviderProps {
  children: React.ReactNode;
}

/**
 * Provider del contexto de autenticación
 * 
 * Funcionalidades:
 * - Maneja el estado de autenticación
 * - Restaura sesión desde localStorage
 * - Programa refresh automático de tokens
 * - Sincroniza entre pestañas via storage events
 * - Proporciona acciones y verificadores de permisos
 * 
 * @example
 * ```typescript
 * // En App.tsx o main.tsx
 * import { AuthProvider } from '@/contexts/AuthContext';
 * 
 * function App() {
 *   return (
 *     <AuthProvider>
 *       <Router>
 *         <Routes />
 *       </Router>
 *     </AuthProvider>
 *   );
 * }
 * ```
 */
export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  // ============================================
  // ESTADO
  // ============================================
  
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState<UsuarioInfo | null>(null);
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [refreshToken, setRefreshToken] = useState<string | null>(null);
  const [menu, setMenu] = useState<Menu[] | null>(null);
  
  // Ref para el timer de refresh automático
  const refreshTimerRef = useRef<NodeJS.Timeout | null>(null);
  
  // ============================================
  // HELPERS INTERNOS
  // ============================================
  
  /**
   * Establece el estado de autenticación completo
   */
  const setAuthState = useCallback((
    userData: UsuarioInfo,
    accessTkn: string,
    refreshTkn: string,
    userMenu: Menu[]
  ) => {
    setUser(userData);
    setAccessToken(accessTkn);
    setRefreshToken(refreshTkn);
    setMenu(userMenu);
    setIsAuthenticated(true);
  }, []);
  
  /**
   * Limpia todo el estado de autenticación
   */
  const clearAuthState = useCallback(() => {
    setUser(null);
    setAccessToken(null);
    setRefreshToken(null);
    setMenu(null);
    setIsAuthenticated(false);
  }, []);
  
  /**
   * Programa el refresh automático del token
   * 
   * Se ejecuta automáticamente 5 minutos antes de que expire el token
   */
  const scheduleTokenRefresh = useCallback((token: string) => {
    // Limpiar timer anterior si existe
    if (refreshTimerRef.current) {
      clearTimeout(refreshTimerRef.current);
    }
    
    // Calcular cuándo hacer refresh
    const timeRemaining = authService.getTokenRemainingTime(token);
    const refreshTime = timeRemaining - (JWT_CONFIG.REFRESH_THRESHOLD * 1000);
    
    if (refreshTime > 0) {
      if (import.meta.env.DEV) {
        console.log(
          `[AuthContext] Refresh programado en ${Math.floor(refreshTime / 1000 / 60)} minutos`
        );
      }
      
      refreshTimerRef.current = setTimeout(async () => {
        try {
          await refreshTokenAction();
        } catch (error) {
          console.error('[AuthContext] Error en refresh automático:', error);
          // Si falla el refresh automático, hacer logout
          await logout();
        }
      }, refreshTime);
    }
  }, []);
  
  // ============================================
  // ACCIONES
  // ============================================
  
  /**
   * Iniciar sesión
   * 
   * @throws Error si las credenciales son inválidas
   * 
   * @remarks
   * - La navegación debe ser manejada por el componente/hook que llama esta función
   * - Este método SOLO maneja el estado de autenticación
   */
  const login = useCallback(async (credentials: LoginRequest) => {
    setIsLoading(true);
    
    try {
      const response = await authService.login(credentials);
      
      // Guardar en localStorage
      storageService.setToken(response.accessToken);
      storageService.setRefreshToken(response.refreshToken);
      storageService.setUser(response.usuario);
      
      // Actualizar estado
      setAuthState(
        response.usuario,
        response.accessToken,
        response.refreshToken,
        [] // El menú se carga después si es necesario
      );
      
      // Programar refresh automático
      scheduleTokenRefresh(response.accessToken);
      
      if (import.meta.env.DEV) {
        console.log('[AuthContext] Login exitoso:', response.usuario.username);
      }
      
      // ✅ SIN navegación - el componente/hook decide a dónde ir
    } catch (error) {
      console.error('[AuthContext] Error en login:', error);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [setAuthState, scheduleTokenRefresh]);
  
  /**
   * Cerrar sesión
   * 
   * @remarks
   * - La navegación debe ser manejada por el componente/hook que llama esta función
   * - Este método SOLO limpia el estado de autenticación
   * - Siempre tiene éxito, incluso si el backend falla
   */
  const logout = useCallback(async () => {
    setIsLoading(true);
    
    try {
      const currentRefreshToken = refreshToken || storageService.getRefreshToken();
      
      if (currentRefreshToken) {
        // Intentar invalidar el refresh token en el backend
        await authService.logout({ refreshToken: currentRefreshToken });
      }
      
      if (import.meta.env.DEV) {
        console.log('[AuthContext] Logout exitoso');
      }
    } catch (error) {
      // Log pero no fallar - el logout debe siempre funcionar
      console.warn('[AuthContext] Error al invalidar refresh token:', error);
    } finally {
      // Limpiar timer de refresh
      if (refreshTimerRef.current) {
        clearTimeout(refreshTimerRef.current);
      }
      
      // Limpiar localStorage
      storageService.clearAuth();
      
      // Limpiar estado
      clearAuthState();
      
      setIsLoading(false);
      
      // ✅ SIN navegación - el componente/hook decide a dónde ir
    }
  }, [refreshToken, clearAuthState]);
  
  /**
   * Renovar el access token
   * 
   * @throws Error si no hay refresh token o si el refresh falla
   */
  const refreshTokenAction = useCallback(async () => {
    const currentRefreshToken = refreshToken || storageService.getRefreshToken();
    
    if (!currentRefreshToken) {
      throw new Error('No hay refresh token disponible');
    }
    
    try {
      if (import.meta.env.DEV) {
        console.log('[AuthContext] Renovando access token...');
      }
      
      const response = await authService.refreshToken({
        refreshToken: currentRefreshToken,
      });
      
      // Actualizar tokens en localStorage
      storageService.setToken(response.accessToken);
      storageService.setRefreshToken(response.refreshToken);
      storageService.setUser(response.usuario);
      
      // Actualizar estado
      setAccessToken(response.accessToken);
      setRefreshToken(response.refreshToken);
      setUser(response.usuario);
      
      // Programar próximo refresh
      scheduleTokenRefresh(response.accessToken);
      
      if (import.meta.env.DEV) {
        console.log('[AuthContext] Token renovado exitosamente');
      }
    } catch (error) {
      console.error('[AuthContext] Error al renovar token:', error);
      // Si falla el refresh, hacer logout
      await logout();
      throw error;
    }
  }, [refreshToken, scheduleTokenRefresh, logout]);
  
  // ============================================
  // VERIFICADORES DE PERMISOS
  // ============================================
  
  /**
   * Verifica si el usuario tiene un rol específico
   * 
   * @param role - Nombre del rol a verificar (case-insensitive)
   * @returns true si el usuario tiene el rol
   */
  const hasRole = useCallback((role: string): boolean => {
    if (!user || !user.roles) {
      return false;
    }
    
    return user.roles.some(
      r => r.toUpperCase() === role.toUpperCase()
    );
  }, [user]);
  
  /**
   * Verifica si el usuario tiene un permiso específico
   * 
   * @param permission - Permiso a verificar (case-insensitive)
   * @returns true si el usuario tiene el permiso
   * 
   * @remarks
   * Los permisos vienen en el array de roles del backend
   * Formato: "MODULO:ACCION" (ej: "USUARIOS:CREATE")
   */
  const hasPermission = useCallback((permission: string): boolean => {
    if (!user || !user.roles) {
      return false;
    }
    
    return user.roles.some(
      r => r.toUpperCase() === permission.toUpperCase()
    );
  }, [user]);
  
  /**
   * Verifica si el usuario puede ver una página específica del menú
   * 
   * @param paginaNombre - Nombre de la página (case-insensitive)
   * @returns true si el usuario puede ver la página
   */
  const canSeePage = useCallback((paginaNombre: string): boolean => {
    if (!menu) {
      return false;
    }
    
    return menu.some(m =>
      m.paginas?.some(
        p => p.nombre.toUpperCase() === paginaNombre.toUpperCase()
      )
    );
  }, [menu]);
  
  // ============================================
  // EFECTOS
  // ============================================
  
  /**
   * Efecto: Restaurar sesión desde localStorage al montar
   */
  useEffect(() => {
    const restoreSession = () => {
      const storedToken = storageService.getToken();
      const storedRefreshToken = storageService.getRefreshToken();
      const storedUser = storageService.getUser();
      
      if (storedToken && storedRefreshToken && storedUser) {
        // Verificar si el token sigue válido
        if (!authService.isTokenExpired(storedToken)) {
          setAuthState(
            storedUser,
            storedToken,
            storedRefreshToken,
            [] // El menú se carga después si es necesario
          );
          
          // Programar refresh
          scheduleTokenRefresh(storedToken);
          
          if (import.meta.env.DEV) {
            console.log('[AuthContext] Sesión restaurada:', storedUser.username);
          }
        } else {
          // Token expirado, limpiar
          if (import.meta.env.DEV) {
            console.warn('[AuthContext] Token expirado, limpiando sesión');
          }
          storageService.clearAuth();
        }
      }
      
      setIsLoading(false);
    };
    
    restoreSession();
  }, [setAuthState, scheduleTokenRefresh]);
  
  /**
   * Efecto: Sincronizar entre pestañas via storage events
   */
  useEffect(() => {
    const handleStorageChange = (event: StorageEvent) => {
      // Si el token cambió en otra pestaña
      if (event.key === STORAGE_KEYS.ACCESS_TOKEN) {
        const newToken = event.newValue;
        
        if (newToken) {
          // Token actualizado en otra pestaña
          const storedUser = storageService.getUser();
          const storedRefreshToken = storageService.getRefreshToken();
          
          if (storedUser && storedRefreshToken) {
            setAuthState(storedUser, newToken, storedRefreshToken, []);
            scheduleTokenRefresh(newToken);
          }
        } else {
          // Token eliminado en otra pestaña (logout)
          clearAuthState();
        }
      }
    };
    
    window.addEventListener('storage', handleStorageChange);
    
    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, [setAuthState, clearAuthState, scheduleTokenRefresh]);
  
  /**
   * Efecto: Limpiar timer al desmontar
   */
  useEffect(() => {
    return () => {
      if (refreshTimerRef.current) {
        clearTimeout(refreshTimerRef.current);
      }
    };
  }, []);
  
  // ============================================
  // VALOR DEL CONTEXTO
  // ============================================
  
  const value: AuthContextValue = useMemo(() => ({
    // Estado
    isAuthenticated,
    isLoading,
    user,
    accessToken,
    refreshToken,
    menu,
    
    // Acciones
    login,
    logout,
    refreshToken: refreshTokenAction,
    
    // Verificadores
    hasRole,
    hasPermission,
    canSeePage,
  }), [
    isAuthenticated,
    isLoading,
    user,
    accessToken,
    refreshToken,
    menu,
    login,
    logout,
    refreshTokenAction,
    hasRole,
    hasPermission,
    canSeePage,
  ]);
  
  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// ============================================
// HOOK
// ============================================

/**
 * Hook para acceder al contexto de autenticación
 * 
 * @throws Error si se usa fuera del AuthProvider
 * 
 * @example
 * ```typescript
 * function MyComponent() {
 *   const { user, isAuthenticated, login, logout } = useAuth();
 *   
 *   if (!isAuthenticated) {
 *     return <LoginForm onSubmit={login} />;
 *   }
 *   
 *   return (
 *     <div>
 *       <p>Bienvenido {user?.username}</p>
 *       <button onClick={logout}>Cerrar Sesión</button>
 *     </div>
 *   );
 * }
 * ```
 */
export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);
  
  if (context === undefined) {
    throw new Error('useAuth debe usarse dentro de un AuthProvider');
  }
  
  return context;
}
