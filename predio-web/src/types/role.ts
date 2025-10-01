/**
 * Tipos de dominio para Seguridad → Roles
 * - Definimos DTOs tal como vienen del backend (OpenAPI)
 * - Definimos modelos de UI (Role, PageRef) para la capa de presentación
 * - Proveemos mapeadores puros (SRP) para no acoplar UI a DTOs
 */
/** ----- DTOs (según OpenAPI) ----- */
export type PageDTO = {
  id: number;
  nombre: string;
  movil: boolean;
  icon?: string | null;
  redirect?: string | null;
  moduloId: number;
  moduloNombre: string;
};

export type RolResponseDTO = {
  id: number;
  nombre: string;
  descripcion?: string | null;
  admin: boolean;
  paginas: PageDTO[];
};

export type RolCreateUpdateDTO = {
  nombre: string;
  descripcion?: string | null;
  admin?: boolean;
  paginaIds?: number[]; // ← OJO: así lo expone el backend
};

/** ----- Modelos de UI ----- */
export type RoleId = number;

export type Role = {
  id: RoleId;
  nombre: string;
  descripcion?: string;
  admin: boolean;
  /** sólo ids para edición/asignación */
  paginaIds: number[];
};

export type RoleFilter = {
  q?: string;
  page?: number;
  size?: number;
  adminOnly?: boolean | null;
};

export type PageRef = {
  id: number;
  nombre: string;
  moduloId: number;
  moduloNombre: string;
};

/** ----- Mapeadores puros (UI ↔︎ DTO) ----- */
export function mapRoleFromDTO(dto: RolResponseDTO): Role {
  return {
    id: dto.id,
    nombre: dto.nombre,
    descripcion: dto.descripcion ?? undefined,
    admin: !!dto.admin,
    paginaIds: (dto.paginas ?? []).map(p => p.id),
  };
}

export function mapRoleToCreateUpdateDTO(model: Role): RolCreateUpdateDTO {
  return {
    nombre: model.nombre,
    descripcion: model.descripcion ?? undefined,
    admin: model.admin,
    paginaIds: model.paginaIds ?? [],
  };
}

export function mapPageRefFromDTO(dto: PageDTO): PageRef {
  return {
    id: dto.id,
    nombre: dto.nombre,
    moduloId: dto.moduloId,
    moduloNombre: dto.moduloNombre,
  };
}
