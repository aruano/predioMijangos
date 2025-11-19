/**
 * ============================================
 * BASE MAPPER - UTILIDADES Y TIPOS GENÉRICOS
 * ============================================
 * 
 * Tipos y funciones base reutilizables para todos los mappers del sistema.
 * 
 * @module mappers/base
 * @author Predio Mijangos Dev Team
 * @version 1.0.0
 * 
 * SINCRONIZACIÓN:
 * - Alineado con DTOs del backend (Spring Boot)
 * - Compatible con ApiResponse<T> y PageResponse<T>
 * - Manejo de campos de auditoría del backend
 * 
 * USO:
 * - Importar tipos y utilidades en mappers específicos
 * - Extender interfaces según necesidades del dominio
 * 
 * @example
 * ```typescript
 * import { BaseMapper, mapArray, formatAuditFields } from './base.mapper';
 * 
 * export const mapUserDTOToUser: BaseMapper<UserDTO, User> = (dto) => ({
 *   ...dto,
 *   ...formatAuditFields(dto),
 * });
 * ```
 */

// ============================================
// TIPOS BASE
// ============================================

/**
 * Tipo genérico para funciones mapper
 * 
 * @template TSource - Tipo de origen (generalmente DTO)
 * @template TTarget - Tipo de destino (generalmente Entity de UI)
 * 
 * @example
 * ```typescript
 * const mapUserDTO: BaseMapper<UsuarioResponseDTO, User> = (dto) => ({
 *   id: dto.id,
 *   username: dto.username,
 *   // ...
 * });
 * ```
 */
export type BaseMapper<TSource, TTarget> = (source: TSource) => TTarget;

/**
 * Tipo para funciones mapper de arrays
 * 
 * @template TSource - Tipo de origen
 * @template TTarget - Tipo de destino
 * 
 * @example
 * ```typescript
 * const mapUserDTOs: ArrayMapper<UsuarioResponseDTO, User> = mapArray(mapUserDTO);
 * ```
 */
export type ArrayMapper<TSource, TTarget> = (sources: TSource[]) => TTarget[];

/**
 * Tipo para funciones mapper opcionales (con null/undefined)
 * 
 * @template TSource - Tipo de origen
 * @template TTarget - Tipo de destino
 * 
 * @example
 * ```typescript
 * const mapOptionalUser: OptionalMapper<UsuarioResponseDTO, User> = 
 *   mapOptional(mapUserDTO);
 * ```
 */
export type OptionalMapper<TSource, TTarget> = (
  source: TSource | null | undefined
) => TTarget | null;

/**
 * Campos de auditoría del backend
 * Todos los DTOs Response del backend incluyen estos campos
 * 
 * @interface AuditFields
 * 
 * @property createdAt - Fecha de creación (ISO 8601 string)
 * @property updatedAt - Fecha de última actualización (ISO 8601 string o null)
 * @property createdBy - Usuario que creó el registro (opcional)
 * @property updatedBy - Usuario que actualizó el registro (opcional)
 */
export interface AuditFields {
  createdAt: string;
  updatedAt: string | null;
  createdBy?: string;
  updatedBy?: string;
}

/**
 * Campos de auditoría formateados para UI
 * 
 * @interface FormattedAuditFields
 * 
 * @property createdAt - Fecha de creación formateada
 * @property updatedAt - Fecha de actualización formateada o null
 * @property createdBy - Usuario que creó (opcional)
 * @property updatedBy - Usuario que actualizó (opcional)
 */
export interface FormattedAuditFields {
  createdAt: string;
  updatedAt: string | null;
  createdBy?: string;
  updatedBy?: string;
}

/**
 * Tipo para objetos que incluyen campos de auditoría
 * 
 * @template T - Tipo base del objeto
 * 
 * @example
 * ```typescript
 * interface UserDTO extends WithAudit<{ id: number; username: string }> {}
 * ```
 */
export type WithAudit<T> = T & AuditFields;

/**
 * Tipo para mapear campos opcionales
 * Convierte todas las propiedades en opcionales
 * 
 * @template T - Tipo base
 * 
 * @example
 * ```typescript
 * type PartialUser = Optional<User>; // Todos los campos opcionales
 * ```
 */
export type Optional<T> = {
  [P in keyof T]?: T[P];
};

// ============================================
// UTILIDADES DE MAPEO
// ============================================

/**
 * Mapea un array de objetos usando un mapper específico
 * 
 * @template TSource - Tipo de origen
 * @template TTarget - Tipo de destino
 * 
 * @param mapper - Función mapper para elementos individuales
 * @returns Función para mapear arrays
 * 
 * @example
 * ```typescript
 * const mapUsers = mapArray(mapUserDTOToUser);
 * const users = mapUsers(userDTOs);
 * ```
 */
export const mapArray = <TSource, TTarget>(
  mapper: BaseMapper<TSource, TTarget>
): ArrayMapper<TSource, TTarget> => {
  return (sources: TSource[]): TTarget[] => sources.map(mapper);
};

