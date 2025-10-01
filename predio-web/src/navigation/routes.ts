/**
 * RUTAS CANÓNICAS de la app.
 * - Evita "strings mágicos" por todo el código.
 * - Si cambian, se actualiza un solo lugar.
 */
export const ROUTES = {
  ROOT: '/',
  // RUTAS PRINCIPALES
  LOGIN: '/login',
  APP: '/app',
  HOME: '/app',
  ACCOUNT_SETTINGS: '/app/configuracion',
  // AUTH
  ROLS: '/app/seguridad/roles',
  USERS: '/app/seguridad/usuarios',
  //PURCHASE
  PROVIDERS: '/app/compras/proveedores',
  PRODUCT_BY_ID: (id: string | number) => `/app/products/${id}`,
  // agrega más helpers según tu menú
} as const;

/** slugify para construir paths a partir de nombres del menú */
export const slug = (s: string) =>
  s
    .normalize('NFD').replace(/[\u0300-\u036f]/g, '') // sin acentos
    .toLowerCase().replace(/[^a-z0-9]+/g, '-')       // espacios → guiones
    .replace(/(^-|-$)/g, '');                        // sin guiones extremos

/** ruta para una página del menú */
export const pagePath = (modName: string, pageName: string) =>
  `/app/${slug(modName)}/${slug(pageName)}`;