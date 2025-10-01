/**
 * useRememberUser — Maneja la preferencia "Recordar Credenciales".
 * - Guarda/recupera las últimas credenciales ingresadas en localStorage.
 */
import { useCallback, useEffect, useState } from 'react';

const REMEMBER_KEY = 'pm_remember';
const LAST_USER_KEY = 'pm_last_user';
const LAST_PASSWORD_KEY = 'pm_last_password';

export function useRememberUser() {
  const [remember, setRemember] = useState<boolean>(() => localStorage.getItem(REMEMBER_KEY) === '1');
  const [lastUser, setLastUser] = useState<string>(() => localStorage.getItem(LAST_USER_KEY) || '');
  const [lastPassword, setLastPassword] = useState<string>(() => localStorage.getItem(LAST_PASSWORD_KEY) || '');

  useEffect(() => {
    localStorage.setItem(REMEMBER_KEY, remember ? '1' : '0');
  }, [remember]);

  const saveCredentials = useCallback((usuario: string, password: string) => {
    if (remember && usuario && password){ 
      localStorage.setItem(LAST_USER_KEY, usuario); 
      localStorage.setItem(LAST_PASSWORD_KEY, password); 
    }
  }, [remember]);

  const clearCredentials = useCallback(() => {
    localStorage.removeItem(LAST_USER_KEY);
    localStorage.removeItem(LAST_PASSWORD_KEY);
    setLastUser('');
    setLastPassword('');
  }, []);

  const toggle = useCallback(() => setRemember((v) => !v), []);

  return { remember, toggle, lastUser, lastPassword, saveCredentials, clearCredentials };
}
