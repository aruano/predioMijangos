/**
 * @file api.d.ts
 * @description Tipos base para comunicación con API
 * 
 * Este archivo define las estructuras de datos que el backend usa para
 * envolver todas sus respuestas. Debe mantenerse 100% sincronizado con:
 * 
 * Backend:
 * - src/main/java/com/predio/mijangos/core/response/ApiResponse.java
 * - src/main/java/com/predio/mijangos/core/response/PageResponse.java
 * - src/main/java/com/predio/mijangos/core/response/ErrorResponse.java
 * 
 * CRÍTICO: Cualquier cambio en las clases Java debe reflejarse aquí
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

// ============================================
// RESPUESTAS DE API
// ============================================

/**
 * Estructura estándar para TODAS las respuestas del backend
 * 
 * El backend envuelve todos los datos en esta estructura para proporcionar
 * metadatos consistentes sobre la respuesta.
 * 
 * @template T - Tipo de dato contenido en el body
 * 
 * @property statusCode - Código de estado HTTP (200, 201, 400, 404, 500, etc.)
 * @property message - Mensaje descriptivo de la respuesta
 * @property body - Datos de la respuesta (puede ser un objeto, array, PageResponse, etc.)
 * @property metadata - Metadatos adicionales (opcional, usado para paginación, filtros, etc.)
 * 
 * @example
 * ```typescript
 * // Respuesta exitosa con datos
 * const response: ApiResponse<User> = {
 *   statusCode: 200,
 *   message: "Usuario obtenido exitosamente",
 *   body: { id: 1, nombre: "Juan", ... }
 * };
 * 
 * // Respuesta exitosa con lista paginada
 * const response: ApiResponse<PageResponse<User>> = {
 *   statusCode: 200,
 *   message: "Usuarios obtenidos exitosamente",
 *   body: {
 *     content: [...],
 *     page: 0,
 *     size: 10,
 *     totalElements: 45,
 *     totalPages: 5,
 *     last: false
 *   }
 * };
 * 
 * // Respuesta de error
 * const response: ApiResponse<ErrorResponse> = {
 *   statusCode: 404,
 *   message: "Usuario no encontrado",
 *   body: {
 *     errorCode: "USER_001",
 *     message: "Usuario con ID 123 no encontrado",
 *     status: 404,
 *     timestamp: "2025-10-30T10:30:00",
 *     validationErrors: []
 *   }
 * };
 * ```
 * 
 * Backend Java equivalente:
 * ```java
 * public class ApiResponse<T> {
 *     private Integer statusCode;
 *     private String message;
 *     private T body;
 *     private Map<String, Object> metadata;
 * }
 * ```
 */
export interface ApiResponse<T> {
  /**
   * Código de estado HTTP
   * 
   * Códigos comunes:
   * - 200: OK - Operación exitosa
   * - 201: CREATED - Recurso creado exitosamente
   * - 204: NO CONTENT - Operación exitosa sin contenido
   * - 400: BAD REQUEST - Error de validación o datos inválidos
   * - 401: UNAUTHORIZED - No autenticado
   * - 403: FORBIDDEN - No autorizado (sin permisos)
   * - 404: NOT FOUND - Recurso no encontrado
   * - 409: CONFLICT - Conflicto (ej: recurso ya existe)
   * - 500: INTERNAL SERVER ERROR - Error interno del servidor
   */
  statusCode: number;
  
  /**
   * Mensaje descriptivo de la respuesta
   * 
   * En respuestas exitosas: mensaje de confirmación
   * En respuestas de error: mensaje general del error
   */
  message: string;
  
  /**
   * Datos de la respuesta
   * 
   * Puede ser:
   * - Un objeto simple (ej: Usuario, Rol)
   * - Una lista (ej: Usuario[])
   * - Un PageResponse<T> para listas paginadas
   * - Un ErrorResponse en caso de error
   * - void/null para respuestas sin contenido (204)
   */
  body: T;
  
