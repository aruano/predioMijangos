/**
 * @file response.interceptor.ts
 * @description Interceptor para transformación y logging de respuestas exitosas
 * 
 * Responsabilidades:
 * - Logging de respuestas exitosas en desarrollo
 * - Transformación de respuestas si es necesario
 * - Notificaciones de éxito para operaciones específicas
 * - Validación de estructura de respuesta
 * - Estadísticas de performance (opcional)
 * 
 * Sincronizado con:
 * Backend: ApiResponse.java
 * Types: api.d.ts (ApiResponse, PageResponse)
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

import type { AxiosResponse } from 'axios';
import { errorBus } from '@/services/errorBus';
import type { ApiResponse } from '@/types/api.d';
import { isSuccessResponse } from '@/types/api.d';

// ============================================
// CONFIGURACIÓN
// ============================================

/**
 * Métodos HTTP que generan notificación de éxito automática
 */
const SUCCESS_NOTIFICATION_METHODS = ['POST', 'PUT', 'DELETE', 'PATCH'];

/**
 * URLs que NO deben generar notificación de éxito
 * (para evitar spam en operaciones de lectura frecuentes)
 */
const SILENT_URLS = [
  '/api/v1/auth/refresh',
  '/api/v1/auth/validate',
  '/api/v1/auth/session',
];

// ============================================
// HELPERS
// ============================================

/**
 * Verifica si una URL debe generar notificación silenciosa
 * 
 * @param url - URL del request
 * @returns true si debe ser silenciosa
 */
function isSilentUrl(url: string | undefined): boolean {
  if (!url) return false;
  return SILENT_URLS.some(silent => url.includes(silent));
}

/**
 * Verifica si el método HTTP debe generar notificación de éxito
 * 
 * @param method - Método HTTP (GET, POST, PUT, DELETE, etc.)
 * @returns true si debe notificar
 */
function shouldNotifySuccess(method: string | undefined): boolean {
  if (!method) return false;
  return SUCCESS_NOTIFICATION_METHODS.includes(method.toUpperCase());
}

/**
 * Extrae un mensaje de éxito personalizado de la respuesta
 * 
 * @param response - Respuesta de Axios
 * @returns Mensaje personalizado o mensaje por defecto
 */
function extractSuccessMessage(response: AxiosResponse): string {
  const apiResponse = response.data as ApiResponse<any>;
  
  // Si la respuesta tiene un mensaje, usarlo
  if (apiResponse?.message) {
    return apiResponse.message;
  }
  
  // Mensajes por defecto según el método HTTP
  const method = response.config.method?.toUpperCase();
  
  switch (method) {
    case 'POST':
      return 'Registro creado exitosamente';
    case 'PUT':
    case 'PATCH':
      return 'Registro actualizado exitosamente';
    case 'DELETE':
      return 'Registro eliminado exitosamente';
    default:
      return 'Operación realizada exitosamente';
  }
}

/**
 * Valida que la respuesta tenga la estructura correcta de ApiResponse
 * 
 * @param response - Respuesta de Axios
 * @returns true si la estructura es válida
 */
function validateResponseStructure(response: AxiosResponse): boolean {
  const data = response.data as ApiResponse<any>;
  
  // Verificar que tenga los campos mínimos
  return (
    typeof data === 'object' &&
    data !== null &&
    'statusCode' in data &&
    'message' in data &&
    'body' in data
  );
}

/**
 * Calcula el tiempo de respuesta del request
 * 
 * @param response - Respuesta de Axios
 * @returns Tiempo en milisegundos o null
 */
function getResponseTime(response: AxiosResponse): number | null {
  // @ts-ignore - Axios agrega requestStartTime en el config
  const startTime = response.config?.requestStartTime;
  
  if (startTime) {
    return Date.now() - startTime;
  }
  
  return null;
}

// ============================================
// INTERCEPTOR PRINCIPAL
// ============================================

/**
 * Interceptor de Response para respuestas exitosas
 * 
 * Flujo:
 * 1. Validar estructura de ApiResponse
 * 2. Verificar si es respuesta exitosa (2xx)
 * 3. Logging detallado en desarrollo
 * 4. Notificar éxito si corresponde
 * 5. Estadísticas de performance (desarrollo)
 * 6. Retornar respuesta sin modificar
 * 
 * @param response - Respuesta de Axios
 * @returns Respuesta sin modificar
 * 
 * @example
 * ```typescript
 * // En http.ts o api.service.ts
 * apiClient.interceptors.response.use(
 *   responseSuccessInterceptor,
 *   (error) => Promise.reject(error)
 * );
 * ```
 */
