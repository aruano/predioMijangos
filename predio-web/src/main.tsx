/**
 * Punto de entrada de la app.
 * - Monta el árbol de React.
 * - Envuelve la app con Providers (router, contextos, etc.).
 */
import React from 'react'
import ReactDOM from 'react-dom/client'
import Providers from './app/providers'
import './styles/theme.css';
import './styles/base.css';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <Providers />
  </React.StrictMode>
)
