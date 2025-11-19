/**
 * @file routes.constants.ts
 * @description Constantes de rutas de navegación
 * 
 * Este archivo define todas las rutas de la aplicación web.
 * Centraliza las rutas para evitar strings literales en el código.
 * 
 * Estructura:
 * - Rutas públicas (sin autenticación)
 * - Rutas protegidas (requieren autenticación)
 * - Rutas administrativas (requieren permisos específicos)
 * 
 * IMPORTANTE: Mantener sincronizado con router.tsx y nav.ts
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

// ============================================
// RUTAS PÚBLICAS
// ============================================

/**
 * Rutas que NO requieren autenticación
 * Accesibles para usuarios no autenticados
 */
export const PUBLIC_ROUTES = {
  /**
   * Página de inicio de sesión
   * GET /login
   */
  LOGIN: '/login',
  
  /**
   * Página de recuperación de contraseña
   * GET /forgot-password
   */
  FORGOT_PASSWORD: '/forgot-password',
  
  /**
   * Página de reseteo de contraseña
   * GET /reset-password
   * Query param: token
   */
  RESET_PASSWORD: '/reset-password',
  
  /**
   * Página 404 - No encontrado
   * GET /404
   */
  NOT_FOUND: '/404',
  
  /**
   * Página de acceso no autorizado
   * GET /unauthorized
   */
  UNAUTHORIZED: '/unauthorized',
  
} as const;

// ============================================
// RUTAS PROTEGIDAS - DASHBOARD
// ============================================

/**
 * Rutas del dashboard principal
 * Requieren autenticación
 */
export const DASHBOARD_ROUTES = {
  /**
   * Dashboard principal
   * GET /dashboard
   * Permisos: Todos los usuarios autenticados
   */
  INDEX: '/dashboard',
  
  /**
   * Página de inicio (Home)
   * GET /
   * Redirecciona a /dashboard
   */
  HOME: '/',
  
} as const;

// ============================================
// RUTAS PROTEGIDAS - SEGURIDAD
// ============================================

/**
 * Rutas del módulo de seguridad (usuarios, roles)
 * Requieren autenticación y permisos específicos
 */
export const SECURITY_ROUTES = {
  // ========== USUARIOS ==========
  /**
   * Listado de usuarios
   * GET /usuarios
   * Permiso: USUARIOS:READ
   */
  USUARIOS: '/usuarios',
  
  /**
   * Crear nuevo usuario
   * GET /usuarios/crear
   * Permiso: USUARIOS:CREATE
   */
  USUARIOS_CREATE: '/usuarios/crear',
  
  /**
   * Editar usuario
   * GET /usuarios/:id/editar
   * Permiso: USUARIOS:UPDATE
   */
  USUARIOS_EDIT: '/usuarios/:id/editar',
  
  /**
   * Ver detalle de usuario
   * GET /usuarios/:id
   * Permiso: USUARIOS:READ
   */
  USUARIOS_DETAIL: '/usuarios/:id',
  
  // ========== ROLES ==========
  /**
   * Listado de roles
   * GET /roles
   * Permiso: ROLES:READ
   */
  ROLES: '/roles',
  
  /**
   * Crear nuevo rol
   * GET /roles/crear
   * Permiso: ROLES:CREATE
   */
  ROLES_CREATE: '/roles/crear',
  
  /**
   * Editar rol
   * GET /roles/:id/editar
   * Permiso: ROLES:UPDATE
   */
  ROLES_EDIT: '/roles/:id/editar',
  
  /**
   * Ver detalle de rol
   * GET /roles/:id
   * Permiso: ROLES:READ
   */
  ROLES_DETAIL: '/roles/:id',
  
} as const;

// ============================================
// RUTAS PROTEGIDAS - COMPRAS
// ============================================

/**
 * Rutas del módulo de compras
 */
