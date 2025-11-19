/**
 * @file storage.ts
 * @description Wrapper tipado y seguro para localStorage
 * 
 * Responsabilidades:
 * - Abstracción de localStorage con TypeScript
 * - Serialización/deserialización automática
 * - Manejo de errores (cuotas, permisos, JSON inválido)
 * - Helpers específicos para tokens y datos de sesión
 * - Limpieza y validación de datos
 * 
 * Sincronizado con:
 * Constants: STORAGE_KEYS, JWT_CONFIG
 * Backend: Token structure, expiration
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 */

import { STORAGE_KEYS } from '@/constants/auth.constants';

// ============================================
// TYPES
// ============================================

/**
 * Opciones para operaciones de storage
 */
interface StorageOptions {
  /** Tiempo de expiración en milisegundos (opcional) */
  expiresIn?: number;
  /** Prefijo para la key (opcional) */
  prefix?: string;
}

/**
 * Estructura interna para items con expiración
 */
interface StorageItem<T> {
  value: T;
  expiresAt?: number;
}

// ============================================
// CLASE PRINCIPAL DE STORAGE
// ============================================

/**
 * Clase principal para manejo de localStorage con tipado
 */
class Storage {
  private storage: globalThis.Storage;

  constructor() {
    this.storage = window.localStorage;
  }

  /**
   * Verifica si localStorage está disponible
   * 
   * @returns true si localStorage está disponible, false en caso contrario
   * 
   * @example
   * ```typescript
   * if (!storage.isAvailable()) {
   *   console.warn('localStorage no disponible, usando fallback');
   * }
   * ```
   */
  isAvailable(): boolean {
    try {
      const test = '__storage_test__';
      this.storage.setItem(test, test);
      this.storage.removeItem(test);
      return true;
    } catch {
      return false;
    }
  }

  /**
   * Guarda un valor en localStorage con serialización automática
   * 
   * @param key - Clave para almacenar el valor
   * @param value - Valor a almacenar (se serializa automáticamente)
   * @param options - Opciones adicionales (expiración, prefijo)
   * 
   * @example
   * ```typescript
   * // Guardar string
   * storage.set('username', 'john.doe');
   * 
   * // Guardar objeto
   * storage.set('user', { id: 1, name: 'John' });
   * 
   * // Guardar con expiración (1 hora)
   * storage.set('temp-data', data, { expiresIn: 3600000 });
   * ```
   */
  set<T>(key: string, value: T, options?: StorageOptions): void {
    if (!this.isAvailable()) {
      console.warn('[Storage] localStorage no disponible');
      return;
    }

    try {
      const finalKey = options?.prefix ? `${options.prefix}${key}` : key;
      
      let item: StorageItem<T> = { value };

      // Agregar expiración si se especifica
      if (options?.expiresIn) {
        item.expiresAt = Date.now() + options.expiresIn;
      }

      const serialized = JSON.stringify(item);
      this.storage.setItem(finalKey, serialized);
    } catch (error) {
      if (error instanceof Error) {
        if (error.name === 'QuotaExceededError') {
          console.error('[Storage] Cuota de localStorage excedida');
        } else {
          console.error('[Storage] Error al guardar:', error.message);
        }
      }
    }
  }

  /**
   * Obtiene un valor de localStorage con deserialización automática
   * 
   * @param key - Clave del valor a obtener
   * @param options - Opciones adicionales (prefijo)
   * @returns El valor deserializado o null si no existe o expiró
   * 
   * @example
   * ```typescript
   * // Obtener string
   * const username = storage.get<string>('username');
   * 
   * // Obtener objeto
   * const user = storage.get<User>('user');
   * 
   * // Con tipo específico
   * interface UserData { id: number; name: string; }
   * const userData = storage.get<UserData>('user');
   * ```
   */
  get<T>(key: string, options?: StorageOptions): T | null {
    if (!this.isAvailable()) {
      return null;
    }

    try {
      const finalKey = options?.prefix ? `${options.prefix}${key}` : key;
      const item = this.storage.getItem(finalKey);

      if (!item) {
        return null;
      }

      const parsed = JSON.parse(item) as StorageItem<T>;

      // Verificar expiración
      if (parsed.expiresAt && Date.now() > parsed.expiresAt) {
        this.remove(key, options);
        return null;
      }

      return parsed.value;
    } catch (error) {
      console.error('[Storage] Error al obtener valor:', error);
      return null;
    }
  }

  /**
   * Elimina un valor de localStorage
   * 
   * @param key - Clave del valor a eliminar
   * @param options - Opciones adicionales (prefijo)
   * 
   * @example
   * ```typescript
   * storage.remove('temp-data');
   * ```
   */
  remove(key: string, options?: StorageOptions): void {
    if (!this.isAvailable()) {
      return;
    }

    try {
      const finalKey = options?.prefix ? `${options.prefix}${key}` : key;
      this.storage.removeItem(finalKey);
    } catch (error) {
      console.error('[Storage] Error al eliminar valor:', error);
    }
  }

