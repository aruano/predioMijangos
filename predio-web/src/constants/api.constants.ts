/**
 * @file api.constants.ts
 * @description Constantes de configuración para la API del backend
 * 
 * Este archivo centraliza toda la configuración relacionada con:
 * - URL base de la API
 * - Timeouts y reintentos
 * - Endpoints de la aplicación
 * 
 * IMPORTANTE: Mantener sincronizado con el backend
 * Backend Base: /api/v1
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

/**
 * Configuración general de la API
 */
export const API_CONFIG = {
  /**
   * URL base de la API
   * Se lee de las variables de entorno, con fallback a localhost
   */
  BASE_URL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  
  /**
   * Timeout para requests HTTP (en milisegundos)
   * 30 segundos para operaciones normales
   */
  TIMEOUT: 30000,
  
  /**
   * Número de reintentos para requests fallidos
   * Solo aplica para errores de red, no para errores HTTP 4xx/5xx
   */
  RETRY_ATTEMPTS: 3,
  
  /**
   * Delay entre reintentos (en milisegundos)
   */
  RETRY_DELAY: 1000,
  
  /**
   * Prefijo de versión de la API
   * Todos los endpoints se construyen a partir de este prefijo
   */
  API_VERSION: '/api/v1',
} as const;

/**
 * Endpoints de la API organizados por módulo
 * 
 * Nomenclatura:
 * - Usar UPPER_SNAKE_CASE para constantes
 * - Mantener la estructura jerárquica del backend
 * - Incluir parámetros con :id cuando sea necesario
 * 
 * Ejemplo de uso:
 * ```typescript
 * const url = API_ENDPOINTS.USUARIOS; // '/api/v1/usuarios'
 * const urlById = API_ENDPOINTS.USUARIOS_BY_ID.replace(':id', '123'); // '/api/v1/usuarios/123'
 * ```
 */
