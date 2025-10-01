/**
 * useLogin — Hook de lógica de negocio para el formulario de login.
 * Responsabilidades:
 *  - Orquestar el llamado a AuthContext.login(credentials).
 *  - Exponer 'loading' y 'submit' a la vista.
 * Importante:
 *  - La vista (LoginPage) NO conoce detalles de persistencia ni del HTTP.
 */
import { useState, useCallback } from 'react';
import { useAuth } from '@/contexts';
import type { LoginRequest } from '@/types';
import { nav } from '@/navigation/nav';

export function useLogin() {
  const { login } = useAuth();
  const [loading, setLoading] = useState(false);

  const submit = useCallback(async (values: LoginRequest) => {
    setLoading(true);
    try {
      await login(values);
      nav.toHome();
    } finally {
      setLoading(false);
    }
  }, [login]);

  return { submit, loading };
}