  /**
   * Limpia todos los valores de localStorage
   * 
   * ⚠️ CUIDADO: Esta operación eliminará TODOS los datos del localStorage
   * 
   * @example
   * ```typescript
   * storage.clear();
   * ```
   */
  clear(): void {
    if (!this.isAvailable()) {
      return;
    }

    try {
      this.storage.clear();
    } catch (error) {
      console.error('[Storage] Error al limpiar localStorage:', error);
    }
  }

  /**
   * Verifica si existe una clave en localStorage
   * 
   * @param key - Clave a verificar
   * @param options - Opciones adicionales (prefijo)
   * @returns true si la clave existe y no ha expirado, false en caso contrario
   * 
   * @example
   * ```typescript
   * if (storage.has('user')) {
   *   console.log('Usuario encontrado en cache');
   * }
   * ```
   */
  has(key: string, options?: StorageOptions): boolean {
    return this.get(key, options) !== null;
  }

  /**
   * Obtiene todas las claves almacenadas
   * 
   * @param prefix - Filtrar solo claves con este prefijo (opcional)
   * @returns Array de claves
   * 
   * @example
   * ```typescript
   * const allKeys = storage.keys();
   * const userKeys = storage.keys('user_');
   * ```
   */
  keys(prefix?: string): string[] {
    if (!this.isAvailable()) {
      return [];
    }

    try {
      const keys: string[] = [];
      
      for (let i = 0; i < this.storage.length; i++) {
        const key = this.storage.key(i);
        if (key) {
          if (!prefix || key.startsWith(prefix)) {
            keys.push(key);
          }
        }
      }
      
      return keys;
    } catch (error) {
      console.error('[Storage] Error al obtener keys:', error);
      return [];
    }
  }

  /**
   * Limpia todas las claves que coincidan con un prefijo
   * 
   * @param prefix - Prefijo de las claves a eliminar
   * 
   * @example
   * ```typescript
   * // Limpiar todos los datos temporales
   * storage.clearByPrefix('temp_');
   * ```
   */
  clearByPrefix(prefix: string): void {
    if (!this.isAvailable()) {
      return;
    }

    try {
      const keys = this.keys(prefix);
      keys.forEach(key => this.storage.removeItem(key));
    } catch (error) {
      console.error('[Storage] Error al limpiar por prefijo:', error);
    }
  }
}

// ============================================
// INSTANCIA SINGLETON
// ============================================

/**
 * Instancia única de Storage para uso en toda la aplicación
 */
export const storage = new Storage();

// ============================================
// HELPERS ESPECÍFICOS PARA AUTH/TOKENS
// ============================================

/**
 * Guarda el access token en localStorage
 * 
 * @param token - Access token JWT
 * 
 * @example
 * ```typescript
 * import { saveAccessToken } from '@/utils/storage';
 * 
 * saveAccessToken(response.accessToken);
 * ```
 */
export function saveAccessToken(token: string): void {
  storage.set(STORAGE_KEYS.ACCESS_TOKEN, token);
}

/**
 * Obtiene el access token de localStorage
 * 
 * @returns Access token o null si no existe
 * 
 * @example
 * ```typescript
 * import { getAccessToken } from '@/utils/storage';
 * 
 * const token = getAccessToken();
 * if (token) {
 *   // Usar token en request
 * }
 * ```
 */
export function getAccessToken(): string | null {
  return storage.get<string>(STORAGE_KEYS.ACCESS_TOKEN);
}

/**
 * Elimina el access token de localStorage
 * 
 * @example
 * ```typescript
 * import { removeAccessToken } from '@/utils/storage';
 * 
 * removeAccessToken();
 * ```
 */
export function removeAccessToken(): void {
  storage.remove(STORAGE_KEYS.ACCESS_TOKEN);
}

/**
 * Guarda el refresh token en localStorage
 * 
 * @param token - Refresh token
 * 
 * @example
 * ```typescript
 * import { saveRefreshToken } from '@/utils/storage';
 * 
 * saveRefreshToken(response.refreshToken);
 * ```
 */
export function saveRefreshToken(token: string): void {
  storage.set(STORAGE_KEYS.REFRESH_TOKEN, token);
}

/**
 * Obtiene el refresh token de localStorage
 * 
 * @returns Refresh token o null si no existe
 * 
 * @example
 * ```typescript
 * import { getRefreshToken } from '@/utils/storage';
 * 
 * const refreshToken = getRefreshToken();
 * ```
 */
export function getRefreshToken(): string | null {
  return storage.get<string>(STORAGE_KEYS.REFRESH_TOKEN);
}

