/**
 * http — Cliente Axios centralizado.
 * Responsabilidades:
 *  - BaseURL desde .env (VITE_API_URL).
 *  - Inyecta el token (si existe) en Authorization.
 *  - Normaliza el manejo de códigos de estado:
 *    · 201 → emite 'api:success'
 *    · 401 → limpia sesión + redirige a /login + emite 'api:error'
 *    · 4xx/5xx → emite 'api:error'
 * Beneficio:
 *  - La UI no maneja los errores uno a uno; los escucha el NotificationProvider.
 */
import axios from 'axios';
import { nav } from '@/navigation/nav';
import { errorBus } from '@/services/errorBus';

export const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080';

const http = axios.create({ baseURL: API_URL, timeout: 15000 });

http.interceptors.request.use((config) => {
  // Lee del AuthContext persistido (LS) si existe
  const authRaw = localStorage.getItem('pm_auth');
  const token = authRaw ? (JSON.parse(authRaw)?.token as string) : null;
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

http.interceptors.response.use(
  (res) => {
    if (res.status === 200) errorBus.emit('api:success', 'Operación realizada con éxito');
    return res;
  },
  (error) => {
    const status = error?.response?.status;
    const message =
      error?.response?.data?.message ||
      error?.message ||
      'Error de comunicación con el servidor';

    if (status === 401) {
      // Limpieza de sesión mínima: AuthContext también sanea al iniciar
      localStorage.removeItem('pm_auth');
      errorBus.emit('api:error', { status, message, details: error?.response?.data });
      if (!location.pathname.startsWith('/login')) nav.toLogin();
      return Promise.reject(error);
    }

    if (status && status >= 400) {
      errorBus.emit('api:error', { status, message, details: error?.response?.data });
    }
    return Promise.reject(error);
  }
);

export default http;
