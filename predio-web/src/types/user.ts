/**
 * Tipos de dominio para Seguridad → Usuarios
 * - DTOs según Swagger
 * - Modelo UI (User) desacoplado de DTOs
 * - Mappers puros (SRP)
 */

export type PersonaDTO = {
  id?: number;
  tipoIdentificacion?: string | null;
  identificacion?: string | null;
  nombres?: string | null;
  apellidos?: string | null;
  correo?: string | null;
  telefono?: string | null;
  direccionDomicilio?: string | null;
  idDepartamento?: number | null;
  idMunicipio?: number | null;
};

export type RolDTO = { id: number; nombre: string; admin?: boolean | null };

export type UsuarioResponseDTO = {
  id: number;
  usuario: string;
  activo: boolean;
  persona?: PersonaDTO | null;
  roles?: RolDTO[] | null;
};

export type UsuarioCreateRequestDTO = {
  usuario: string;
  password: string; 
  persona?: PersonaDTO | null;
  rolesIds: number[];
  activo?: boolean;
};

export type UsuarioUpdateRequestDTO = {
  password?: string; // opcional en update
  persona?: PersonaDTO | null;
  rolesIds?: number[];
  activo?: boolean;
};

/** ---- Modelo UI ---- */
export type User = {
  id: number;
  usuario: string;
  activo: boolean;
  persona: {
    tipoIdentificacion: string;
    identificacion: string;
    nombres: string;
    apellidos: string;
    correo: string;
    telefono: string;
    direccionDomicilio?: string | null;
    idDepartamento?: number;
    idMunicipio?: number;
  };
  roleIds: number[];
  admin: boolean;
};

/** DTO → UI */
export function mapUserFromDTO(dto: UsuarioResponseDTO): User {
  const p = dto.persona ?? {};
  const roles = dto.roles ?? [];
  return {
    id: dto.id,
    usuario: dto.usuario,
    activo: !!dto.activo,
    persona: {
      tipoIdentificacion: p.tipoIdentificacion ?? '',
      identificacion: p.identificacion ?? '',
      nombres: p.nombres ?? '',
      apellidos: p.apellidos ?? '',
      correo: p.correo ?? '',
      telefono: p.telefono ?? '',
      direccionDomicilio: p.direccionDomicilio,
      idDepartamento: p.idDepartamento ?? undefined,
      idMunicipio: p.idMunicipio ?? undefined,
    },
    roleIds: roles.map(r => r.id),
    admin: roles.some(r => !!r.admin),
  };
}

/** UI → DTO (create) */
export function mapUserToCreateDTO(u: User, password: string): UsuarioCreateRequestDTO {
  return {
    usuario: u.usuario,
    password,
    persona: { ...u.persona },
    rolesIds: u.roleIds,
    activo: u.activo,
  };
}

/** UI → DTO (update) */
export function mapUserToUpdateDTO(u: User): UsuarioUpdateRequestDTO {
  return {
    persona: { ...u.persona },
    rolesIds: u.roleIds,
    activo: u.activo,
  };
}
