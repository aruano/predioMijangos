/**
 * @file error.constants.ts
 * @description Códigos de error y mensajes localizados
 * 
 * Este archivo define todos los códigos de error que el backend puede retornar.
 * Debe mantenerse 100% sincronizado con:
 * Backend: src/main/java/com/predio/mijangos/core/constants/ErrorCodes.java
 * 
 * CRÍTICO: Cualquier cambio en ErrorCodes.java debe reflejarse aquí
 * 
 * Estructura de códigos:
 * - AUTH_XXX: Errores de autenticación
 * - USER_XXX: Errores relacionados con usuarios
 * - VAL_XXX: Errores de validación
 * - BUS_XXX: Errores de reglas de negocio
 * - SYS_XXX: Errores de sistema
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

// ============================================
// CÓDIGOS DE ERROR
// ============================================

/**
 * Códigos de error del sistema
 * Sincronizados con ErrorCodes.java del backend
 * 
 * @example
 * ```typescript
 * import { ERROR_CODES } from '@/constants/error.constants';
 * 
 * if (errorCode === ERROR_CODES.AUTH_INVALID_CREDENTIALS) {
 *   // Mostrar mensaje de credenciales inválidas
 * }
 * ```
 */
export const ERROR_CODES = {
  // ============================================
  // AUTENTICACIÓN (AUTH_XXX)
  // ============================================
  
  /**
   * AUTH_001: Credenciales inválidas
   * Cuando username o password son incorrectos
   */
  AUTH_INVALID_CREDENTIALS: 'AUTH_001',
  
  /**
   * AUTH_002: Token expirado
   * El access token ha expirado, renovar con refresh token
   */
  AUTH_TOKEN_EXPIRED: 'AUTH_002',
  
  /**
   * AUTH_003: Token inválido
   * El token JWT no es válido (firma incorrecta, malformado, etc.)
   */
  AUTH_TOKEN_INVALID: 'AUTH_003',
  
  /**
   * AUTH_004: Refresh token inválido
   * El refresh token no existe o ha sido revocado
   */
  AUTH_REFRESH_TOKEN_INVALID: 'AUTH_004',
  
  /**
   * AUTH_005: Usuario no autenticado
   * No hay token o el token no es válido
   */
  AUTH_UNAUTHENTICATED: 'AUTH_005',
  
  /**
   * AUTH_006: Acceso denegado
   * Usuario autenticado pero sin permisos para la acción
   */
  AUTH_ACCESS_DENIED: 'AUTH_006',
  
  /**
   * AUTH_007: Sesión inválida
   * La sesión del usuario no es válida o ha expirado
   */
  AUTH_SESSION_INVALID: 'AUTH_007',
  
  // ============================================
  // USUARIOS (USER_XXX)
  // ============================================
  
  /**
   * USER_001: Usuario no encontrado
   * El ID de usuario no existe en la base de datos
   */
  USER_NOT_FOUND: 'USER_001',
  
  /**
   * USER_002: Usuario ya existe
   * Username o email ya están registrados
   */
  USER_ALREADY_EXISTS: 'USER_002',
  
  /**
   * USER_003: Usuario inactivo
   * El usuario existe pero está marcado como inactivo
   */
  USER_INACTIVE: 'USER_003',
  
  /**
   * USER_004: Username ya existe
   * El username ya está en uso por otro usuario
   */
  USER_USERNAME_EXISTS: 'USER_004',
  
  /**
   * USER_005: Email ya existe
   * El email ya está en uso por otro usuario
   */
  USER_EMAIL_EXISTS: 'USER_005',
  
  /**
   * USER_006: Contraseña actual incorrecta
   * Al cambiar contraseña, la contraseña actual no coincide
   */
  USER_WRONG_PASSWORD: 'USER_006',
  
  // ============================================
  // ROLES (ROLE_XXX)
  // ============================================
  
  /**
   * ROLE_001: Rol no encontrado
   */
  ROLE_NOT_FOUND: 'ROLE_001',
  
  /**
   * ROLE_002: Rol ya existe
   */
  ROLE_ALREADY_EXISTS: 'ROLE_002',
  
  /**
   * ROLE_003: Rol inactivo
   */
  ROLE_INACTIVE: 'ROLE_003',
  
  /**
   * ROLE_004: No se puede eliminar rol
   * El rol está asignado a usuarios
   */
  ROLE_CANNOT_DELETE: 'ROLE_004',
  
  // ============================================
  // VALIDACIÓN (VAL_XXX)
  // ============================================
  
  /**
   * VAL_001: Formato inválido
   * El formato del campo no es válido (email, teléfono, etc.)
   */
  VAL_INVALID_FORMAT: 'VAL_001',
  
  /**
   * VAL_002: Campo requerido
   * Un campo obligatorio no fue proporcionado
   */
  VAL_REQUIRED_FIELD: 'VAL_002',
  
  /**
   * VAL_003: Valor fuera de rango
   * El valor numérico está fuera del rango permitido
   */
  VAL_OUT_OF_RANGE: 'VAL_003',
  
  /**
   * VAL_004: Longitud inválida
   * La longitud del texto no cumple con los requisitos
   */
  VAL_INVALID_LENGTH: 'VAL_004',
  
  /**
   * VAL_005: Tipo de dato inválido
   * El tipo de dato no es el esperado
   */
  VAL_INVALID_TYPE: 'VAL_005',
  
  /**
   * VAL_006: Patrón no coincide
   * El valor no coincide con el patrón regex requerido
   */
  VAL_PATTERN_MISMATCH: 'VAL_006',
  
  /**
   * VAL_007: Fecha inválida
   * La fecha no es válida o está fuera del rango permitido
   */
  VAL_INVALID_DATE: 'VAL_007',
  
  /**
   * VAL_008: Combinación inválida
   * La combinación de valores no es válida
   */
  VAL_INVALID_COMBINATION: 'VAL_008',
  
  // ============================================
  // NEGOCIO (BUS_XXX)
  // ============================================
  
  /**
   * BUS_001: Operación de negocio fallida
   * La operación no pudo completarse por reglas de negocio
   */
  BUS_OPERATION_FAILED: 'BUS_001',
  
  /**
   * BUS_002: Estado inválido
   * La entidad no está en el estado correcto para la operación
   */
  BUS_INVALID_STATE: 'BUS_002',
  
  /**
   * BUS_003: Entidad inactiva
   * Se intentó operar con una entidad marcada como inactiva
   */
  BUS_INACTIVE_ENTITY: 'BUS_003',
  
  /**
   * BUS_004: Violación de regla de negocio
   * Se violó una regla de negocio específica
   */
  BUS_RULE_VIOLATION: 'BUS_004',
  
  /**
   * BUS_005: Recurso no disponible
   * El recurso solicitado no está disponible
   */
  BUS_RESOURCE_UNAVAILABLE: 'BUS_005',
  
  /**
   * BUS_006: Stock insuficiente
   * No hay suficiente stock para completar la operación
   */
  BUS_INSUFFICIENT_STOCK: 'BUS_006',
  
  /**
   * BUS_007: Límite de crédito excedido
   * El cliente ha excedido su límite de crédito
   */
  BUS_CREDIT_LIMIT_EXCEEDED: 'BUS_007',
  
  // ============================================
  // SISTEMA (SYS_XXX)
  // ============================================
  
  /**
   * SYS_001: Error interno del servidor
   * Error inesperado en el servidor
   */
  SYS_INTERNAL_ERROR: 'SYS_001',
  
  /**
   * SYS_002: Servicio no disponible
   * El servicio está temporalmente no disponible
   */
  SYS_SERVICE_UNAVAILABLE: 'SYS_002',
  
  /**
   * SYS_003: Timeout
   * La operación tomó demasiado tiempo y se canceló
   */
  SYS_TIMEOUT: 'SYS_003',
  
  /**
   * SYS_004: Error de base de datos
   * Error al acceder a la base de datos
   */
  SYS_DATABASE_ERROR: 'SYS_004',
  
  /**
   * SYS_005: Recurso no encontrado
   * El recurso solicitado no existe (404)
   */
  SYS_RESOURCE_NOT_FOUND: 'SYS_005',
  
  /**
   * SYS_006: Error de red
   * Error de comunicación con el servidor
   */
  SYS_NETWORK_ERROR: 'SYS_006',
  
  /**
   * SYS_007: Límite de rate excedido
   * Se han hecho demasiadas peticiones en poco tiempo
   */
  SYS_RATE_LIMIT_EXCEEDED: 'SYS_007',
  
  /**
   * SYS_008: Archivo demasiado grande
   * El archivo excede el tamaño máximo permitido
   */
  SYS_FILE_TOO_LARGE: 'SYS_008',
  
  /**
   * SYS_009: Tipo de archivo no soportado
   * El tipo de archivo no es válido para esta operación
   */
  SYS_UNSUPPORTED_FILE_TYPE: 'SYS_009',
  
} as const;