  /**
   * Metadatos adicionales opcionales
   * 
   * Usado para información suplementaria como:
   * - Filtros aplicados
   * - Estadísticas de la consulta
   * - Información de paginación adicional
   * - Timestamps de procesamiento
   */
  metadata?: Record<string, any>;
}

// ============================================
// PAGINACIÓN
// ============================================

/**
 * Estructura para respuestas paginadas
 * 
 * El backend usa esta estructura para todas las consultas que retornan
 * listas paginadas de datos.
 * 
 * @template T - Tipo de los elementos en el array content
 * 
 * @property content - Array de elementos de la página actual
 * @property page - Número de página actual (0-indexed)
 * @property size - Cantidad de elementos por página
 * @property totalElements - Total de elementos en todas las páginas
 * @property totalPages - Total de páginas disponibles
 * @property last - Indica si es la última página
 * 
 * @example
 * ```typescript
 * // Respuesta paginada de usuarios
 * const pageResponse: PageResponse<User> = {
 *   content: [
 *     { id: 1, nombre: "Juan" },
 *     { id: 2, nombre: "María" }
 *   ],
 *   page: 0,           // Primera página
 *   size: 10,          // 10 elementos por página
 *   totalElements: 25, // 25 usuarios en total
 *   totalPages: 3,     // 3 páginas en total
 *   last: false        // No es la última página
 * };
 * 
 * // Uso en servicio
 * const response = await api.get<ApiResponse<PageResponse<User>>>(
 *   '/api/v1/usuarios',
 *   { params: { page: 0, size: 10 } }
 * );
 * const users = response.body.content;
 * const hasNextPage = !response.body.last;
 * ```
 * 
 * Backend Java equivalente:
 * ```java
 * public class PageResponse<T> {
 *     private List<T> content;
 *     private Integer currentPage;  // En frontend: page
 *     private Integer pageSize;     // En frontend: size
 *     private Long totalElements;
 *     private Integer totalPages;
 *     private Boolean last;
 *     private Boolean first;
 *     private Boolean empty;
 * }
 * ```
 * 
 * NOTA: El backend usa `currentPage` y `pageSize`, pero en frontend
 * usamos `page` y `size` por convención de JavaScript/TypeScript
 */
export interface PageResponse<T> {
  /**
   * Array de elementos de la página actual
   */
  content: T[];
  
  /**
   * Número de página actual (0-indexed)
   * 
   * - 0: Primera página
   * - 1: Segunda página
   * - etc.
   */
  page: number;
  
  /**
   * Cantidad de elementos por página
   * 
   * Valores típicos: 10, 20, 50, 100
   */
  size: number;
  
  /**
   * Total de elementos en todas las páginas
   * 
   * Útil para mostrar: "Mostrando 1-10 de 45 resultados"
   */
  totalElements: number;
  
  /**
   * Total de páginas disponibles
   * 
   * Calculado como: ceil(totalElements / size)
   */
  totalPages: number;
  
  /**
   * Indica si es la última página
   * 
   * - true: No hay más páginas después de esta
   * - false: Hay más páginas disponibles
   * 
   * Útil para: deshabilitar botón "Siguiente"
   */
  last: boolean;
}

// ============================================
// MANEJO DE ERRORES
// ============================================

