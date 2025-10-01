/**
 * config.ts — fuentes/base de APIs por aplicación o microservicio.
 * - Lee variables de entorno y expone un objeto `api` con cada base.
 * - `pduno` es un alias para tu backend actual (ajústalo si usas más orígenes).
 */
export const ORIGINS = {
  predio: import.meta.env.VITE_API_PDUNO ?? import.meta.env.VITE_API_URL ?? 'http://localhost:8080',
} as const;

/** Helper para unir paths con seguridad (evita dobles/faltantes “/”) */
export function joinUrl(base: string, path: string): string {
  return `${base.replace(/\/+$/, '')}/${path.replace(/^\/+/, '')}`;
}

/** Raíces de cada recurso de pduno (ajusta estos paths a tu backend real) */
export const api = {
  predio: {
    oauth: joinUrl(ORIGINS.predio, '/api/auth'),
    users: joinUrl(ORIGINS.predio, '/api/users'),
    rols: joinUrl(ORIGINS.predio, '/api/rols'),
    pages: joinUrl(ORIGINS.predio, '/api/pages'),
    providers: joinUrl(ORIGINS.predio, '/api/providers'),
    operations: joinUrl(ORIGINS.predio, '/api/operations'),
    terminals: joinUrl(ORIGINS.predio, '/api/terminals'),
    accounts: joinUrl(ORIGINS.predio, '/api/accounts'),
    report: joinUrl(ORIGINS.predio, '/api/report'),
    payments: joinUrl(ORIGINS.predio, '/api/payments'),
  },
} as const;