export const PURCHASE_ROUTES = {
  // ========== PROVEEDORES ==========
  /**
   * Listado de proveedores
   * GET /proveedores
   * Permiso: PROVEEDORES:READ
   */
  PROVEEDORES: '/proveedores',
  
  /**
   * Crear nuevo proveedor
   * GET /proveedores/crear
   * Permiso: PROVEEDORES:CREATE
   */
  PROVEEDORES_CREATE: '/proveedores/crear',
  
  /**
   * Editar proveedor
   * GET /proveedores/:id/editar
   * Permiso: PROVEEDORES:UPDATE
   */
  PROVEEDORES_EDIT: '/proveedores/:id/editar',
  
  /**
   * Ver detalle de proveedor
   * GET /proveedores/:id
   * Permiso: PROVEEDORES:READ
   */
  PROVEEDORES_DETAIL: '/proveedores/:id',
  
  // ========== ÓRDENES DE COMPRA ==========
  /**
   * Listado de órdenes de compra
   * GET /compras
   * Permiso: COMPRAS:READ
   */
  COMPRAS: '/compras',
  
  /**
   * Crear nueva orden de compra
   * GET /compras/crear
   * Permiso: COMPRAS:CREATE
   */
  COMPRAS_CREATE: '/compras/crear',
  
  /**
   * Editar orden de compra
   * GET /compras/:id/editar
   * Permiso: COMPRAS:UPDATE
   */
  COMPRAS_EDIT: '/compras/:id/editar',
  
  /**
   * Ver detalle de orden de compra
   * GET /compras/:id
   * Permiso: COMPRAS:READ
   */
  COMPRAS_DETAIL: '/compras/:id',
  
} as const;

// ============================================
// RUTAS PROTEGIDAS - INVENTARIO
// ============================================

/**
 * Rutas del módulo de inventario
 */
export const INVENTORY_ROUTES = {
  // ========== PRODUCTOS ==========
  /**
   * Listado de productos
   * GET /productos
   * Permiso: PRODUCTOS:READ
   */
  PRODUCTOS: '/productos',
  
  /**
   * Crear nuevo producto
   * GET /productos/crear
   * Permiso: PRODUCTOS:CREATE
   */
  PRODUCTOS_CREATE: '/productos/crear',
  
  /**
   * Editar producto
   * GET /productos/:id/editar
   * Permiso: PRODUCTOS:UPDATE
   */
  PRODUCTOS_EDIT: '/productos/:id/editar',
  
  /**
   * Ver detalle de producto
   * GET /productos/:id
   * Permiso: PRODUCTOS:READ
   */
  PRODUCTOS_DETAIL: '/productos/:id',
  
  // ========== CATEGORÍAS ==========
  /**
   * Listado de categorías
   * GET /categorias
   * Permiso: CATEGORIAS:READ
   */
  CATEGORIAS: '/categorias',
  
  // ========== INVENTARIO ==========
  /**
   * Vista de inventario general
   * GET /inventario
   * Permiso: INVENTARIO:READ
   */
  INVENTARIO: '/inventario',
  
  /**
   * Ajustes de inventario
   * GET /inventario/ajustes
   * Permiso: INVENTARIO:ADJUST
   */
  INVENTARIO_AJUSTES: '/inventario/ajustes',
  
  /**
   * Transferencias entre bodegas
   * GET /inventario/transferencias
   * Permiso: INVENTARIO:TRANSFER
   */
  INVENTARIO_TRANSFERENCIAS: '/inventario/transferencias',
  
  // ========== BODEGAS ==========
  /**
   * Listado de bodegas
   * GET /bodegas
   * Permiso: BODEGAS:READ
   */
  BODEGAS: '/bodegas',
  
  /**
   * Crear nueva bodega
   * GET /bodegas/crear
   * Permiso: BODEGAS:CREATE
   */
  BODEGAS_CREATE: '/bodegas/crear',
  
  /**
   * Editar bodega
   * GET /bodegas/:id/editar
   * Permiso: BODEGAS:UPDATE
   */
  BODEGAS_EDIT: '/bodegas/:id/editar',
  
  /**
   * Ver detalle de bodega
   * GET /bodegas/:id
   * Permiso: BODEGAS:READ
   */
  BODEGAS_DETAIL: '/bodegas/:id',
  
} as const;

