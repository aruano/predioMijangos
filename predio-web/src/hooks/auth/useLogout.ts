/**
 * @file useLogout.ts
 * @description Hook de lógica de negocio para el logout
 * 
 * Responsabilidades:
 *  - Orquestar el llamado a AuthContext.logout()
 *  - Manejar la navegación post-logout
 *  - Exponer 'loading' y 'handleLogout' al componente
 * 
 * Importante:
 *  - La vista NO conoce detalles de persistencia ni HTTP
 *  - Sigue el mismo patrón que useLogin para consistencia
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 */

import { useState, useCallback } from 'react';
import { useAuth } from '@/contexts';
import { nav } from '@/navigation/nav';

/**
 * Hook para manejar el logout con navegación
 * 
 * @returns Objeto con función handleLogout y estado de carga
 * 
 * @example
 * ```typescript
 * function HeaderMenu() {
 *   const { handleLogout, loading } = useLogout();
 *   
 *   return (
 *     <button 
 *       onClick={handleLogout}
 *       disabled={loading}
 *     >
 *       {loading ? 'Cerrando sesión...' : 'Cerrar Sesión'}
 *     </button>
 *   );
 * }
 * ```
 */
export function useLogout() {
  const { logout } = useAuth();
  const [loading, setLoading] = useState(false);

  /**
   * Ejecuta el logout y navega a la página de login
   * 
   * @remarks
   * - Siempre tiene éxito, incluso si el backend falla
   * - La navegación se ejecuta después del logout
   */
  const handleLogout = useCallback(async () => {
    setLoading(true);
    try {
      await logout();
      // Navegar a login después del logout exitoso
      nav.toLogin();
    } catch (error) {
      // El logout nunca debería fallar, pero por seguridad:
      console.error('[useLogout] Error inesperado:', error);
      // Navegar de todas formas al login
      nav.toLogin();
    } finally {
      setLoading(false);
    }
  }, [logout]);

  return { 
    handleLogout, 
    loading 
  };
}
