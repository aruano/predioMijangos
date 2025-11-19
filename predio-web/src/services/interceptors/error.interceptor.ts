/**
 * @file error.interceptor.ts
 * @description Interceptor para manejo centralizado de errores HTTP
 * 
 * Responsabilidades:
 * - Manejo centralizado de todos los errores HTTP
 * - Auto-refresh de JWT cuando el access token expira
 * - Retry automático de requests fallidos
 * - Integración con errorBus para notificaciones
 * - Manejo especial por código de estado (401, 403, 404, 500, etc.)
 * - Logout automático en errores críticos de auth
 * - Logging detallado en desarrollo
 * 
 * Sincronizado con:
 * Backend: GlobalExceptionHandler.java, ErrorResponse.java
 * Constants: error.constants.ts (ERROR_CODES, ERROR_MESSAGES)
 * Types: api.d.ts (ErrorResponse, ValidationError)
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

import type { AxiosError, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import axios from 'axios';
import { errorBus } from '@/services/errorBus';
import { 
  ERROR_CODES, 
  ERROR_MESSAGES, 
  getErrorMessage,
  shouldLogoutOnError,
  isAuthError,
} from '@/constants/error.constants';
import { STORAGE_KEYS } from '@/constants/auth.constants';
import { API_CONFIG } from '@/constants/api.constants';
import type { ErrorResponse, ApiResponse } from '@/types/api.d';
import { clearAuthData, updateAccessToken } from './auth.interceptor';

// ============================================
// INTERFACES
// ============================================

/**
 * Configuración extendida para retry logic
 */
interface RetryConfig extends InternalAxiosRequestConfig {
  _retry?: boolean;
  _retryCount?: number;
}

// ============================================
// CONFIGURACIÓN
// ============================================

/**
 * Máximo número de reintentos para un request
 */
const MAX_RETRY_ATTEMPTS = API_CONFIG.RETRY_ATTEMPTS || 3;

/**
 * Delay entre reintentos (ms)
 */
const RETRY_DELAY = API_CONFIG.RETRY_DELAY || 1000;

// ============================================
// HELPERS
// ============================================

/**
 * Extrae el ErrorResponse del error de Axios
 * 
 * @param error - Error de Axios
 * @returns ErrorResponse o undefined
 */
function extractErrorResponse(error: AxiosError): ErrorResponse | undefined {
  const response = error.response?.data as ApiResponse<ErrorResponse> | undefined;
  return response?.body;
}

/**
 * Obtiene el refresh token del localStorage
 * 
 * @returns Refresh token o null
 */
function getRefreshToken(): string | null {
  try {
    return localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN);
  } catch (error) {
    console.error('[Error Interceptor] Error al leer refresh token:', error);
    return null;
  }
}

/**
 * Intenta refrescar el access token
 * 
 * @returns Promise con el nuevo access token o null si falla
 */
async function refreshAccessToken(): Promise<string | null> {
  const refreshToken = getRefreshToken();
  
  if (!refreshToken) {
    if (import.meta.env.DEV) {
      console.warn('[Error Interceptor] No hay refresh token disponible');
    }
    return null;
  }
  
  try {
    if (import.meta.env.DEV) {
      console.log('[Error Interceptor] Intentando refresh de token...');
    }
    
    const response = await axios.post(
      `${API_CONFIG.BASE_URL}/api/v1/auth/refresh`,
      { refreshToken },
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );
    
    const newAccessToken = response.data.body.accessToken;
    
    if (newAccessToken) {
      updateAccessToken(newAccessToken);
      
      if (import.meta.env.DEV) {
        console.log('[Error Interceptor] ✅ Token refreshed exitosamente');
      }
      
      return newAccessToken;
    }
    
    return null;
  } catch (error) {
    if (import.meta.env.DEV) {
      console.error('[Error Interceptor] ❌ Error al refresh token:', error);
    }
    return null;
  }
}

/**
 * Espera un tiempo antes de reintentar
 * 
 * @param ms - Milisegundos a esperar
 */
function delay(ms: number): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/**
 * Emite el error al errorBus para notificaciones
 * 
 * @param errorResponse - ErrorResponse del backend
 * @param status - Código de estado HTTP
 */
function emitErrorToBus(errorResponse: ErrorResponse | undefined, status: number): void {
  const message = errorResponse 
    ? errorResponse.message 
    : ERROR_MESSAGES[status] || 'Error desconocido';
    
  errorBus.emit('api:error', {
    status,
    message,
    details: errorResponse,
  });
}

/**
 * Maneja el logout del usuario
 */
function handleLogout(): void {
  clearAuthData();
  
  // Evitar loop infinito si ya estamos en login
  if (!window.location.pathname.startsWith('/login')) {
    window.location.href = '/login';
  }
}

// ============================================
// MANEJADORES POR CÓDIGO DE ESTADO
// ============================================

/**
 * Maneja errores 401 Unauthorized
 * 
 * Intenta refresh del token y reintenta el request original
 * 
 * @param error - Error de Axios
 * @returns Promise con respuesta o error
 */
async function handle401Error(error: AxiosError): Promise<AxiosResponse | never> {
  const originalRequest = error.config as RetryConfig;
  
  if (!originalRequest) {
    return Promise.reject(error);
  }
  
  // Si ya se intentó refresh, hacer logout
  if (originalRequest._retry) {
    if (import.meta.env.DEV) {
      console.error('[Error Interceptor] Refresh ya intentado, haciendo logout');
    }
    
    handleLogout();
    return Promise.reject(error);
  }
  
  // Marcar como reintentado
  originalRequest._retry = true;
  
  // Intentar refresh del token
  const newToken = await refreshAccessToken();
  
  if (newToken) {
    // Actualizar header con nuevo token
    originalRequest.headers.Authorization = `Bearer ${newToken}`;
    
    // Reintentar request original
    if (import.meta.env.DEV) {
      console.log('[Error Interceptor] Reintentando request con nuevo token');
    }
    
    return axios(originalRequest);
  }
  
  // Refresh falló, hacer logout
  handleLogout();
  return Promise.reject(error);
}

