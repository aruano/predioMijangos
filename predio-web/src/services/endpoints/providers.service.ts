/**
 * Repositorio de Proveedores — Capa de acceso a datos HTTP (DIP).
 * ---------------------------------------------------------------
 * - La UI NUNCA importa axios directo.
 * - Maneja rutas desde API_ENDPOINTS y devuelve modelos UI (Provider).
 * - Implementa fallback de activar/desactivar vía PUT si no hay PATCH.
 */
import http from '@/services/http';
import { API_ENDPOINTS } from '@/services/apiEndpoints';
import type { Supplier, ProveedorDTO, ProveedorCreateUpdateDTO } from '@/types';
import {
  mapSupplierFromDTO,
  mapSupplierToCreateDTO,
  mapSupplierToUpdateDTO,
} from '@/types';

const BASE = API_ENDPOINTS.PROVIDERS.BASE;

/** Lista paginada (GET /api/proveedores) → devuelve {content,totalElements} en UI */
export async function listProviders(params: { page?: number; size?: number; q?: string } = {}) {
  const { page = 0, size = 10, q } = params || {};
  const { data } = await http.get(BASE, { params: { page, size, q } });
  const body = data?.body ?? { content: [], totalElements: 0 };
  return {
    content: (body.content as ProveedorDTO[]).map(mapSupplierFromDTO),
    totalElements: Number(body.totalElements ?? 0),
  };
}

/** Detalle (GET /api/proveedores/{id}) */
export async function getProvider(id: number): Promise<Supplier> {
  const { data } = await http.get(`${BASE}/${id}`);
  return mapSupplierFromDTO(data.body as ProveedorDTO);
}

/** Crear (POST /api/proveedores) */
export async function createProvider(input: Partial<Supplier>): Promise<Supplier> {
  const payload: ProveedorCreateUpdateDTO = mapSupplierToCreateDTO(input);
  const { data } = await http.post(BASE, payload);
  return mapSupplierFromDTO(data.body as ProveedorDTO);
}

/** Actualizar (PUT /api/proveedores/{id}) */
export async function updateProvider(id: number, input: Supplier): Promise<Supplier> {
  const payload: ProveedorCreateUpdateDTO = mapSupplierToUpdateDTO(input);
  const { data } = await http.put(`${BASE}/${id}`, payload);
  return mapSupplierFromDTO(data.body as ProveedorDTO);
}

/** Activar (PATCH /api/proveedores/{id}/activar) con fallback a PUT */
export async function activateProvider(id: number) {
  try {
    await http.patch(`${BASE}/${id}/activar`);
  } catch {
    const cur = await getProvider(id);
    await updateProvider(id, { ...cur, activo: true });
  }
}

/** Desactivar (PATCH /api/proveedores/{id}/desactivar) con fallback a PUT */
export async function deactivateProvider(id: number) {
  try {
    await http.patch(`${BASE}/${id}/desactivar`);
  } catch {
    const cur = await getProvider(id);
    await updateProvider(id, { ...cur, activo: false });
  }
}
