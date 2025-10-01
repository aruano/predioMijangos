/**
 * ProtectedRoute — Guard de enrutado.
 * Regla:
 *  - Si no hay token en sesión (AuthContext/LS), redirige a /login.
 * Nota:
 *  - Lógica mínima aquí; la sesión la gobierna AuthContext.
 */
import { JSX } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { SplashScreen } from '@/components';

export default function ProtectedRoute({ children }: { children: JSX.Element }) {
  const { isAuthenticated, loading } = useAuth();
  if (loading) return <SplashScreen />;
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  return children;
}
