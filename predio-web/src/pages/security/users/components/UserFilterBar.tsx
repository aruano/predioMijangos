import React from 'react';
import Icon from '@mdi/react';
import { mdiFilter, mdiChevronDown, mdiClose, mdiRestore } from '@mdi/js';

type Props = {
  qUsuario: string; onUsuario: (v: string) => void; onUsuarioDebounced: (v: string) => void;
  qNombre: string; onNombre: (v: string) => void; onNombreDebounced: (v: string) => void;
  qIdent: string; onIdent: (v: string) => void; onIdentDebounced: (v: string) => void;
  onReset: () => void;
};

function useOutsideClose<T extends HTMLElement>(open: boolean, onClose: () => void) {
  const ref = React.useRef<T | null>(null);
  React.useEffect(() => {
    if (!open) return;
    const h = (e: MouseEvent) => { if (ref.current && !ref.current.contains(e.target as Node)) onClose(); };
    const esc = (e: KeyboardEvent) => { if (e.key === 'Escape') onClose(); };
    document.addEventListener('mousedown', h); document.addEventListener('keydown', esc);
    return () => { document.removeEventListener('mousedown', h); document.removeEventListener('keydown', esc); };
  }, [open, onClose]);
  return ref;
}

export default function UserFilterBar(p: Props) {
  // debounce
  React.useEffect(() => { const id = setTimeout(() => p.onUsuarioDebounced(p.qUsuario), 350); return () => clearTimeout(id); }, [p.qUsuario]);
  React.useEffect(() => { const id = setTimeout(() => p.onNombreDebounced(p.qNombre), 350); return () => clearTimeout(id); }, [p.qNombre]);
  React.useEffect(() => { const id = setTimeout(() => p.onIdentDebounced(p.qIdent), 350); return () => clearTimeout(id); }, [p.qIdent]);

  const [openU, setOpenU] = React.useState(false);
  const [openN, setOpenN] = React.useState(false);
  const [openI, setOpenI] = React.useState(false);
  const refU = useOutsideClose<HTMLDivElement>(openU, () => setOpenU(false));
  const refN = useOutsideClose<HTMLDivElement>(openN, () => setOpenN(false));
  const refI = useOutsideClose<HTMLDivElement>(openI, () => setOpenI(false));

  return (
    <div className="filters"style={{width: '90%'}}>
      <div className="filters__icon"><Icon path={mdiFilter} size={1} /></div>
      <span className="filters__title">Filtrar Por</span>
      <div className="filters__sep" />

      {/* Usuario */}
      <div style={{ position: 'relative' }} ref={refU}>
        <button className="filters__item" onClick={() => (setOpenU(v => !v), setOpenN(false), setOpenI(false))}>
          <span className="filters__label">Usuario</span>
          <span className="filters__value">{p.qUsuario ? `"${p.qUsuario}"` : '—'}</span>
          <Icon path={mdiChevronDown} size={0.8} />
        </button>
        {openU && (
          <div className="dropdown" style={{ width: 280 }}>
            <div className="dropdown__body">
              <input
                className="input input--sm"
                autoFocus
                placeholder="Buscar usuario…"
                value={p.qUsuario}
                onChange={e => p.onUsuario(e.target.value)}
              />
              {p.qUsuario && (
                <button className="btn btn--ghost" onClick={() => p.onUsuario('')} style={{ justifySelf: 'end' }}>
                  <Icon path={mdiClose} size={0.8} /> Limpiar
                </button>
              )}
            </div>
          </div>
        )}
      </div>

      <div className="filters__sep" />

      {/* Nombre */}
      <div style={{ position: 'relative' }} ref={refN}>
        <button className="filters__item" onClick={() => (setOpenN(v => !v), setOpenU(false), setOpenI(false))}>
          <span className="filters__label">Nombre</span>
          <span className="filters__value">{p.qNombre ? `"${p.qNombre}"` : '—'}</span>
          <Icon path={mdiChevronDown} size={0.8} />
        </button>
        {openN && (
          <div className="dropdown" style={{ width: 280 }}>
            <div className="dropdown__body">
              <input
                className="input input--sm"
                placeholder="Buscar por nombres/apellidos…"
                value={p.qNombre}
                onChange={e => p.onNombre(e.target.value)}
              />
              {p.qNombre && (
                <button className="btn btn--ghost" onClick={() => p.onNombre('')} style={{ justifySelf: 'end' }}>
                  <Icon path={mdiClose} size={0.8} /> Limpiar
                </button>
              )}
            </div>
          </div>
        )}
      </div>

      <div className="filters__sep" />

      {/* Identificación */}
      <div style={{ position: 'relative' }} ref={refI}>
        <button className="filters__item" onClick={() => (setOpenI(v => !v), setOpenU(false), setOpenN(false))}>
          <span className="filters__label">Identificación</span>
          <span className="filters__value">{p.qIdent ? `"${p.qIdent}"` : '—'}</span>
          <Icon path={mdiChevronDown} size={0.8} />
        </button>
        {openI && (
          <div className="dropdown" style={{ width: 280 }}>
            <div className="dropdown__body">
              <input
                className="input input--sm"
                placeholder="DPI/ID…"
                value={p.qIdent}
                onChange={e => p.onIdent(e.target.value)}
              />
              {p.qIdent && (
                <button className="btn btn--ghost" onClick={() => p.onIdent('')} style={{ justifySelf: 'end' }}>
                  <Icon path={mdiClose} size={0.8} /> Limpiar
                </button>
              )}
            </div>
          </div>
        )}
      </div>

      <div className="filters__sep" />

      <div className="filters__right">
        <button className="filters__reset" onClick={p.onReset} style={{ display:'flex' }}>
          <Icon path={mdiRestore} size={0.8} style={{ marginRight: 6 }} />
          Resetear
        </button>
      </div>
    </div>
  );
}