/**
 * Type helper para códigos de error
 */
export type ErrorCodeType = typeof ERROR_CODES[keyof typeof ERROR_CODES];

// ============================================
// MENSAJES DE ERROR LOCALIZADOS
// ============================================

/**
 * Mensajes de error en español para cada código
 * 
 * Estos mensajes son genéricos. El backend puede enviar mensajes
 * más específicos que deben mostrarse preferentemente.
 * 
 * @example
 * ```typescript
 * import { ERROR_CODES, ERROR_MESSAGES } from '@/constants/error.constants';
 * 
 * const message = ERROR_MESSAGES[ERROR_CODES.USER_NOT_FOUND];
 * console.log(message); // "Usuario no encontrado"
 * ```
 */
export const ERROR_MESSAGES: Record<string, string> = {
  // Autenticación
  [ERROR_CODES.AUTH_INVALID_CREDENTIALS]: 'Usuario o contraseña incorrectos',
  [ERROR_CODES.AUTH_TOKEN_EXPIRED]: 'Su sesión ha expirado. Por favor, inicie sesión nuevamente',
  [ERROR_CODES.AUTH_TOKEN_INVALID]: 'Token de autenticación inválido',
  [ERROR_CODES.AUTH_REFRESH_TOKEN_INVALID]: 'No se pudo renovar la sesión. Por favor, inicie sesión nuevamente',
  [ERROR_CODES.AUTH_UNAUTHENTICATED]: 'Debe iniciar sesión para continuar',
  [ERROR_CODES.AUTH_ACCESS_DENIED]: 'No tiene permisos para realizar esta acción',
  [ERROR_CODES.AUTH_SESSION_INVALID]: 'Su sesión no es válida. Por favor, inicie sesión nuevamente',
  
  // Usuarios
  [ERROR_CODES.USER_NOT_FOUND]: 'Usuario no encontrado',
  [ERROR_CODES.USER_ALREADY_EXISTS]: 'El usuario ya existe',
  [ERROR_CODES.USER_INACTIVE]: 'El usuario está inactivo',
  [ERROR_CODES.USER_USERNAME_EXISTS]: 'El nombre de usuario ya está en uso',
  [ERROR_CODES.USER_EMAIL_EXISTS]: 'El correo electrónico ya está registrado',
  [ERROR_CODES.USER_WRONG_PASSWORD]: 'La contraseña actual es incorrecta',
  
  // Roles
  [ERROR_CODES.ROLE_NOT_FOUND]: 'Rol no encontrado',
  [ERROR_CODES.ROLE_ALREADY_EXISTS]: 'El rol ya existe',
  [ERROR_CODES.ROLE_INACTIVE]: 'El rol está inactivo',
  [ERROR_CODES.ROLE_CANNOT_DELETE]: 'No se puede eliminar el rol porque está asignado a usuarios',
  
  // Validación
  [ERROR_CODES.VAL_INVALID_FORMAT]: 'El formato del campo no es válido',
  [ERROR_CODES.VAL_REQUIRED_FIELD]: 'Este campo es obligatorio',
  [ERROR_CODES.VAL_OUT_OF_RANGE]: 'El valor está fuera del rango permitido',
  [ERROR_CODES.VAL_INVALID_LENGTH]: 'La longitud del campo no es válida',
  [ERROR_CODES.VAL_INVALID_TYPE]: 'El tipo de dato no es válido',
  [ERROR_CODES.VAL_PATTERN_MISMATCH]: 'El formato no coincide con el patrón requerido',
  [ERROR_CODES.VAL_INVALID_DATE]: 'La fecha no es válida',
  [ERROR_CODES.VAL_INVALID_COMBINATION]: 'La combinación de valores no es válida',
  
  // Negocio
  [ERROR_CODES.BUS_OPERATION_FAILED]: 'No se pudo completar la operación',
  [ERROR_CODES.BUS_INVALID_STATE]: 'El estado de la entidad no permite esta operación',
  [ERROR_CODES.BUS_INACTIVE_ENTITY]: 'No se puede operar con una entidad inactiva',
  [ERROR_CODES.BUS_RULE_VIOLATION]: 'Se violó una regla de negocio',
  [ERROR_CODES.BUS_RESOURCE_UNAVAILABLE]: 'El recurso no está disponible',
  [ERROR_CODES.BUS_INSUFFICIENT_STOCK]: 'No hay suficiente stock disponible',
  [ERROR_CODES.BUS_CREDIT_LIMIT_EXCEEDED]: 'Límite de crédito excedido',
  
  // Sistema
  [ERROR_CODES.SYS_INTERNAL_ERROR]: 'Ha ocurrido un error interno. Por favor, contacte al administrador',
  [ERROR_CODES.SYS_SERVICE_UNAVAILABLE]: 'Servicio no disponible temporalmente. Por favor, intente más tarde',
  [ERROR_CODES.SYS_TIMEOUT]: 'La operación tomó demasiado tiempo y se canceló',
  [ERROR_CODES.SYS_DATABASE_ERROR]: 'Error de base de datos. Por favor, contacte al administrador',
  [ERROR_CODES.SYS_RESOURCE_NOT_FOUND]: 'Recurso no encontrado',
  [ERROR_CODES.SYS_NETWORK_ERROR]: 'Error de conexión. Verifique su conexión a internet',
  [ERROR_CODES.SYS_RATE_LIMIT_EXCEEDED]: 'Demasiadas solicitudes. Por favor, espere un momento',
  [ERROR_CODES.SYS_FILE_TOO_LARGE]: 'El archivo es demasiado grande',
  [ERROR_CODES.SYS_UNSUPPORTED_FILE_TYPE]: 'Tipo de archivo no soportado',
};

