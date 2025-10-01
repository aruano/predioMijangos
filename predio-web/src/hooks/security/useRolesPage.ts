/**
 * useRolesPage — encapsula la lógica de Roles (capa de “casos de uso”).
 * - Maneja filtros, paginación, selección y panel derecho.
 * - Orquesta CRUD contra el repositorio (servicios HTTP).
 * - Expone la mínima API para que la UI permanezca “tonta”.
 */
import React from 'react';
import { useNotice } from '@/components';
import {
  listRols, getRol, createRol, updateRol, deleteRol, listPagesForWeb,
} from '@/services/endpoints/';
import type { Role, RoleId, PageRef } from '@/types/role';

export type RolesTab = 'info' | 'pages';

export function useRolesPage() {
  const notice = useNotice();

  // ===== Filtros & paginación (UI)
  const [adminOnly, setAdminOnly] = React.useState<boolean | null>(null);
  const [q, setQ] = React.useState('');
  const [page, setPage] = React.useState(0);
  const size = 10;

  // ===== Data en memoria
  const [allRows, setAllRows] = React.useState<Role[]>([]); // <-- TODO el catálogo en memoria
  const [rows, setRows] = React.useState<Role[]>([]);       // <-- página actual (slice)
  const [total, setTotal] = React.useState(0);              // <-- total filtrado
  const [loading, setLoading] = React.useState(false);

  // ===== Selección / panel derecho
  const [selected, setSelected] = React.useState<Role | null>(null);
  const [tab, setTab] = React.useState<RolesTab>('info');

  // ===== Catálogo de páginas
  const [pagesByModule, setPagesByModule] = React.useState<Record<string, PageRef[]>>({});
  const [pagesLoaded, setPagesLoaded] = React.useState(false);

  // ---- Helpers
  const normalize = (s?: string) =>
    (s || '')
      .toLowerCase()
      .normalize('NFD')
      .replace(/\p{Diacritic}/gu, '');

  /** Recalcula filtrado + paginado en memoria */
  const recompute = React.useCallback(() => {
    const nq = normalize(q).trim();

    let filtered = allRows;

    if (nq) {
      filtered = filtered.filter(r =>
        normalize(r.nombre).includes(nq) ||
        normalize(r.descripcion).includes(nq)
      );
    }

    if (adminOnly !== null) {
      filtered = filtered.filter(r => (!!r.admin) === adminOnly);
    }

    setTotal(filtered.length);

    // paginado local
    const start = page * size;
    const end = start + size;
    setRows(filtered.slice(start, end));
  }, [allRows, q, adminOnly, page, size]);

  React.useEffect(() => { recompute(); }, [recompute]);

  /** Carga TODO el catálogo (pidiendo páginas grandes para reducir requests) */
  const fetchAll = React.useCallback(async () => {
    setLoading(true);
    try {
      const PAGE_SIZE = 200;
      let p = 0;
      let acc: Role[] = [];
      let totalServer = Number.POSITIVE_INFINITY;

      while (acc.length < totalServer) {
        // 🚫 No pasamos filtros al backend; traemos todo
        const res: any = await listRols({ page: p, size: PAGE_SIZE });
        const content: Role[] = res.content ?? res.items ?? [];
        totalServer = res.totalElements ?? res.total ?? content.length;

        acc = acc.concat(content);
        if (content.length < PAGE_SIZE) break; // última página corta
        p += 1;
      }

      setAllRows(acc);
      setPage(0); // resetea paginación de UI
    } catch {
      notice.error('¡Ha ocurrido un error!','No se pudo cargar la lista de roles');
    } finally {
      setLoading(false);
    }
  }, [notice]);

  React.useEffect(() => { fetchAll(); }, [fetchAll]);

  /** Carga catálogo de páginas (una vez) */
  React.useEffect(() => {
    (async () => {
      try {
        const list = await listPagesForWeb();
        const grouped: Record<string, PageRef[]> = {};
        list.forEach(p => { (grouped[p.moduloNombre] ||= []).push(p); });
        setPagesByModule(grouped);
        setPagesLoaded(true);
      } catch {
        notice.error('¡Ha ocurrido un error!','No se pudo cargar el catálogo de páginas');
      }
    })();
  }, [notice]);

  // ===== Acciones de UI
  function resetFilters() {
    setQ('');
    setAdminOnly(null);
    setPage(0);
  }

  const applySearch = React.useCallback((nextQ: string) => {
    setQ(nextQ);
    setPage(0);
  }, []);

  function startCreate() {
    setSelected({ id: 0, nombre: '', descripcion: '', admin: false, paginaIds: [] });
    setTab('info');
  }

  async function openRole(id: RoleId) {
    try {
      const full = await getRol(id);
      setSelected(full);
      setTab('info');
    } catch {
      notice.error('¡Ha ocurrido un error!','No se pudo obtener el rol');
    }
  }

  /** Guardar info general (actualiza memoria sin depender del refetch) */
  async function saveInfo(vals: any) {
    const base = selected ?? { id: 0, paginaIds: [] as number[] };
    const payload: Role = {
      ...base,
      nombre: (vals.nombre ?? '').toUpperCase().trim(),
      descripcion: (vals.descripcion ?? '').trim(),
      admin: !!vals.admin,
    };

    try {
      const saved = payload.id > 0
        ? await updateRol(payload.id, payload)
        : await createRol(payload);

      setSelected(saved);

      // 🔄 Actualiza el catálogo en memoria (add/replace)
      setAllRows(prev => {
        const idx = prev.findIndex(r => r.id === saved.id);
        if (idx === -1) return [saved, ...prev];
        const copy = prev.slice();
        copy[idx] = saved;
        return copy;
      });

      notice.success('Operación Realizada', payload.id > 0 ? 'Rol actualizado' : 'Rol creado');
    } catch (error) {
      // deja que el handler global pinte el error si lo tienes
      throw error;
    }
  }

  /** Guardar páginas asignadas (PUT acepta paginaIds) */
  async function savePages() {
    if (!selected) return;
    try {
      const saved = await updateRol(selected.id, selected);
      setSelected(saved);

      setAllRows(prev => {
        const idx = prev.findIndex(r => r.id === saved.id);
        if (idx === -1) return [saved, ...prev];
        const copy = prev.slice();
        copy[idx] = saved;
        return copy;
      });

      notice.success('Operación Realizada','Páginas asignadas actualizadas');
    } catch {
      notice.error('¡Ha ocurrido un error!','No se pudo actualizar páginas');
    }
  }

  function togglePage(pageId: number, checked: boolean) {
    setSelected(prev => {
      if (!prev) return prev;
      const set = new Set(prev.paginaIds);
      checked ? set.add(pageId) : set.delete(pageId);
      return { ...prev, paginaIds: Array.from(set) };
    });
  }

  /** Eliminar (actualiza memoria y luego notificación) */
  function confirmDelete(id: RoleId) {
    notice.warning(
      'Eliminar rol',
      '¿Confirma eliminar este rol? Se rechazará si tiene usuarios asociados.',
      {
        label: 'Eliminar',
        onPress: async () => {
          try {
            await deleteRol(id);

            setAllRows(prev => prev.filter(r => r.id !== id)); // <-- quita de memoria
            if (selected?.id === id) setSelected(null);

            // la UI se recalcula sola por efecto de recompute()
            setTimeout(() => notice.success('Operación Realizada','Rol eliminado'), 0);
          } catch {
            notice.error('¡Ha ocurrido un error!','No se pudo eliminar el rol');
          }
        },
      },
      { label: 'Cancelar' }
    );
  }

  return {
    // estado expuesto
    q, setQ, page, setPage, size,
    rows, total, loading,
    selected, setSelected,
    tab, setTab,
    pagesByModule, pagesLoaded,
    adminOnly, setAdminOnly,

    // acciones
    resetFilters,
    applySearch,
    startCreate,
    openRole,
    saveInfo,
    savePages,
    togglePage,
    confirmDelete,
  };
}