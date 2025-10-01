/** -------------------------
 *  Tipos base del dominio
 *  ------------------------- */

export interface Pagina {
  id: number;
  nombre: string;
  movil: boolean;        // para filtrar menú Web/App
  icon?: string | null;
  redirect?: string | null;
}

export interface Menu {
  id: number;
  nombre: string;
  paginas: Pagina[];     // el backend nos envía un menú con páginas por módulo
}

/** -------------------------
 *  Contratos de autenticación
 *  ------------------------- */

export interface LoginRequest {
  usuario: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  usuario: string;
  roles: string[];
  admin: boolean;      
  menu: Menu[];    // menú dinámico por roles (web/app se filtra por 'movil')
  exp?: number;
}