/**
 * Maneja errores 403 Forbidden
 * 
 * @param error - Error de Axios
 */
function handle403Error(error: AxiosError): void {
  const errorResponse = extractErrorResponse(error);
  
  if (import.meta.env.DEV) {
    console.error('[Error Interceptor] 403 Forbidden:', errorResponse?.message);
  }
  
  emitErrorToBus(errorResponse, 403);
}

/**
 * Maneja errores 404 Not Found
 * 
 * @param error - Error de Axios
 */
function handle404Error(error: AxiosError): void {
  const errorResponse = extractErrorResponse(error);
  
  if (import.meta.env.DEV) {
    console.error('[Error Interceptor] 404 Not Found:', error.config?.url);
  }
  
  emitErrorToBus(errorResponse, 404);
}

/**
 * Maneja errores 422 Unprocessable Entity (Validation)
 * 
 * @param error - Error de Axios
 */
function handle422Error(error: AxiosError): void {
  const errorResponse = extractErrorResponse(error);
  
  if (import.meta.env.DEV) {
    console.error('[Error Interceptor] 422 Validation Error:', errorResponse);
    
    if (errorResponse?.validationErrors) {
      console.table(errorResponse.validationErrors);
    }
  }
  
  // Emitir solo el primer error de validación (para no abrumar al usuario)
  if (errorResponse?.validationErrors && errorResponse.validationErrors.length > 0) {
    const firstError = errorResponse.validationErrors[0];
    errorBus.emit('api:error', {
      status: 422,
      message: `${firstError.field}: ${firstError.message}`,
      details: errorResponse,
    });
  } else {
    emitErrorToBus(errorResponse, 422);
  }
}

/**
 * Maneja errores 500 Internal Server Error
 * 
 * @param error - Error de Axios
 */
function handle500Error(error: AxiosError): void {
  const errorResponse = extractErrorResponse(error);
  
  if (import.meta.env.DEV) {
    console.error('[Error Interceptor] 500 Internal Server Error:', errorResponse);
  }
  
  emitErrorToBus(errorResponse, 500);
}

/**
 * Maneja errores de red (Network Error)
 * 
 * @param error - Error de Axios
 */
function handleNetworkError(error: AxiosError): void {
  if (import.meta.env.DEV) {
    console.error('[Error Interceptor] Network Error:', error.message);
  }
  
  errorBus.emit('api:error', {
    message: 'Error de conexión. Verifique su conexión a internet.',
    details: error,
  });
}

// ============================================
// INTERCEPTOR PRINCIPAL
// ============================================

/**
 * Interceptor de Response para manejo de errores
 * 
 * Flujo:
 * 1. Si es Network Error → notificar
 * 2. Si es 401 → intentar refresh y reintentar
 * 3. Si es 403, 404, 422, 500 → manejar específicamente
 * 4. Si es otro error → manejo genérico
 * 5. Verificar si requiere logout automático
 * 6. Logging en desarrollo
 * 
 * @param error - Error de Axios
 * @returns Promise rechazada o respuesta si se recupera
 * 
 * @example
 * ```typescript
 * // En http.ts o api.service.ts
 * apiClient.interceptors.response.use(
 *   (response) => response,
 *   errorResponseInterceptor
 * );
 * ```
 */
export async function errorResponseInterceptor(
  error: AxiosError
): Promise<AxiosResponse | never> {
  // Error de red (sin respuesta del servidor)
  if (!error.response) {
    handleNetworkError(error);
    return Promise.reject(error);
  }
  
  const status = error.response.status;
  const errorResponse = extractErrorResponse(error);
  const errorCode = errorResponse?.errorCode;
  
  // Logging en desarrollo
  if (import.meta.env.DEV) {
    console.group(`[Error Interceptor] ❌ ${status} - ${error.config?.method?.toUpperCase()} ${error.config?.url}`);
    console.error('Error Code:', errorCode);
    console.error('Message:', errorResponse?.message);
    console.error('Details:', errorResponse);
    console.groupEnd();
  }
  
  // Manejo específico por código de estado
  switch (status) {
    case 401:
      // Unauthorized - intentar refresh
      return handle401Error(error);
      
    case 403:
      // Forbidden - sin permisos
      handle403Error(error);
      break;
      
    case 404:
      // Not Found
      handle404Error(error);
      break;
      
    case 422:
      // Validation Error
      handle422Error(error);
      break;
      
    case 500:
      // Internal Server Error
      handle500Error(error);
      break;
      
    default:
      // Otros errores
      if (status >= 400) {
        emitErrorToBus(errorResponse, status);
      }
  }
  
  // Verificar si se requiere logout automático
  if (errorCode && shouldLogoutOnError(errorCode)) {
    if (import.meta.env.DEV) {
      console.warn(`[Error Interceptor] Error crítico (${errorCode}), haciendo logout`);
    }
    handleLogout();
  }
  
  return Promise.reject(error);
}

// ============================================
// EXPORTACIÓN DEFAULT
// ============================================

/**
 * Objeto con el handler para fácil integración
 * 
 * @example
 * ```typescript
 * import { errorInterceptor } from './interceptors/error.interceptor';
 * 
 * apiClient.interceptors.response.use(
 *   (response) => response,
 *   errorInterceptor.onRejected
 * );
 * ```
 */
export const errorInterceptor = {
  onRejected: errorResponseInterceptor,
};
