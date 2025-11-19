/**
 * @file auth.types.ts
 * @description Tipos TypeScript para el módulo de autenticación
 * 
 * Este archivo define los tipos para:
 * - Login Request/Response
 * - Refresh Token Request/Response
 * - Logout Request
 * - Usuario Info
 * 
 * CRÍTICO: Tipos 100% sincronizados con el backend
 * Backend DTOs: 
 * - LoginRequestDTO.java
 * - LoginResponseDTO.java
 * - RefreshTokenRequestDTO.java
 * - LogoutRequestDTO.java
 * - UsuarioInfoDTO.java
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 * @sincronizado Backend v2.0.0
 */

// ============================================
// TIPOS BASE
// ============================================

/**
 * Información de una página del sistema
 * Usada para construir el menú dinámico
 * 
 * Corresponde a: PaginaResponseDTO.java
 */
export interface Pagina {
  /**
   * ID único de la página
   */
  id: number;
  
  /**
   * Nombre de la página (para mostrar en UI)
   * Ejemplo: "Gestión de Usuarios", "Roles y Permisos"
   */
  nombre: string;
  
  /**
   * Flag para indicar si la página es para la app móvil
   * - true: página visible solo en app móvil
   * - false: página visible en web
   */
  movil: boolean;
  
  /**
   * Ícono de la página (opcional)
   * Puede ser un nombre de ícono de Material Design
   * Ejemplo: "mdi-account", "mdi-home"
   */
  icon?: string | null;
  
  /**
   * Ruta de redirección de la página
   * Ejemplo: "/usuarios", "/dashboard"
   */
  redirect?: string | null;
}

/**
 * Menú del sistema agrupado por módulo
 * 
 * Corresponde a: ModuloResponseDTO.java con páginas anidadas
 */
export interface Menu {
  /**
   * ID del módulo
   */
  id: number;
  
  /**
   * Nombre del módulo
   * Ejemplo: "Seguridad", "Ventas", "Inventario"
   */
  nombre: string;
  
  /**
   * Páginas que pertenecen a este módulo
   * Filtradas por permisos del usuario
   */
  paginas: Pagina[];
}

// ============================================
// LOGIN
// ============================================

/**
 * Request para login de usuario
 * 
 * Corresponde a: LoginRequestDTO.java
 * 
 * @example
 * ```typescript
 * const credentials: LoginRequest = {
 *   username: 'admin@prediomijangos.com',
 *   password: 'Admin123!'
 * };
 * ```
 */
export interface LoginRequest {
  /**
   * Username del usuario (puede ser email o username)
   * 
   * Backend valida con @NotBlank
   */
  username: string;
  
  /**
   * Contraseña del usuario
   * 
   * Backend valida con @NotBlank
   */
  password: string;
}

/**
 * Información básica del usuario autenticado
 * 
 * Corresponde a: UsuarioInfoDTO.java (anidado en LoginResponseDTO)
 */
export interface UsuarioInfo {
  /**
   * ID del usuario
   */
  id: number;
  
  /**
   * Username único del usuario
   */
  username: string;
  
  /**
   * Email del usuario
   */
  email: string;
  
  /**
   * Nombre completo del usuario (desde Persona)
   * Ejemplo: "Juan Carlos Pérez García"
   */
  nombreCompleto?: string;
  
  /**
   * Lista de roles del usuario
   * Ejemplo: ["ROLE_ADMIN", "ROLE_SUPERVISOR"]
   */
  roles: string[];
  
  /**
   * Indica si el usuario está activo
   * Si false, el usuario no puede autenticarse
   */
  activo: boolean;
}

/**
 * Response exitoso de login
 * 
 * Corresponde a: LoginResponseDTO.java
 * 
 * @example
 * ```typescript
 * const response: LoginResponse = {
 *   accessToken: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...',
 *   refreshToken: '550e8400-e29b-41d4-a716-446655440000',
 *   tokenType: 'Bearer',
 *   expiresIn: 28800,
 *   usuario: {
 *     id: 1,
 *     username: 'admin',
 *     email: 'admin@prediomijangos.com',
 *     nombreCompleto: 'Administrador del Sistema',
 *     roles: ['ROLE_ADMIN'],
 *     activo: true
 *   }
 * };
 * ```
 */
export interface LoginResponse {
  /**
   * Access token JWT
   * Usado en header Authorization: Bearer <accessToken>
   * Duración: 8 horas (28800 segundos)
   */
  accessToken: string;
  
  /**
   * Refresh token (UUID)
   * Usado para renovar el access token cuando expire
   * Duración: 7 días
   * Almacenado en BD
   */
  refreshToken: string;
  
  /**
   * Tipo de token (siempre "Bearer")
   */
  tokenType: string;
  
  /**
   * Tiempo de expiración del access token (en segundos)
   * Ejemplo: 28800 = 8 horas
   */
  expiresIn: number;
  
  /**
   * Información del usuario autenticado
   */
  usuario: UsuarioInfo;
}

