/**
 * ============================================
 * USER MAPPER - TRANSFORMACIONES DE USUARIOS
 * ============================================
 * 
 * Mappers para transformar datos de usuarios entre DTOs del backend y entidades de UI.
 * 
 * @module mappers/user
 * @author Predio Mijangos Dev Team
 * @version 2.0.0
 * 
 * SINCRONIZACIÓN CON BACKEND:
 * - Alineado con UsuarioResponseDTO del backend
 * - Maneja PersonaResponseDTO anidado
 * - Incluye campos de auditoría (createdAt, updatedAt)
 * - Compatible con UsuarioCreateDTO y UsuarioUpdateDTO
 * 
 * MEJORAS vs VERSIÓN ANTERIOR:
 * - Usa utilidades de base.mapper.ts
 * - Formatea fechas automáticamente
 * - Construye nombre completo
 * - Maneja campos de auditoría
 * - Documentación JSDoc completa
 * - Tipado estricto con TypeScript
 * 
 * @example
 * ```typescript
 * import { mapUsuarioDTOToUser, mapUsuariosDTOToUsers } from '@/mappers/user.mapper';
 * 
 * // En un servicio
 * const response = await apiService.get('/api/v1/usuarios/1');
 * const user = mapUsuarioDTOToUser(response.data.body);
 * ```
 */

import type {
  UsuarioResponseDTO,
  UsuarioListDTO,
  UsuarioCreateDTO,
  UsuarioUpdateDTO,
  PersonaResponseDTO,
  RolSimpleDTO,
} from '@/types/api.d';

import {
  type BaseMapper,
  type ArrayMapper,
  mapArray,
  mapOptional,
  formatAuditFields,
  buildFullName,
  getInitials,
  cleanString,
  emptyToNull,
  nullToEmpty,
} from './base.mapper';

// ============================================
// TIPOS DE UI (FRONTEND)
// ============================================

/**
 * Modelo de Usuario para UI
 * Versión enriquecida del DTO del backend con campos computados
 * 
 * @interface User
 * 
 * @property id - ID único del usuario
 * @property username - Nombre de usuario (único)
 * @property email - Email del usuario
 * @property activo - Estado del usuario
 * @property persona - Datos personales completos
 * @property roles - Roles asignados
 * @property isAdmin - Indica si tiene rol de administrador
 * @property nombreCompleto - Nombre completo computado
 * @property iniciales - Iniciales computadas
 * @property createdAt - Fecha de creación formateada
 * @property updatedAt - Fecha de última actualización formateada
 */
export interface User {
  // Identificación
  id: number;
  username: string;
  email: string;
  activo: boolean;
  
  // Datos personales
  persona: {
    id: number;
    primerNombre: string;
    segundoNombre: string | null;
    primerApellido: string;
    segundoApellido: string | null;
    dpi: string | null;
    nit: string | null;
    telefono: string | null;
    fechaNacimiento: string | null; // Formato DD/MM/YYYY
    direccion: string | null;
    // Ubicación
    departamento: { id: number; nombre: string } | null;
    municipio: { id: number; nombre: string } | null;
  };
  
  // Roles y permisos
  roles: {
    id: number;
    nombre: string;
    codigo: string;
  }[];
  isAdmin: boolean;
  
  // Campos computados (solo en UI)
  nombreCompleto: string;
  iniciales: string;
  
  // Auditoría
  createdAt: string; // Formateada para UI
  updatedAt: string | null; // Formateada para UI
}

/**
 * Modelo simplificado de Usuario para listados
 * Versión ligera para tablas y selects
 * 
 * @interface UserList
 */
export interface UserList {
  id: number;
  username: string;
  nombreCompleto: string;
  email: string;
  activo: boolean;
  rolesCount: number;
  isAdmin: boolean;
}

/**
 * Datos de formulario para crear/editar usuario
 * Estructura que usan los forms con React Hook Form
 * 
 * @interface UserFormData
 */
export interface UserFormData {
  // Credenciales
  username: string;
  email: string;
  password?: string; // Requerido en create, opcional en update
  activo: boolean;
  