// ============================================
// RUTAS PROTEGIDAS - VENTAS
// ============================================

/**
 * Rutas del módulo de ventas
 */
export const SALES_ROUTES = {
  // ========== CLIENTES ==========
  /**
   * Listado de clientes
   * GET /clientes
   * Permiso: CLIENTES:READ
   */
  CLIENTES: '/clientes',
  
  /**
   * Crear nuevo cliente
   * GET /clientes/crear
   * Permiso: CLIENTES:CREATE
   */
  CLIENTES_CREATE: '/clientes/crear',
  
  /**
   * Editar cliente
   * GET /clientes/:id/editar
   * Permiso: CLIENTES:UPDATE
   */
  CLIENTES_EDIT: '/clientes/:id/editar',
  
  /**
   * Ver detalle de cliente
   * GET /clientes/:id
   * Permiso: CLIENTES:READ
   */
  CLIENTES_DETAIL: '/clientes/:id',
  
  // ========== VENTAS/ÓRDENES ==========
  /**
   * Listado de ventas
   * GET /ventas
   * Permiso: VENTAS:READ
   */
  VENTAS: '/ventas',
  
  /**
   * Crear nueva venta
   * GET /ventas/crear
   * Permiso: VENTAS:CREATE
   */
  VENTAS_CREATE: '/ventas/crear',
  
  /**
   * Editar venta
   * GET /ventas/:id/editar
   * Permiso: VENTAS:UPDATE
   */
  VENTAS_EDIT: '/ventas/:id/editar',
  
  /**
   * Ver detalle de venta
   * GET /ventas/:id
   * Permiso: VENTAS:READ
   */
  VENTAS_DETAIL: '/ventas/:id',
  
  // ========== CRÉDITOS ==========
  /**
   * Listado de créditos
   * GET /creditos
   * Permiso: CREDITOS:READ
   */
  CREDITOS: '/creditos',
  
  /**
   * Ver detalle de crédito
   * GET /creditos/:id
   * Permiso: CREDITOS:READ
   */
  CREDITOS_DETAIL: '/creditos/:id',
  
} as const;

// ============================================
// RUTAS PROTEGIDAS - REPORTES
// ============================================

/**
 * Rutas del módulo de reportes
 */
export const REPORT_ROUTES = {
  /**
   * Dashboard de reportes
   * GET /reportes
   * Permiso: REPORTES:READ
   */
  INDEX: '/reportes',
  
  /**
   * Reporte de ventas
   * GET /reportes/ventas
   * Permiso: REPORTES:VENTAS
   */
  VENTAS: '/reportes/ventas',
  
  /**
   * Reporte de inventario
   * GET /reportes/inventario
   * Permiso: REPORTES:INVENTARIO
   */
  INVENTARIO: '/reportes/inventario',
  
  /**
   * Reporte financiero
   * GET /reportes/financiero
   * Permiso: REPORTES:FINANCIERO
   */
  FINANCIERO: '/reportes/financiero',
  
  /**
   * Reporte de auditoría
   * GET /reportes/auditoria
   * Permiso: REPORTES:AUDITORIA
   */
  AUDITORIA: '/reportes/auditoria',
  
} as const;

// ============================================
// RUTAS PROTEGIDAS - CONFIGURACIÓN
// ============================================

/**
 * Rutas de configuración y ajustes
 */
export const SETTINGS_ROUTES = {
  /**
   * Configuración general
   * GET /configuracion
   */
  INDEX: '/configuracion',
  
  /**
   * Perfil del usuario
   * GET /perfil
   */
  PERFIL: '/perfil',
  
  /**
   * Cambiar contraseña
   * GET /cambiar-password
   */
  CAMBIAR_PASSWORD: '/cambiar-password',
  
  /**
   * Configuración del sistema
   * GET /configuracion/sistema
   * Permiso: ADMIN
   */
  SISTEMA: '/configuracion/sistema',
  
} as const;

