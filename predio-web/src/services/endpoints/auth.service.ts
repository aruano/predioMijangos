/**
 * @file auth.service.ts
 * @description Servicio de autenticación para interactuar con la API del backend
 * 
 * Este servicio maneja:
 * - Login de usuario
 * - Renovación de tokens (refresh)
 * - Logout de usuario
 * 
 * IMPORTANTE: No maneja el estado de autenticación (eso es responsabilidad del AuthContext)
 * Solo se encarga de las llamadas HTTP al backend
 * 
 * Endpoints backend:
 * - POST /api/v1/auth/login
 * - POST /api/v1/auth/refresh
 * - POST /api/v1/auth/logout
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

import { apiService } from '@/services/api.service';
import { API_ENDPOINTS } from '@/constants/api.constants';
import type { ApiResponse } from '@/types/api.types';
import type {
  LoginRequest,
  LoginResponse,
  RefreshTokenRequest,
  RefreshTokenResponse,
  LogoutRequest,
} from '@/types/auth.types';

// ============================================
// LOGIN
// ============================================

/**
 * Autentica un usuario con sus credenciales
 * 
 * @param credentials - Username y password del usuario
 * @returns Promise con la respuesta de login (tokens + usuario)
 * 
 * @throws Error si las credenciales son inválidas
 * @throws Error si hay problemas de red
 * 
 * @example
 * ```typescript
 * try {
 *   const response = await login({
 *     username: 'admin@prediomijangos.com',
 *     password: 'Admin123!'
 *   });
 *   
 *   console.log('Access Token:', response.accessToken);
 *   console.log('Usuario:', response.usuario.username);
 *   console.log('Roles:', response.usuario.roles);
 * } catch (error) {
 *   console.error('Error al iniciar sesión:', error);
 * }
 * ```
 */
export async function login(credentials: LoginRequest): Promise<LoginResponse> {
  const response = await apiService.post<ApiResponse<LoginResponse>>(
    API_ENDPOINTS.AUTH_LOGIN,
    credentials
  );
  
  return response.body;
}

// ============================================
// REFRESH TOKEN
// ============================================

/**
 * Renueva el access token usando el refresh token
 * 
 * Backend validará:
 * - Que el refresh token exista en BD
 * - Que no esté expirado (7 días)
 * - Que esté asociado a un usuario válido
 * 
 * @param request - Refresh token a usar
 * @returns Promise con nuevos tokens y datos del usuario
 * 
 * @throws Error si el refresh token es inválido
 * @throws Error si el refresh token expiró
 * @throws Error si hay problemas de red
 * 
 * @example
 * ```typescript
 * try {
 *   const response = await refreshToken({
 *     refreshToken: '550e8400-e29b-41d4-a716-446655440000'
 *   });
 *   
 *   // Actualizar tokens en localStorage
 *   localStorage.setItem('accessToken', response.accessToken);
 *   localStorage.setItem('refreshToken', response.refreshToken);
 * } catch (error) {
 *   // Si falla, hacer logout
 *   console.error('Error al renovar token:', error);
 *   // Redirigir a login
 * }
 * ```
 */
export async function refreshToken(
  request: RefreshTokenRequest
): Promise<RefreshTokenResponse> {
  const response = await apiService.post<ApiResponse<RefreshTokenResponse>>(
    API_ENDPOINTS.AUTH_REFRESH,
    request
  );
  
  return response.body;
}

// ============================================
// LOGOUT
// ============================================

/**
 * Cierra sesión del usuario e invalida el refresh token
 * 
 * Backend eliminará el refresh token de la BD para que no pueda ser usado nuevamente
 * 
 * @param request - Refresh token a invalidar
 * @returns Promise<void>
 * 
 * Nota: Esta función siempre tiene éxito, incluso si el token ya no existe
 * Esto es por diseño para que el logout siempre funcione
 * 
 * @example
 * ```typescript
 * try {
 *   await logout({
 *     refreshToken: '550e8400-e29b-41d4-a716-446655440000'
 *   });
 *   
 *   // Limpiar localStorage
 *   localStorage.clear();
 *   
 *   // Redirigir a login
 *   navigate('/login');
 * } catch (error) {
 *   // Incluso si falla, limpiar sesión local
 *   console.error('Error al hacer logout:', error);
 *   localStorage.clear();
 *   navigate('/login');
 * }
 * ```
 */