export const API_ENDPOINTS = {
  // ============================================
  // AUTENTICACIÓN
  // ============================================
  
  /**
   * POST /api/v1/auth/login
   * Login de usuario con username y password
   * Response: { accessToken, refreshToken, usuario }
   */
  AUTH_LOGIN: `${API_CONFIG.API_VERSION}/auth/login`,
  
  /**
   * POST /api/v1/auth/refresh
   * Renovar access token con refresh token
   * Body: { refreshToken }
   * Response: { accessToken, refreshToken, usuario }
   */
  AUTH_REFRESH: `${API_CONFIG.API_VERSION}/auth/refresh`,
  
  /**
   * POST /api/v1/auth/logout
   * Cerrar sesión e invalidar refresh token
   * Body: { refreshToken }
   */
  AUTH_LOGOUT: `${API_CONFIG.API_VERSION}/auth/logout`,
  
  // ============================================
  // USUARIOS
  // ============================================
  
  /**
   * GET /api/v1/usuarios
   * Listar usuarios con paginación y filtros
   * Query params: page, size, search, etc.
   * 
   * POST /api/v1/usuarios
   * Crear nuevo usuario
   * Body: UsuarioCreateDTO
   */
  USUARIOS: `${API_CONFIG.API_VERSION}/usuarios`,
  
  /**
   * GET /api/v1/usuarios/:id
   * Obtener usuario por ID
   * 
   * PUT /api/v1/usuarios/:id
   * Actualizar usuario
   * Body: UsuarioUpdateDTO
   * 
   * DELETE /api/v1/usuarios/:id
   * Eliminar usuario (soft delete)
   */
  USUARIOS_BY_ID: `${API_CONFIG.API_VERSION}/usuarios/:id`,
  
  /**
   * PUT /api/v1/usuarios/:id/password
   * Cambiar contraseña de usuario
   * Body: { currentPassword, newPassword }
   */
  USUARIOS_PASSWORD: `${API_CONFIG.API_VERSION}/usuarios/:id/password`,
  
  // ============================================
  // ROLES
  // ============================================
  
  /**
   * GET /api/v1/rols
   * Listar roles con paginación
   * 
   * POST /api/v1/rols
   * Crear nuevo rol
   * Body: RoleCreateDTO
   */
  ROLES: `${API_CONFIG.API_VERSION}/rols`,
  
  /**
   * GET /api/v1/rols/:id
   * Obtener rol por ID
   * 
   * PUT /api/v1/rols/:id
   * Actualizar rol
   * Body: RoleUpdateDTO
   * 
   * DELETE /api/v1/rols/:id
   * Eliminar rol
   */
  ROLES_BY_ID: `${API_CONFIG.API_VERSION}/rols/:id`,
  
  // ============================================
  // PÁGINAS
  // ============================================
  
  /**
   * GET /api/v1/pages
   * Listar páginas del sistema
   */
  PAGINAS: `${API_CONFIG.API_VERSION}/pages`,
  
  /**
   * GET /api/v1/pages/:id
   * Obtener página por ID
   */
  PAGINAS_BY_ID: `${API_CONFIG.API_VERSION}/pages/:id`,
  
  // ============================================
  // MÓDULOS
  // ============================================
  
  /**
   * GET /api/v1/modules
   * Listar módulos del sistema
   */
  MODULOS: `${API_CONFIG.API_VERSION}/modules`,
  
  // ============================================
  // GEOGRAFÍA
  // ============================================
  
  /**
   * GET /api/v1/departamentos
   * Listar departamentos de Guatemala
   */
  DEPARTAMENTOS: `${API_CONFIG.API_VERSION}/departamentos`,
  
  /**
   * GET /api/v1/departamentos/:id/municipios
   * Listar municipios de un departamento
   */
  MUNICIPIOS: `${API_CONFIG.API_VERSION}/departamentos/:id/municipios`,
  
  // ============================================
  // PROVEEDORES
  // ============================================
  
  /**
   * GET /api/v1/providers
   * Listar proveedores
   * 
   * POST /api/v1/providers
   * Crear nuevo proveedor
   */
  PROVEEDORES: `${API_CONFIG.API_VERSION}/providers`,
  
  /**
   * GET /api/v1/providers/:id
   * Obtener proveedor por ID
   * 
   * PUT /api/v1/providers/:id
   * Actualizar proveedor
   * 
   * DELETE /api/v1/providers/:id
   * Eliminar proveedor
   */
  PROVEEDORES_BY_ID: `${API_CONFIG.API_VERSION}/providers/:id`,
  
  // ============================================
  // PRODUCTOS (Futuro)
  // ============================================
  PRODUCTOS: `${API_CONFIG.API_VERSION}/productos`,
  PRODUCTOS_BY_ID: `${API_CONFIG.API_VERSION}/productos/:id`,
  
  // ============================================
  // INVENTARIO (Futuro)
  // ============================================
  INVENTARIO: `${API_CONFIG.API_VERSION}/inventario`,
  
  // ============================================
  // VENTAS (Futuro)
  // ============================================
  VENTAS: `${API_CONFIG.API_VERSION}/ventas`,
  VENTAS_BY_ID: `${API_CONFIG.API_VERSION}/ventas/:id`,
  
} as const;

/**
 * Helper para reemplazar parámetros en URLs
 * 
 * @param endpoint - Endpoint con parámetros (ej: '/api/v1/usuarios/:id')
 * @param params - Objeto con los valores de los parámetros
 * @returns URL con parámetros reemplazados
 * 
 * @example
 * ```typescript
 * const url = replaceParams(API_ENDPOINTS.USUARIOS_BY_ID, { id: '123' });
 * // Result: '/api/v1/usuarios/123'
 * ```
 */
export function replaceParams(
  endpoint: string,
  params: Record<string, string | number>
): string {
  let result = endpoint;
  
  Object.entries(params).forEach(([key, value]) => {
    result = result.replace(`:${key}`, String(value));
  });
  
  return result;
}

/**
 * Helper para construir query params
 * 
 * @param params - Objeto con los query params
 * @returns String con query params formateados
 * 
 * @example
 * ```typescript
 * const query = buildQueryParams({ page: 0, size: 20, search: 'john' });
 * // Result: '?page=0&size=20&search=john'
 * ```
 */
export function buildQueryParams(params: Record<string, any>): string {
  const searchParams = new URLSearchParams();
  
  Object.entries(params).forEach(([key, value]) => {
    if (value !== null && value !== undefined && value !== '') {
      searchParams.append(key, String(value));
    }
  });
  
  const query = searchParams.toString();
  return query ? `?${query}` : '';
}