// ============================================
// TODAS LAS RUTAS
// ============================================

/**
 * Objeto que agrupa todas las rutas de la aplicación
 * 
 * @example
 * ```typescript
 * import { ROUTES } from '@/constants/routes.constants';
 * 
 * navigate(ROUTES.USUARIOS.INDEX);
 * navigate(ROUTES.PRODUCTOS.CREATE);
 * ```
 */
export const ROUTES = {
  PUBLIC: PUBLIC_ROUTES,
  DASHBOARD: DASHBOARD_ROUTES,
  USUARIOS: SECURITY_ROUTES,
  ROLES: SECURITY_ROUTES,
  PROVEEDORES: PURCHASE_ROUTES,
  COMPRAS: PURCHASE_ROUTES,
  PRODUCTOS: INVENTORY_ROUTES,
  INVENTARIO: INVENTORY_ROUTES,
  BODEGAS: INVENTORY_ROUTES,
  CLIENTES: SALES_ROUTES,
  VENTAS: SALES_ROUTES,
  CREDITOS: SALES_ROUTES,
  REPORTES: REPORT_ROUTES,
  CONFIGURACION: SETTINGS_ROUTES,
} as const;

// ============================================
// HELPERS
// ============================================

/**
 * Reemplaza parámetros en una ruta
 * 
 * @param route - Ruta con parámetros (ej: '/usuarios/:id/editar')
 * @param params - Objeto con los valores de los parámetros
 * @returns Ruta con parámetros reemplazados
 * 
 * @example
 * ```typescript
 * const route = replaceRouteParams(
 *   SECURITY_ROUTES.USUARIOS_EDIT,
 *   { id: '123' }
 * );
 * // Result: '/usuarios/123/editar'
 * ```
 */
export function replaceRouteParams(
  route: string,
  params: Record<string, string | number>
): string {
  let result = route;
  
  Object.entries(params).forEach(([key, value]) => {
    result = result.replace(`:${key}`, String(value));
  });
  
  return result;
}

/**
 * Verifica si una ruta es pública
 * 
 * @param pathname - Ruta actual
 * @returns true si la ruta es pública
 * 
 * @example
 * ```typescript
 * if (isPublicRoute('/login')) {
 *   // No requiere autenticación
 * }
 * ```
 */
export function isPublicRoute(pathname: string): boolean {
  const publicPaths = Object.values(PUBLIC_ROUTES);
  return publicPaths.some(path => pathname === path || pathname.startsWith(path));
}

/**
 * Obtiene el nombre del módulo desde una ruta
 * 
 * @param pathname - Ruta actual
 * @returns Nombre del módulo o null
 * 
 * @example
 * ```typescript
 * getModuleFromRoute('/usuarios/123/editar'); // 'usuarios'
 * getModuleFromRoute('/productos'); // 'productos'
 * ```
 */
export function getModuleFromRoute(pathname: string): string | null {
  const parts = pathname.split('/').filter(Boolean);
  return parts.length > 0 ? parts[0] : null;
}

/**
 * Construye una ruta con query params
 * 
 * @param route - Ruta base
 * @param params - Query params
 * @returns Ruta con query params
 * 
 * @example
 * ```typescript
 * buildRouteWithQuery('/usuarios', { page: 0, size: 20 });
 * // Result: '/usuarios?page=0&size=20'
 * ```
 */
export function buildRouteWithQuery(
  route: string,
  params: Record<string, any>
): string {
  const searchParams = new URLSearchParams();
  
  Object.entries(params).forEach(([key, value]) => {
    if (value !== null && value !== undefined && value !== '') {
      searchParams.append(key, String(value));
    }
  });
  
  const query = searchParams.toString();
  return query ? `${route}?${query}` : route;
}