/**
 * RoleFilterBar — activadores con chevron + dropdown:
 * - Rol: búsqueda por nombre
 * - Admin: selector (Todos / Sí / No)
 * - Reset alineado a la derecha, botón plano
 */
import React from 'react';
import Icon from '@mdi/react';
import { mdiFilter, mdiChevronDown, mdiClose, mdiRestore } from '@mdi/js';

type Props = {
  q: string; onQChange: (v: string) => void; onQDebounced: (v: string) => void;
  adminOnly: boolean | null; onAdminChange: (v: boolean | null) => void;
  onReset: () => void;
};

function useOutsideClose<T extends HTMLElement>(open: boolean, onClose: () => void) {
  const ref = React.useRef<T | null>(null);
  React.useEffect(() => {
    if (!open) return;
    const handler = (e: MouseEvent) => { if (ref.current && !ref.current.contains(e.target as Node)) onClose(); };
    const esc = (e: KeyboardEvent) => { if (e.key === 'Escape') onClose(); };
    document.addEventListener('mousedown', handler);
    document.addEventListener('keydown', esc);
    return () => { document.removeEventListener('mousedown', handler); document.removeEventListener('keydown', esc); };
  }, [open, onClose]);
  return ref;
}

export default function RoleFilterBar({ q, onQChange, onQDebounced, adminOnly, onAdminChange, onReset }: Props) {
  // debounce búsqueda
  React.useEffect(() => { const id = setTimeout(() => onQDebounced(q), 350); return () => clearTimeout(id); }, [q, onQDebounced]);

  // estados dropdown
  const [openRol, setOpenRol] = React.useState(false);
  const [openAdmin, setOpenAdmin] = React.useState(false);
  const rolRef = useOutsideClose<HTMLDivElement>(openRol, () => setOpenRol(false));
  const adminRef = useOutsideClose<HTMLDivElement>(openAdmin, () => setOpenAdmin(false));

  const adminLabel = adminOnly === null ? 'Todos' : adminOnly ? 'Sí' : 'No';

  return (
    <div className="filters" style={{width: '65%'}}>
      <div className="filters__icon"><Icon path={mdiFilter} size={1} /></div>
      <span className="filters__title">Filtros</span>
      <div className="filters__sep" />

      {/* Rol */}
      <div style={{ position: 'relative' }} ref={rolRef}>
        <button className="filters__item" onClick={() => (setOpenRol(v => !v), setOpenAdmin(false))}>
          <span className="filters__label">Rol</span>
          <span className="filters__value">{q ? `"${q}"` : '—'}</span>
          <Icon path={mdiChevronDown} size={0.8} />
        </button>
        {openRol && (
          <div className="dropdown">
            <div className="dropdown__body">
              <input
                className="input input--sm"
                autoFocus
                placeholder="Buscar por nombre…"
                value={q}
                onChange={e => onQChange(e.target.value)}
              />
              {q && (
                <button className="btn btn--ghost" onClick={() => onQChange('')} style={{ justifySelf: 'end' }}>
                  <Icon path={mdiClose} size={0.8} /> Limpiar
                </button>
              )}
            </div>
          </div>
        )}
      </div>
      <div className="filters__sep" />

      {/* Admin */}
      <div style={{ position: 'relative' }} ref={adminRef}>
        <button className="filters__item" onClick={() => (setOpenAdmin(v => !v), setOpenRol(false))}>
          <span className="filters__label">Admin</span>
          <span className="filters__value">{adminLabel}</span>
          <Icon path={mdiChevronDown} size={0.8} />
        </button>
        {openAdmin && (
          <div className="dropdown" style={{ width: 220 }}>
            <div className="dropdown__body">
              <button className="btn" onClick={() => (onAdminChange(null), setOpenAdmin(false))}>Todos</button>
              <button className="btn btn--ghost" onClick={() => (onAdminChange(true), setOpenAdmin(false))}>Sí</button>
              <button className="btn btn--ghost" onClick={() => (onAdminChange(false), setOpenAdmin(false))}>No</button>
            </div>
          </div>
        )}
      </div>
      <div className="filters__sep" />
      {/* Right actions */}
      <div className="filters__right">
        <button className="filters__reset" onClick={onReset} style={{ marginLeft: 0, display:'flex' }}>
            <Icon path={mdiRestore} size={0.8} style={{ marginRight: 6 }} />
            Resetear
        </button>
      </div>
    </div>
  );
}
