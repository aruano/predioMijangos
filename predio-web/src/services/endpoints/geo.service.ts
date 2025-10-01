/**
 * Repositorio GEO — catálogos de Departamento y Municipio (DIP)
 */
import http from '@/services/http';

const BASE = ''; // si usas API_ENDPOINTS, cámbialo por API_ENDPOINTS.GEO, o usa rutas absolutas

export type DepartamentoDTO = { id: number; nombre: string };
export type MunicipioDTO    = { id: number; nombre: string; departamentoId: number };

export async function listDepartamentos(): Promise<DepartamentoDTO[]> {
  const { data } = await http.get('/api/geo/departamentos');
  return data.body as DepartamentoDTO[];
}

export async function listMunicipios(departamentoId: number): Promise<MunicipioDTO[]> {
  const { data } = await http.get('/api/geo/municipios', { params: { departamentoId } });
  return data.body as MunicipioDTO[];
}
