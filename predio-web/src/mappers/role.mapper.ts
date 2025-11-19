/**
 * ============================================
 * ROLE MAPPER - TRANSFORMACIONES DE ROLES
 * ============================================
 * 
 * Mappers para transformar datos de roles entre DTOs del backend y entidades de UI.
 * 
 * @module mappers/role
 * @author Predio Mijangos Dev Team
 * @version 2.0.0
 * 
 * SINCRONIZACIÓN CON BACKEND:
 * - Alineado con RolResponseDTO del backend
 * - Maneja PaginaMenuDTO para permisos
 * - Incluye campos de auditoría
 * - Compatible con RolCreateDTO y RolUpdateDTO
 * 
 * CARACTERÍSTICAS:
 * - Agrupa permisos por módulo
 * - Genera código único de rol
 * - Formatea fechas automáticamente
 * - Validaciones de estructura
 * 
 * @example
 * ```typescript
 * import { mapRolDTOToRole, mapRolesDTOToRoles } from '@/mappers/role.mapper';
 * 
 * const role = mapRolDTOToRole(dto);
 * console.log(role.permissionsCount); // 15
 * console.log(role.moduleGroups); // Permisos agrupados por módulo
 * ```
 */

import type {
  RolResponseDTO,
  RolListDTO,
  RolCreateDTO,
  RolUpdateDTO,
  PaginaMenuDTO,
} from '@/types/api.d';

import {
  type BaseMapper,
  type ArrayMapper,
  mapArray,
  mapOptional,
  formatAuditFields,
  cleanString,
  emptyToNull,
  nullToEmpty,
} from './base.mapper';

// ============================================
// TIPOS DE UI (FRONTEND)
// ============================================

/**
 * Modelo de Rol para UI
 * Versión enriquecida del DTO con permisos agrupados y campos computados
 * 
 * @interface Role
 * 
 * @property id - ID único del rol
 * @property nombre - Nombre descriptivo del rol
 * @property codigo - Código único (formato: ROLE_NOMBRE)
 * @property descripcion - Descripción del rol
 * @property activo - Estado del rol
 * @property admin - Indica si es rol de administrador
 * @property paginas - Permisos/páginas asignadas
 * @property permissionsCount - Cantidad total de permisos
 * @property moduleGroups - Permisos agrupados por módulo
 * @property createdAt - Fecha de creación formateada
 * @property updatedAt - Fecha de actualización formateada
 */
export interface Role {
  // Identificación
  id: number;
  nombre: string;
  codigo: string;
  descripcion: string | null;
  activo: boolean;
  admin: boolean;
  
  // Permisos
  paginas: {
    id: number;
    nombre: string;
    url: string;
    icono: string | null;
    moduloNombre: string;
    orden: number;
  }[];
  
  // Campos computados
  permissionsCount: number;
  moduleGroups: {
    moduleName: string;
    permissions: string[];
    count: number;
  }[];
  
  // Auditoría
  createdAt: string;
  updatedAt: string | null;
}

/**
 * Modelo simplificado de Rol para listados
 * 
 * @interface RoleList
 */
export interface RoleList {
  id: number;
  nombre: string;
  codigo: string;
  activo: boolean;
  admin: boolean;
  permissionsCount: number;
}

/**
 * Datos de formulario para crear/editar rol
 * 
 * @interface RoleFormData
 */
export interface RoleFormData {
  nombre: string;
  codigo: string;
  descripcion: string;
  activo: boolean;
  admin: boolean;
  paginaIds: number[]; // IDs de páginas (permisos) asignados
}

/**
 * Permiso agrupado por módulo
 * Estructura para visualización organizada en UI
 * 
 * @interface ModuleGroup
 */
export interface ModuleGroup {
  moduleName: string;
  permissions: string[];
  count: number;
}

// ============================================
// MAPPERS: DTO → UI
// ============================================

/**
 * Convierte RolResponseDTO del backend a Role del frontend
 * 
 * Transformaciones:
 * - Formatea fechas de auditoría
 * - Cuenta permisos totales
 * - Agrupa permisos por módulo
 * - Normaliza estructura de páginas
 * 
 * @param dto - DTO del backend
 * @returns Entidad Role para UI
 * 
 * @example
 * ```typescript
 * const role = mapRolDTOToRole(dto);
 * console.log(role.permissionsCount); // 15
 * console.log(role.moduleGroups); // Array de grupos por módulo
 * ```
 */
export const mapRolDTOToRole: BaseMapper<RolResponseDTO, Role> = (dto) => {
  const audit = formatAuditFields(dto);
  
  // Normalizar páginas
  const paginas = (dto.paginas || []).map((pagina) => ({
    id: pagina.id,
    nombre: pagina.nombre,
    url: pagina.url,
    icono: pagina.icono,
    moduloNombre: pagina.modulo?.nombre || 'Sin módulo',
    orden: pagina.orden || 0,
  }));
  
  // Contar permisos
  const permissionsCount = paginas.length;
  
  // Agrupar permisos por módulo
  const moduleGroups = groupPermissionsByModule(paginas);
  
  return {
    // Identificación
    id: dto.id,
    nombre: dto.nombre,
    codigo: dto.codigo,
    descripcion: dto.descripcion,
    activo: dto.activo,
    admin: dto.admin || false,
    
    // Permisos
    paginas,
    
    // Campos computados
    permissionsCount,
    moduleGroups,
    
    // Auditoría
    createdAt: audit.createdAt,
    updatedAt: audit.updatedAt,
  };
};

