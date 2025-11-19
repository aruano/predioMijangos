/**
 * @file validation.constants.ts
 * @description Constantes de validación, patrones regex y límites
 * 
 * Este archivo define:
 * - Patrones regex para validaciones
 * - Límites de longitud para campos
 * - Rangos numéricos permitidos
 * - Mensajes de validación
 * 
 * IMPORTANTE: Debe estar alineado con las validaciones del backend
 * Backend: @Pattern, @Size, @Min, @Max en DTOs
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

// ============================================
// PATRONES REGEX
// ============================================

/**
 * Patrones de validación regex
 * Sincronizados con las validaciones del backend
 * 
 * @example
 * ```typescript
 * import { VALIDATION_PATTERNS } from '@/constants/validation.constants';
 * 
 * if (!VALIDATION_PATTERNS.EMAIL.test(email)) {
 *   // Email inválido
 * }
 * ```
 */
export const VALIDATION_PATTERNS = {
  /**
   * Username
   * - Solo alfanumérico, punto, guion bajo y guion
   * - 3-50 caracteres
   * Backend: @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
   */
  USERNAME: /^[a-zA-Z0-9._-]+$/,
  
  /**
   * Email
   * RFC 5322 simplificado
   */
  EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  
  /**
   * Contraseña
   * - Al menos 8 caracteres
   * - Al menos una minúscula
   * - Al menos una mayúscula
   * - Al menos un número
   * - Al menos un carácter especial (@$!%*?&#)
   * Backend: @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$")
   */
  PASSWORD: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/,
  
  /**
   * Teléfono Guatemala
   * - 8 dígitos
   * - Puede empezar con código de país opcional (+502 o 502)
   * Ejemplos válidos: 12345678, 50212345678, +50212345678
   */
  TELEFONO_GT: /^(\+?502)?[0-9]{8}$/,
  
  /**
   * DPI (Documento Personal de Identificación)
   * - 13 dígitos
   * - Sin espacios ni guiones
   */
  DPI: /^[0-9]{13}$/,
  
  /**
   * NIT (Número de Identificación Tributaria)
   * - Formato: 1234567-8 o 12345678
   * - 7-8 dígitos antes del guion
   * - 1 dígito verificador después del guion (opcional)
   */
  NIT: /^[0-9]{7,8}(-?[0-9kK])?$/,
  
  /**
   * Código postal Guatemala
   * - 5 dígitos
   */
  CODIGO_POSTAL: /^[0-9]{5}$/,
  
  /**
   * Placa de vehículo Guatemala
   * - Formato: P-123ABC o C-123ABC
   * - Letra inicial (P=Particular, C=Comercial, M=Moto, etc.)
   * - Guion
   * - 3 números
   * - 3 letras
   */
  PLACA_VEHICULO: /^[A-Z]-[0-9]{3}[A-Z]{3}$/,
  
  /**
   * Número de serie/VIN
   * - 17 caracteres alfanuméricos
   */
  VIN: /^[A-HJ-NPR-Z0-9]{17}$/,
  
  /**
   * Solo letras y espacios
   * Para nombres, apellidos
   */
  SOLO_LETRAS: /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/,
  
  /**
   * Solo números
   */
  SOLO_NUMEROS: /^[0-9]+$/,
  
  /**
   * Alfanumérico
   * Letras, números, espacios
   */
  ALFANUMERICO: /^[a-zA-Z0-9\s]+$/,
  
  /**
   * Código SKU
   * - Alfanumérico con guiones
   * - Ejemplo: PROD-2024-001
   */
  SKU: /^[A-Z0-9-]+$/,
  
  /**
   * URL
   * Validación básica de URL
   */
  URL: /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/,
  
  /**
   * Hexadecimal
   * Para colores, códigos, etc.
   */
  HEXADECIMAL: /^[0-9A-Fa-f]+$/,
  
} as const;

// ============================================
// LÍMITES DE LONGITUD
// ============================================

