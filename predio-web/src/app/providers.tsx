/**
 * Providers — punto único para contextos globales:
 *  - AuthProvider: persistencia de sesión, roles y menú.
 *  - NotificationProvider: UI global para errores/éxitos.
 *  - RouterProvider: routing de la app.
 */
import { RouterProvider } from 'react-router-dom'
import { router } from './router'
import { AuthProvider } from '@/contexts'
import { NotificationProvider } from '@/components'

export default function Providers() {
  return (
    <AuthProvider>
      <NotificationProvider>
        <RouterProvider router={router} />
      </NotificationProvider>
    </AuthProvider>
  );
}
