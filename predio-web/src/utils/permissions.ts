/**
 * @file permissions.ts
 * @description Sistema de verificación de permisos y roles (RBAC)
 * 
 * Responsabilidades:
 * - Verificación de permisos individuales y múltiples
 * - Verificación de roles
 * - Helpers para control de acceso basado en roles
 * - Integración con AuthContext y componentes de UI
 * 
 * Sincronizado con:
 * Backend: SecurityConfig.java, @PreAuthorize annotations
 * Constants: AUTH_CONSTANTS (ROLES, PERMISSIONS)
 * Types: User type (roles[], permissions[])
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 */

import { ROLES, PERMISSIONS } from '@/constants/auth.constants';

// ============================================
// TYPES
// ============================================

/**
 * Interface básica de usuario para verificación de permisos
 * Debe coincidir con el tipo User completo del proyecto
 */
export interface PermissibleUser {
  roles?: Array<{ nombre: string }>;
  permissions?: string[];
}

/**
 * Tipo para valores de ROLES
 */
export type RoleValue = (typeof ROLES)[keyof typeof ROLES];

/**
 * Tipo para valores de PERMISSIONS
 */
export type PermissionValue = (typeof PERMISSIONS)[keyof typeof PERMISSIONS];

// ============================================
// VERIFICACIÓN DE PERMISOS
// ============================================

/**
 * Verifica si el usuario tiene un permiso específico
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @param permission - Permiso a verificar (e.g., "USUARIOS:CREATE")
 * @returns true si el usuario tiene el permiso, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { hasPermission } from '@/utils/permissions';
 * import { PERMISSIONS } from '@/constants/auth.constants';
 * 
 * if (hasPermission(user, PERMISSIONS.USUARIOS_CREATE)) {
 *   // Mostrar botón "Crear Usuario"
 * }
 * ```
 * 
 * @example
 * ```typescript
 * // En un componente
 * const canCreate = hasPermission(user, PERMISSIONS.PRODUCTOS_CREATE);
 * const canDelete = hasPermission(user, PERMISSIONS.PRODUCTOS_DELETE);
 * ```
 */
export function hasPermission(
  user: PermissibleUser | null | undefined,
  permission: string
): boolean {
  if (!user || !user.permissions) {
    return false;
  }
  
  return user.permissions.includes(permission);
}

/**
 * Verifica si el usuario tiene TODOS los permisos especificados
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @param permissions - Array de permisos requeridos
 * @returns true si el usuario tiene TODOS los permisos, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { hasAllPermissions } from '@/utils/permissions';
 * import { PERMISSIONS } from '@/constants/auth.constants';
 * 
 * // Verificar que tenga permisos de crear Y actualizar
 * if (hasAllPermissions(user, [
 *   PERMISSIONS.USUARIOS_CREATE,
 *   PERMISSIONS.USUARIOS_UPDATE
 * ])) {
 *   // Mostrar panel de administración completo
 * }
 * ```
 */
export function hasAllPermissions(
  user: PermissibleUser | null | undefined,
  permissions: string[]
): boolean {
  if (!user || !user.permissions || permissions.length === 0) {
    return false;
  }
  
  return permissions.every(permission => user.permissions!.includes(permission));
}

/**
 * Verifica si el usuario tiene AL MENOS UNO de los permisos especificados
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @param permissions - Array de permisos a verificar
 * @returns true si el usuario tiene al menos uno de los permisos, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { hasAnyPermission } from '@/utils/permissions';
 * import { PERMISSIONS } from '@/constants/auth.constants';
 * 
 * // Verificar que tenga permiso de crear O actualizar
 * if (hasAnyPermission(user, [
 *   PERMISSIONS.PRODUCTOS_CREATE,
 *   PERMISSIONS.PRODUCTOS_UPDATE
 * ])) {
 *   // Mostrar botón de "Gestionar Productos"
 * }
 * ```
 */
export function hasAnyPermission(
  user: PermissibleUser | null | undefined,
  permissions: string[]
): boolean {
  if (!user || !user.permissions || permissions.length === 0) {
    return false;
  }
  
  return permissions.some(permission => user.permissions!.includes(permission));
}

// ============================================
// VERIFICACIÓN DE ROLES
// ============================================

/**
 * Verifica si el usuario tiene un rol específico
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @param roleName - Nombre del rol (e.g., "ROLE_ADMIN")
 * @returns true si el usuario tiene el rol, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { hasRole } from '@/utils/permissions';
 * import { ROLES } from '@/constants/auth.constants';
 * 
 * if (hasRole(user, ROLES.ADMIN)) {
 *   // Mostrar menú de administración
 * }
 * ```
 * 
 * @example
 * ```typescript
 * // Verificar múltiples roles individualmente
 * const isAdmin = hasRole(user, ROLES.ADMIN);
 * const isSupervisor = hasRole(user, ROLES.SUPERVISOR);
 * const isContador = hasRole(user, ROLES.CONTADOR);
 * ```
 */
