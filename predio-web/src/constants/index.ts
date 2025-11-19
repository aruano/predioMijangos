/**
 * @file constants/index.ts
 * @description Punto de entrada centralizado para todas las constantes
 * 
 * Este archivo exporta todas las constantes de la aplicación
 * de forma organizada para facilitar su importación.
 * 
 * @example
 * ```typescript
 * // Importar constantes individuales
 * import { API_ENDPOINTS, ERROR_CODES, ROLES } from '@/constants';
 * 
 * // O importar categorías completas
 * import * as ApiConstants from '@/constants/api.constants';
 * ```
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

// ============================================
// API CONSTANTS
// ============================================
export {
  API_CONFIG,
  API_ENDPOINTS,
  replaceParams,
  buildQueryParams,
} from './api.constants';

// ============================================
// AUTH CONSTANTS
// ============================================
export {
  ROLES,
  type RoleType,
  PERMISSIONS,
  type PermissionType,
  JWT_CONFIG,
  STORAGE_KEYS,
  SESSION_CONFIG,
  PUBLIC_ROUTES as AUTH_PUBLIC_ROUTES,
  DEFAULT_REDIRECT_AFTER_LOGIN,
  UNAUTHORIZED_REDIRECT,
} from './auth.constants';

// ============================================
// ERROR CONSTANTS
// ============================================
export {
  ERROR_CODES,
  type ErrorCodeType,
  ERROR_MESSAGES,
  getErrorMessage,
  isAuthError,
  isValidationError,
  isSystemError,
  isBusinessError,
  shouldLogoutOnError,
} from './error.constants';

// ============================================
// VALIDATION CONSTANTS
// ============================================
export {
  VALIDATION_PATTERNS,
  LENGTH_LIMITS,
  NUMERIC_RANGES,
  FILE_SIZE_LIMITS,
  ALLOWED_FILE_TYPES,
  VALIDATION_MESSAGES,
  formatBytes,
  isValid,
  isLengthValid,
  isInRange,
  isFileTypeAllowed,
  isFileSizeValid,
} from './validation.constants';

// ============================================
// ROUTES CONSTANTS
// ============================================
export {
  PUBLIC_ROUTES,
  DASHBOARD_ROUTES,
  SECURITY_ROUTES,
  PURCHASE_ROUTES,
  INVENTORY_ROUTES,
  SALES_ROUTES,
  REPORT_ROUTES,
  SETTINGS_ROUTES,
  ROUTES,
  replaceRouteParams,
  isPublicRoute,
  getModuleFromRoute,
  buildRouteWithQuery,
} from './routes.constants';