// ============================================
// REFRESH TOKEN
// ============================================

/**
 * Request para renovar access token
 * 
 * Corresponde a: RefreshTokenRequestDTO.java
 * 
 * @example
 * ```typescript
 * const request: RefreshTokenRequest = {
 *   refreshToken: '550e8400-e29b-41d4-a716-446655440000'
 * };
 * ```
 */
export interface RefreshTokenRequest {
  /**
   * Refresh token previamente recibido en login
   * 
   * Backend valida:
   * - @NotBlank
   * - Que exista en BD
   * - Que no esté expirado
   */
  refreshToken: string;
}

/**
 * Response de renovación de token
 * 
 * Corresponde a: LoginResponseDTO.java (mismo DTO que login)
 * Retorna nuevos tokens y datos del usuario
 */
export type RefreshTokenResponse = LoginResponse;

// ============================================
// LOGOUT
// ============================================

/**
 * Request para cerrar sesión
 * 
 * Corresponde a: LogoutRequestDTO.java
 * 
 * @example
 * ```typescript
 * const request: LogoutRequest = {
 *   refreshToken: '550e8400-e29b-41d4-a716-446655440000'
 * };
 * ```
 */
export interface LogoutRequest {
  /**
   * Refresh token a invalidar
   * 
   * Backend valida:
   * - @NotBlank
   * - Elimina el token de la BD
   */
  refreshToken: string;
}

// ============================================
// SESIÓN DE USUARIO (Estado Frontend)
// ============================================

/**
 * Sesión del usuario en el frontend
 * Este tipo NO viene del backend, es para manejo de estado local
 * 
 * Combina datos de LoginResponse con información calculada
 */
export interface UserSession {
  /**
   * Access token JWT
   */
  accessToken: string;
  
  /**
   * Refresh token
   */
  refreshToken: string;
  
  /**
   * Información del usuario
   */
  user: UsuarioInfo;
  
  /**
   * Timestamp de cuando se creó la sesión (milisegundos)
   * Usado para calcular expiración
   */
  createdAt: number;
  
  /**
   * Timestamp de cuando expira el access token (milisegundos)
   * Calculado como: createdAt + (expiresIn * 1000)
   */
  expiresAt: number;
  
  /**
   * Menú dinámico del usuario (opcional)
   * Cargado después del login según permisos
   */
  menu?: Menu[];
}

// ============================================
// HELPERS Y UTILIDADES
// ============================================

/**
 * Estado de autenticación del usuario
 * Usado para renderizado condicional en UI
 */
export interface AuthState {
  /**
   * Si el usuario está autenticado
   */
  isAuthenticated: boolean;
  
  /**
   * Si se está cargando la sesión
   * (restaurando desde localStorage, haciendo login, etc.)
   */
  isLoading: boolean;
  
  /**
   * Usuario actual (null si no autenticado)
   */
  user: UsuarioInfo | null;
  
  /**
   * Access token (null si no autenticado)
   */
  accessToken: string | null;
  
  /**
   * Refresh token (null si no autenticado)
   */
  refreshToken: string | null;
  
  /**
   * Menú del usuario (null si no autenticado)
   */
  menu: Menu[] | null;
}

/**
 * Acciones de autenticación
 * Interfaz para las acciones disponibles en el contexto
 */
export interface AuthActions {
  /**
   * Iniciar sesión con credenciales
   * 
   * @throws Error si las credenciales son inválidas
   */
  login: (credentials: LoginRequest) => Promise<void>;
  
  /**
   * Cerrar sesión del usuario
   * Invalida el refresh token en el backend
   */
  logout: () => Promise<void>;
  
  /**
   * Renovar el access token usando el refresh token
   * 
   * @throws Error si el refresh token es inválido o expiró
   */
  refreshToken: () => Promise<void>;
}

/**
 * Contexto completo de autenticación
 * Combina estado y acciones
 */
export interface AuthContextValue extends AuthState, AuthActions {
  /**
   * Verificar si el usuario tiene un rol específico
   * 
   * @param role - Nombre del rol (ej: "ROLE_ADMIN")
   * @returns true si el usuario tiene el rol
   */
  hasRole: (role: string) => boolean;
  
  /**
   * Verificar si el usuario tiene un permiso específico
   * 
   * @param permission - Permiso a verificar (ej: "USUARIOS:CREATE")
   * @returns true si el usuario tiene el permiso
   */
  hasPermission: (permission: string) => boolean;
  
  /**
   * Verificar si el usuario puede ver una página
   * 
   * @param paginaNombre - Nombre de la página
   * @returns true si el usuario tiene acceso
   */
  canSeePage: (paginaNombre: string) => boolean;
}

// ============================================
// EXPORTS AGRUPADOS
// ============================================

/**
 * Tipos de Request
 */
export type AuthRequest = LoginRequest | RefreshTokenRequest | LogoutRequest;

/**
 * Tipos de Response
 */
export type AuthResponse = LoginResponse | RefreshTokenResponse;
