import React from 'react';
import { useLocation } from 'react-router-dom';
import Icon from '@mdi/react';
import { mdiChevronDown, mdiChevronRight, mdiCogOutline, mdiPower } from '@mdi/js';
import { useAuth } from '@/contexts';
import { AppIcon } from '@/icons';
import { pagePath, slug, ROUTES } from '@/navigation/routes';
import { useNav } from '@/navigation/useNav';
import { useNotice } from '@/components';

const LS_KEY = 'pm_sidenav_open';

function useOpenState(modIds: number[], activeModId?: number) {
  const [open, setOpen] = React.useState<Record<number, boolean>>(() => {
    try {
      const raw = localStorage.getItem(LS_KEY);
      const s = raw ? (JSON.parse(raw) as Record<number, boolean>) : {};
      if (activeModId != null && s[activeModId] == null) s[activeModId] = true;
      return s;
    } catch { return {}; }
  });
  React.useEffect(() => { localStorage.setItem(LS_KEY, JSON.stringify(open)); }, [open]);
  const toggle = (id: number) => setOpen(prev => ({ ...prev, [id]: !prev[id] }));
  React.useEffect(() => {
    setOpen(prev => {
      const next = { ...prev };
      modIds.forEach(id => { if (next[id] == null) next[id] = false; });
      return next;
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [JSON.stringify(modIds)]);
  return { open, toggle };
}

export default function SideMenu() {
  const { webMenu, logout } = useAuth();
  const nav = useNav();
  const { pathname } = useLocation();
  const notice = useNotice();

  const [, , modSlug] = pathname.split('/');
  const activeModId = webMenu.find(m => slug(m.nombre) === modSlug)?.id;
  const { open, toggle } = useOpenState(webMenu.map(m => m.id), activeModId);
  const isActive = (to: string) => pathname === to;

  const goSettings = () => nav.push(ROUTES.ACCOUNT_SETTINGS);
  const confirmLogout = () => {
    notice.warning('Cerrar sesión', '¿Seguro que deseas salir?', {
      label: 'Salir',
      onPress: async () => { logout(); nav.toLogin(); }
    }, { label: 'Cancelar' });
  };

  return (
    <nav aria-label="Menú principal" className="sidenav">
      <div className="sidenav__brand" aria-label="Predio Mijangos">
        <span className="brand-primary">Predio</span>
        <span className="brand-dark">Mijangos</span>
      </div>

      <div className="sidenav__groups">
        {webMenu.map((mod) => {
  const first = mod.paginas[0];
  const ModIcon = <AppIcon name={first?.icon ?? undefined} size={18} title={mod.nombre} />;
  const expanded = !!open[mod.id];

  return (
    <section key={mod.id} className="sidenav__section">
      <button className="sidenav__header" onClick={() => toggle(mod.id)} aria-expanded={expanded}>
        <div className="sidenav__header__left">
          {ModIcon}
          <span>{mod.nombre}</span>
        </div>
        <Icon path={expanded ? mdiChevronDown : mdiChevronRight} size={0.8} />
      </button>

      {/* ⟵ Contenedor con transición */}
      <div className="sidenav__collapse" data-open={expanded}>
        <ul className="sidenav__pages">
          {mod.paginas.map((p) => {
            const to = p.redirect ?? pagePath(mod.nombre, p.nombre);
            const active = isActive(to);
            return (
              <li key={p.id}>
                <button
                  className={`sidenav__page ${active ? 'sidenav__page--active' : ''}`}
                  onClick={() => nav.push(to)}
                  aria-current={active ? 'page' : undefined}
                >
                  <AppIcon name={p.icon ?? undefined} size={18} />
                  <span>{p.nombre}</span>
                </button>
              </li>
            );
          })}
        </ul>
      </div>
    </section>
  );
})}
      </div>

      <div className="sidenav__footer">
        <button className="sidenav__page" onClick={goSettings} aria-label="Configuración de cuenta">
          <Icon path={mdiCogOutline} size={0.9} />
          <span>Configuración</span>
        </button>
        <button className="sidenav__page" onClick={confirmLogout} aria-label="Salir">
          <Icon path={mdiPower} size={0.9} />
          <span>Salir</span>
        </button>
      </div>
    </nav>
  );
}
