/**
 * ProvidersListPage — Orquestación de UI para Proveedores.
 * --------------------------------------------------------
 * - Lista + panel derecho (igual patrón de Usuarios).
 * - Tabs: "Información" y "Compras" (esta última vacía por ahora).
 */
import React from 'react';
import DataGrid, { Column } from '@/components/common/DataGrid';
import { Tabs } from '@/components';
import { useProvidersPage } from '@/hooks';
import ProviderFilterBar from './components/ProviderFilterBar';
import ProviderInfoFormRHF from './components/ProviderInfoFormRHF';
import ProviderPurchasesPanel from './components/ProviderPurchasesPanel';
import type { Supplier } from '@/types/provider';

export default function ProvidersListPage() {
  const vm = useProvidersPage();
  const showDetail = !!vm.selected;

  const columns: Column<Supplier>[] = [
    { key: 'nombre', header: 'NOMBRE', width: 260, render: r => <span>{r.nombre}</span>, sortable: true },
    { key: 'telefono', header: 'TELÉFONO', width: 160, render: r => <span>{r.telefono}</span> },
    { key: 'celular', header: 'CELULAR', width: 160, render: r => <span>{r.celular}</span> },
    { key: 'correo', header: 'CORREO', width: 260, render: r => <span>{r.correo}</span> },
    {
      key: 'activo', header: 'STATUS', width: 120, align: 'center',
      render: r => (
        <button
          className={`chip ${r.activo ? 'chip--success' : 'chip--danger'}`}
          title={r.activo ? 'Desactivar' : 'Activar'}
          onClick={(e) => { e.stopPropagation(); vm.toggleActive(r); }}
        >
          {r.activo ? 'ACTIVO' : 'INACTIVO'}
        </button>
      )
    },
  ];

  return (
    <div className="page">
      <div className="page__header">
        <h1>Listado de Proveedores</h1>
        <button className="btn btn--primary" onClick={vm.create}>+ AGREGAR PROVEEDOR</button>
      </div>

      <div className="panel panel--filters">
        <ProviderFilterBar
          q={vm.q}
          onQChange={(v) => { vm.setQ(v); }}
          onQDebounced={(v) => { vm.setQ(v); vm.setPage(0); vm.recompute(); }}
          onReset={() => { vm.setQ(''); vm.setPage(0); vm.recompute(); }}
        />
      </div>

      <div className={`split ${showDetail ? 'split--with-right' : ''}`}>
        <div className="split__left">
          <DataGrid
            rows={vm.rows}
            columns={columns}
            onRowClick={(r) => vm.open(r.id)}
            page={vm.page}
            total={vm.total}
            onPageChange={vm.setPage}
            loading={vm.loading}
          />
        </div>

        {showDetail && (
          <div className="split__right">
            <Tabs
              value={vm.tab}
              onChange={(t) => vm.setTab(t as any)}
              items={[
                { id: 'info', label: 'Información' },
                { id: 'compras', label: 'Compras' },
              ]}
            />
            {vm.tab === 'info' && vm.selected && (
              <>
                <ProviderInfoFormRHF
                  value={vm.selected}
                  onChange={(v) => vm.setSelected(v)}
                />
                <div className="panel__actions">
                  <button className="btn btn--ghost" onClick={() => vm.setSelected(null)}>Cancelar</button>
                  <button className="btn btn--primary" onClick={() => vm.selected && vm.save(vm.selected)}>Guardar</button>
                </div>
              </>
            )}
            {vm.tab === 'compras' && <ProviderPurchasesPanel provider={vm.selected!} />}
          </div>
        )}
      </div>
    </div>
  );
}
