/**
 * ============================================
 * MAPPERS - EXPORTACIONES CENTRALIZADAS
 * ============================================
 * 
 * Punto único de exportación para todos los mappers del sistema.
 * Facilita las importaciones y mantiene consistencia.
 * 
 * @module mappers
 * @author Predio Mijangos Dev Team
 * @version 1.0.0
 * 
 * USO:
 * ```typescript
 * // Importar mappers específicos
 * import { mapUsuarioDTOToUser, mapRolDTOToRole } from '@/mappers';
 * 
 * // Importar todos los mappers de un dominio
 * import { userMappers, roleMappers } from '@/mappers';
 * 
 * // Importar utilidades base
 * import { mapArray, formatAuditFields } from '@/mappers';
 * ```
 */

// ============================================
// UTILIDADES BASE
// ============================================

export {
  // Tipos
  type BaseMapper,
  type ArrayMapper,
  type OptionalMapper,
  type AuditFields,
  type FormattedAuditFields,
  type WithAudit,
  type Optional,
  type MapperResult,
  
  // Funciones de mapeo
  mapArray,
  mapOptional,
  mapOptionalArray,
  safeMap,
  
  // Funciones de formateo
  formatAuditFields,
  formatDateForUI,
  formatDateForBackend,
  formatDateOnly,
  
  // Funciones de transformación
  buildFullName,
  getInitials,
  cleanString,
  emptyToNull,
  nullToEmpty,
  
  // Funciones de validación
  hasValidAudit,
  isValidId,
  areValidIds,
} from './base.mapper';

// ============================================
// MAPPERS DE USUARIOS
// ============================================

export {
  // Tipos de UI
  type User,
  type UserList,
  type UserFormData,
  
  // Mappers individuales: DTO → UI
  mapUsuarioDTOToUser,
  mapUsuarioDTOToUser as mapUserDTOToUser, // Alias en inglés
  
  // Mappers de arrays: DTO → UI
  mapUsuariosDTOToUsers,
  mapUsuariosDTOToUsers as mapUsersDTOToUsers, // Alias en inglés
  
  // Mappers de listas: ListDTO → UI
  mapUsuarioListDTOToUserList,
  mapUsuariosListDTOToUsersList,
  
  // Mappers opcionales
  mapOptionalUser,
  
  // Mappers: Form → DTO
  mapUserFormToCreateDTO,
  mapUserFormToUpdateDTO,
  
  // Mappers: UI → Form
  mapUserToFormData,
  
  // Validación
  isValidUsuarioDTO,
  
  // Default export como objeto
  default as userMappers,
} from './user.mapper';

// ============================================
// MAPPERS DE ROLES
// ============================================

export {
  // Tipos de UI
  type Role,
  type RoleList,
  type RoleFormData,
  type ModuleGroup,
  
  // Mappers individuales: DTO → UI
  mapRolDTOToRole,
  mapRolDTOToRole as mapRoleDTOToRole, // Alias en inglés
  
  // Mappers de arrays: DTO → UI
  mapRolesDTOToRoles,
  
  // Mappers de listas: ListDTO → UI
  mapRolListDTOToRoleList,
  mapRolesListDTOToRolesList,
  
  // Mappers opcionales
  mapOptionalRole,
  
  // Mappers: Form → DTO
  mapRoleFormToCreateDTO,
  mapRoleFormToUpdateDTO,
  
  // Mappers: UI → Form
  mapRoleToFormData,
  
  // Utilidades
  extractPaginaIds,
  filterActiveRoles,
  filterRolesByText,
  sortRolesByName,
  
  // Validación
  isValidRolDTO,
  
  // Default export como objeto
  default as roleMappers,
} from './role.mapper';

// ============================================
// RE-EXPORTACIONES PARA COMPATIBILIDAD
// ============================================

/**
 * Alias en inglés para mantener compatibilidad con código existente
 * 
 * @deprecated Preferir nombres en español alineados con backend
 */

// Usuarios
export {
  mapUsuarioDTOToUser as mapDTOToUser,
  mapUsuariosDTOToUsers as mapDTOsToUsers,
} from './user.mapper';

// Roles
export {
  mapRolDTOToRole as mapDTOToRole,
  mapRolesDTOToRoles as mapDTOsToRoles,
} from './role.mapper';

// ============================================
// TIPOS GLOBALES DE MAPPERS
// ============================================

/**
 * Tipo genérico para cualquier mapper del sistema
 */
export type AnyMapper<TSource = any, TTarget = any> = (source: TSource) => TTarget;

/**
 * Tipo para mapper bidireccional
 */
export interface BiMapper<TSource, TTarget> {
  toTarget: (source: TSource) => TTarget;
  toSource: (target: TTarget) => TSource;
}

/**
 * Tipo para mapper con validación
 */
export interface ValidatedMapper<TSource, TTarget> {
  map: (source: TSource) => TTarget;
  validate: (source: TSource) => boolean;
}

// ============================================
// REGISTRO DE MAPPERS (PARA FUTURO)
// ============================================

/**
 * Registro centralizado de mappers por dominio
 * Útil para aplicaciones grandes con muchos mappers
 * 
 * @example
 * ```typescript
 * const mapper = MapperRegistry.get('user', 'dtoToEntity');
 * const user = mapper(dto);
 * ```
 */
