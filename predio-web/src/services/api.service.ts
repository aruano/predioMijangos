/**
 * @file api.service.ts
 * @description Cliente HTTP centralizado para todas las llamadas a la API
 * 
 * Este servicio:
 * - Configura Axios con la URL base y timeout
 * - Aplica interceptores (auth, error, response)
 * - Proporciona métodos tipados para GET, POST, PUT, DELETE
 * - Envuelve las respuestas del backend en el tipo ApiResponse<T>
 * 
 * IMPORTANTE: Todas las llamadas HTTP deben usar este servicio
 * NO crear instancias nuevas de Axios en otros archivos
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 2.0.0
 */

import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { API_CONFIG } from '@/constants/api.constants';
import { authInterceptor } from './interceptors/auth.interceptor';
import { errorInterceptor } from './interceptors/error.interceptor';
import { responseInterceptor } from './interceptors/response.interceptor';

// ============================================
// CLASE API SERVICE
// ============================================

/**
 * Servicio de API centralizado
 * 
 * Singleton que configura y expone un cliente HTTP con interceptores
 */
class ApiService {
  /**
   * Instancia de Axios configurada
   */
  private client: AxiosInstance;

  constructor() {
    // Crear instancia de Axios con configuración base
    this.client = axios.create({
      baseURL: API_CONFIG.BASE_URL,
      timeout: API_CONFIG.TIMEOUT,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Configurar interceptores
    this.setupInterceptors();
  }

  /**
   * Configura los interceptores de request y response
   * 
   * @private
   */
  private setupInterceptors(): void {
    // ============================================
    // REQUEST INTERCEPTORS
    // ============================================
    
    /**
     * Interceptor de autenticación
     * Agrega el token JWT al header Authorization
     */
    this.client.interceptors.request.use(
      authInterceptor,
      (error) => Promise.reject(error)
    );

    // ============================================
    // RESPONSE INTERCEPTORS
    // ============================================
    
    /**
     * Interceptor de respuesta
     * Log de respuestas exitosas en desarrollo
     */
    this.client.interceptors.response.use(
      responseInterceptor,
      // El errorInterceptor maneja errores y refresh de tokens
      errorInterceptor
    );
  }

  /**
   * Obtiene la instancia de Axios configurada
   * 
   * Útil para casos avanzados donde se necesita acceso directo a Axios
   * Ejemplo: para configurar interceptores adicionales específicos de un módulo
   * 
   * @returns Instancia de Axios
   * 
   * @example
   * ```typescript
   * const axiosInstance = apiService.getAxiosInstance();
   * axiosInstance.interceptors.request.use(...);
   * ```
   */
  public getAxiosInstance(): AxiosInstance {
    return this.client;
  }

  // ============================================
  // HTTP METHODS
  // ============================================

  /**
   * Realiza una petición GET
   * 
   * @template T - Tipo de la respuesta esperada
   * @param url - URL del endpoint
   * @param config - Configuración adicional de Axios (opcional)
   * @returns Promise con los datos de la respuesta
   * 
   * @example
   * ```typescript
   * // Sin query params
   * const users = await apiService.get<ApiResponse<User[]>>('/api/v1/usuarios');
   * 
   * // Con query params
   * const users = await apiService.get<ApiResponse<PageResponse<User>>>('/api/v1/usuarios', {
   *   params: { page: 0, size: 20, search: 'juan' }
   * });
   * ```
   */
  async get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response: AxiosResponse<T> = await this.client.get(url, config);
    return response.data;
  }

  /**
   * Realiza una petición POST
   * 
   * @template T - Tipo de la respuesta esperada
   * @param url - URL del endpoint
   * @param data - Datos a enviar en el body
   * @param config - Configuración adicional de Axios (opcional)
   * @returns Promise con los datos de la respuesta
   * 
   * @example
   * ```typescript
   * const newUser = await apiService.post<ApiResponse<User>>(
   *   '/api/v1/usuarios',
   *   {
   *     username: 'jperez',
   *     email: 'jperez@example.com',
   *     // ...resto de campos
   *   }
   * );
   * ```
   */
  async post<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<T> {
    const response: AxiosResponse<T> = await this.client.post(url, data, config);
    return response.data;
  }

  /**
   * Realiza una petición PUT
   * 
   * @template T - Tipo de la respuesta esperada
   * @param url - URL del endpoint
   * @param data - Datos a enviar en el body
   * @param config - Configuración adicional de Axios (opcional)
   * @returns Promise con los datos de la respuesta
   * 
   * @example
   * ```typescript
   * const updatedUser = await apiService.put<ApiResponse<User>>(
   *   '/api/v1/usuarios/123',
   *   {
   *     email: 'nuevoemail@example.com',
   *     // ...resto de campos a actualizar
   *   }
   * );
   * ```
   */
  async put<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<T> {
    const response: AxiosResponse<T> = await this.client.put(url, data, config);
    return response.data;
  }

  /**
   * Realiza una petición PATCH
   * 
   * @template T - Tipo de la respuesta esperada
   * @param url - URL del endpoint
   * @param data - Datos a enviar en el body
   * @param config - Configuración adicional de Axios (opcional)
   * @returns Promise con los datos de la respuesta
   * 
   * @example
   * ```typescript
   * // Actualización parcial
   * const updatedUser = await apiService.patch<ApiResponse<User>>(
   *   '/api/v1/usuarios/123/estado',
   *   { activo: false }
   * );
   * ```
   */
  async patch<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<T> {
    const response: AxiosResponse<T> = await this.client.patch(url, data, config);
    return response.data;
  }

  /**
   * Realiza una petición DELETE
   * 
   * @template T - Tipo de la respuesta esperada
   * @param url - URL del endpoint
   * @param config - Configuración adicional de Axios (opcional)
   * @returns Promise con los datos de la respuesta
   * 
   * @example
   * ```typescript
   * await apiService.delete<ApiResponse<void>>('/api/v1/usuarios/123');
   * ```
   */
  async delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response: AxiosResponse<T> = await this.client.delete(url, config);
    return response.data;
  }
}

// ============================================
// SINGLETON EXPORT
// ============================================

/**
 * Instancia singleton del servicio de API
 * 
 * Usar esta instancia en toda la aplicación para garantizar:
 * - Configuración consistente
 * - Interceptores aplicados globalmente
 * - Una sola instancia de Axios
 * 
 * @example
 * ```typescript
 * import { apiService } from '@/services/api.service';
 * 
 * // En un servicio de endpoint
 * export async function getUsers() {
 *   const response = await apiService.get<ApiResponse<User[]>>('/api/v1/usuarios');
 *   return response.body;
 * }
 * ```
 */
export const apiService = new ApiService();

/**
 * Exportar también la clase para casos donde se necesite
 * crear instancias adicionales (muy raro)
 */
export default ApiService;