/**
 * Mapea un valor opcional (null/undefined)
 * Retorna null si el valor fuente es null o undefined
 * 
 * @template TSource - Tipo de origen
 * @template TTarget - Tipo de destino
 * 
 * @param mapper - Función mapper para el valor
 * @returns Función mapper opcional
 * 
 * @example
 * ```typescript
 * const mapOptionalUser = mapOptional(mapUserDTOToUser);
 * const user = mapOptionalUser(userDTO); // User | null
 * ```
 */
export const mapOptional = <TSource, TTarget>(
  mapper: BaseMapper<TSource, TTarget>
): OptionalMapper<TSource, TTarget> => {
  return (source: TSource | null | undefined): TTarget | null => {
    return source ? mapper(source) : null;
  };
};

/**
 * Mapea un array opcional
 * Retorna array vacío si el valor fuente es null o undefined
 * 
 * @template TSource - Tipo de origen
 * @template TTarget - Tipo de destino
 * 
 * @param mapper - Función mapper para elementos individuales
 * @returns Función mapper para arrays opcionales
 * 
 * @example
 * ```typescript
 * const mapOptionalUsers = mapOptionalArray(mapUserDTOToUser);
 * const users = mapOptionalUsers(userDTOs); // User[]
 * ```
 */
export const mapOptionalArray = <TSource, TTarget>(
  mapper: BaseMapper<TSource, TTarget>
) => {
  return (sources: TSource[] | null | undefined): TTarget[] => {
    return sources ? sources.map(mapper) : [];
  };
};

// ============================================
// UTILIDADES DE FORMATEO
// ============================================

/**
 * Formatea campos de auditoría del backend para UI
 * Convierte fechas ISO 8601 a formato legible
 * 
 * @param audit - Objeto con campos de auditoría
 * @returns Campos de auditoría formateados
 * 
 * @example
 * ```typescript
 * const formatted = formatAuditFields(dto);
 * // { createdAt: "01/01/2024 10:00", updatedAt: "01/02/2024 15:30" }
 * ```
 */
export const formatAuditFields = (audit: AuditFields): FormattedAuditFields => {
  return {
    createdAt: formatDateForUI(audit.createdAt),
    updatedAt: audit.updatedAt ? formatDateForUI(audit.updatedAt) : null,
    createdBy: audit.createdBy,
    updatedBy: audit.updatedBy,
  };
};

/**
 * Formatea una fecha ISO 8601 a formato legible
 * 
 * @param isoDate - Fecha en formato ISO 8601
 * @returns Fecha formateada (DD/MM/YYYY HH:mm)
 * 
 * @example
 * ```typescript
 * formatDateForUI("2024-01-01T10:00:00Z") // "01/01/2024 10:00"
 * ```
 */
export const formatDateForUI = (isoDate: string): string => {
  try {
    const date = new Date(isoDate);
    
    if (isNaN(date.getTime())) {
      return isoDate; // Retornar original si no es válida
    }
    
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    
    return `${day}/${month}/${year} ${hours}:${minutes}`;
  } catch (error) {
    console.error('Error formatting date:', error);
    return isoDate;
  }
};

/**
 * Formatea una fecha para el backend (ISO 8601)
 * Convierte Date a string ISO 8601
 * 
 * @param date - Objeto Date
 * @returns Fecha en formato ISO 8601
 * 
 * @example
 * ```typescript
 * formatDateForBackend(new Date()) // "2024-01-01T10:00:00.000Z"
 * ```
 */
export const formatDateForBackend = (date: Date): string => {
  return date.toISOString();
};

/**
 * Formatea solo la fecha (sin hora) para el backend
 * 
 * @param date - Objeto Date
 * @returns Fecha en formato YYYY-MM-DD
 * 
 * @example
 * ```typescript
 * formatDateOnly(new Date()) // "2024-01-01"
 * ```
 */
export const formatDateOnly = (date: Date): string => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  
  return `${year}-${month}-${day}`;
};

// ============================================
// UTILIDADES DE TRANSFORMACIÓN
// ============================================

/**
 * Construye nombre completo a partir de campos de persona
 * 
 * @param primerNombre - Primer nombre
 * @param segundoNombre - Segundo nombre (opcional)
 * @param primerApellido - Primer apellido
 * @param segundoApellido - Segundo apellido (opcional)
 * @returns Nombre completo
 * 
 * @example
 * ```typescript
 * buildFullName("Juan", "Carlos", "Pérez", "López")
 * // "Juan Carlos Pérez López"
 * ```
 */
export const buildFullName = (
  primerNombre: string,
  segundoNombre: string | null,
  primerApellido: string,
  segundoApellido: string | null
): string => {
  return [primerNombre, segundoNombre, primerApellido, segundoApellido]
    .filter(Boolean)
    .join(' ');
};

/**
 * Extrae iniciales de un nombre completo
 * 
 * @param fullName - Nombre completo
 * @returns Iniciales (máximo 2 caracteres)
 * 
 * @example
 * ```typescript
 * getInitials("Juan Pérez") // "JP"
 * getInitials("María") // "M"
 * ```
 */
