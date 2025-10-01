/**
 * Repositorio de Usuarios (DIP) — la UI NO depende de axios directo.
 */
import http from '@/services/http';
import { API_ENDPOINTS } from '@/services/apiEndpoints';
import type {
  User, UsuarioResponseDTO, UsuarioCreateRequestDTO, UsuarioUpdateRequestDTO
} from '@/types/user';
import { mapUserFromDTO, mapUserToCreateDTO, mapUserToUpdateDTO } from '@/types/user';

const BASE = API_ENDPOINTS.USERS.BASE;

/** Lista paginada (GET /api/users) */
export async function listUsers(params: { page?: number; size?: number; q?: string; activo?: boolean | null } = {}) {
  const { page = 0, size = 10, q, activo = null } = params || {};
  const { data } = await http.get(BASE, { params: { page, size, q, activo } });
  const body = data.body as {
    content: UsuarioResponseDTO[];
    totalElements: number;
  };
  return {
    content: body.content.map(mapUserFromDTO),
    totalElements: body.totalElements,
  };
}

/** Detalle (GET /api/users/{id}) */
export async function getUser(id: number): Promise<User> {
  const { data } = await http.get(`${BASE}/${id}`);
  return mapUserFromDTO(data.body as UsuarioResponseDTO);
}

/** Crear (POST /api/users) */
export async function createUser(payload: User, password: string): Promise<User> {
  const dto: UsuarioCreateRequestDTO = mapUserToCreateDTO(payload, password);
  const { data } = await http.post(BASE, dto);
  return mapUserFromDTO(data.body as UsuarioResponseDTO);
}

/** Actualizar (PUT /api/users/{id}) */
export async function updateUser(id: number, payload: User): Promise<User> {
  const dto: UsuarioUpdateRequestDTO = mapUserToUpdateDTO(payload);
  const { data } = await http.put(`${BASE}/${id}`, dto);
  return mapUserFromDTO(data.body as UsuarioResponseDTO);
}

/** Activar (POST /api/users/{id})/activar */
export async function activateUser(id: number): Promise<void> {
  await http.patch(`${BASE}/${id}/activar`);
}

/** Desactivar (POST /api/users/{id})/desactivar */
export async function deactivateUser(id: number): Promise<void> {
  await http.patch(`${BASE}/${id}/desactivar`);
}
