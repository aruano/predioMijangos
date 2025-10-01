/**
 * Router de la app.
 * - "/" ahora es una ruta de *arranque* que decide: /app (si hay sesión) o /login.
 * - Todo lo protegido vive bajo "/app/*".
 * - Mantiene /login público y 404.
 */
import { createBrowserRouter, Navigate } from 'react-router-dom';
import { AppLayout, ProtectedRoute, SplashScreen } from '@/components';
import { useAuth } from '@/contexts';
import LoginPage from '@/pages/LoginPage';
import HomePage from '@/pages/HomePage';
import SettingsPage from '@/pages/SettingsPage';
import NotFoundPage from '@/pages/NotFoundPage';
import StubPage from '@/pages/StubPage';
import RolesListPage from '@/pages/security/roles/RolesListPage';
import UserListPage from '@/pages/security/users/UserListPage';
import ProviderListPage from '@/pages/purchase/providers/ProviderListPage';

/** Decide a dónde ir al entrar a "/" */
function EntryRedirect() {
  const { isAuthenticated, loading } = useAuth();
  if (loading) return <SplashScreen />;           // mientras se restaura la sesión
  return <Navigate to={isAuthenticated ? '/app' : '/login'} replace />;
}

export const router = createBrowserRouter([
  { path: '/', element: <EntryRedirect /> },      // <- dominio raíz
  { path: '/login', element: <LoginPage /> },     // público
  {
    path: '/app',
    element: (
      <ProtectedRoute>
        <AppLayout />
      </ProtectedRoute>
    ),
    children: [
      { index: true, element: <HomePage /> },
      { path: 'configuracion', element: <SettingsPage /> },
      { path: 'seguridad/roles', element: <RolesListPage /> },
      { path: 'seguridad/usuarios', element: <UserListPage /> },
      { path: 'compras/proveedores', element: <ProviderListPage /> },
      { path: ':mod/:page', element: <StubPage /> },
    ],
  },
  { path: '*', element: <NotFoundPage /> },
])