export const getInitials = (fullName: string): string => {
  const parts = fullName.trim().split(' ');
  
  if (parts.length === 1) {
    return parts[0].charAt(0).toUpperCase();
  }
  
  const firstInitial = parts[0].charAt(0);
  const lastInitial = parts[parts.length - 1].charAt(0);
  
  return `${firstInitial}${lastInitial}`.toUpperCase();
};

/**
 * Limpia y normaliza strings de formularios
 * Elimina espacios extras, convierte a minúsculas si es email
 * 
 * @param value - Valor a limpiar
 * @param isEmail - Si es un campo email
 * @returns Valor limpio
 * 
 * @example
 * ```typescript
 * cleanString("  Juan  Pérez  ") // "Juan Pérez"
 * cleanString("  USER@EMAIL.COM  ", true) // "user@email.com"
 * ```
 */
export const cleanString = (value: string | null | undefined, isEmail = false): string | null => {
  if (!value) return null;
  
  const cleaned = value.trim().replace(/\s+/g, ' ');
  return isEmail ? cleaned.toLowerCase() : cleaned;
};

/**
 * Convierte valores vacíos a null
 * Útil para formularios donde "" debe ser null para el backend
 * 
 * @param value - Valor a convertir
 * @returns Valor o null
 * 
 * @example
 * ```typescript
 * emptyToNull("") // null
 * emptyToNull("  ") // null
 * emptyToNull("valor") // "valor"
 * ```
 */
export const emptyToNull = <T>(value: T | string | null | undefined): T | null => {
  if (value === null || value === undefined) return null;
  if (typeof value === 'string' && value.trim() === '') return null;
  return value;
};

/**
 * Convierte null/undefined a string vacío
 * Útil para poblar formularios desde DTOs
 * 
 * @param value - Valor a convertir
 * @returns Valor o string vacío
 * 
 * @example
 * ```typescript
 * nullToEmpty(null) // ""
 * nullToEmpty("valor") // "valor"
 * ```
 */
export const nullToEmpty = (value: string | null | undefined): string => {
  return value ?? '';
};

// ============================================
// UTILIDADES DE VALIDACIÓN
// ============================================

/**
 * Verifica si un objeto tiene campos de auditoría válidos
 * 
 * @param obj - Objeto a verificar
 * @returns true si tiene campos de auditoría válidos
 * 
 * @example
 * ```typescript
 * hasValidAudit(dto) // true/false
 * ```
 */
export const hasValidAudit = (obj: any): obj is WithAudit<any> => {
  return (
    obj &&
    typeof obj === 'object' &&
    typeof obj.createdAt === 'string' &&
    (obj.updatedAt === null || typeof obj.updatedAt === 'string')
  );
};

/**
 * Verifica si un ID es válido
 * IDs del backend son números enteros positivos
 * 
 * @param id - ID a verificar
 * @returns true si es un ID válido
 * 
 * @example
 * ```typescript
 * isValidId(1) // true
 * isValidId(0) // false
 * isValidId(-1) // false
 * ```
 */
export const isValidId = (id: any): id is number => {
  return typeof id === 'number' && id > 0 && Number.isInteger(id);
};

/**
 * Valida un array de IDs
 * 
 * @param ids - Array de IDs a validar
 * @returns true si todos los IDs son válidos
 * 
 * @example
 * ```typescript
 * areValidIds([1, 2, 3]) // true
 * areValidIds([1, 0, 3]) // false
 * ```
 */
export const areValidIds = (ids: any[]): ids is number[] => {
  return Array.isArray(ids) && ids.length > 0 && ids.every(isValidId);
};

// ============================================
// TIPOS DE EXPORTACIÓN
// ============================================

/**
 * Tipo para resultado de mapeo con posible error
 * 
 * @template T - Tipo del resultado exitoso
 */
export type MapperResult<T> =
  | { success: true; data: T }
  | { success: false; error: string };

/**
 * Ejecuta un mapper capturando errores
 * 
 * @template TSource - Tipo de origen
 * @template TTarget - Tipo de destino
 * 
 * @param mapper - Función mapper
 * @param source - Dato a mapear
 * @param errorMessage - Mensaje de error personalizado
 * @returns Resultado del mapeo o error
 * 
 * @example
 * ```typescript
 * const result = safeMap(mapUserDTO, dto, "Error mapeando usuario");
 * if (result.success) {
 *   console.log(result.data);
 * } else {
 *   console.error(result.error);
 * }
 * ```
 */
export const safeMap = <TSource, TTarget>(
  mapper: BaseMapper<TSource, TTarget>,
  source: TSource,
  errorMessage = 'Error en mapeo'
): MapperResult<TTarget> => {
  try {
    const data = mapper(source);
    return { success: true, data };
  } catch (error) {
    console.error(errorMessage, error);
    return {
      success: false,
      error: error instanceof Error ? error.message : errorMessage,
    };
  }
};
