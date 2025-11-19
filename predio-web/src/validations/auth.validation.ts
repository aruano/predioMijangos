/**
 * @file auth.validation.ts
 * @description Esquemas de validación Zod para el módulo de autenticación
 * 
 * Este archivo define los esquemas de validación para:
 * - Login (username, password)
 * - Refresh Token
 * - Logout
 * 
 * Los esquemas están sincronizados con las validaciones del backend
 * Backend: @NotBlank, @Email, @Size en DTOs
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

import { z } from 'zod';

// ============================================
// MENSAJES DE ERROR PERSONALIZADOS
// ============================================

const ERROR_MESSAGES = {
  REQUIRED: 'Este campo es requerido',
  INVALID_EMAIL: 'Debe ser un email válido',
  MIN_LENGTH: (min: number) => `Debe tener al menos ${min} caracteres`,
  MAX_LENGTH: (max: number) => `Debe tener máximo ${max} caracteres`,
} as const;

// ============================================
// LOGIN
// ============================================

/**
 * Schema de validación para login
 * 
 * Sincronizado con: LoginRequestDTO.java
 * 
 * Validaciones backend:
 * - username: @NotBlank
 * - password: @NotBlank
 * 
 * @example
 * ```typescript
 * import { loginSchema } from '@/validations/auth.validation';
 * 
 * const result = loginSchema.safeParse({
 *   username: 'admin@prediomijangos.com',
 *   password: 'Admin123!'
 * });
 * 
 * if (result.success) {
 *   // Datos válidos
 *   console.log(result.data);
 * } else {
 *   // Errores de validación
 *   console.error(result.error.errors);
 * }
 * ```
 */
export const loginSchema = z.object({
  /**
   * Username del usuario
   * 
   * Validaciones:
   * - Requerido
   * - Mínimo 3 caracteres
   * - Máximo 100 caracteres
   * 
   * Backend acepta email o username
   */
  username: z
    .string({
      required_error: ERROR_MESSAGES.REQUIRED,
      invalid_type_error: 'El usuario debe ser texto',
    })
    .min(3, ERROR_MESSAGES.MIN_LENGTH(3))
    .max(100, ERROR_MESSAGES.MAX_LENGTH(100))
    .trim(),
  
  /**
   * Contraseña del usuario
   * 
   * Validaciones:
   * - Requerido
   * - Mínimo 6 caracteres
   * - Máximo 100 caracteres
   * 
   * Nota: No validamos complejidad aquí porque el backend ya lo hace
   * y porque es la pantalla de login (no cambio de contraseña)
   */
  password: z
    .string({
      required_error: ERROR_MESSAGES.REQUIRED,
      invalid_type_error: 'La contraseña debe ser texto',
    })
    .min(6, ERROR_MESSAGES.MIN_LENGTH(6))
    .max(100, ERROR_MESSAGES.MAX_LENGTH(100)),
});

/**
 * Tipo inferido del schema de login
 * Usar para tipado de formularios
 */
export type LoginFormData = z.infer<typeof loginSchema>;

// ============================================
// REFRESH TOKEN
// ============================================

/**
 * Schema de validación para refresh token
 * 
 * Sincronizado con: RefreshTokenRequestDTO.java
 * 
 * Validaciones backend:
 * - refreshToken: @NotBlank
 * 
 * @example
 * ```typescript
 * const result = refreshTokenSchema.safeParse({
 *   refreshToken: '550e8400-e29b-41d4-a716-446655440000'
 * });
 * ```
 */
export const refreshTokenSchema = z.object({
  /**
   * Refresh token (UUID)
   * 
   * Validaciones:
   * - Requerido
   * - Formato UUID (opcional, pero recomendado)
   */
  refreshToken: z
    .string({
      required_error: 'Refresh token es requerido',
      invalid_type_error: 'Refresh token debe ser texto',
    })
    .min(1, 'Refresh token no puede estar vacío')
    .uuid('Refresh token debe ser un UUID válido'),
});

/**
 * Tipo inferido del schema de refresh token
 */
export type RefreshTokenFormData = z.infer<typeof refreshTokenSchema>;

// ============================================
// LOGOUT
// ============================================

/**
 * Schema de validación para logout
 * 
 * Sincronizado con: LogoutRequestDTO.java
 * 
 * Validaciones backend:
 * - refreshToken: @NotBlank
 * 
 * @example
 * ```typescript
 * const result = logoutSchema.safeParse({
 *   refreshToken: '550e8400-e29b-41d4-a716-446655440000'
 * });
 * ```
 */
export const logoutSchema = z.object({
  /**
   * Refresh token a invalidar
   * 
   * Validaciones:
   * - Requerido
   * - Formato UUID (opcional, pero recomendado)
   */
  refreshToken: z
    .string({
      required_error: 'Refresh token es requerido',
      invalid_type_error: 'Refresh token debe ser texto',
    })
    .min(1, 'Refresh token no puede estar vacío')
    .uuid('Refresh token debe ser un UUID válido'),
});

/**
 * Tipo inferido del schema de logout
 */
export type LogoutFormData = z.infer<typeof logoutSchema>;

// ============================================
// UTILIDADES DE VALIDACIÓN
// ============================================

/**
 * Valida credenciales de login y retorna errores formateados
 * 
 * @param data - Datos a validar
 * @returns Objeto con success y errores
 * 
 * @example
 * ```typescript
 * const validation = validateLogin({
 *   username: 'admin',
 *   password: '123'
 * });
 * 
 * if (!validation.success) {
 *   validation.errors.forEach(err => {
 *     console.error(`${err.field}: ${err.message}`);
 *   });
 * }
 * ```
 */
export function validateLogin(data: unknown) {
  const result = loginSchema.safeParse(data);
  
  if (result.success) {
    return {
      success: true as const,
      data: result.data,
      errors: [],
    };
  }
  
  return {
    success: false as const,
    data: null,
    errors: result.error.errors.map(err => ({
      field: err.path.join('.'),
      message: err.message,
    })),
  };
}

/**
 * Valida refresh token y retorna errores formateados
 * 
 * @param data - Datos a validar
 * @returns Objeto con success y errores
 */
export function validateRefreshToken(data: unknown) {
  const result = refreshTokenSchema.safeParse(data);
  
  if (result.success) {
    return {
      success: true as const,
      data: result.data,
      errors: [],
    };
  }
  
  return {
    success: false as const,
    data: null,
    errors: result.error.errors.map(err => ({
      field: err.path.join('.'),
      message: err.message,
    })),
  };
}

/**
 * Valida logout y retorna errores formateados
 * 
 * @param data - Datos a validar
 * @returns Objeto con success y errores
 */
export function validateLogout(data: unknown) {
  const result = logoutSchema.safeParse(data);
  
  if (result.success) {
    return {
      success: true as const,
      data: result.data,
      errors: [],
    };
  }
  
  return {
    success: false as const,
    data: null,
    errors: result.error.errors.map(err => ({
      field: err.path.join('.'),
      message: err.message,
    })),
  };
}