/**
 * Elimina el refresh token de localStorage
 * 
 * @example
 * ```typescript
 * import { removeRefreshToken } from '@/utils/storage';
 * 
 * removeRefreshToken();
 * ```
 */
export function removeRefreshToken(): void {
  storage.remove(STORAGE_KEYS.REFRESH_TOKEN);
}

/**
 * Guarda los datos del usuario en localStorage
 * 
 * @param user - Objeto de usuario a guardar
 * 
 * @example
 * ```typescript
 * import { saveUser } from '@/utils/storage';
 * 
 * saveUser({
 *   id: 1,
 *   username: 'john.doe',
 *   email: 'john@example.com',
 *   roles: [{ id: 1, nombre: 'ROLE_ADMIN' }],
 *   permissions: ['USUARIOS:READ', 'USUARIOS:CREATE']
 * });
 * ```
 */
export function saveUser<T = any>(user: T): void {
  storage.set(STORAGE_KEYS.USER_DATA, user);
}

/**
 * Obtiene los datos del usuario de localStorage
 * 
 * @returns Datos del usuario o null si no existen
 * 
 * @example
 * ```typescript
 * import { getUser } from '@/utils/storage';
 * 
 * interface User {
 *   id: number;
 *   username: string;
 *   email: string;
 * }
 * 
 * const user = getUser<User>();
 * ```
 */
export function getUser<T = any>(): T | null {
  return storage.get<T>(STORAGE_KEYS.USER_DATA);
}

/**
 * Elimina los datos del usuario de localStorage
 * 
 * @example
 * ```typescript
 * import { removeUser } from '@/utils/storage';
 * 
 * removeUser();
 * ```
 */
export function removeUser(): void {
  storage.remove(STORAGE_KEYS.USER_DATA);
}

/**
 * Limpia todos los datos de autenticación (tokens + usuario)
 * 
 * @example
 * ```typescript
 * import { clearAuthData } from '@/utils/storage';
 * 
 * // En logout
 * clearAuthData();
 * ```
 */
export function clearAuthData(): void {
  removeAccessToken();
  removeRefreshToken();
  removeUser();
}

/**
 * Verifica si el usuario está autenticado (tiene access token válido)
 * 
 * @returns true si existe un access token, false en caso contrario
 * 
 * @example
 * ```typescript
 * import { isAuthenticated } from '@/utils/storage';
 * 
 * if (!isAuthenticated()) {
 *   navigate('/login');
 * }
 * ```
 */
export function isAuthenticated(): boolean {
  return storage.has(STORAGE_KEYS.ACCESS_TOKEN);
}

/**
 * Guarda el timestamp de la última actividad del usuario
 * Útil para implementar auto-logout por inactividad
 * 
 * @example
 * ```typescript
 * import { saveLastActivity } from '@/utils/storage';
 * 
 * // En cada interacción del usuario
 * saveLastActivity();
 * ```
 */
export function saveLastActivity(): void {
  storage.set(STORAGE_KEYS.LAST_ACTIVITY, Date.now());
}

/**
 * Obtiene el timestamp de la última actividad del usuario
 * 
 * @returns Timestamp en milisegundos o null si no existe
 * 
 * @example
 * ```typescript
 * import { getLastActivity } from '@/utils/storage';
 * 
 * const lastActivity = getLastActivity();
 * if (lastActivity) {
 *   const inactiveTime = Date.now() - lastActivity;
 *   if (inactiveTime > 30 * 60 * 1000) { // 30 minutos
 *     // Auto logout
 *   }
 * }
 * ```
 */
export function getLastActivity(): number | null {
  return storage.get<number>(STORAGE_KEYS.LAST_ACTIVITY);
}

// ============================================
// HELPERS PARA PREFERENCIAS DE USUARIO
// ============================================

/**
 * Guarda las preferencias de UI del usuario
 * 
 * @param preferences - Objeto con preferencias de usuario
 * 
 * @example
 * ```typescript
 * import { saveUserPreferences } from '@/utils/storage';
 * 
 * saveUserPreferences({
 *   theme: 'dark',
 *   language: 'es',
 *   pageSize: 20,
 * });
 * ```
 */
export function saveUserPreferences<T = any>(preferences: T): void {
  storage.set('user_preferences', preferences);
}

/**
 * Obtiene las preferencias de UI del usuario
 * 
 * @returns Preferencias del usuario o null si no existen
 * 
 * @example
 * ```typescript
 * import { getUserPreferences } from '@/utils/storage';
 * 
 * interface Preferences {
 *   theme: 'light' | 'dark';
 *   language: string;
 *   pageSize: number;
 * }
 * 
 * const prefs = getUserPreferences<Preferences>();
 * ```
 */
export function getUserPreferences<T = any>(): T | null {
  return storage.get<T>('user_preferences');
}

// ============================================
// EXPORTACIÓN DEFAULT
// ============================================

export default storage;