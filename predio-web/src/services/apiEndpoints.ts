/**
 * endpoints.ts — ESQUELETO de endpoints REST.
 * - No hace llamadas; solo define rutas (estáticas y dinámicas).
 * - Reutiliza `api.pduno.*` como BASE de cada grupo.
 * - Agrega helpers (funciones) para endpoints con parámetros.
 */
import { api, joinUrl } from './config';

export const API_ENDPOINTS = {
  OAUTH: {
    BASE: api.predio.oauth,
    LOGIN: '/login'
  },
  USERS: {
    BASE: api.predio.users,
  },
  ROLS: {
    BASE: api.predio.rols
  },
  PAGES: {
    BASE: api.predio.pages
  },
  PROVIDERS: {
    BASE: api.predio.providers
  }
} as const;

/** Helper para construir URL final: joinUrl(BASE, PATH) */
export const url = (base: string, path: string) => joinUrl(base, path);
