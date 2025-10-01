import React from 'react';
import type { User } from '@/types/user';
import type { Role } from '@/types/role';

export default function UserRolesForm({
  user, roles, onToggle, onCancel, onSave,
}: {
  user: User;
  roles: Role[];
  onToggle: (roleId: number, checked: boolean) => void;
  onCancel: () => void;
  onSave: () => void;
}) {
  const set = new Set(user.roleIds);

  return (
    <>
      <div className="detail__body" style={{ paddingBottom: 0 }}>
        <div className="label" style={{ fontSize: 16, fontWeight: 800, marginBottom: 8 }}>Roles Asignados</div>
        <div style={{ display: 'grid', gap: 8 }}>
          {roles.map(r => (
            <label key={r.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 4px', borderBottom: '1px solid var(--color-border)' }}>
              <span style={{ fontWeight: 700 }}>{r.nombre}</span>
              <input
                type="checkbox"
                className="admin-check"
                checked={set.has(r.id)}
                onChange={e => onToggle(r.id, (e.target as HTMLInputElement).checked)}
              />
            </label>
          ))}
        </div>
      </div>
      <div style={{ display: 'flex', gap: 12, padding: '12px 16px', borderTop: '1px solid var(--color-border)' }}>
        <button className="btn btn--ghost" onClick={onCancel} style={{ background: '#ef4444', color: '#fff', border: 0 }}>CANCELAR</button>
        <button className="btn" onClick={onSave}>GUARDAR</button>
      </div>
    </>
  );
}
