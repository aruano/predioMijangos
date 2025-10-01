/**
 * Repositorio de Roles (DIP): la UI depende de esta abstracción typed,
 * no de axios directamente. Cada método devuelve modelos de UI.
 */
import http from '@/services/http';
import { API_ENDPOINTS } from '@/services/apiEndpoints';
import type { Role, RoleFilter, RoleId, RolResponseDTO, PageRef } from '@/types/role';
import { mapRoleFromDTO, mapRoleToCreateUpdateDTO, mapPageRefFromDTO } from '@/types/role';

const BASE = API_ENDPOINTS.ROLS.BASE;

/** Lista paginada (GET /api/rols) */
export async function listRols(params: RoleFilter) {
  const { q, page = 0, size = 10, adminOnly = null } = params || {};
  const admin = adminOnly === null ? undefined : adminOnly;
  const { data } = await http.get(BASE, { params: { q, page, size, admin } });
  const body = data.body as {
    content: RolResponseDTO[];
    totalElements: number;
  };
  return {
    content: body.content.map(mapRoleFromDTO),
    totalElements: body.totalElements,
  };
}

/** Detalle (GET /api/rols/{id}) */
export async function getRol(id: RoleId): Promise<Role> {
  const { data } = await http.get(`${BASE}/${id}`);
  return mapRoleFromDTO(data.body as RolResponseDTO);
}

/** Crear (POST /api/rols) */
export async function createRol(payload: Role): Promise<Role> {
  const dto = mapRoleToCreateUpdateDTO(payload);
  const { data } = await http.post(BASE, dto);
  return mapRoleFromDTO(data.body as RolResponseDTO);
}

/** Actualizar (PUT /api/rols/{id}) */
export async function updateRol(id: RoleId, payload: Role): Promise<Role> {
  const dto = mapRoleToCreateUpdateDTO(payload);
  const { data } = await http.put(`${BASE}/${id}`, dto);
  return mapRoleFromDTO(data.body as RolResponseDTO);
}

/** Eliminar (DELETE /api/rols/{id}) */
export async function deleteRol(id: RoleId): Promise<void> {
  await http.delete(`${BASE}/${id}`);
}

/** Catálogo de páginas (GET /api/paginas?movil=false) */
export async function listPagesForWeb(): Promise<PageRef[]> {
  const { data } = await http.get(API_ENDPOINTS.PAGES.BASE, { params: { movil: false } });
  const list = (data.body as any[]).map(mapPageRefFromDTO);
  // Orden por módulo y nombre para UI estable
  return list.sort((a, b) => a.moduloNombre.localeCompare(b.moduloNombre) || a.nombre.localeCompare(b.nombre));
}