/**
 * Límites de longitud para campos de texto
 * Sincronizados con @Size del backend
 * 
 * @example
 * ```typescript
 * import { LENGTH_LIMITS } from '@/constants/validation.constants';
 * 
 * maxLength={LENGTH_LIMITS.USERNAME.MAX}
 * ```
 */
export const LENGTH_LIMITS = {
  // Autenticación y Usuario
  USERNAME: { MIN: 3, MAX: 50 },
  PASSWORD: { MIN: 8, MAX: 100 },
  EMAIL: { MIN: 5, MAX: 100 },
  
  // Nombres
  PRIMER_NOMBRE: { MIN: 2, MAX: 50 },
  SEGUNDO_NOMBRE: { MIN: 0, MAX: 50 },
  PRIMER_APELLIDO: { MIN: 2, MAX: 50 },
  SEGUNDO_APELLIDO: { MIN: 0, MAX: 50 },
  
  // Identificación
  DPI: { EXACT: 13 },
  NIT: { MIN: 8, MAX: 10 },
  TELEFONO: { MIN: 8, MAX: 15 },
  
  // Direcciones
  DIRECCION: { MIN: 10, MAX: 200 },
  CIUDAD: { MIN: 2, MAX: 50 },
  DEPARTAMENTO: { MIN: 2, MAX: 50 },
  CODIGO_POSTAL: { EXACT: 5 },
  
  // Productos
  NOMBRE_PRODUCTO: { MIN: 3, MAX: 100 },
  DESCRIPCION_PRODUCTO: { MIN: 10, MAX: 500 },
  SKU: { MIN: 3, MAX: 50 },
  CODIGO_BARRAS: { MIN: 8, MAX: 20 },
  
  // General
  NOMBRE_CORTO: { MIN: 2, MAX: 50 },
  NOMBRE_LARGO: { MIN: 3, MAX: 100 },
  DESCRIPCION_CORTA: { MIN: 10, MAX: 200 },
  DESCRIPCION_LARGA: { MIN: 20, MAX: 1000 },
  NOTAS: { MIN: 0, MAX: 500 },
  OBSERVACIONES: { MIN: 0, MAX: 1000 },
  
  // Archivos
  NOMBRE_ARCHIVO: { MIN: 1, MAX: 255 },
  
} as const;

// ============================================
// RANGOS NUMÉRICOS
// ============================================

/**
 * Rangos numéricos permitidos
 * Sincronizados con @Min, @Max del backend
 * 
 * @example
 * ```typescript
 * import { NUMERIC_RANGES } from '@/constants/validation.constants';
 * 
 * min={NUMERIC_RANGES.CANTIDAD.MIN}
 * max={NUMERIC_RANGES.CANTIDAD.MAX}
 * ```
 */
export const NUMERIC_RANGES = {
  // Cantidades
  CANTIDAD: { MIN: 0, MAX: 999999 },
  STOCK: { MIN: 0, MAX: 999999 },
  STOCK_MINIMO: { MIN: 0, MAX: 999999 },
  STOCK_MAXIMO: { MIN: 1, MAX: 999999 },
  
  // Precios y montos (en centavos)
  PRECIO: { MIN: 0, MAX: 99999999 }, // Hasta Q999,999.99
  DESCUENTO_PORCENTAJE: { MIN: 0, MAX: 100 },
  DESCUENTO_MONTO: { MIN: 0, MAX: 99999999 },
  
  // Crédito
  CREDITO_DIAS: { MIN: 1, MAX: 365 },
  CREDITO_LIMITE: { MIN: 0, MAX: 9999999999 }, // Hasta Q99,999,999.99
  
  // Paginación
  PAGE_SIZE: { MIN: 1, MAX: 100 },
  PAGE_NUMBER: { MIN: 0, MAX: 999999 },
  
  // Prioridad
  PRIORIDAD: { MIN: 1, MAX: 10 },
  
  // Calificación
  CALIFICACION: { MIN: 1, MAX: 5 },
  
  // Porcentajes
  PORCENTAJE: { MIN: 0, MAX: 100 },
  
  // Edad
  EDAD: { MIN: 18, MAX: 120 },
  
  // Año
  ANIO: { MIN: 1900, MAX: 2100 },
  
} as const;