export function responseSuccessInterceptor(
  response: AxiosResponse
): AxiosResponse {
  const url = response.config.url;
  const method = response.config.method?.toUpperCase();
  const status = response.status;
  
  // Validar estructura de respuesta
  if (!validateResponseStructure(response)) {
    if (import.meta.env.DEV) {
      console.warn(
        `[Response Interceptor] ⚠️ Respuesta sin estructura ApiResponse: ${method} ${url}`
      );
    }
  }
  
  // Verificar si es respuesta exitosa usando el type guard
  const apiResponse = response.data as ApiResponse<any>;
  const isSuccess = isSuccessResponse(apiResponse);
  
  // Logging en desarrollo
  if (import.meta.env.DEV) {
    const responseTime = getResponseTime(response);
    
    console.group(
      `[Response Interceptor] ✅ ${status} - ${method} ${url}`
    );
    console.log('Status Code:', apiResponse.statusCode);
    console.log('Message:', apiResponse.message);
    console.log('Body:', apiResponse.body);
    
    if (apiResponse.metadata) {
      console.log('Metadata:', apiResponse.metadata);
    }
    
    if (responseTime !== null) {
      console.log(`⏱️ Response Time: ${responseTime}ms`);
      
      // Advertir si el request es lento
      if (responseTime > 2000) {
        console.warn(`⚠️ Request lento (> 2s)`);
      }
    }
    
    console.groupEnd();
  }
  
  // Notificación de éxito (solo para POST, PUT, DELETE, PATCH)
  if (
    isSuccess &&
    shouldNotifySuccess(method) &&
    !isSilentUrl(url)
  ) {
    const message = extractSuccessMessage(response);
    errorBus.emit('api:success', message);
  }
  
  // Retornar respuesta sin modificar
  return response;
}

// ============================================
// INTERCEPTOR DE REQUEST (OPCIONAL)
// ============================================

/**
 * Interceptor de Request para agregar timestamp de inicio
 * (usado para calcular response time)
 * 
 * @param config - Configuración de Axios
 * @returns Configuración modificada
 */
export function requestTimingInterceptor(
  config: any
): any {
  // Agregar timestamp de inicio
  config.requestStartTime = Date.now();
  return config;
}

// ============================================
// EXPORTACIÓN DEFAULT
// ============================================

/**
 * Objeto con ambos handlers para fácil integración
 * 
 * @example
 * ```typescript
 * import { responseInterceptor } from './interceptors/response.interceptor';
 * 
 * // Request interceptor (opcional, para timing)
 * apiClient.interceptors.request.use(
 *   responseInterceptor.onRequest,
 *   (error) => Promise.reject(error)
 * );
 * 
 * // Response interceptor (principal)
 * apiClient.interceptors.response.use(
 *   responseInterceptor.onFulfilled,
 *   (error) => Promise.reject(error)
 * );
 * ```
 */
export const responseInterceptor = {
  onRequest: requestTimingInterceptor,
  onFulfilled: responseSuccessInterceptor,
};

// ============================================
// UTILIDADES ADICIONALES
// ============================================

/**
 * Transforma snake_case a camelCase recursivamente
 * 
 * ⚠️ NOTA: Actualmente NO se usa porque el backend ya envía camelCase
 * Mantener por si en el futuro se necesita transformación
 * 
 * @param obj - Objeto a transformar
 * @returns Objeto transformado
 * 
 * @example
 * ```typescript
 * const snakeData = { user_name: 'John', created_at: '2024-01-01' };
 * const camelData = transformSnakeToCamel(snakeData);
 * // { userName: 'John', createdAt: '2024-01-01' }
 * ```
 */
export function transformSnakeToCamel(obj: any): any {
  if (obj === null || typeof obj !== 'object') {
    return obj;
  }
  
  if (Array.isArray(obj)) {
    return obj.map(item => transformSnakeToCamel(item));
  }
  
  const result: any = {};
  
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      // Convertir snake_case a camelCase
      const camelKey = key.replace(/_([a-z])/g, (_, letter) => letter.toUpperCase());
      result[camelKey] = transformSnakeToCamel(obj[key]);
    }
  }
  
  return result;
}

/**
 * Verifica si una respuesta es paginada
 * 
 * @param response - Respuesta de Axios
 * @returns true si es una respuesta paginada
 * 
 * @example
 * ```typescript
 * if (isPaginatedResponse(response)) {
 *   console.log('Total páginas:', response.data.body.totalPages);
 * }
 * ```
 */
export function isPaginatedResponse(response: AxiosResponse): boolean {
  const body = (response.data as ApiResponse<any>)?.body;
  
  return (
    typeof body === 'object' &&
    body !== null &&
    'content' in body &&
    'page' in body &&
    'size' in body &&
    'totalElements' in body &&
    'totalPages' in body
  );
}
