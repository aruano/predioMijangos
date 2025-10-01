/**
 * AppLayout — layout con sidebar colapsable y topbar con botón hamburguesa.
 */
import NavBinder from '@/navigation/NavBinder';
import SideMenu from './SideMenu';
import Icon from '@mdi/react';
import { mdiMenu } from '@mdi/js';
import React from 'react';
import AnimatedOutlet from '@/components/layout/AnimatedOutlet';

const LS_COLLAPSED = 'pm_sidebar_collapsed';

export default function AppLayout() {
  const [collapsed, setCollapsed] = React.useState<boolean>(() => {
    return localStorage.getItem(LS_COLLAPSED) === '1';
  });

  const toggle = () => {
    setCollapsed(v => {
      const next = !v;
      localStorage.setItem(LS_COLLAPSED, next ? '1' : '0');
      return next;
    });
  };

  return (
    <div className={`layout ${collapsed ? 'layout--collapsed' : ''}`}>
      <NavBinder />
      <aside className="layout__aside" aria-label="Navegación lateral">
        <SideMenu />
      </aside>

      <div className="layout__main">
        <header className="topbar">
          <button className="topbar__btn" onClick={toggle} aria-label="Mostrar/ocultar menú">
            <Icon path={mdiMenu} size={0.9} />
          </button>
          {/* aquí puedes añadir buscador/acciones */}
        </header>

        <main style={{ padding: 16 }}>
          <AnimatedOutlet />
        </main>
      </div>
    </div>
  );
}
