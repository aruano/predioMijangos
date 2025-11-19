/**
 * @file auth.constants.ts
 * @description Constantes de autenticación, roles y permisos
 * 
 * Este archivo define:
 * - Roles del sistema (sincronizados con backend)
 * - Permisos granulares por módulo
 * - Configuración de JWT
 * - Keys de almacenamiento local
 * 
 * CRÍTICO: Los roles y permisos deben coincidir EXACTAMENTE con el backend
 * Backend: src/main/java/com/predio/mijangos/core/constants/SecurityConstants.java
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

// ============================================
// ROLES DEL SISTEMA
// ============================================

/**
 * Roles disponibles en el sistema
 * 
 * Sincronizado con backend SecurityConstants.java
 * 
 * Descripción de roles:
 * - ADMIN: Acceso total al sistema
 * - SUPERVISOR: Aprobaciones y supervisión
 * - CONTADOR: Reportes y análisis financiero
 * - OPERADOR: Operaciones generales (oficina)
 * - VENDEDOR: Gestión de ventas (móvil) - NO usa la web
 * - BODEGUERO: Gestión de inventario (móvil) - NO usa la web
 */
export const ROLES = {
  ADMIN: 'ROLE_ADMIN',
  SUPERVISOR: 'ROLE_SUPERVISOR',
  CONTADOR: 'ROLE_CONTADOR',
  OPERADOR: 'ROLE_OPERADOR',
  VENDEDOR: 'ROLE_VENDEDOR',      // Solo móvil
  BODEGUERO: 'ROLE_BODEGUERO',    // Solo móvil
} as const;

/**
 * Type helper para roles
 */
export type RoleType = typeof ROLES[keyof typeof ROLES];

// ============================================
// PERMISOS GRANULARES
// ============================================

/**
 * Permisos del sistema organizados por módulo
 * 
 * Formato: MODULO:ACCION
 * Acciones estándar: CREATE, READ, UPDATE, DELETE
 * Acciones especiales según el módulo
 * 
 * Sincronizado con backend SecurityConstants.java
 * 
 * @example
 * ```typescript
 * // Verificar si el usuario puede crear usuarios
 * if (hasPermission(user, PERMISSIONS.USUARIOS_CREATE)) {
 *   // Mostrar botón de crear
 * }
 * ```
 */
export const PERMISSIONS = {
  // ============================================
  // USUARIOS
  // ============================================
  /**
   * Crear nuevos usuarios
   * Requerido para: Botón "Crear Usuario", formulario de creación
   */
  USUARIOS_CREATE: 'USUARIOS:CREATE',
  
  /**
   * Leer/listar usuarios
   * Requerido para: Acceso a la página de usuarios, listado
   */
  USUARIOS_READ: 'USUARIOS:READ',
  
  /**
   * Actualizar usuarios existentes
   * Requerido para: Botón "Editar", formulario de edición
   */
  USUARIOS_UPDATE: 'USUARIOS:UPDATE',
  
  /**
   * Eliminar usuarios
   * Requerido para: Botón "Eliminar", confirmación de eliminación
   */
  USUARIOS_DELETE: 'USUARIOS:DELETE',
  
  /**
   * Resetear contraseñas de usuarios
   * Requerido para: Acción "Resetear contraseña"
   */
  USUARIOS_RESET_PASSWORD: 'USUARIOS:RESET_PASSWORD',
  
  // ============================================
  // ROLES
  // ============================================
  ROLES_CREATE: 'ROLES:CREATE',
  ROLES_READ: 'ROLES:READ',
  ROLES_UPDATE: 'ROLES:UPDATE',
  ROLES_DELETE: 'ROLES:DELETE',
  
  /**
   * Asignar permisos a roles
   * Requerido para: Formulario de asignación de páginas/permisos
   */
  ROLES_ASSIGN_PERMISSIONS: 'ROLES:ASSIGN_PERMISSIONS',
  
  // ============================================
  // PROVEEDORES
  // ============================================
  PROVEEDORES_CREATE: 'PROVEEDORES:CREATE',
  PROVEEDORES_READ: 'PROVEEDORES:READ',
  PROVEEDORES_UPDATE: 'PROVEEDORES:UPDATE',
  PROVEEDORES_DELETE: 'PROVEEDORES:DELETE',
  
  // ============================================
  // PRODUCTOS (Futuro)
  // ============================================
  PRODUCTOS_CREATE: 'PRODUCTOS:CREATE',
  PRODUCTOS_READ: 'PRODUCTOS:READ',
  PRODUCTOS_UPDATE: 'PRODUCTOS:UPDATE',
  PRODUCTOS_DELETE: 'PRODUCTOS:DELETE',
  
  /**
   * Ajustar precios de productos
   * Requerido para: Acciones de cambio de precio masivo
   */
  PRODUCTOS_ADJUST_PRICE: 'PRODUCTOS:ADJUST_PRICE',
  
  // ============================================
  // INVENTARIO (Futuro)
  // ============================================
  INVENTARIO_READ: 'INVENTARIO:READ',
  INVENTARIO_ADJUST: 'INVENTARIO:ADJUST',
  INVENTARIO_TRANSFER: 'INVENTARIO:TRANSFER',
  
  // ============================================
  // VENTAS (Futuro)
  // ============================================
  VENTAS_CREATE: 'VENTAS:CREATE',
  VENTAS_READ: 'VENTAS:READ',
  VENTAS_UPDATE: 'VENTAS:UPDATE',
  VENTAS_DELETE: 'VENTAS:DELETE',
  
  /**
   * Aprobar ventas (crédito)
   * Requerido para: Botón "Aprobar venta a crédito"
   */
  VENTAS_APPROVE: 'VENTAS:APPROVE',
  
  /**
   * Cancelar ventas
   * Requerido para: Botón "Cancelar venta"
   */
  VENTAS_CANCEL: 'VENTAS:CANCEL',
  
  // ============================================
  // REPORTES (Futuro)
  // ============================================
  REPORTES_VENTAS: 'REPORTES:VENTAS',
  REPORTES_INVENTARIO: 'REPORTES:INVENTARIO',
  REPORTES_FINANCIERO: 'REPORTES:FINANCIERO',
  REPORTES_AUDITORIA: 'REPORTES:AUDITORIA',
  
} as const;

