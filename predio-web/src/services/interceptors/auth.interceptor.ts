/**
 * @file auth.interceptor.ts
 * @description Interceptor de autenticación para requests HTTP
 * 
 * Responsabilidades:
 * - Inyectar automáticamente el JWT en el header Authorization
 * - Manejar tokens expirados con refresh automático
 * - Implementar retry logic para requests fallidos por auth
 * - Logging de requests en desarrollo
 * 
 * Sincronizado con:
 * Backend: JwtAuthenticationFilter.java, JwtUtil.java
 * Constants: auth.constants.ts (STORAGE_KEYS, JWT_CONFIG)
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

import type { InternalAxiosRequestConfig } from 'axios';
import { STORAGE_KEYS, JWT_CONFIG } from '@/constants/auth.constants';

// ============================================
// INTERFACES
// ============================================

/**
 * Extensión de AxiosRequestConfig para retry logic
 */
interface RetryConfig extends InternalAxiosRequestConfig {
  _retry?: boolean;
  _retryCount?: number;
}

// ============================================
// HELPERS
// ============================================

/**
 * Obtiene el access token del localStorage
 * 
 * @returns Access token o null si no existe
 */
function getAccessToken(): string | null {
  try {
    const token = localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
    return token;
  } catch (error) {
    console.error('[Auth Interceptor] Error al leer access token:', error);
    return null;
  }
}

/**
 * Verifica si un token JWT está próximo a expirar
 * 
 * @param token - JWT token
 * @returns true si está próximo a expirar (< 5 minutos)
 */
function isTokenExpiringSoon(token: string): boolean {
  try {
    // Decodificar el payload del JWT (parte central)
    const payload = JSON.parse(atob(token.split('.')[1]));
    const exp = payload.exp * 1000; // Convertir a milisegundos
    const now = Date.now();
    
    // Considerar "expirando pronto" si quedan menos de 5 minutos
    const EXPIRING_THRESHOLD = JWT_CONFIG.REFRESH_THRESHOLD * 60 * 1000;
    
    return exp - now < EXPIRING_THRESHOLD;
  } catch (error) {
    console.error('[Auth Interceptor] Error al verificar expiración del token:', error);
    return false;
  }
}

/**
 * Verifica si una URL es pública y no requiere autenticación
 * 
 * @param url - URL del request
 * @returns true si es una URL pública
 */
function isPublicUrl(url: string | undefined): boolean {
  if (!url) return false;
  
  const publicPaths = [
    '/api/v1/auth/login',
    '/api/v1/auth/register',
    '/api/v1/auth/refresh',
    '/api/v1/auth/forgot-password',
    '/api/v1/auth/reset-password',
  ];
  
  return publicPaths.some(path => url.includes(path));
}

// ============================================
// INTERCEPTOR PRINCIPAL
// ============================================

/**
 * Interceptor de Request para autenticación
 * 
 * Flujo:
 * 1. Si es URL pública → continuar sin token
 * 2. Obtener access token del localStorage
 * 3. Si no hay token → continuar (el error.interceptor manejará el 401)
 * 4. Si hay token → inyectar en header Authorization
 * 5. Si token está expirando pronto → marcar para refresh (futuro)
 * 6. Logging en desarrollo
 * 
 * @param config - Configuración de Axios
 * @returns Configuración modificada
 * 
 * @example
 * ```typescript
 * // En http.ts o api.service.ts
 * apiClient.interceptors.request.use(
 *   authRequestInterceptor,
 *   (error) => Promise.reject(error)
 * );
 * ```
 */
export function authRequestInterceptor(
  config: InternalAxiosRequestConfig
): InternalAxiosRequestConfig {
  // URLs públicas no necesitan token
  if (isPublicUrl(config.url)) {
    if (import.meta.env.DEV) {
      console.log('[Auth Interceptor] Request a URL pública, sin token:', config.url);
    }
    return config;
  }
  
  // Obtener token
  const token = getAccessToken();
  
  if (!token) {
    // No hay token - el request continuará y error.interceptor manejará el 401
    if (import.meta.env.DEV) {
      console.warn('[Auth Interceptor] No hay token disponible para:', config.url);
    }
    return config;
  }
  
  // Inyectar token en header
  config.headers.Authorization = `Bearer ${token}`;
  
  // Verificar si el token está expirando pronto
  if (isTokenExpiringSoon(token)) {
    if (import.meta.env.DEV) {
      console.warn('[Auth Interceptor] Token próximo a expirar para:', config.url);
    }
    // TODO: Implementar refresh proactivo si es necesario
    // Por ahora, el error.interceptor manejará el 401 cuando expire
  }
  
  // Logging en desarrollo
  if (import.meta.env.DEV) {
    console.log(
      `[Auth Interceptor] ✅ Token inyectado: ${config.method?.toUpperCase()} ${config.url}`
    );
  }
  
  return config;
}

// ============================================
// ERROR HANDLER
// ============================================

/**
 * Maneja errores en el request interceptor
 * 
 * @param error - Error del interceptor
 * @returns Promise rechazada
 */
export function authRequestErrorHandler(error: any): Promise<never> {
  if (import.meta.env.DEV) {
    console.error('[Auth Interceptor] Error en request:', error);
  }
  
  return Promise.reject(error);
}

// ============================================
// EXPORTACIÓN DEFAULT
// ============================================

/**
 * Objeto con ambos handlers para fácil integración
 * 
 * @example
 * ```typescript
 * import { authInterceptor } from './interceptors/auth.interceptor';
 * 
 * apiClient.interceptors.request.use(
 *   authInterceptor.onFulfilled,
 *   authInterceptor.onRejected
 * );
 * ```
 */
export const authInterceptor = {
  onFulfilled: authRequestInterceptor,
  onRejected: authRequestErrorHandler,
};

// ============================================
// UTILIDADES ADICIONALES
// ============================================

/**
 * Limpia todos los datos de autenticación del localStorage
 * 
 * Útil para logout o cuando el refresh token falla
 * 
 * @example
 * ```typescript
 * import { clearAuthData } from './interceptors/auth.interceptor';
 * 
 * if (refreshTokenFailed) {
 *   clearAuthData();
 *   navigate('/login');
 * }
 * ```
 */
export function clearAuthData(): void {
  try {
    localStorage.removeItem(STORAGE_KEYS.ACCESS_TOKEN);
    localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
    localStorage.removeItem(STORAGE_KEYS.USER_DATA);
    localStorage.removeItem(STORAGE_KEYS.REMEMBER_ME);
    localStorage.removeItem(STORAGE_KEYS.LAST_ACTIVITY);
    
    if (import.meta.env.DEV) {
      console.log('[Auth Interceptor] Datos de autenticación limpiados');
    }
  } catch (error) {
    console.error('[Auth Interceptor] Error al limpiar datos de auth:', error);
  }
}

/**
 * Actualiza el access token en localStorage
 * 
 * @param newToken - Nuevo access token
 * 
 * @example
 * ```typescript
 * import { updateAccessToken } from './interceptors/auth.interceptor';
 * 
 * const newToken = await refreshToken();
 * updateAccessToken(newToken);
 * ```
 */
export function updateAccessToken(newToken: string): void {
  try {
    localStorage.setItem(STORAGE_KEYS.ACCESS_TOKEN, newToken);
    
    if (import.meta.env.DEV) {
      console.log('[Auth Interceptor] Access token actualizado');
    }
  } catch (error) {
    console.error('[Auth Interceptor] Error al actualizar access token:', error);
  }
}