/**
 * Convierte array de RolResponseDTO a array de Role
 * 
 * @param dtos - Array de DTOs del backend
 * @returns Array de entidades Role para UI
 */
export const mapRolesDTOToRoles: ArrayMapper<RolResponseDTO, Role> =
  mapArray(mapRolDTOToRole);

/**
 * Convierte RolListDTO a RoleList
 * Para listados optimizados
 * 
 * @param dto - DTO simplificado del backend
 * @returns Entidad RoleList para tablas
 */
export const mapRolListDTOToRoleList: BaseMapper<RolListDTO, RoleList> = (dto) => {
  return {
    id: dto.id,
    nombre: dto.nombre,
    codigo: dto.codigo,
    activo: dto.activo,
    admin: dto.admin || false,
    permissionsCount: dto.paginasCount || 0,
  };
};

/**
 * Convierte array de RolListDTO a array de RoleList
 */
export const mapRolesListDTOToRolesList: ArrayMapper<RolListDTO, RoleList> =
  mapArray(mapRolListDTOToRoleList);

/**
 * Convierte RolResponseDTO opcional a Role opcional
 */
export const mapOptionalRole = mapOptional(mapRolDTOToRole);

// ============================================
// MAPPERS: UI → DTO (FORM → BACKEND)
// ============================================

/**
 * Convierte RoleFormData a RolCreateDTO para el backend
 * 
 * Transformaciones:
 * - Limpia strings
 * - Convierte valores vacíos a null
 * - Genera código si no existe
 * - Valida estructura
 * 
 * @param formData - Datos del formulario
 * @returns DTO listo para enviar al backend
 * 
 * @example
 * ```typescript
 * const createDTO = mapRoleFormToCreateDTO(formValues);
 * await rolService.create(createDTO);
 * ```
 */
export const mapRoleFormToCreateDTO: BaseMapper<RoleFormData, RolCreateDTO> = (
  formData
) => {
  return {
    nombre: cleanString(formData.nombre) || '',
    codigo: generateRoleCode(formData.nombre, formData.codigo),
    descripcion: emptyToNull(cleanString(formData.descripcion)),
    activo: formData.activo,
    admin: formData.admin,
    paginaIds: formData.paginaIds,
  };
};

/**
 * Convierte RoleFormData a RolUpdateDTO para el backend
 * 
 * @param formData - Datos del formulario
 * @returns DTO listo para actualizar en backend
 */
export const mapRoleFormToUpdateDTO: BaseMapper<RoleFormData, RolUpdateDTO> = (
  formData
) => {
  return {
    nombre: cleanString(formData.nombre) || '',
    codigo: generateRoleCode(formData.nombre, formData.codigo),
    descripcion: emptyToNull(cleanString(formData.descripcion)),
    activo: formData.activo,
    admin: formData.admin,
    paginaIds: formData.paginaIds,
  };
};

// ============================================
// MAPPERS: DTO → FORM (PARA EDICIÓN)
// ============================================

/**
 * Convierte Role a RoleFormData
 * Útil para poblar formularios de edición
 * 
 * @param role - Entidad Role de UI
 * @returns Datos listos para React Hook Form
 * 
 * @example
 * ```typescript
 * const formValues = mapRoleToFormData(role);
 * form.reset(formValues); // Poblar formulario
 * ```
 */
export const mapRoleToFormData: BaseMapper<Role, RoleFormData> = (role) => {
  return {
    nombre: role.nombre,
    codigo: role.codigo,
    descripcion: nullToEmpty(role.descripcion),
    activo: role.activo,
    admin: role.admin,
    paginaIds: role.paginas.map((p) => p.id),
  };
};

// ============================================
// UTILIDADES AUXILIARES
// ============================================

/**
 * Agrupa permisos (páginas) por módulo
 * 
 * @param paginas - Array de páginas/permisos
 * @returns Array de grupos por módulo
 * 
 * @example
 * ```typescript
 * const groups = groupPermissionsByModule(paginas);
 * // [
 * //   { moduleName: "Usuarios", permissions: ["Ver", "Crear"], count: 2 },
 * //   { moduleName: "Ventas", permissions: ["Ver"], count: 1 }
 * // ]
 * ```
 */