export class MapperRegistry {
  private static mappers = new Map<string, Map<string, AnyMapper>>();
  
  /**
   * Registra un mapper
   * 
   * @param domain - Dominio (ej: 'user', 'role')
   * @param name - Nombre del mapper (ej: 'dtoToEntity')
   * @param mapper - Función mapper
   */
  static register<TSource, TTarget>(
    domain: string,
    name: string,
    mapper: AnyMapper<TSource, TTarget>
  ): void {
    if (!this.mappers.has(domain)) {
      this.mappers.set(domain, new Map());
    }
    this.mappers.get(domain)!.set(name, mapper);
  }
  
  /**
   * Obtiene un mapper registrado
   * 
   * @param domain - Dominio
   * @param name - Nombre del mapper
   * @returns Función mapper o undefined
   */
  static get<TSource, TTarget>(
    domain: string,
    name: string
  ): AnyMapper<TSource, TTarget> | undefined {
    return this.mappers.get(domain)?.get(name) as AnyMapper<TSource, TTarget> | undefined;
  }
  
  /**
   * Verifica si existe un mapper
   * 
   * @param domain - Dominio
   * @param name - Nombre del mapper
   * @returns true si existe
   */
  static has(domain: string, name: string): boolean {
    return this.mappers.get(domain)?.has(name) || false;
  }
  
  /**
   * Lista todos los mappers de un dominio
   * 
   * @param domain - Dominio
   * @returns Array de nombres de mappers
   */
  static list(domain: string): string[] {
    const domainMappers = this.mappers.get(domain);
    return domainMappers ? Array.from(domainMappers.keys()) : [];
  }
  
  /**
   * Limpia el registro
   */
  static clear(): void {
    this.mappers.clear();
  }
}

// ============================================
// INICIALIZACIÓN (OPCIONAL)
// ============================================

/**
 * Inicializa el registro con mappers básicos
 * Solo si se quiere usar el sistema de registro
 * 
 * @example
 * ```typescript
 * import { initializeMappers } from '@/mappers';
 * initializeMappers();
 * ```
 */
export function initializeMappers(): void {
  // Importar mappers dinámicamente solo si se necesitan
  import('./user.mapper').then((userMappers) => {
    MapperRegistry.register('user', 'dtoToEntity', userMappers.mapUsuarioDTOToUser);
    MapperRegistry.register('user', 'entitiesToDTOs', userMappers.mapUsuariosDTOToUsers);
    MapperRegistry.register('user', 'formToCreateDTO', userMappers.mapUserFormToCreateDTO);
    MapperRegistry.register('user', 'formToUpdateDTO', userMappers.mapUserFormToUpdateDTO);
  });
  
  import('./role.mapper').then((roleMappers) => {
    MapperRegistry.register('role', 'dtoToEntity', roleMappers.mapRolDTOToRole);
    MapperRegistry.register('role', 'entitiesToDTOs', roleMappers.mapRolesDTOToRoles);
    MapperRegistry.register('role', 'formToCreateDTO', roleMappers.mapRoleFormToCreateDTO);
    MapperRegistry.register('role', 'formToUpdateDTO', roleMappers.mapRoleFormToUpdateDTO);
  });
}

// ============================================
// HELPER PARA TESTING
// ============================================

/**
 * Verifica que todos los mappers estén correctamente exportados
 * Útil para pruebas automatizadas
 * 
 * @returns Objeto con estado de los mappers
 */
export function verifyMappers(): {
  base: boolean;
  user: boolean;
  role: boolean;
  errors: string[];
} {
  const errors: string[] = [];
  
  // Verificar base
  const hasBase = typeof mapArray === 'function' && typeof formatAuditFields === 'function';
  if (!hasBase) errors.push('Base mappers missing');
  
  // Verificar user
  const hasUser = typeof mapUsuarioDTOToUser === 'function';
  if (!hasUser) errors.push('User mappers missing');
  
  // Verificar role
  const hasRole = typeof mapRolDTOToRole === 'function';
  if (!hasRole) errors.push('Role mappers missing');
  
  return {
    base: hasBase,
    user: hasUser,
    role: hasRole,
    errors,
  };
}

// ============================================
// NOTAS DE DESARROLLO
// ============================================

/**
 * PARA AGREGAR NUEVOS MAPPERS:
 * 
 * 1. Crear archivo [dominio].mapper.ts siguiendo el patrón de user.mapper.ts
 * 2. Definir tipos de UI específicos del dominio
 * 3. Implementar mappers: DTO → UI, Form → DTO, UI → Form
 * 4. Agregar utilidades específicas si son necesarias
 * 5. Exportar todo en este archivo index.ts
 * 6. Documentar con JSDoc
 * 7. Agregar tests unitarios
 * 
 * CONVENCIONES:
 * - Nombres de funciones en español alineados con backend
 * - Tipos de UI en inglés por convención TypeScript
 * - Usar utilidades de base.mapper.ts
 * - Documentación completa con ejemplos
 * - Manejo de null/undefined explícito
 * - Validaciones de estructura
 */