/**
 * Estructura detallada para respuestas de error
 * 
 * El backend usa esta estructura cuando ocurre un error para proporcionar
 * información detallada sobre qué salió mal.
 * 
 * @property errorCode - Código interno del error (ej: USER_001, AUTH_002)
 * @property message - Mensaje descriptivo del error
 * @property status - Código de estado HTTP (400, 404, 500, etc.)
 * @property timestamp - Marca de tiempo ISO 8601 de cuando ocurrió el error
 * @property validationErrors - Lista de errores de validación por campo (opcional)
 * 
 * @example
 * ```typescript
 * // Error simple (404 Not Found)
 * const errorResponse: ErrorResponse = {
 *   errorCode: "USER_001",
 *   message: "Usuario no encontrado",
 *   status: 404,
 *   timestamp: "2025-10-30T10:30:00"
 * };
 * 
 * // Error con validaciones (400 Bad Request)
 * const errorResponse: ErrorResponse = {
 *   errorCode: "VAL_001",
 *   message: "Errores de validación",
 *   status: 400,
 *   timestamp: "2025-10-30T10:30:00",
 *   validationErrors: [
 *     {
 *       field: "email",
 *       message: "El formato del email no es válido",
 *       rejectedValue: "invalido@"
 *     },
 *     {
 *       field: "password",
 *       message: "La contraseña debe tener al menos 8 caracteres",
 *       rejectedValue: "123"
 *     }
 *   ]
 * };
 * 
 * // Manejo en interceptor
 * if (response.statusCode >= 400) {
 *   const error = response.body as ErrorResponse;
 *   console.error(`[${error.errorCode}] ${error.message}`);
 *   
 *   if (error.validationErrors) {
 *     error.validationErrors.forEach(ve => {
 *       console.error(`  ${ve.field}: ${ve.message}`);
 *     });
 *   }
 * }
 * ```
 * 
 * Backend Java equivalente:
 * ```java
 * public class ErrorResponse {
 *     private String errorCode;
 *     private String message;
 *     private Integer status;
 *     private String timestamp;
 *     private List<ValidationError> validationErrors;
 *     private String path;
 * }
 * ```
 */
export interface ErrorResponse {
  /**
   * Código interno del error
   * 
   * Definidos en: @/constants/error.constants.ts
   * Sincronizados con: ErrorCodes.java del backend
   * 
   * Ejemplos:
   * - AUTH_001: Credenciales inválidas
   * - USER_001: Usuario no encontrado
   * - VAL_001: Error de validación
   * - SYS_001: Error interno del servidor
   */
  errorCode: string;
  
  /**
   * Mensaje descriptivo del error
   * 
   * Mensaje legible para el usuario final o para logs
   */
  message: string;
  
  /**
   * Código de estado HTTP
   * 
   * - 400: Bad Request
   * - 401: Unauthorized
   * - 403: Forbidden
   * - 404: Not Found
   * - 409: Conflict
   * - 500: Internal Server Error
   */
  status: number;
  
  /**
   * Marca de tiempo ISO 8601 de cuando ocurrió el error
   * 
   * Formato: "2025-10-30T10:30:00"
   * Útil para logs y debugging
   */
  timestamp: string;
  
  /**
   * Lista de errores de validación por campo
   * 
   * Solo presente cuando el error es de validación (status 400)
   * y hay errores específicos por campo.
   */
  validationErrors?: ValidationError[];
}

/**
 * Estructura para errores de validación por campo
 * 
 * Usado en ErrorResponse.validationErrors para detallar qué campos
 * fallaron la validación y por qué.
 * 
 * @property field - Nombre del campo que falló (ej: "email", "password")
 * @property message - Mensaje de error específico del campo
 * @property rejectedValue - Valor que fue rechazado (opcional, para debugging)
 * 
 * @example
 * ```typescript
 * const validationError: ValidationError = {
 *   field: "email",
 *   message: "El formato del email no es válido",
 *   rejectedValue: "invalido@"
 * };
 * 
 * // Uso en formulario React Hook Form
 * if (errorResponse.validationErrors) {
 *   errorResponse.validationErrors.forEach(error => {
 *     setError(error.field as any, {
 *       type: 'server',
 *       message: error.message
 *     });
 *   });
 * }
 * ```
 * 
 * Backend Java equivalente:
 * ```java
 * public class ValidationError {
 *     private String field;
 *     private String message;
 *     private Object rejectedValue;
 * }
 * ```
 */
export interface ValidationError {
  /**
   * Nombre del campo que falló la validación
   * 
   * Corresponde al nombre del campo en el DTO
   * Ejemplos: "email", "password", "persona.primerNombre"
   */
  field: string;
  
  /**
   * Mensaje de error específico del campo
   * 
   * Mensaje legible que describe por qué el campo no es válido
   */
  message: string;
  
  /**
   * Valor que fue rechazado
   * 
   * Opcional. Útil para debugging.
   * Puede ser string, number, boolean, etc.
   */
  rejectedValue?: any;
}

