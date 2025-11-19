# 🔄 Mappers - Sistema de Transformación de Datos

**Versión:** 2.0.0  
**Última Actualización:** Octubre 2025  
**Autor:** Equipo Técnico Predio Mijangos

---

## 📋 Índice

1. [Introducción](#introducción)
2. [¿Por Qué Mapear?](#por-qué-mapear)
3. [Arquitectura](#arquitectura)
4. [Sincronización con Backend](#sincronización-con-backend)
5. [Estructura de Archivos](#estructura-de-archivos)
6. [Guía de Uso](#guía-de-uso)
7. [Ejemplos Completos](#ejemplos-completos)
8. [Mejores Prácticas](#mejores-prácticas)
9. [Crear Nuevos Mappers](#crear-nuevos-mappers)
10. [Testing](#testing)
11. [Troubleshooting](#troubleshooting)

---

## 🎯 Introducción

Los **Mappers** son funciones puras que transforman datos entre diferentes representaciones:

- **DTO (Data Transfer Object)**: Datos del backend optimizados para transporte
- **Entity (UI Model)**: Datos del frontend optimizados para la interfaz
- **FormData**: Datos del formulario (React Hook Form + Zod)

### Flujo de Datos

```
┌─────────────┐      Mapper      ┌──────────┐      Componente      ┌────────┐
│  Backend    │ ─── DTO → UI ──→ │ Service  │ ────────────────────→ │   UI   │
│  (Spring)   │                  │          │                       │ (React)│
└─────────────┘                  └──────────┘                       └────────┘
       ↑                                                                  │
       │                          ┌──────────┐      Mapper               │
       └──────── Form → DTO ───── │ Service  │ ←────────────────────────┘
                                  └──────────┘
```

---

## 🤔 ¿Por Qué Mapear?

### Problemas que Resuelven los Mappers

| Problema | Solución con Mappers |
|----------|---------------------|
| **Fechas ISO 8601** del backend | Formatear a DD/MM/YYYY para UI |
| **Campos anidados** complejos | Aplanar o normalizar estructura |
| **Campos null** del backend | Convertir a string vacío para inputs |
| **Valores vacíos** de forms | Convertir a null para backend |
| **Nombres compuestos** | Construir nombre completo |
| **Roles múltiples** | Detectar si es admin |
| **Auditoría** | Formatear createdAt/updatedAt |

### Beneficios

✅ **Desacoplamiento**: Frontend independiente de cambios en backend  
✅ **Tipado Fuerte**: TypeScript valida transformaciones  
✅ **Reutilización**: Funciones puras reutilizables  
✅ **Testeable**: Fácil de probar unitariamente  
✅ **Mantenible**: Cambios centralizados  
✅ **Documentado**: JSDoc completo con ejemplos  

---

## 🏗️ Arquitectura

### Capas de Mappers

```
┌──────────────────────────────────────────────────┐
│          base.mapper.ts (CORE)                   │
│  • Tipos genéricos (BaseMapper, ArrayMapper)    │
│  • Utilidades (mapArray, formatAuditFields)     │
│  • Funciones de transformación (buildFullName)  │
└──────────────────────────────────────────────────┘
                      ↓ importa
┌──────────────────────────────────────────────────┐
│     Mappers Específicos (user, role, etc)       │
│  • Tipos de UI (User, Role, FormData)           │
│  • Mappers DTO → UI                              │
│  • Mappers Form → DTO                            │
│  • Utilidades específicas del dominio           │
└──────────────────────────────────────────────────┘
                      ↓ usa
┌──────────────────────────────────────────────────┐
│            Services y Hooks                      │
│  • Llaman API                                    │
│  • Transforman DTOs con mappers                  │
│  • Retornan entidades UI                         │
└──────────────────────────────────────────────────┘
```

### Tipos de Transformaciones

```typescript
// 1. DTO → UI (Lectura)
UsuarioResponseDTO → User

// 2. Form → DTO (Escritura - Create)
UserFormData → UsuarioCreateDTO

// 3. Form → DTO (Escritura - Update)
UserFormData → UsuarioUpdateDTO

// 4. UI → Form (Edición)
User → UserFormData
```

---

## 🔗 Sincronización con Backend

### DTOs del Backend (Spring Boot)

```java
// Backend: UsuarioResponseDTO.java
public class UsuarioResponseDTO {
    private Integer id;
    private PersonaResponseDTO persona;
    private String username;
    private Boolean activo;
    private Set<RolSimpleDTO> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// Backend: PersonaResponseDTO.java
public class PersonaResponseDTO {
    private Integer id;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String email;
    private String dpi;
    private String nit;
    // ...
}
```

### Tipos en Frontend (TypeScript)

```typescript
// Frontend: types/api.d.ts
export interface UsuarioResponseDTO {
  id: number;
  username: string;
  activo: boolean;
  persona: PersonaResponseDTO;
  roles: RolSimpleDTO[];
  createdAt: string; // ISO 8601
  updatedAt: string | null;
}

// Frontend: mappers/user.mapper.ts
export interface User {
  id: number;
  username: string;
  email: string;
  activo: boolean;
  persona: { /* ... */ };
  roles: Role[];
  nombreCompleto: string; // ← COMPUTADO
  iniciales: string; // ← COMPUTADO
  isAdmin: boolean; // ← COMPUTADO
  createdAt: string; // ← FORMATEADO
  updatedAt: string | null; // ← FORMATEADO
}
```

### Campos de Auditoría

Todos los DTOs Response del backend incluyen:

```typescript
interface AuditFields {
  createdAt: string; // ISO 8601
  updatedAt: string | null;
  createdBy?: string;
  updatedBy?: string;
}
```

Los mappers formatean automáticamente usando `formatAuditFields()`:

```typescript
// ISO 8601: "2024-01-01T10:30:00Z"
// ↓
// UI: "01/01/2024 10:30"
```

---

## 📁 Estructura de Archivos

```
src/mappers/
├── base.mapper.ts          # ⭐ Tipos y utilidades base (CORE)
│   ├── Tipos genéricos (BaseMapper, ArrayMapper)
│   ├── Utilidades de mapeo (mapArray, mapOptional)
│   ├── Formateo de fechas (formatDateForUI, formatAuditFields)
│   ├── Transformaciones (buildFullName, getInitials)
│   └── Validaciones (isValidId, hasValidAudit)
│
├── user.mapper.ts          # 👤 Mappers de Usuarios
│   ├── User, UserList, UserFormData
│   ├── mapUsuarioDTOToUser
│   ├── mapUserFormToCreateDTO
│   ├── mapUserFormToUpdateDTO
│   └── mapUserToFormData
│
├── role.mapper.ts          # 🔐 Mappers de Roles
│   ├── Role, RoleList, RoleFormData
│   ├── mapRolDTOToRole
│   ├── mapRoleFormToCreateDTO
│   ├── mapRoleFormToUpdateDTO
│   └── groupPermissionsByModule
│
├── index.ts                # 📤 Exportaciones centralizadas
│   ├── Re-exporta todos los mappers
│   ├── Aliases para compatibilidad
│   └── MapperRegistry (opcional)
│
└── README.md               # 📖 Esta documentación
```

---

## 📚 Guía de Uso

### Importación

```typescript
// Opción 1: Importar mappers específicos
import { mapUsuarioDTOToUser, mapUserFormToCreateDTO } from '@/mappers';

// Opción 2: Importar todos los mappers de un dominio
import { userMappers } from '@/mappers';

// Opción 3: Importar utilidades base
import { mapArray, formatAuditFields, buildFullName } from '@/mappers';
```

### Uso Básico en Servicios

```typescript
// services/endpoints/usuarios.service.ts
import { mapUsuarioDTOToUser, mapUsuariosDTOToUsers } from '@/mappers';
import { apiService } from '@/services/api.service';

/**
 * Obtener usuario por ID
 */
export const getUsuario = async (id: number): Promise<User> => {
  const response = await apiService.get<UsuarioResponseDTO>(`/api/v1/usuarios/${id}`);
  
  // Transformar DTO a entidad UI
  const user = mapUsuarioDTOToUser(response.data.body);
  
  return user;
};

/**
 * Listar usuarios con paginación
 */
export const listUsuarios = async (
  params: UsuarioFilterDTO
): Promise<PageResponse<User>> => {
  const response = await apiService.get<PageResponse<UsuarioResponseDTO>>(
    '/api/v1/usuarios',
    { params }
  );
  
  // Transformar array de DTOs
  const users = mapUsuariosDTOToUsers(response.data.body.content);
  
  return {
    ...response.data.body,
    content: users, // ← Reemplazar DTOs con entidades UI
  };
};

/**
 * Crear usuario
 */
export const createUsuario = async (formData: UserFormData): Promise<User> => {
  // Transformar form data a DTO
  const createDTO = mapUserFormToCreateDTO(formData);
  
  const response = await apiService.post<UsuarioResponseDTO>(
    '/api/v1/usuarios',
    createDTO
  );
  
  // Transformar DTO de respuesta
  return mapUsuarioDTOToUser(response.data.body);
};
```

### Uso en Hooks

```typescript
// hooks/users/useUsuariosPage.ts
import { useQuery, useMutation } from '@tanstack/react-query';
import { mapUserToFormData } from '@/mappers';
import * as usuarioService from '@/services/endpoints/usuarios.service';

export const useUsuariosPage = () => {
  // Fetch con transformación automática
  const { data: users } = useQuery({
    queryKey: ['usuarios'],
    queryFn: () => usuarioService.listUsuarios({}),
  });
  
  // Mutación para crear
  const createMutation = useMutation({
    mutationFn: (formData: UserFormData) => usuarioService.createUsuario(formData),
    onSuccess: () => {
      toast.success('Usuario creado exitosamente');
    },
  });
  
  // Poblar formulario para edición
  const handleEdit = (user: User) => {
    const formData = mapUserToFormData(user);
    form.reset(formData); // ← React Hook Form
  };
  
  return {
    users,
    createMutation,
    handleEdit,
  };
};
```

### Uso con React Hook Form

```typescript
// components/UsuarioForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { mapUserToFormData } from '@/mappers';
import type { UserFormData } from '@/mappers';
import { usuarioSchema } from '@/validations/usuario.validation';

interface Props {
  user?: User; // undefined = create, User = edit
  onSubmit: (data: UserFormData) => void;
}

export const UsuarioForm = ({ user, onSubmit }: Props) => {
  const form = useForm<UserFormData>({
    resolver: zodResolver(usuarioSchema),
    defaultValues: user ? mapUserToFormData(user) : {
      username: '',
      email: '',
      password: '',
      activo: true,
      // ... valores por defecto
    },
  });
  
  return (
    <form onSubmit={form.handleSubmit(onSubmit)}>
      {/* Inputs del formulario */}
    </form>
  );
};
```

---

## 💡 Ejemplos Completos

### Ejemplo 1: CRUD Completo de Usuarios

```typescript
// =============================================
// SERVICE: usuarios.service.ts
// =============================================

import {
  mapUsuarioDTOToUser,
  mapUsuariosDTOToUsers,
  mapUserFormToCreateDTO,
  mapUserFormToUpdateDTO,
} from '@/mappers';

/**
 * Listar usuarios
 */
export const listUsuarios = async (): Promise<User[]> => {
  const response = await apiService.get<PageResponse<UsuarioResponseDTO>>(
    '/api/v1/usuarios'
  );
  
  return mapUsuariosDTOToUsers(response.data.body.content);
};

/**
 * Obtener usuario por ID
 */
export const getUsuario = async (id: number): Promise<User> => {
  const response = await apiService.get<UsuarioResponseDTO>(
    `/api/v1/usuarios/${id}`
  );
  
  return mapUsuarioDTOToUser(response.data.body);
};

/**
 * Crear usuario
 */
export const createUsuario = async (formData: UserFormData): Promise<User> => {
  const createDTO = mapUserFormToCreateDTO(formData);
  
  const response = await apiService.post<UsuarioResponseDTO>(
    '/api/v1/usuarios',
    createDTO
  );
  
  return mapUsuarioDTOToUser(response.data.body);
};

/**
 * Actualizar usuario
 */
export const updateUsuario = async (
  id: number,
  formData: UserFormData
): Promise<User> => {
  const updateDTO = mapUserFormToUpdateDTO(formData);
  
  const response = await apiService.put<UsuarioResponseDTO>(
    `/api/v1/usuarios/${id}`,
    updateDTO
  );
  
  return mapUsuarioDTOToUser(response.data.body);
};

// =============================================
// HOOK: useUsuariosPage.ts
// =============================================

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { mapUserToFormData } from '@/mappers';

export const useUsuariosPage = () => {
  const queryClient = useQueryClient();
  
  // Listado
  const { data: users, isLoading } = useQuery({
    queryKey: ['usuarios'],
    queryFn: listUsuarios,
  });
  
  // Crear
  const createMutation = useMutation({
    mutationFn: createUsuario,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['usuarios'] });
      toast.success('Usuario creado');
    },
  });
  
  // Actualizar
  const updateMutation = useMutation({
    mutationFn: ({ id, formData }: { id: number; formData: UserFormData }) =>
      updateUsuario(id, formData),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['usuarios'] });
      toast.success('Usuario actualizado');
    },
  });
  
  // Handler para editar
  const handleEdit = (user: User) => {
    const formData = mapUserToFormData(user);
    // Pasar a formulario
    return formData;
  };
  
  return {
    users,
    isLoading,
    createMutation,
    updateMutation,
    handleEdit,
  };
};

// =============================================
// COMPONENTE: UsuariosList.tsx
// =============================================

export const UsuariosList = () => {
  const { users, isLoading, handleEdit } = useUsuariosPage();
  
  if (isLoading) return <Loading />;
  
  return (
    <div>
      {users?.map((user) => (
        <div key={user.id}>
          <span>{user.nombreCompleto}</span>
          <span>{user.email}</span>
          <span>{user.isAdmin ? 'Admin' : 'Usuario'}</span>
          <button onClick={() => handleEdit(user)}>Editar</button>
        </div>
      ))}
    </div>
  );
};
```

### Ejemplo 2: Búsqueda y Filtrado

```typescript
// Filtrar usuarios activos con rol específico
import { filterActiveUsers, filterUsersByRole } from '@/utils/user.utils';

const activeAdmins = users
  .filter((user) => user.activo)
  .filter((user) => user.isAdmin);

// O usando utilidades personalizadas
const admins = filterUsersByRole(users, 'ROLE_ADMIN');
```

### Ejemplo 3: Manejo de Errores

```typescript
import { safeMap } from '@/mappers';

const result = safeMap(
  mapUsuarioDTOToUser,
  dto,
  'Error mapeando usuario'
);

if (result.success) {
  console.log('Usuario mapeado:', result.data);
} else {
  console.error('Error:', result.error);
  toast.error('Error al procesar usuario');
}
```

---

## ✨ Mejores Prácticas

### 1. Siempre Usar Mappers en Servicios

❌ **INCORRECTO:**
```typescript
// Usar DTOs directamente en componentes
const user = response.data.body; // UsuarioResponseDTO
```

✅ **CORRECTO:**
```typescript
// Transformar a entidad UI
const user = mapUsuarioDTOToUser(response.data.body);
```

### 2. No Modificar DTOs

❌ **INCORRECTO:**
```typescript
// Modificar DTO directamente
dto.nombreCompleto = `${dto.persona.primerNombre} ${dto.persona.primerApellido}`;
```

✅ **CORRECTO:**
```typescript
// Crear nueva entidad con campos computados
const user = mapUsuarioDTOToUser(dto); // ← Incluye nombreCompleto
```

### 3. Validar Estructura Antes de Mapear

❌ **INCORRECTO:**
```typescript
// Asumir estructura sin validar
const user = mapUsuarioDTOToUser(dto);
```

✅ **CORRECTO:**
```typescript
// Validar primero
if (!isValidUsuarioDTO(dto)) {
  throw new Error('DTO inválido');
}
const user = mapUsuarioDTOToUser(dto);
```

### 4. Usar Mappers Opcionales para Valores Null

❌ **INCORRECTO:**
```typescript
// Verificar manualmente
const user = dto ? mapUsuarioDTOToUser(dto) : null;
```

✅ **CORRECTO:**
```typescript
// Usar mapper opcional
const user = mapOptionalUser(dto); // User | null
```

### 5. No Duplicar Lógica de Transformación

❌ **INCORRECTO:**
```typescript
// Duplicar lógica en varios lugares
const nombreCompleto = `${dto.persona.primerNombre} ${dto.persona.primerApellido}`;
```

✅ **CORRECTO:**
```typescript
// Usar utilidad centralizada
import { buildFullName } from '@/mappers';
const nombreCompleto = buildFullName(
  dto.persona.primerNombre,
  dto.persona.segundoNombre,
  dto.persona.primerApellido,
  dto.persona.segundoApellido
);
```

### 6. Documentar Mappers Personalizados

```typescript
/**
 * Convierte UsuarioResponseDTO a formato CSV
 * 
 * @param dto - DTO del backend
 * @returns String en formato CSV
 * 
 * @example
 * ```typescript
 * const csv = mapUserToCSV(dto);
 * // "1,Juan Pérez,juan@email.com,Activo"
 * ```
 */
export const mapUserToCSV: BaseMapper<UsuarioResponseDTO, string> = (dto) => {
  const user = mapUsuarioDTOToUser(dto);
  return `${user.id},${user.nombreCompleto},${user.email},${user.activo ? 'Activo' : 'Inactivo'}`;
};
```

---

## 🆕 Crear Nuevos Mappers

### Paso 1: Crear Archivo del Dominio

```typescript
// src/mappers/cliente.mapper.ts

import type { ClienteResponseDTO, ClienteCreateDTO } from '@/types/api.d';
import { type BaseMapper, formatAuditFields, buildFullName } from './base.mapper';

// Tipos de UI
export interface Cliente {
  id: number;
  nombreCompleto: string;
  email: string;
  telefono: string;
  creditoDisponible: number;
  // ...
}

export interface ClienteFormData {
  primerNombre: string;
  primerApellido: string;
  email: string;
  // ...
}

// Mappers
export const mapClienteDTOToCliente: BaseMapper<ClienteResponseDTO, Cliente> = (dto) => {
  // Implementación
};

export const mapClienteFormToCreateDTO: BaseMapper<ClienteFormData, ClienteCreateDTO> = (formData) => {
  // Implementación
};
```

### Paso 2: Agregar Exportaciones

```typescript
// src/mappers/index.ts

export {
  type Cliente,
  type ClienteFormData,
  mapClienteDTOToCliente,
  mapClienteFormToCreateDTO,
  // ...
} from './cliente.mapper';
```

### Paso 3: Documentar

Agregar sección en este README con ejemplos de uso.

### Plantilla de Mapper

```typescript
/**
 * ============================================
 * [DOMINIO] MAPPER - TRANSFORMACIONES DE [ENTIDAD]
 * ============================================
 */

import type { [Entity]ResponseDTO, [Entity]CreateDTO } from '@/types/api.d';
import { type BaseMapper, formatAuditFields } from './base.mapper';

// ============================================
// TIPOS DE UI
// ============================================

export interface [Entity] {
  // Campos de la entidad
}

export interface [Entity]FormData {
  // Campos del formulario
}

// ============================================
// MAPPERS: DTO → UI
// ============================================

export const map[Entity]DTOTo[Entity]: BaseMapper<[Entity]ResponseDTO, [Entity]> = (dto) => {
  const audit = formatAuditFields(dto);
  
  return {
    id: dto.id,
    // ... mapear campos
    createdAt: audit.createdAt,
    updatedAt: audit.updatedAt,
  };
};

// ============================================
// MAPPERS: FORM → DTO
// ============================================

export const map[Entity]FormToCreateDTO: BaseMapper<[Entity]FormData, [Entity]CreateDTO> = (formData) => {
  return {
    // ... mapear campos
  };
};

// ============================================
// EXPORTACIÓN DEFAULT
// ============================================

export default {
  dtoTo[Entity]: map[Entity]DTOTo[Entity],
  formToCreateDTO: map[Entity]FormToCreateDTO,
};
```

---

## 🧪 Testing

### Test Unitario de Mapper

```typescript
// mappers/__tests__/user.mapper.test.ts

import { describe, it, expect } from 'vitest';
import { mapUsuarioDTOToUser } from '../user.mapper';

describe('User Mapper', () => {
  it('debe transformar DTO a User correctamente', () => {
    const dto: UsuarioResponseDTO = {
      id: 1,
      username: 'jperez',
      activo: true,
      persona: {
        id: 1,
        primerNombre: 'Juan',
        segundoNombre: 'Carlos',
        primerApellido: 'Pérez',
        segundoApellido: 'López',
        email: 'juan@email.com',
        // ...
      },
      roles: [{ id: 1, nombre: 'Admin', codigo: 'ROLE_ADMIN' }],
      createdAt: '2024-01-01T10:00:00Z',
      updatedAt: null,
    };
    
    const user = mapUsuarioDTOToUser(dto);
    
    expect(user.id).toBe(1);
    expect(user.username).toBe('jperez');
    expect(user.nombreCompleto).toBe('Juan Carlos Pérez López');
    expect(user.iniciales).toBe('JP');
    expect(user.isAdmin).toBe(true);
    expect(user.createdAt).toBe('01/01/2024 10:00');
  });
  
  it('debe manejar campos null correctamente', () => {
    const dto: UsuarioResponseDTO = {
      // ... con campos null
      persona: {
        segundoNombre: null,
        segundoApellido: null,
        // ...
      },
    };
    
    const user = mapUsuarioDTOToUser(dto);
    
    expect(user.persona.segundoNombre).toBeNull();
    expect(user.nombreCompleto).toBe('Juan Pérez'); // Sin segundos nombres
  });
});
```

### Test de Integración

```typescript
// services/__tests__/usuarios.service.test.ts

import { describe, it, expect, vi } from 'vitest';
import * as usuarioService from '../usuarios.service';
import { mockUsuarioResponseDTO } from '@/mocks/usuario.mock';

describe('Usuario Service', () => {
  it('debe retornar User (no DTO) desde listUsuarios', async () => {
    // Mock API
    vi.spyOn(apiService, 'get').mockResolvedValue({
      data: { body: { content: [mockUsuarioResponseDTO] } },
    });
    
    const users = await usuarioService.listUsuarios({});
    
    expect(users[0]).toHaveProperty('nombreCompleto'); // Campo computado
    expect(users[0]).toHaveProperty('iniciales'); // Campo computado
    expect(users[0]).not.toHaveProperty('persona'); // No debe ser DTO
  });
});
```

---

## 🔧 Troubleshooting

### Error: "Cannot read property 'primerNombre' of undefined"

**Causa:** El DTO no tiene la estructura esperada.

**Solución:**
```typescript
// Validar estructura antes de mapear
if (!isValidUsuarioDTO(dto)) {
  console.error('DTO inválido:', dto);
  throw new Error('Estructura de DTO incorrecta');
}
```

### Error: "Fechas no se formatean correctamente"

**Causa:** El backend está enviando fechas en formato incorrecto.

**Solución:**
```typescript
// Verificar formato ISO 8601
console.log('Fecha raw:', dto.createdAt); // Debe ser "2024-01-01T10:00:00Z"

// Si el formato es diferente, ajustar formatDateForUI()
```

### Warn: "Campos computados no aparecen"

**Causa:** No estás usando el mapper o estás usando el DTO directamente.

**Solución:**
```typescript
// ❌ Incorrecto
const user = response.data.body; // DTO
console.log(user.nombreCompleto); // undefined

// ✅ Correcto
const user = mapUsuarioDTOToUser(response.data.body);
console.log(user.nombreCompleto); // "Juan Pérez"
```

### Error: "Tipo incompatible al usar mapper"

**Causa:** Desincronización entre tipos del backend y frontend.

**Solución:**
1. Revisar Swagger del backend
2. Actualizar tipos en `types/api.d.ts`
3. Actualizar mapper correspondiente

---

## 📚 Referencias

- **Directrices Backend:** `/docs/DIRECTRICES_TECNICAS_BACKEND.md`
- **Directrices Frontend:** `/docs/DIRECTRICES_TECNICAS_FRONTEND.md`
- **Tipos API:** `/src/types/api.d.ts`
- **Constants:** `/src/constants/`
- **Validations:** `/src/validations/`

---

## 📝 Changelog

### v2.0.0 (Octubre 2025)
- ✅ Refactorización completa de mappers
- ✅ Sincronización 100% con backend Spring Boot
- ✅ Utilidades base reutilizables (base.mapper.ts)
- ✅ Documentación completa con ejemplos
- ✅ Mappers de usuarios y roles actualizados
- ✅ Sistema de registro opcional (MapperRegistry)
- ✅ Soporte para TypeScript strict mode

### v1.0.0 (Anterior)
- ⚠️ Mappers básicos sin utilidades compartidas
- ⚠️ Documentación limitada
- ⚠️ Sin formateo de fechas consistente

---

## 🤝 Contribuir

Para agregar nuevos mappers o mejorar existentes:

1. Seguir convenciones de naming (español para funciones, inglés para tipos)
2. Usar utilidades de `base.mapper.ts`
3. Documentar con JSDoc completo
4. Agregar ejemplos de uso
5. Escribir tests unitarios
6. Actualizar este README

---

**¿Preguntas?** Consulta al equipo técnico de Predio Mijangos.