export async function logout(request: LogoutRequest): Promise<void> {
  try {
    await apiService.post<ApiResponse<void>>(
      API_ENDPOINTS.AUTH_LOGOUT,
      request
    );
  } catch (error) {
    // Log el error pero no lo propagamos
    // El logout siempre debe tener éxito en el frontend
    // para que el usuario pueda cerrar sesión incluso si hay problemas de red
    console.warn('Error al invalidar refresh token en el backend:', error);
  }
}

// ============================================
// UTILIDADES
// ============================================

/**
 * Decodifica un JWT y extrae el payload
 * 
 * IMPORTANTE: No valida la firma del token, solo lo decodifica
 * La validación de firma la hace el backend
 * 
 * @param token - JWT a decodificar
 * @returns Payload del token
 * 
 * @example
 * ```typescript
 * const payload = decodeToken('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...');
 * console.log('Username:', payload.sub);
 * console.log('Roles:', payload.roles);
 * console.log('Expira en:', new Date(payload.exp * 1000));
 * ```
 */
export function decodeToken(token: string): any {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Error al decodificar token:', error);
    return null;
  }
}

/**
 * Verifica si un token JWT ha expirado
 * 
 * @param token - JWT a verificar
 * @returns true si el token expiró o es inválido
 * 
 * @example
 * ```typescript
 * const token = localStorage.getItem('accessToken');
 * 
 * if (isTokenExpired(token)) {
 *   // Token expiró, intentar refresh
 *   await refreshToken({ refreshToken: localStorage.getItem('refreshToken') });
 * }
 * ```
 */
export function isTokenExpired(token: string | null): boolean {
  if (!token) {
    return true;
  }
  
  const payload = decodeToken(token);
  
  if (!payload || !payload.exp) {
    return true;
  }
  
  // exp viene en segundos, Date.now() en milisegundos
  const expirationTime = payload.exp * 1000;
  const currentTime = Date.now();
  
  return currentTime >= expirationTime;
}

/**
 * Obtiene el tiempo restante hasta que expire el token (en milisegundos)
 * 
 * @param token - JWT a verificar
 * @returns Tiempo restante en milisegundos, 0 si ya expiró o es inválido
 * 
 * @example
 * ```typescript
 * const token = localStorage.getItem('accessToken');
 * const timeLeft = getTokenRemainingTime(token);
 * 
 * console.log(`Token expira en ${timeLeft / 1000} segundos`);
 * 
 * // Programar refresh 5 minutos antes de expirar
 * const refreshTime = timeLeft - (5 * 60 * 1000);
 * setTimeout(() => refreshToken(...), refreshTime);
 * ```
 */
export function getTokenRemainingTime(token: string | null): number {
  if (!token) {
    return 0;
  }
  
  const payload = decodeToken(token);
  
  if (!payload || !payload.exp) {
    return 0;
  }
  
  const expirationTime = payload.exp * 1000;
  const currentTime = Date.now();
  const remaining = expirationTime - currentTime;
  
  return remaining > 0 ? remaining : 0;
}

/**
 * Verifica si un token está próximo a expirar
 * 
 * @param token - JWT a verificar
 * @param thresholdSeconds - Umbral en segundos (default: 300 = 5 minutos)
 * @returns true si el token expira dentro del umbral especificado
 * 
 * @example
 * ```typescript
 * const token = localStorage.getItem('accessToken');
 * 
 * // Verificar si expira en los próximos 5 minutos
 * if (isTokenExpiringSoon(token)) {
 *   // Renovar token proactivamente
 *   await refreshToken({ refreshToken: localStorage.getItem('refreshToken') });
 * }
 * ```
 */
export function isTokenExpiringSoon(
  token: string | null,
  thresholdSeconds: number = 300
): boolean {
  const remainingTime = getTokenRemainingTime(token);
  const thresholdMs = thresholdSeconds * 1000;
  
  return remainingTime > 0 && remainingTime <= thresholdMs;
}