  // Datos personales
  primerNombre: string;
  segundoNombre: string;
  primerApellido: string;
  segundoApellido: string;
  dpi: string;
  nit: string;
  telefono: string;
  fechaNacimiento: string; // YYYY-MM-DD
  direccion: string;
  departamentoId: number | null;
  municipioId: number | null;
  
  // Roles
  roleIds: number[];
}

// ============================================
// MAPPERS: DTO → UI
// ============================================

/**
 * Convierte UsuarioResponseDTO del backend a User del frontend
 * 
 * Transformaciones principales:
 * - Formatea fechas de auditoría
 * - Construye nombre completo
 * - Genera iniciales
 * - Normaliza estructura de roles
 * - Detecta si es administrador
 * 
 * @param dto - DTO del backend
 * @returns Entidad User para UI
 * 
 * @example
 * ```typescript
 * const user = mapUsuarioDTOToUser(dto);
 * console.log(user.nombreCompleto); // "Juan Pérez López"
 * console.log(user.iniciales); // "JP"
 * console.log(user.isAdmin); // true
 * ```
 */
export const mapUsuarioDTOToUser: BaseMapper<UsuarioResponseDTO, User> = (dto) => {
  const persona = dto.persona;
  const audit = formatAuditFields(dto);
  
  // Construir nombre completo
  const nombreCompleto = buildFullName(
    persona.primerNombre,
    persona.segundoNombre,
    persona.primerApellido,
    persona.segundoApellido
  );
  
  // Generar iniciales
  const iniciales = getInitials(nombreCompleto);
  
  // Detectar si es administrador
  const isAdmin = dto.roles.some(
    (rol) => rol.codigo === 'ROLE_ADMIN' || rol.admin === true
  );
  
  return {
    // Identificación
    id: dto.id,
    username: dto.username,
    email: persona.email || '',
    activo: dto.activo,
    
    // Datos personales
    persona: {
      id: persona.id,
      primerNombre: persona.primerNombre,
      segundoNombre: persona.segundoNombre,
      primerApellido: persona.primerApellido,
      segundoApellido: persona.segundoApellido,
      dpi: persona.dpi,
      nit: persona.nit,
      telefono: persona.telefono,
      fechaNacimiento: persona.fechaNacimiento
        ? formatDateOnly(persona.fechaNacimiento)
        : null,
      direccion: persona.direccion,
      departamento: persona.departamento
        ? {
            id: persona.departamento.id,
            nombre: persona.departamento.nombre,
          }
        : null,
      municipio: persona.municipio
        ? {
            id: persona.municipio.id,
            nombre: persona.municipio.nombre,
          }
        : null,
    },
    
    // Roles
    roles: dto.roles.map((rol) => ({
      id: rol.id,
      nombre: rol.nombre,
      codigo: rol.codigo,
    })),
    isAdmin,
    
    // Campos computados
    nombreCompleto,
    iniciales,
    
    // Auditoría
    createdAt: audit.createdAt,
    updatedAt: audit.updatedAt,
  };
};

/**
 * Convierte array de UsuarioResponseDTO a array de User
 * 
 * @param dtos - Array de DTOs del backend
 * @returns Array de entidades User para UI
 * 
 * @example
 * ```typescript
 * const users = mapUsuariosDTOToUsers(response.data.body.content);
 * ```
 */
export const mapUsuariosDTOToUsers: ArrayMapper<UsuarioResponseDTO, User> =
  mapArray(mapUsuarioDTOToUser);

/**
 * Convierte UsuarioListDTO a UserList
 * Para listados optimizados con menos datos
 * 
 * @param dto - DTO simplificado del backend
 * @returns Entidad UserList para tablas
 * 
 * @example
 * ```typescript
 * const userList = mapUsuarioListDTOToUserList(dto);
 * ```
 */
export const mapUsuarioListDTOToUserList: BaseMapper<UsuarioListDTO, UserList> = (dto) => {
  const nombreCompleto = buildFullName(
    dto.persona.primerNombre,
    dto.persona.segundoNombre,
    dto.persona.primerApellido,
    dto.persona.segundoApellido
  );
  
  const isAdmin = dto.roles.some(
    (rol) => rol.codigo === 'ROLE_ADMIN' || rol.admin === true
  );
  
  return {
    id: dto.id,
    username: dto.username,
    nombreCompleto,
    email: dto.persona.email || '',
    activo: dto.activo,
    rolesCount: dto.roles.length,
    isAdmin,
  };
};

