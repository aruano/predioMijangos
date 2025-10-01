/**
 * RolePagesForm — asignación de páginas agrupadas por módulo.
 */
import React from 'react';
import type { Role, PageRef } from '@/types/role';

type Props = {
  role: Role;
  pagesByModule: Record<string, PageRef[]>;
  onToggle: (pageId: number, checked: boolean) => void;
  onCancel: () => void;
  onSave: () => void;
};

export default function RolePagesForm({ role, pagesByModule, onToggle, onCancel, onSave }: Props) {
  return (
    <div style={{ display: 'grid', gap: 12 }}>
      {Object.entries(pagesByModule).map(([mod, pages]) => (
        <fieldset key={mod} style={{ border: '2px solid var(--color-border)', borderRadius: 12, padding: 10 }}>
          <legend className="text-muted" style={{ padding: '0 6px' }}>{mod}</legend>
          <div style={{ display: 'grid', gap: 8 }}>
            {pages.map(p => {
              const checked = role.paginaIds.includes(p.id);
              return (
                <label key={p.id} className="check" style={{ justifyContent: 'space-between' }}>
                  <span>{p.nombre}</span>
                  <input type="checkbox" checked={checked} onChange={e => onToggle(p.id, e.target.checked)} />
                </label>
              );
            })}
          </div>
        </fieldset>
      ))}
      <div style={{ marginTop: 'auto', display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
        <button className="btn btn--ghost" onClick={onCancel}>Cancelar</button>
        <button className="btn" onClick={onSave}>Guardar</button>
      </div>
    </div>
  );
}
