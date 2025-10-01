/**
 * RolesListPage — composición de microcomponentes.
 * Orquesta el hook (negocio) y renderiza la UI según mockup.
 */
import { useRolesPage } from '@/hooks';
import Icon from '@mdi/react';
import { mdiTrashCanOutline } from '@mdi/js';
import DataGrid, { Column } from '@/components/common/DataGrid';
import RoleInfoFormRHF from './components/RoleInfoFormRHF';
import RolePagesForm from './components/RolePagesForm';
import { Role } from '@/types';
import RoleFilterBar from './components/RoleFilterBar';
import { Tabs } from '@/components';

export default function RolesListPage() {
  const vm = useRolesPage();
  const showDetail = !!vm.selected;

  const columns: Column<Role>[] = [
    { key: 'nombre', header: 'ROL', width: 260, render: (r) => <span>{r.nombre}</span>, sortable: true }, // <- SIN negrita
    { key: 'descripcion', header: 'DESCRIPCIÓN', sortable: true },
    { key: 'admin', header: 'ADMIN', width: 110, align: 'center', sortable: true, getSortValue: (r) => r.admin ? 1 : 0,  
      render: (r) => <input type="checkbox" checked={!!r.admin} readOnly aria-label="Es administrador" className="admin-check" tabIndex={-1} /> },
    {
      key: 'actions', header: '', width: 60, align: 'center',
      render: (r) => (
        <button className="btn--icon btn--danger" onClick={(e) => { e.stopPropagation(); vm.confirmDelete(r.id); }} aria-label="Eliminar">
          <Icon path={mdiTrashCanOutline} size={0.85} />
        </button>
      ),
    },
  ];

  return (
    <div className="page">
      <h1 className="page__title">Listado de Roles</h1>

       {/* Filtros + (fuera) botón agregar */}
      <div className="page__actions">
        <div style={{ flex: 1 }}>
          <RoleFilterBar
            q={vm.q}
            onQChange={vm.setQ}
            onQDebounced={vm.applySearch}
            adminOnly={vm.adminOnly}
            onAdminChange={(v) => { vm.setAdminOnly(v); vm.setPage(0); }}
            onReset={() => vm.resetFilters()}
          />
        </div>
        <button className="btn btn-add" onClick={vm.startCreate} style={{ whiteSpace:'nowrap', width:170 }}>+ Agregar Rol</button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: showDetail ? '1fr 440px' : '1fr', gap: 12 }}>
        <DataGrid<Role>
          rows={vm.rows}
          columns={columns}
          loading={vm.loading}
          page={vm.page}
          size={vm.size}
          total={vm.total}
          selectedId={vm.selected?.id}
          onRowClick={(r) => vm.openRole(r.id)}
          onPageChange={vm.setPage}
        />

        {showDetail && (
          <div className="card">
            <div className="detail__header">
              <Tabs
                value={vm.tab}
                onChange={vm.setTab}
                items={[
                  { id: 'info',   label: 'Información General' },
                  { id: 'pages',  label: 'Páginas Asignadas', disabled: !vm.selected?.id },
                ]}
                full
                equal
                variant="brand"
              />
            </div>

            {vm.tab === 'info' && (
              <RoleInfoFormRHF
                key={vm.selected?.id ?? 0}
                value={vm.selected}
                onCancel={() => vm.setSelected(null)}  /* oculta panel */
                onSubmit={async (vals) => {await vm.saveInfo(vals);}}
              />
            )}
            {vm.tab === 'pages' && vm.selected?.id && (
              <div className="detail__body">
                <RolePagesForm
                  role={vm.selected}
                  pagesByModule={vm.pagesByModule}
                  onToggle={vm.togglePage}
                  onCancel={() => vm.setSelected(null)}
                  onSave={vm.savePages}
                />
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