// ============================================
// HELPERS
// ============================================

/**
 * Obtiene el mensaje de error para un código dado
 * 
 * @param errorCode - Código de error
 * @param defaultMessage - Mensaje por defecto si no se encuentra el código
 * @returns Mensaje de error localizado
 * 
 * @example
 * ```typescript
 * const message = getErrorMessage('USER_001', 'Error desconocido');
 * console.log(message); // "Usuario no encontrado"
 * ```
 */
export function getErrorMessage(
  errorCode: string,
  defaultMessage: string = 'Ha ocurrido un error inesperado'
): string {
  return ERROR_MESSAGES[errorCode] || defaultMessage;
}

/**
 * Verifica si un código de error es de autenticación
 * 
 * @param errorCode - Código de error
 * @returns true si es error de autenticación
 * 
 * @example
 * ```typescript
 * if (isAuthError('AUTH_001')) {
 *   // Redirigir a login
 * }
 * ```
 */
export function isAuthError(errorCode: string): boolean {
  return errorCode.startsWith('AUTH_');
}

/**
 * Verifica si un código de error es de validación
 * 
 * @param errorCode - Código de error
 * @returns true si es error de validación
 */
export function isValidationError(errorCode: string): boolean {
  return errorCode.startsWith('VAL_');
}

/**
 * Verifica si un código de error es de sistema
 * 
 * @param errorCode - Código de error
 * @returns true si es error de sistema
 */
export function isSystemError(errorCode: string): boolean {
  return errorCode.startsWith('SYS_');
}

/**
 * Verifica si un código de error es de negocio
 * 
 * @param errorCode - Código de error
 * @returns true si es error de negocio
 */
export function isBusinessError(errorCode: string): boolean {
  return errorCode.startsWith('BUS_');
}

/**
 * Verifica si un error requiere logout automático
 * 
 * @param errorCode - Código de error
 * @returns true si debe hacer logout
 * 
 * @example
 * ```typescript
 * if (shouldLogoutOnError('AUTH_002')) {
 *   logout();
 *   navigate('/login');
 * }
 * ```
 */
export function shouldLogoutOnError(errorCode: string): boolean {
  // Anotación explícita de tipo para evitar error de TypeScript
  const logoutErrors: string[] = [
    ERROR_CODES.AUTH_TOKEN_INVALID,
    ERROR_CODES.AUTH_REFRESH_TOKEN_INVALID,
    ERROR_CODES.AUTH_SESSION_INVALID,
    ERROR_CODES.USER_INACTIVE,
  ];
  
  return logoutErrors.includes(errorCode);
}