/**
 * Type helper para permisos
 */
export type PermissionType = typeof PERMISSIONS[keyof typeof PERMISSIONS];

// ============================================
// CONFIGURACIÓN JWT
// ============================================

/**
 * Configuración de tokens JWT
 */
export const JWT_CONFIG = {
  /**
   * Header HTTP donde se envía el token
   */
  HEADER_NAME: 'Authorization',
  
  /**
   * Prefijo del token en el header
   * Formato: "Bearer <token>"
   */
  TOKEN_PREFIX: 'Bearer',
  
  /**
   * Tiempo antes de expiración para renovar token (en segundos)
   * Si el token expira en menos de 5 minutos, intentar renovar
   */
  REFRESH_THRESHOLD: 300, // 5 minutos
  
} as const;

// ============================================
// STORAGE KEYS
// ============================================

/**
 * Keys para localStorage
 * 
 * IMPORTANTE: Cambiar estos valores invalidará todas las sesiones activas
 */
export const STORAGE_KEYS = {
  /**
   * Access token JWT
   * Formato: "Bearer <token>"
   */
  ACCESS_TOKEN: 'pm_access_token',
  
  /**
   * Refresh token
   * UUID para renovar el access token
   */
  REFRESH_TOKEN: 'pm_refresh_token',
  
  /**
   * Información del usuario autenticado
   * JSON serializado del objeto User
   */
  USER_DATA: 'pm_user',
  
  /**
   * Flag de "recordarme"
   * Si true, mantener sesión al cerrar navegador
   */
  REMEMBER_ME: 'pm_remember',
  
  /**
   * Timestamp de última actividad
   * Para auto-logout por inactividad
   */
  LAST_ACTIVITY: 'pm_last_activity',
  
} as const;

// ============================================
// CONFIGURACIÓN DE SESIÓN
// ============================================

/**
 * Configuración de timeout de sesión
 */
export const SESSION_CONFIG = {
  /**
   * Tiempo de inactividad antes de logout automático (en milisegundos)
   * 30 minutos sin actividad = logout
   */
  INACTIVITY_TIMEOUT: 30 * 60 * 1000, // 30 minutos
  
  /**
   * Eventos que cuentan como "actividad del usuario"
   */
  ACTIVITY_EVENTS: ['mousedown', 'keydown', 'scroll', 'touchstart'] as const,
  
  /**
   * Intervalo para verificar inactividad (en milisegundos)
   */
  CHECK_INTERVAL: 60 * 1000, // 1 minuto
  
} as const;

// ============================================
// RUTAS PÚBLICAS
// ============================================

/**
 * Rutas que NO requieren autenticación
 * Estas rutas son accesibles sin estar logueado
 */
export const PUBLIC_ROUTES = [
  '/login',
  '/forgot-password',
  '/reset-password',
  '/404',
  '/unauthorized',
] as const;

/**
 * Ruta de redirección después de login exitoso
 */
export const DEFAULT_REDIRECT_AFTER_LOGIN = '/dashboard';

/**
 * Ruta de redirección cuando el usuario no tiene permisos
 */
export const UNAUTHORIZED_REDIRECT = '/unauthorized';