// ============================================
// TAMAÑOS DE ARCHIVO
// ============================================

/**
 * Tamaños máximos de archivo en bytes
 */
export const FILE_SIZE_LIMITS = {
  /**
   * Imagen de perfil
   * Máximo: 2 MB
   */
  PROFILE_IMAGE: 2 * 1024 * 1024,
  
  /**
   * Imagen de producto
   * Máximo: 5 MB
   */
  PRODUCT_IMAGE: 5 * 1024 * 1024,
  
  /**
   * Documento general
   * Máximo: 10 MB
   */
  DOCUMENT: 10 * 1024 * 1024,
  
  /**
   * Documento grande (reportes, exports)
   * Máximo: 50 MB
   */
  LARGE_DOCUMENT: 50 * 1024 * 1024,
  
} as const;

/**
 * Tipos MIME permitidos por categoría
 */
export const ALLOWED_FILE_TYPES = {
  /**
   * Imágenes
   */
  IMAGES: ['image/jpeg', 'image/png', 'image/gif', 'image/webp'],
  
  /**
   * Documentos
   */
  DOCUMENTS: [
    'application/pdf',
    'application/msword',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  ],
  
  /**
   * Todos los tipos permitidos
   */
  ALL: [
    'image/jpeg', 'image/png', 'image/gif', 'image/webp',
    'application/pdf',
    'application/msword',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  ],
  
} as const;

// ============================================
// MENSAJES DE VALIDACIÓN
// ============================================

/**
 * Mensajes de error de validación
 * Usados por Zod y React Hook Form
 * 
 * @example
 * ```typescript
 * import { VALIDATION_MESSAGES } from '@/constants/validation.constants';
 * 
 * email: z.string()
 *   .email(VALIDATION_MESSAGES.EMAIL_INVALID)
 * ```
 */
export const VALIDATION_MESSAGES = {
  // General
  REQUIRED: 'Este campo es obligatorio',
  INVALID_FORMAT: 'El formato no es válido',
  
  // Texto
  TOO_SHORT: (min: number) => `Debe tener al menos ${min} caracteres`,
  TOO_LONG: (max: number) => `Debe tener máximo ${max} caracteres`,
  EXACT_LENGTH: (length: number) => `Debe tener exactamente ${length} caracteres`,
  
  // Números
  TOO_SMALL: (min: number) => `Debe ser mayor o igual a ${min}`,
  TOO_LARGE: (max: number) => `Debe ser menor o igual a ${max}`,
  NOT_NUMBER: 'Debe ser un número',
  NOT_INTEGER: 'Debe ser un número entero',
  NEGATIVE_NUMBER: 'No se permiten números negativos',
  
  // Específicos
  USERNAME_INVALID: 'El usuario solo puede contener letras, números, puntos, guiones bajos y guiones',
  EMAIL_INVALID: 'El correo electrónico no es válido',
  PASSWORD_WEAK: 'La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial',
  PASSWORD_MISMATCH: 'Las contraseñas no coinciden',
  TELEFONO_INVALID: 'El teléfono debe tener 8 dígitos',
  DPI_INVALID: 'El DPI debe tener 13 dígitos',
  NIT_INVALID: 'El NIT no es válido',
  URL_INVALID: 'La URL no es válida',
  
  // Fechas
  DATE_INVALID: 'La fecha no es válida',
  DATE_FUTURE: 'La fecha no puede ser futura',
  DATE_PAST: 'La fecha no puede ser pasada',
  DATE_RANGE_INVALID: 'El rango de fechas no es válido',
  
  // Archivos
  FILE_TOO_LARGE: (maxSize: number) => `El archivo es demasiado grande. Máximo ${formatBytes(maxSize)}`,
  FILE_TYPE_INVALID: 'Tipo de archivo no permitido',
  
  // Comparación
  MUST_MATCH: (field: string) => `Debe coincidir con ${field}`,
  MUST_BE_GREATER: (field: string) => `Debe ser mayor que ${field}`,
  MUST_BE_LESS: (field: string) => `Debe ser menor que ${field}`,
  
  // Selección
  SELECT_REQUIRED: 'Debe seleccionar una opción',
  SELECT_MIN_OPTIONS: (min: number) => `Debe seleccionar al menos ${min} opciones`,
  SELECT_MAX_OPTIONS: (max: number) => `Debe seleccionar máximo ${max} opciones`,
  
} as const;

