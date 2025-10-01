/**
 * auth.service.ts — llamadas reales a APIs de autenticación.
 * - Usa http centralizado (interceptores, errorBus, headers).
 * - Usa `API_ENDPOINTS` para formar URLs (sin “quemar” strings).
 */
import http from '@/services/http';
import { API_ENDPOINTS, url } from '@/services/apiEndpoints';
import type { ApiResponse, LoginResponse, LoginRequest } from '@/types';

export async function loginApi(payload: LoginRequest): Promise<LoginResponse> {
  const { data } = await http.post<ApiResponse<LoginResponse>>(
    url(API_ENDPOINTS.OAUTH.BASE, API_ENDPOINTS.OAUTH.LOGIN),
    payload
  );
  return data.body;
}