export function hasRole(
  user: PermissibleUser | null | undefined,
  roleName: string
): boolean {
  if (!user || !user.roles || user.roles.length === 0) {
    return false;
  }
  
  return user.roles.some(role => role.nombre === roleName);
}

/**
 * Verifica si el usuario tiene TODOS los roles especificados
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @param roleNames - Array de nombres de roles requeridos
 * @returns true si el usuario tiene TODOS los roles, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { hasAllRoles } from '@/utils/permissions';
 * import { ROLES } from '@/constants/auth.constants';
 * 
 * // Verificar que tenga rol de Admin Y Supervisor
 * if (hasAllRoles(user, [ROLES.ADMIN, ROLES.SUPERVISOR])) {
 *   // Acceso a funcionalidad especial
 * }
 * ```
 */
export function hasAllRoles(
  user: PermissibleUser | null | undefined,
  roleNames: string[]
): boolean {
  if (!user || !user.roles || roleNames.length === 0) {
    return false;
  }
  
  return roleNames.every(roleName => 
    user.roles!.some(role => role.nombre === roleName)
  );
}

/**
 * Verifica si el usuario tiene AL MENOS UNO de los roles especificados
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @param roleNames - Array de nombres de roles a verificar
 * @returns true si el usuario tiene al menos uno de los roles, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { hasAnyRole } from '@/utils/permissions';
 * import { ROLES } from '@/constants/auth.constants';
 * 
 * // Verificar que sea Admin O Supervisor
 * if (hasAnyRole(user, [ROLES.ADMIN, ROLES.SUPERVISOR])) {
 *   // Mostrar opciones administrativas
 * }
 * ```
 */
export function hasAnyRole(
  user: PermissibleUser | null | undefined,
  roleNames: string[]
): boolean {
  if (!user || !user.roles || roleNames.length === 0) {
    return false;
  }
  
  return roleNames.some(roleName => 
    user.roles!.some(role => role.nombre === roleName)
  );
}

// ============================================
// HELPERS ESPECÍFICOS DE ROLES
// ============================================

/**
 * Verifica si el usuario es Administrador
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si el usuario tiene rol ADMIN, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { isAdmin } from '@/utils/permissions';
 * 
 * if (isAdmin(user)) {
 *   // Acceso total al sistema
 * }
 * ```
 */
export function isAdmin(user: PermissibleUser | null | undefined): boolean {
  return hasRole(user, ROLES.ADMIN);
}

/**
 * Verifica si el usuario es Supervisor
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si el usuario tiene rol SUPERVISOR, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { isSupervisor } from '@/utils/permissions';
 * 
 * if (isSupervisor(user)) {
 *   // Mostrar panel de supervisión
 * }
 * ```
 */
export function isSupervisor(user: PermissibleUser | null | undefined): boolean {
  return hasRole(user, ROLES.SUPERVISOR);
}

/**
 * Verifica si el usuario es Contador
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si el usuario tiene rol CONTADOR, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { isContador } from '@/utils/permissions';
 * 
 * if (isContador(user)) {
 *   // Acceso a módulos financieros
 * }
 * ```
 */
export function isContador(user: PermissibleUser | null | undefined): boolean {
  return hasRole(user, ROLES.CONTADOR);
}

/**
 * Verifica si el usuario es Operador
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si el usuario tiene rol OPERADOR, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { isOperador } from '@/utils/permissions';
 * 
 * if (isOperador(user)) {
 *   // Acceso limitado a operaciones básicas
 * }
 * ```
 */
export function isOperador(user: PermissibleUser | null | undefined): boolean {
  return hasRole(user, ROLES.OPERADOR);
}

/**
 * Verifica si el usuario tiene permisos administrativos (Admin o Supervisor)
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si el usuario es Admin o Supervisor, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { hasAdminAccess } from '@/utils/permissions';
 * 
 * if (hasAdminAccess(user)) {
 *   // Mostrar opciones de configuración avanzada
 * }
 * ```
 */
export function hasAdminAccess(user: PermissibleUser | null | undefined): boolean {
  return hasAnyRole(user, [ROLES.ADMIN, ROLES.SUPERVISOR]);
}

// ============================================
// VERIFICACIÓN DE MÓDULOS ESPECÍFICOS
// ============================================

/**
 * Verifica si el usuario puede acceder al módulo de Usuarios
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si tiene al menos permiso de lectura, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { canAccessUsuarios } from '@/utils/permissions';
 * 
 * if (canAccessUsuarios(user)) {
 *   // Mostrar enlace a módulo de usuarios
 * }
 * ```
 */
export function canAccessUsuarios(user: PermissibleUser | null | undefined): boolean {
  return hasPermission(user, PERMISSIONS.USUARIOS_READ);
}

/**
 * Verifica si el usuario puede gestionar roles
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si tiene al menos permiso de lectura de roles, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { canAccessRoles } from '@/utils/permissions';
 * 
 * if (canAccessRoles(user)) {
 *   // Mostrar enlace a gestión de roles
 * }
 * ```
 */
export function canAccessRoles(user: PermissibleUser | null | undefined): boolean {
  return hasPermission(user, PERMISSIONS.ROLES_READ);
}