// ============================================
// HELPERS
// ============================================

/**
 * Formatea bytes a formato legible
 * 
 * @param bytes - Cantidad de bytes
 * @param decimals - Número de decimales
 * @returns String formateado
 * 
 * @example
 * ```typescript
 * formatBytes(1024); // "1 KB"
 * formatBytes(1048576); // "1 MB"
 * ```
 */
export function formatBytes(bytes: number, decimals: number = 2): string {
  if (bytes === 0) return '0 Bytes';
  
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  
  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(dm))} ${sizes[i]}`;
}

/**
 * Valida si un string cumple con un patrón regex
 * 
 * @param value - Valor a validar
 * @param pattern - Patrón regex
 * @returns true si cumple, false si no
 * 
 * @example
 * ```typescript
 * isValid('test@example.com', VALIDATION_PATTERNS.EMAIL); // true
 * ```
 */
export function isValid(value: string, pattern: RegExp): boolean {
  return pattern.test(value);
}

/**
 * Valida si una longitud está dentro del rango
 * 
 * @param value - Valor a validar
 * @param limits - Límites de longitud
 * @returns true si está dentro del rango
 * 
 * @example
 * ```typescript
 * isLengthValid('test', LENGTH_LIMITS.USERNAME); // false (muy corto)
 * ```
 */
export function isLengthValid(
  value: string,
  limits: { MIN?: number; MAX?: number; EXACT?: number }
): boolean {
  const length = value.length;
  
  if (limits.EXACT !== undefined) {
    return length === limits.EXACT;
  }
  
  if (limits.MIN !== undefined && length < limits.MIN) {
    return false;
  }
  
  if (limits.MAX !== undefined && length > limits.MAX) {
    return false;
  }
  
  return true;
}

/**
 * Valida si un número está dentro del rango
 * 
 * @param value - Valor a validar
 * @param range - Rango numérico
 * @returns true si está dentro del rango
 * 
 * @example
 * ```typescript
 * isInRange(50, NUMERIC_RANGES.DESCUENTO_PORCENTAJE); // true
 * ```
 */
export function isInRange(
  value: number,
  range: { MIN: number; MAX: number }
): boolean {
  return value >= range.MIN && value <= range.MAX;
}

/**
 * Valida si el tipo de archivo es permitido
 * 
 * @param fileType - Tipo MIME del archivo
 * @param allowedTypes - Array de tipos permitidos
 * @returns true si el tipo es permitido
 * 
 * @example
 * ```typescript
 * isFileTypeAllowed('image/jpeg', ALLOWED_FILE_TYPES.IMAGES); // true
 * ```
 */
export function isFileTypeAllowed(
  fileType: string,
  allowedTypes: readonly string[]
): boolean {
  return allowedTypes.includes(fileType);
}

/**
 * Valida si el tamaño del archivo está dentro del límite
 * 
 * @param fileSize - Tamaño del archivo en bytes
 * @param maxSize - Tamaño máximo permitido en bytes
 * @returns true si el tamaño es válido
 * 
 * @example
 * ```typescript
 * isFileSizeValid(1024000, FILE_SIZE_LIMITS.PROFILE_IMAGE); // true
 * ```
 */
export function isFileSizeValid(fileSize: number, maxSize: number): boolean {
  return fileSize <= maxSize;
}