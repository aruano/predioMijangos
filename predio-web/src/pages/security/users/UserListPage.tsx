import React from 'react';
import DataGrid, { Column } from '@/components/common/DataGrid';
import { Tabs } from '@/components';
import { useUsersPage } from '@/hooks';
import UserFilterBar from './components/UserFilterBar';
import UserRolesForm from './components/UserRolesForm';
import type { User } from '@/types/user';
import UserInfoFormRHF from './components/UserInfoFormRHF';

export default function UsersListPage() {
  const vm = useUsersPage();
  const showDetail = !!vm.selected;
  const [tab, setTab] = React.useState<'info' | 'roles'>('info');

  const columns: Column<User>[] = [
    { key: 'usuario', header: 'USUARIO', width: 220, render: r => <span>{r.usuario}</span> },
    { key: 'nombres', header: 'NOMBRES', width: 200, render: r => <span>{r.persona.nombres}</span> },
    { key: 'apellidos', header: 'APELLIDOS', width: 200, render: r => <span>{r.persona.apellidos}</span> },
    { key: 'correo', header: 'CORREO', width: 260, render: r => <span>{r.persona.correo}</span> },
    { key: 'telefono', header: 'TELÉFONO', width: 140, render: r => <span>{r.persona.telefono}</span> },
    {
      key: 'admin', header: 'ADMIN', width: 110, align: 'center',
      render: r => <input type="checkbox" checked={r.admin} readOnly aria-label="Es administrador" className="admin-check" tabIndex={-1} />
    },
    {
      key: 'status', header: 'STATUS', width: 130, align: 'center',
      render: (r) => (
        <button
          type="button"
          className={`chip ${r.activo ? 'chip--ok' : 'chip--off'}`}
          onClick={(e) => {
            e.stopPropagation();
            if (r.id === 0) return;
            const toActive = !r.activo;
            vm.toggleActive(r.id, toActive);
          }}
          role="switch"
          aria-checked={r.activo}
          aria-label={r.activo ? 'Desactivar usuario' : 'Activar usuario'}
          title={r.activo ? 'Click para desactivar' : 'Click para activar'}
          disabled={vm.loading}                  // opcional: bloquear mientras carga
        >
          {r.activo ? 'ACTIVO' : 'INACTIVO'}
        </button>
      ),
    }
  ];

  return (
    <div className="page">
      <h1 className="page__title">Listado de Usuarios</h1>

      <div className="page__actions">
        <div style={{ flex: 1 }}>
          <UserFilterBar
            qUsuario={vm.qUsuario} onUsuario={vm.setQUsuario} onUsuarioDebounced={vm.setQUsuario}
            qNombre={vm.qNombre} onNombre={vm.setQNombre} onNombreDebounced={vm.setQNombre}
            qIdent={vm.qIdent} onIdent={vm.setQIdent} onIdentDebounced={vm.setQIdent}
            onReset={vm.resetFilters}
          />
        </div>
        <button className="btn btn-add" onClick={vm.startCreate} style={{ whiteSpace: 'nowrap', width: 170 }}>+ Agregar Usuario</button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: showDetail ? '1fr 580px' : '1fr', gap: 12 }}>
        <DataGrid<User>
          rows={vm.rows}
          columns={columns}
          loading={vm.loading}
          page={vm.page}
          size={vm.size}
          total={vm.total}
          selectedId={vm.selected?.id}
          onRowClick={(r) => vm.openUser(r.id)}
          onPageChange={vm.setPage}
        />

        {showDetail && vm.selected && (
          <div className="card">
            <div className="detail__header">
              <Tabs
                value={tab}
                onChange={setTab}
                items={[
                  { id: 'info', label: 'Información General' },
                  { id: 'roles', label: 'Roles Asignados', disabled: vm.selected.id === 0 },
                ]}
                full equal variant="brand"
              />
            </div>
            {tab === 'info' && (
              <UserInfoFormRHF
                value={vm.selected}
                onCancel={() => vm.setSelected(null)}
                onSave={(vals, isNew) => vm.saveInfo(vals, isNew)}
              />
            )}

            {tab === 'roles' && (
              <div className="detail__body">
                <UserRolesForm
                  user={vm.selected}
                  roles={vm.rolesCat}
                  onToggle={vm.toggleRole}
                  onCancel={() => vm.setSelected(null)}
                  onSave={vm.saveRoles}
                />
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
