/**
 * useUsersPage — lógica de Usuarios con filtros y paginación **en cliente**.
 * - Carga TODO en memoria (allUsers)
 * - Aplica filtros por Usuario/Nombre/Identificación (insensible a acentos)
 * - Calcula Admin (si el usuario tiene algún rol admin)
 * - Permite activar/desactivar y asignar roles
 */
import React from 'react';
import { useNotice } from '@/components';
import { listUsers, getUser, updateUser, createUser, activateUser, deactivateUser } from '@/services/endpoints/users.service';
import { listRols } from '@/services/endpoints/security.service';
import type { User } from '@/types/user';
import type { Role } from '@/types/role';
import { norm } from '@/utils/helpers';

export function useUsersPage() {
  const notice = useNotice();

  // Filtros + paginación UI
  const [qUsuario, setQUsuario] = React.useState('');
  const [qNombre, setQNombre] = React.useState('');
  const [qIdent, setQIdent] = React.useState('');
  const [page, setPage] = React.useState(0);
  const size = 10;

  // Catálogo roles (para panel de asignación)
  const [rolesCat, setRolesCat] = React.useState<Role[]>([]);

  // Data
  const [allUsers, setAllUsers] = React.useState<User[]>([]);
  const [rows, setRows] = React.useState<User[]>([]);
  const [total, setTotal] = React.useState(0);
  const [loading, setLoading] = React.useState(false);

  // Selección / panel
  const [selected, setSelected] = React.useState<User | null>(null);

  // ====== Carga completa de usuarios (loop por páginas) ======
  const fetchAllUsers = React.useCallback(async () => {
    setLoading(true);
    try {
      const PAGE = 200;
      let p = 0, acc: User[] = [], totalServer = Infinity;
      while (acc.length < totalServer) {
        const res: any = await listUsers({ page: p, size: PAGE }); // 🚫 sin filtros; los aplicamos en cliente
        const content: User[] = res.content ?? [];
        totalServer = res.totalElements ?? content.length;
        acc = acc.concat(content);
        if (content.length < PAGE) break;
        p += 1;
      }
      setAllUsers(acc);
      setPage(0);
    } catch (error) {
      notice.error('¡Ha ocurrido un error!','No se pudo cargar usuarios');
    } finally {
      setLoading(false);
    }
  }, [notice]);

  React.useEffect(() => { fetchAllUsers(); }, [fetchAllUsers]);

  // ====== Carga catálogo de roles (para asignación) ======
  React.useEffect(() => {
    (async () => {
      try {
        // Trae algunas páginas hasta completar
        const PAGE = 200;
        let p = 0, acc: Role[] = [], totalServer = Infinity;
        while (acc.length < totalServer) {
          const res: any = await listRols({ page: p, size: PAGE });
          const content: Role[] = res.content ?? res.items ?? [];
          totalServer = res.totalElements ?? res.total ?? content.length;
          acc = acc.concat(content);
          if (content.length < PAGE) break;
          p += 1;
        }
        setRolesCat(acc);
      } catch {
        notice.error('¡Ha ocurrido un error!','No se pudo cargar el catálogo de roles');
      }
    })();
  }, [notice]);

  // ====== Recompute (filtros + paginación local) ======
  const recompute = React.useCallback(() => {
    const u = norm(qUsuario), n = norm(qNombre), i = norm(qIdent);
    let filtered = allUsers;

    if (u) filtered = filtered.filter(x => norm(x.usuario).includes(u));
    if (n) filtered = filtered.filter(x => `${norm(x.persona.nombres)} ${norm(x.persona.apellidos)}`.includes(n));
    if (i) filtered = filtered.filter(x => norm(x.persona.identificacion).includes(i));

    setTotal(filtered.length);
    const start = page * size, end = start + size;
    setRows(filtered.slice(start, end));
  }, [allUsers, qUsuario, qNombre, qIdent, page, size]);

  React.useEffect(() => { recompute(); }, [recompute]);

  // ====== Acciones ======
  function resetFilters() { setQUsuario(''); setQNombre(''); setQIdent(''); setPage(0); }

  async function openUser(id: number) {
    try { setSelected(await getUser(id)); }
    catch { notice.error('¡Ha ocurrido un error!','No se pudo obtener el usuario'); }
  }

  function startCreate() {
    setSelected({
      id: 0,
      usuario: '',
      activo: true,
      persona: { tipoIdentificacion: '', identificacion: '', nombres: '', apellidos: '', correo: '', telefono: '' },
      roleIds: [],
      admin: false,
    });
  }

  // update general 
  async function saveUser(u: User) {
    try {
      const saved = u.id ? await updateUser(u.id, u) : await createUser(u, '');
      setSelected(saved);
      setAllUsers(prev => {
        const idx = prev.findIndex(x => x.id === saved.id);
        if (idx === -1) return [saved, ...prev];
        const copy = prev.slice(); copy[idx] = saved; return copy;
      });
      notice.success('Operación Realizada', u.id ? 'Usuario actualizado' : 'Usuario creado');
    } catch { /* handler global de errores */ }
  }

  async function saveInfo(vals: {
  usuario: string;
  password?: string;
  persona: {
    tipoIdentificacion?: string;
    identificacion?: string;
    nombres: string;
    apellidos: string;
    correo?: string;
    telefono?: string;
    idDepartamento?: number;
    idMunicipio?: number;
    direccionDomicilio?: string;
  };
  activo: boolean;
}, isNew: boolean) {
  const base: User = selected ?? {
    id: 0,
    usuario: '',
    activo: true,
    persona: { tipoIdentificacion: '', identificacion: '', nombres: '', apellidos: '', correo: '', telefono: '' },
    roleIds: [],
    admin: false,
  };

  const merged: User = {
    ...base,
    usuario: vals.usuario.trim(),
    activo: !!vals.activo,
    persona: {
      ...base.persona,
      ...vals.persona,
    },
  };

  try {
    const saved = isNew
      ? await createUser(merged, (vals.password ?? '').trim())
      : await updateUser(base.id, { ...merged, ...(vals.password?.trim() ? { password: vals.password.trim() } : {}) });

    setSelected(saved);
    setAllUsers(prev => {
      const idx = prev.findIndex(u => u.id === saved.id);
      if (idx === -1) return [saved, ...prev];
      const copy = prev.slice(); copy[idx] = saved; return copy;
    });
    notice.success('Operación Realizada', !isNew ? 'Usuario actualizado' : 'Usuario creado');
  } catch {
    
  }
}

  async function toggleActive(id: number, toActive: boolean) {
      notice.warning(
            toActive ? 'Activar Usuario' : 'Desactivar Usuario', 
            `¿Confirma ${toActive ? 'activar' : 'desactivar'} este usuario?`,
            {
              label: 'Confirmar',
              onPress: async () => {
                try {
                  toActive ? await activateUser(id) : await deactivateUser(id);
      
                  setAllUsers(prev => prev.map(x => (x.id === id ? { ...x, activo: toActive } : x)));
                  if (selected?.id === id) setSelected({ ...selected, activo: toActive });
      
                  setTimeout(() => notice.success('Operación Realizada', toActive ? 'Usuario activado' : 'Usuario desactivado'), 0);
                } catch {
                  notice.error('¡Ha ocurrido un error!','No se pudo activar/desactivar el usuario');
                }
              },
            },
            { label: 'Cancelar' }
          );
  }

  function toggleRole(roleId: number, checked: boolean) {
    setSelected(prev => {
      if (!prev) return prev;
      const set = new Set(prev.roleIds);
      checked ? set.add(roleId) : set.delete(roleId);
      return { ...prev, roleIds: Array.from(set) };
    });
  }

  async function saveRoles() {
    if (!selected) return;
    await saveUser(selected); // update con rolesIds en el modelo
  }

  return {
    // estado/props para UI
    qUsuario, setQUsuario,
    qNombre, setQNombre,
    qIdent, setQIdent,
    page, setPage, size,
    rows, total, loading,
    selected, setSelected,
    rolesCat,

    // acciones
    resetFilters,
    openUser,
    startCreate,
    saveInfo,
    saveRoles,
    toggleActive,
    toggleRole,
  };
}
