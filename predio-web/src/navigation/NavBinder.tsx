/**
 * NavBinder — Enlaza el navigate() de React Router con nuestro controlador global.
 * - No renderiza UI; solo conecta.
 */
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { setNavigator } from './nav';

export default function NavBinder() {
  const navigate = useNavigate();
  useEffect(() => setNavigator(navigate), [navigate]);
  return null;
}
