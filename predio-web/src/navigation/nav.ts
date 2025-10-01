/**
 * nav — Controlador global de navegación estilo "navigation.navigate".
 * - Funciona DENTRO y FUERA de componentes (servicios, interceptores, etc.).
 * - Si aún no está enlazado el `navigate` de React Router, hace fallback a location.assign.
 */
import type { NavigateFunction } from 'react-router-dom';
import { ROUTES } from './routes';

let navigateRef: NavigateFunction | null = null;

export function setNavigator(navigate: NavigateFunction) {
  navigateRef = navigate;
}

/** Navega de forma segura (fallback si still no hay navigator). */
function safeNavigate(
  to: string | number,
  opts?: { replace?: boolean; state?: any }
) {
  if (typeof to === 'number') {
    if (navigateRef) (navigateRef as any)(to);
    else window.history.go(to);
    return;
  }
  if (navigateRef) navigateRef(to, { replace: !!opts?.replace, state: opts?.state });
  else window.location.assign(to);
}

export const nav = {
  /** Navegación imperativa */
  push: (to: string, state?: any) => safeNavigate(to, { replace: false, state }),
  replace: (to: string, state?: any) => safeNavigate(to, { replace: true, state }),
  back: () => safeNavigate(-1),

  /** Atajos semánticos */
  toLogin: () => safeNavigate(ROUTES.LOGIN, { replace: true }),
  toAppRoot: () => safeNavigate(ROUTES.APP, { replace: true }),
  toHome: () => safeNavigate(ROUTES.HOME, { replace: true }),
  toRols: () => safeNavigate(ROUTES.ROLS, { replace: true }),
  toUsers: () => safeNavigate(ROUTES.USERS, { replace: true }),
  toProviders: () => safeNavigate(ROUTES.PROVIDERS, { replace: true }),

  /** Helpers por dominio (ejemplos) */
  toProduct: (id: string | number) => safeNavigate(ROUTES.PRODUCT_BY_ID(id)),
};