const groupPermissionsByModule = (
  paginas: Role['paginas']
): ModuleGroup[] => {
  const grouped = new Map<string, string[]>();
  
  paginas.forEach((pagina) => {
    const moduleName = pagina.moduloNombre;
    const permissions = grouped.get(moduleName) || [];
    permissions.push(pagina.nombre);
    grouped.set(moduleName, permissions);
  });
  
  return Array.from(grouped.entries())
    .map(([moduleName, permissions]) => ({
      moduleName,
      permissions,
      count: permissions.length,
    }))
    .sort((a, b) => a.moduleName.localeCompare(b.moduleName));
};

/**
 * Genera código de rol a partir del nombre
 * Formato: ROLE_NOMBRE_EN_MAYUSCULAS
 * 
 * @param nombre - Nombre del rol
 * @param existingCode - Código existente (si ya tiene uno)
 * @returns Código generado o existente
 * 
 * @example
 * ```typescript
 * generateRoleCode("Vendedor", "") // "ROLE_VENDEDOR"
 * generateRoleCode("Gerente de Ventas", "") // "ROLE_GERENTE_VENTAS"
 * generateRoleCode("Admin", "ROLE_ADMIN") // "ROLE_ADMIN" (mantiene existente)
 * ```
 */
const generateRoleCode = (nombre: string, existingCode?: string): string => {
  // Si ya tiene código, mantenerlo
  if (existingCode && existingCode.trim() !== '') {
    return existingCode.trim().toUpperCase();
  }
  
  // Generar código desde el nombre
  const normalized = nombre
    .trim()
    .toUpperCase()
    .replace(/[^A-Z0-9\s]/g, '') // Remover caracteres especiales
    .replace(/\s+/g, '_'); // Espacios a underscores
  
  // Agregar prefijo ROLE_ si no lo tiene
  return normalized.startsWith('ROLE_') ? normalized : `ROLE_${normalized}`;
};

/**
 * Valida que un RolResponseDTO tenga estructura mínima requerida
 * 
 * @param dto - DTO a validar
 * @returns true si es válido
 */
export const isValidRolDTO = (dto: any): dto is RolResponseDTO => {
  return (
    dto &&
    typeof dto === 'object' &&
    typeof dto.id === 'number' &&
    typeof dto.nombre === 'string' &&
    typeof dto.codigo === 'string' &&
    typeof dto.activo === 'boolean' &&
    Array.isArray(dto.paginas)
  );
};

/**
 * Extrae IDs de páginas de un array de PaginaMenuDTO
 * Útil para poblar formularios
 * 
 * @param paginas - Array de páginas
 * @returns Array de IDs
 */
export const extractPaginaIds = (paginas: PaginaMenuDTO[]): number[] => {
  return paginas.map((p) => p.id);
};

/**
 * Filtra roles activos de un array
 * 
 * @param roles - Array de roles
 * @returns Array solo con roles activos
 */
export const filterActiveRoles = (roles: Role[]): Role[] => {
  return roles.filter((role) => role.activo);
};

/**
 * Filtra roles por búsqueda de texto
 * Busca en nombre, código y descripción
 * 
 * @param roles - Array de roles
 * @param searchText - Texto a buscar
 * @returns Array filtrado
 */
export const filterRolesByText = (roles: Role[], searchText: string): Role[] => {
  if (!searchText || searchText.trim() === '') return roles;
  
  const search = searchText.toLowerCase().trim();
  
  return roles.filter(
    (role) =>
      role.nombre.toLowerCase().includes(search) ||
      role.codigo.toLowerCase().includes(search) ||
      (role.descripcion && role.descripcion.toLowerCase().includes(search))
  );
};

/**
 * Ordena roles alfabéticamente por nombre
 * 
 * @param roles - Array de roles
 * @param ascending - Orden ascendente (default: true)
 * @returns Array ordenado
 */
export const sortRolesByName = (roles: Role[], ascending = true): Role[] => {
  return [...roles].sort((a, b) => {
    const comparison = a.nombre.localeCompare(b.nombre);
    return ascending ? comparison : -comparison;
  });
};

// ============================================
// EXPORTACIONES POR DEFECTO
// ============================================

/**
 * Objeto con todos los mappers de roles
 * 
 * @example
 * ```typescript
 * import roleMappers from '@/mappers/role.mapper';
 * 
 * const role = roleMappers.dtoToRole(dto);
 * const createDTO = roleMappers.formToCreateDTO(formData);
 * ```
 */
export default {
  // DTO → UI
  dtoToRole: mapRolDTOToRole,
  dtosToRoles: mapRolesDTOToRoles,
  listDtoToRoleList: mapRolListDTOToRoleList,
  listDtosToRolesList: mapRolesListDTOToRolesList,
  optionalRole: mapOptionalRole,
  
  // Form → DTO
  formToCreateDTO: mapRoleFormToCreateDTO,
  formToUpdateDTO: mapRoleFormToUpdateDTO,
  
  // DTO → Form
  roleToFormData: mapRoleToFormData,
  
  // Utilidades
  extractPaginaIds,
  filterActiveRoles,
  filterRolesByText,
  sortRolesByName,
  
  // Validación
  isValidDTO: isValidRolDTO,
};
