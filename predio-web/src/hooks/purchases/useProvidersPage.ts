/**
 * useProvidersPage — Lógica de Compras → Proveedores (caso de uso).
 * -----------------------------------------------------------------
 * Responsabilidades:
 * - Cargar TODO el catálogo (paginación en cliente).
 * - Filtros por nombre, teléfono, celular y correo (insensible a acentos).
 * - Selección y edición en panel derecho.
 * - Alta, actualización, activar/desactivar.
 * Expone:
 * - rows (página actual), total, page/size, loading
 * - q (filtro), selected, setSelected, open/create/save/toggle
 */
import React from 'react';
import { useNotice } from '@/components';
import type { Supplier } from '@/types/provider';
import { norm } from '@/utils/helpers';
import {
  listProviders, getProvider, createProvider, updateProvider,
  activateProvider, deactivateProvider,
} from '@/services/endpoints/providers.service';

export function useProvidersPage() {
  const notice = useNotice();

  // Filtros + paginación UI
  const [q, setQ] = React.useState('');
  const [page, setPage] = React.useState(0);
  const size = 10;

  // Data en memoria
  const [allRows, setAllRows] = React.useState<Supplier[]>([]);
  const [rows, setRows] = React.useState<Supplier[]>([]);
  const [total, setTotal] = React.useState(0);
  const [loading, setLoading] = React.useState(false);

  // Selección / panel derecho
  const [selected, setSelected] = React.useState<Supplier | null>(null);
  const [tab, setTab] = React.useState<'info' | 'compras'>('info');

  /** Recalcula filtrado + paginado en memoria */
  const recompute = React.useCallback(() => {
    const n = norm(q).trim();
    let filtered = allRows;

    if (n) {
      filtered = filtered.filter(r =>
        norm(r.nombre).includes(n) ||
        norm(r.telefono).includes(n) ||
        norm(r.celular).includes(n) ||
        norm(r.correo).includes(n)
      );
    }

    setTotal(filtered.length);
    const start = page * size;
    const end = start + size;
    setRows(filtered.slice(start, end));
  }, [allRows, page, size, q]);

  React.useEffect(() => { recompute(); }, [recompute]);

  /** Carga TODO el catálogo en memoria */
  const fetchAll = React.useCallback(async () => {
    setLoading(true);
    try {
      const PAGE_SIZE = 200;
      let p = 0;
      let acc: Supplier[] = [];
      // pedimos por páginas grandes hasta agotar
      // (la API nos devuelve totalElements)
      // usamos listProviders para mantener contrato único
      while (true) {
        const res = await listProviders({ page: p, size: PAGE_SIZE });
        const content = res.content ?? [];
        acc = acc.concat(content);
        if (content.length < PAGE_SIZE) break;
        p += 1;
      }
      setAllRows(acc);
      setPage(0);
    } catch {
    } finally {
      setLoading(false);
    }
  }, [notice]);

  /** Abrir detalle */
  async function open(id: number) {
    try {
      const full = await getProvider(id);
      setSelected(full);
      setTab('info');
    } catch {
    }
  }

  /** Crear */
  async function create() {
    setSelected({
      id: 0, nombre: '', direccion: '', telefono: '', celular: '',
      correo: '', observaciones: '', activo: true,
    });
    setTab('info');
  }

  /** Guardar (create/update) y refrescar memoria incremental */
  async function save(values: Supplier) {
    try {
      let saved: Supplier;
      if (!values.id) {
        saved = await createProvider(values);
        setAllRows(prev => [saved, ...prev]);
      } else {
        saved = await updateProvider(values.id, values);
        setAllRows(prev => prev.map(r => (r.id === saved.id ? saved : r)));
      }
      setSelected(saved);
      notice.success('¡Listo!', 'Proveedor guardado correctamente');
    } catch {
    }
  }

  /** Activar/Desactivar */
  async function toggleActive(row: Supplier) {
    try {
      if (row.activo) {
        await deactivateProvider(row.id);
        setAllRows(prev => prev.map(r => r.id === row.id ? { ...r, activo: false } : r));
      } else {
        await activateProvider(row.id);
        setAllRows(prev => prev.map(r => r.id === row.id ? { ...r, activo: true } : r));
      }
    } catch {
    }
  }

  React.useEffect(() => { fetchAll(); }, [fetchAll]);

  return {
    // estado UI
    q, setQ, page, setPage, size, total, rows, loading,
    // selección
    selected, setSelected, tab, setTab,
    // acciones
    open, create, save, toggleActive, recompute,
  };
}