/**
 * Verifica si el usuario puede acceder al módulo de Productos
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si tiene al menos permiso de lectura, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { canAccessProductos } from '@/utils/permissions';
 * 
 * if (canAccessProductos(user)) {
 *   // Mostrar enlace a catálogo de productos
 * }
 * ```
 */
export function canAccessProductos(user: PermissibleUser | null | undefined): boolean {
  return hasPermission(user, PERMISSIONS.PRODUCTOS_READ);
}

/**
 * Verifica si el usuario puede acceder al módulo de Ventas
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si tiene al menos permiso de lectura, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { canAccessVentas } from '@/utils/permissions';
 * 
 * if (canAccessVentas(user)) {
 *   // Mostrar enlace a módulo de ventas
 * }
 * ```
 */
export function canAccessVentas(user: PermissibleUser | null | undefined): boolean {
  return hasPermission(user, PERMISSIONS.VENTAS_READ);
}

/**
 * Verifica si el usuario puede acceder al módulo de Inventario
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si tiene al menos permiso de lectura, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { canAccessInventario } from '@/utils/permissions';
 * 
 * if (canAccessInventario(user)) {
 *   // Mostrar enlace a módulo de inventario
 * }
 * ```
 */
export function canAccessInventario(user: PermissibleUser | null | undefined): boolean {
  return hasPermission(user, PERMISSIONS.INVENTARIO_READ);
}

/**
 * Verifica si el usuario puede acceder al módulo de Proveedores
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @returns true si tiene al menos permiso de lectura, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { canAccessProveedores } from '@/utils/permissions';
 * 
 * if (canAccessProveedores(user)) {
 *   // Mostrar enlace a módulo de proveedores
 * }
 * ```
 */
export function canAccessProveedores(user: PermissibleUser | null | undefined): boolean {
  return hasPermission(user, PERMISSIONS.PROVEEDORES_READ);
}

// ============================================
// HELPERS DE OPERACIONES CRUD
// ============================================

/**
 * Obtiene los permisos CRUD disponibles para un módulo específico
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @param modulePrefix - Prefijo del módulo (e.g., "USUARIOS", "PRODUCTOS")
 * @returns Objeto con flags de operaciones disponibles
 * 
 * @example
 * ```typescript
 * import { getModulePermissions } from '@/utils/permissions';
 * 
 * const permissions = getModulePermissions(user, 'USUARIOS');
 * // { canRead: true, canCreate: true, canUpdate: false, canDelete: false }
 * 
 * if (permissions.canCreate) {
 *   // Mostrar botón crear
 * }
 * ```
 */
export function getModulePermissions(
  user: PermissibleUser | null | undefined,
  modulePrefix: string
): {
  canRead: boolean;
  canCreate: boolean;
  canUpdate: boolean;
  canDelete: boolean;
} {
  return {
    canRead: hasPermission(user, `${modulePrefix}_READ`),
    canCreate: hasPermission(user, `${modulePrefix}_CREATE`),
    canUpdate: hasPermission(user, `${modulePrefix}_UPDATE`),
    canDelete: hasPermission(user, `${modulePrefix}_DELETE`),
  };
}

/**
 * Verifica si el usuario puede realizar operaciones de escritura en un módulo
 * (Crear, Actualizar o Eliminar)
 * 
 * @param user - Usuario autenticado (puede ser null)
 * @param modulePrefix - Prefijo del módulo (e.g., "USUARIOS", "PRODUCTOS")
 * @returns true si tiene al menos un permiso de escritura, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { canModifyModule } from '@/utils/permissions';
 * 
 * if (canModifyModule(user, 'PRODUCTOS')) {
 *   // Mostrar opciones de edición
 * }
 * ```
 */
export function canModifyModule(
  user: PermissibleUser | null | undefined,
  modulePrefix: string
): boolean {
  return hasAnyPermission(user, [
    `${modulePrefix}_CREATE`,
    `${modulePrefix}_UPDATE`,
    `${modulePrefix}_DELETE`,
  ]);
}

// ============================================
// EXPORTACIONES AGRUPADAS
// ============================================

/**
 * Objeto con todas las funciones de verificación de permisos
 * Útil para pasar como prop o usar con destructuring
 * 
 * @example
 * ```typescript
 * import { permissionsHelper } from '@/utils/permissions';
 * 
 * const userPermissions = permissionsHelper.getModulePermissions(user, 'USUARIOS');
 * ```
 */
export const permissionsHelper = {
  // Verificación de permisos
  hasPermission,
  hasAllPermissions,
  hasAnyPermission,
  
  // Verificación de roles
  hasRole,
  hasAllRoles,
  hasAnyRole,
  
  // Helpers específicos de roles
  isAdmin,
  isSupervisor,
  isContador,
  isOperador,
  hasAdminAccess,
  
  // Verificación de módulos
  canAccessUsuarios,
  canAccessRoles,
  canAccessProductos,
  canAccessVentas,
  canAccessInventario,
  canAccessProveedores,
  
  // Helpers CRUD
  getModulePermissions,
  canModifyModule,
};