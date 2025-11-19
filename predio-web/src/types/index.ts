/**
 * @file index.ts
 * @description Exportaciones centralizadas de todos los tipos
 * 
 * Este archivo re-exporta todos los tipos del proyecto para facilitar
 * su importación desde otros módulos.
 * 
 * @example
 * ```typescript
 * // En lugar de múltiples imports:
 * import { ApiResponse } from '@/types/api.d';
 * import { User } from '@/types/user';
 * import { Role } from '@/types/role';
 * 
 * // Importar desde un solo lugar:
 * import { ApiResponse, User, Role } from '@/types';
 * ```
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

// ============================================
// TIPOS BASE DE API
// ============================================

/**
 * Tipos y funciones para comunicación con la API
 * 
 * Incluye:
 * - ApiResponse<T>
 * - PageResponse<T>
 * - ErrorResponse
 * - ValidationError
 * - Type guards (isSuccessResponse, isErrorResponse, hasValidationErrors)
 * - Tipos auxiliares (PaginationParams, BaseFilterParams, etc.)
 */
export type {
  ApiResponse,
  PageResponse,
  ErrorResponse,
  ValidationError,
  PaginationParams,
  BaseFilterParams,
  SuccessResponse,
  ErrorApiResponse,
  PaginatedResponse,
  NoContentResponse,
} from './api.d';

// Exportar también las funciones de type guards
export {
  isSuccessResponse,
  isErrorResponse,
  hasValidationErrors,
} from './api.d';

// ============================================
// AUTENTICACIÓN Y AUTORIZACIÓN
// ============================================

/**
 * Tipos relacionados con autenticación y autorización
 * 
 * Incluye:
 * - LoginRequest, LoginResponse
 * - AuthUser, AuthState
 * - Tokens, etc.
 */
export type * from './auth';

// ============================================
// ROLES Y PERMISOS
// ============================================

/**
 * Tipos relacionados con roles y permisos
 * 
 * Incluye:
 * - Role, RoleCreateDTO, RoleUpdateDTO
 * - Permission, etc.
 */
export * from './role';

// ============================================
// USUARIOS
// ============================================

/**
 * Tipos relacionados con usuarios
 * 
 * Incluye:
 * - User, UserCreateDTO, UserUpdateDTO
 * - UserListDTO, UserResponseDTO, etc.
 */
export * from './user';

// ============================================
// PROVEEDORES
// ============================================

/**
 * Tipos relacionados con proveedores
 * 
 * Incluye:
 * - Provider, ProviderCreateDTO, ProviderUpdateDTO, etc.
 */
export * from './provider';