// ============================================
// TIPOS AUXILIARES
// ============================================

/**
 * Parámetros comunes para paginación
 * 
 * Usado en servicios para estandarizar los parámetros de paginación
 * 
 * @example
 * ```typescript
 * const params: PaginationParams = {
 *   page: 0,
 *   size: 10,
 *   sort: 'nombre,asc'
 * };
 * 
 * const response = await api.get<ApiResponse<PageResponse<User>>>(
 *   '/api/v1/usuarios',
 *   { params }
 * );
 * ```
 */
export interface PaginationParams {
  /**
   * Número de página (0-indexed)
   * @default 0
   */
  page?: number;
  
  /**
   * Cantidad de elementos por página
   * @default 10
   */
  size?: number;
  
  /**
   * Criterio de ordenamiento
   * 
   * Formato: "campo,direccion"
   * Ejemplos: "nombre,asc", "fechaCreacion,desc"
   */
  sort?: string;
}

/**
 * Parámetros base para filtros
 * 
 * Extender esta interfaz en cada módulo para agregar filtros específicos
 * 
 * @example
 * ```typescript
 * interface UsuarioFilterParams extends BaseFilterParams {
 *   nombre?: string;
 *   email?: string;
 *   activo?: boolean;
 * }
 * ```
 */
export interface BaseFilterParams extends PaginationParams {
  /**
   * Búsqueda general (opcional)
   * 
   * El backend puede implementar búsqueda en múltiples campos
   */
  search?: string;
}

/**
 * Tipo helper para respuestas exitosas con datos
 */
export type SuccessResponse<T> = ApiResponse<T>;

/**
 * Tipo helper para respuestas de error
 */
export type ErrorApiResponse = ApiResponse<ErrorResponse>;

/**
 * Tipo helper para respuestas paginadas
 */
export type PaginatedResponse<T> = ApiResponse<PageResponse<T>>;

/**
 * Tipo helper para respuestas sin contenido (204)
 */
export type NoContentResponse = ApiResponse<void>;

// ============================================
// GUARDS DE TIPO
// ============================================

/**
 * Verifica si una respuesta es exitosa (status 2xx)
 * 
 * @param response - Respuesta de la API
 * @returns true si la respuesta es exitosa
 * 
 * @example
 * ```typescript
 * const response = await api.get<ApiResponse<User>>('/api/v1/usuarios/1');
 * 
 * if (isSuccessResponse(response)) {
 *   console.log('Usuario:', response.body);
 * } else {
 *   console.error('Error:', response.message);
 * }
 * ```
 */
export function isSuccessResponse<T>(
  response: ApiResponse<T>
): response is SuccessResponse<T> {
  return response.statusCode >= 200 && response.statusCode < 300;
}

/**
 * Verifica si una respuesta es de error (status >= 400)
 * 
 * @param response - Respuesta de la API
 * @returns true si la respuesta es de error
 * 
 * @example
 * ```typescript
 * const response = await api.get<ApiResponse<User | ErrorResponse>>(
 *   '/api/v1/usuarios/999'
 * );
 * 
 * if (isErrorResponse(response)) {
 *   const error = response.body as ErrorResponse;
 *   console.error(`[${error.errorCode}] ${error.message}`);
 * }
 * ```
 */
export function isErrorResponse<T>(
  response: ApiResponse<T>
): response is ErrorApiResponse {
  return response.statusCode >= 400;
}

/**
 * Verifica si un error tiene validationErrors
 * 
 * @param error - ErrorResponse
 * @returns true si tiene errores de validación
 * 
 * @example
 * ```typescript
 * if (isErrorResponse(response)) {
 *   const error = response.body as ErrorResponse;
 *   
 *   if (hasValidationErrors(error)) {
 *     error.validationErrors.forEach(ve => {
 *       console.error(`${ve.field}: ${ve.message}`);
 *     });
 *   }
 * }
 * ```
 */
export function hasValidationErrors(
  error: ErrorResponse
): error is ErrorResponse & { validationErrors: ValidationError[] } {
  return Boolean(error.validationErrors && error.validationErrors.length > 0);
}