/**
 * Convierte array de UsuarioListDTO a array de UserList
 * 
 * @param dtos - Array de DTOs simplificados
 * @returns Array de entidades UserList
 */
export const mapUsuariosListDTOToUsersList: ArrayMapper<UsuarioListDTO, UserList> =
  mapArray(mapUsuarioListDTOToUserList);

/**
 * Convierte UsuarioResponseDTO opcional a User opcional
 * Útil para endpoints que pueden retornar null
 * 
 * @param dto - DTO del backend o null
 * @returns User o null
 * 
 * @example
 * ```typescript
 * const user = mapOptionalUser(dto); // User | null
 * ```
 */
export const mapOptionalUser = mapOptional(mapUsuarioDTOToUser);

// ============================================
// MAPPERS: UI → DTO (FORM → BACKEND)
// ============================================

/**
 * Convierte UserFormData a UsuarioCreateDTO para el backend
 * 
 * Transformaciones:
 * - Limpia strings (trim, normaliza espacios)
 * - Convierte valores vacíos a null
 * - Normaliza emails a lowercase
 * - Estructura PersonaCreateDTO anidado
 * 
 * @param formData - Datos del formulario
 * @returns DTO listo para enviar al backend
 * 
 * @example
 * ```typescript
 * const createDTO = mapUserFormToCreateDTO(formValues);
 * await usuarioService.create(createDTO);
 * ```
 */
export const mapUserFormToCreateDTO: BaseMapper<UserFormData, UsuarioCreateDTO> = (
  formData
) => {
  return {
    // Credenciales
    username: cleanString(formData.username) || '',
    password: formData.password || '',
    
    // Persona anidada
    persona: {
      primerNombre: cleanString(formData.primerNombre) || '',
      segundoNombre: emptyToNull(cleanString(formData.segundoNombre)),
      primerApellido: cleanString(formData.primerApellido) || '',
      segundoApellido: emptyToNull(cleanString(formData.segundoApellido)),
      email: cleanString(formData.email, true) || '', // Normalizar email
      dpi: emptyToNull(cleanString(formData.dpi)),
      nit: emptyToNull(cleanString(formData.nit)),
      telefono: emptyToNull(cleanString(formData.telefono)),
      fechaNacimiento: emptyToNull(formData.fechaNacimiento),
      direccion: emptyToNull(cleanString(formData.direccion)),
      departamentoId: formData.departamentoId,
      municipioId: formData.municipioId,
    },
    
    // Roles
    roleIds: formData.roleIds,
  };
};

/**
 * Convierte UserFormData a UsuarioUpdateDTO para el backend
 * Similar a create pero sin password obligatorio
 * 
 * @param formData - Datos del formulario
 * @returns DTO listo para actualizar en backend
 * 
 * @example
 * ```typescript
 * const updateDTO = mapUserFormToUpdateDTO(formValues);
 * await usuarioService.update(userId, updateDTO);
 * ```
 */
export const mapUserFormToUpdateDTO: BaseMapper<UserFormData, UsuarioUpdateDTO> = (
  formData
) => {
  return {
    // Credenciales (password opcional en update)
    username: cleanString(formData.username) || '',
    password: emptyToNull(formData.password), // Puede ser null
    
    // Persona anidada
    persona: {
      primerNombre: cleanString(formData.primerNombre) || '',
      segundoNombre: emptyToNull(cleanString(formData.segundoNombre)),
      primerApellido: cleanString(formData.primerApellido) || '',
      segundoApellido: emptyToNull(cleanString(formData.segundoApellido)),
      email: cleanString(formData.email, true) || '',
      dpi: emptyToNull(cleanString(formData.dpi)),
      nit: emptyToNull(cleanString(formData.nit)),
      telefono: emptyToNull(cleanString(formData.telefono)),
      fechaNacimiento: emptyToNull(formData.fechaNacimiento),
      direccion: emptyToNull(cleanString(formData.direccion)),
      departamentoId: formData.departamentoId,
      municipioId: formData.municipioId,
    },
    
    // Roles
    roleIds: formData.roleIds,
    
    // Estado
    activo: formData.activo,
  };
};

// ============================================
// MAPPERS: DTO → FORM (PARA EDICIÓN)
// ============================================

/**
 * Convierte User a UserFormData
 * Útil para poblar formularios de edición
 * 
 * Transformaciones:
 * - Convierte null a string vacío para inputs
 * - Formatea fecha para input type="date"
 * - Extrae IDs de relaciones anidadas
 * 
 * @param user - Entidad User de UI
 * @returns Datos listos para React Hook Form
 * 
 * @example
 * ```typescript
 * const formValues = mapUserToFormData(user);
 * form.reset(formValues); // Poblar formulario
 * ```
 */
export const mapUserToFormData: BaseMapper<User, UserFormData> = (user) => {
  return {
    // Credenciales
    username: user.username,
    email: user.email,
    password: '', // Nunca prellenar password
    activo: user.activo,
    
    // Datos personales
    primerNombre: user.persona.primerNombre,
    segundoNombre: nullToEmpty(user.persona.segundoNombre),
    primerApellido: user.persona.primerApellido,
    segundoApellido: nullToEmpty(user.persona.segundoApellido),
    dpi: nullToEmpty(user.persona.dpi),
    nit: nullToEmpty(user.persona.nit),
    telefono: nullToEmpty(user.persona.telefono),
    fechaNacimiento: user.persona.fechaNacimiento
      ? formatDateForInput(user.persona.fechaNacimiento)
      : '',
    direccion: nullToEmpty(user.persona.direccion),
    departamentoId: user.persona.departamento?.id || null,
    municipioId: user.persona.municipio?.id || null,
    
    // Roles
    roleIds: user.roles.map((rol) => rol.id),
  };
};

// ============================================
// UTILIDADES AUXILIARES
// ============================================

/**
 * Formatea fecha solo como DD/MM/YYYY (sin hora)
 * 
 * @param isoDate - Fecha ISO 8601
 * @returns Fecha formateada DD/MM/YYYY
 */
const formatDateOnly = (isoDate: string): string => {
  try {
    const date = new Date(isoDate);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  } catch {
    return isoDate;
  }
};

/**
 * Formatea fecha para input type="date" (YYYY-MM-DD)
 * 
 * @param dateStr - Fecha en formato DD/MM/YYYY
 * @returns Fecha en formato YYYY-MM-DD
 */
const formatDateForInput = (dateStr: string): string => {
  try {
    // Si viene en formato DD/MM/YYYY
    const [day, month, year] = dateStr.split('/');
    return `${year}-${month}-${day}`;
  } catch {
    return dateStr;
  }
};

/**
 * Valida que un UsuarioResponseDTO tenga estructura mínima requerida
 * 
 * @param dto - DTO a validar
 * @returns true si es válido
 */
export const isValidUsuarioDTO = (dto: any): dto is UsuarioResponseDTO => {
  return (
    dto &&
    typeof dto === 'object' &&
    typeof dto.id === 'number' &&
    typeof dto.username === 'string' &&
    typeof dto.activo === 'boolean' &&
    dto.persona &&
    typeof dto.persona === 'object' &&
    Array.isArray(dto.roles)
  );
};

// ============================================
// EXPORTACIONES POR DEFECTO
// ============================================

/**
 * Objeto con todos los mappers de usuarios
 * Útil para importar todo de una vez
 * 
 * @example
 * ```typescript
 * import userMappers from '@/mappers/user.mapper';
 * 
 * const user = userMappers.dtoToUser(dto);
 * const createDTO = userMappers.formToCreateDTO(formData);
 * ```
 */
export default {
  // DTO → UI
  dtoToUser: mapUsuarioDTOToUser,
  dtosToUsers: mapUsuariosDTOToUsers,
  listDtoToUserList: mapUsuarioListDTOToUserList,
  listDtosToUsersList: mapUsuariosListDTOToUsersList,
  optionalUser: mapOptionalUser,
  
  // Form → DTO
  formToCreateDTO: mapUserFormToCreateDTO,
  formToUpdateDTO: mapUserFormToUpdateDTO,
  
  // DTO → Form
  userToFormData: mapUserToFormData,
  
  // Validación
  isValidDTO: isValidUsuarioDTO,
};
