# Directrices Técnicas y Políticas de Desarrollo Frontend

**Sistema de Gestión de Inventario y Ventas - Predio Mijangos Web**

**Versión:** 2.0.0  
**Última Actualización:** 21 de Octubre 2025  
**Autor:** Equipo Técnico Predio Mijangos

---

## 📋 Índice

1. [Información del Proyecto](#1-información-del-proyecto)
2. [Stack Tecnológico](#2-stack-tecnológico)
3. [Arquitectura General](#3-arquitectura-general)
4. [Estructura del Proyecto](#4-estructura-del-proyecto)
5. [Alineación con Backend](#5-alineación-con-backend)
6. [Patrones de Código](#6-patrones-de-código)
7. [Naming Conventions](#7-naming-conventions)
8. [Manejo de Estado](#8-manejo-de-estado)
9. [Integración con API](#9-integración-con-api)
10. [Autenticación y Seguridad](#10-autenticación-y-seguridad)
11. [Sistema de Permisos](#11-sistema-de-permisos)
12. [Validaciones](#12-validaciones)
13. [Componentes UI](#13-componentes-ui)
14. [Diseño Responsive](#14-diseño-responsive)
15. [Patrones de Panel Expandible](#15-patrones-de-panel-expandible)
16. [Mapeo DTO ↔ UI Model](#16-mapeo-dto--ui-model)
17. [Sistema de Constantes](#17-sistema-de-constantes)
18. [Interceptores HTTP](#18-interceptores-http)
19. [Testing](#19-testing)
20. [Optimización y Performance](#20-optimización-y-performance)
21. [Checklist por Módulo Nuevo](#21-checklist-por-módulo-nuevo)
22. [Recursos y Referencias](#22-recursos-y-referencias)

---

## 1. Información del Proyecto

### 1.1 Descripción

Aplicación web administrativa para el sistema de gestión integral de inventario y ventas de Predio Mijangos. Proporciona interfaces para administración, gestión de usuarios, roles, clientes, proveedores, productos y reportería.

### 1.2 Objetivos Principales

- Interfaz intuitiva y responsive para administración del sistema
- Integración completa con API REST del backend
- Manejo robusto de autenticación y permisos
- Experiencia de usuario fluida con feedback visual
- Performance optimizado con lazy loading y code splitting

### 1.3 Usuarios del Sistema Web

- **ADMIN**: Acceso total al sistema
- **SUPERVISOR**: Aprobaciones y supervisión
- **CONTADOR**: Reportes y análisis financiero
- **OPERADOR**: Operaciones generales (oficina)

**Nota**: VENDEDOR y BODEGUERO usan la aplicación móvil.

---

## 2. Stack Tecnológico

### 2.1 Core

```yaml
Lenguaje: TypeScript 5.8.x
Framework: React 19.1.x
Build Tool: Vite 7.1.x
Routing: React Router DOM 6.30.x
State Management: React Context API + Custom Hooks (sin Redux/Zustand)
```

### 2.2 Formularios y Validación

```yaml
Forms: React Hook Form 7.62.x
Validation: Zod 4.1.x + @hookform/resolvers 5.2.x
```

### 2.3 HTTP y API

```yaml
HTTP Client: Axios 1.11.x
```

### 2.4 UI y Componentes

```yaml
Icons: @mdi/react 1.6.x + @mdi/js 7.4.x
Animations: react-transition-group 4.4.5
Data Grid: @tanstack/react-table 8.x (para tablas avanzadas)
Loading Skeletons: react-loading-skeleton
Toast Notifications: react-hot-toast
Date Picker: react-datepicker
Select: react-select
Charts: recharts
```

### 2.5 Desarrollo

```yaml
Linting: ESLint 9.x + typescript-eslint 8.x
Type Checking: TypeScript strict mode
```

### 2.6 Testing (Próxima Fase)

```yaml
Unit Testing: Vitest + React Testing Library
E2E Testing: Playwright
```

---

## 3. Arquitectura General

### 3.1 Patrón Arquitectónico

**Arquitectura por Capas** alineada con el backend:

```
Backend:    Controller → Service   → Repository → Entity
Frontend:   Page       → Hook      → Service    → Types/DTOs
```

**Justificación**:
- Mantiene coherencia con el backend
- Separación clara de responsabilidades
- Facilita testing y mantenimiento
- Código predecible y escalable

### 3.2 Estructura de Capas por Módulo

```
┌─────────────────────────────────┐
│        PAGE LAYER               │ ← Componente de página principal
│   (Lógica de presentación)     │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│       HOOK LAYER                │ ← Custom hooks con lógica de UI
│   (Estado, efectos, handlers)  │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│      SERVICE LAYER              │ ← Llamadas a API
│   (Axios, HTTP, interceptores)  │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│       TYPES/DTOs                │ ← Interfaces TypeScript
│   (Contratos de datos)          │
└─────────────────────────────────┘
```

### 3.3 Principios SOLID Aplicados

- **Single Responsibility**: Un componente = una responsabilidad
- **Open/Closed**: Componentes extensibles mediante props
- **Liskov Substitution**: Interfaces consistentes
- **Interface Segregation**: Props específicos por componente
- **Dependency Inversion**: Inyección de dependencias vía props/context

---

## 4. Estructura del Proyecto

### 4.1 Estructura de Directorios

```
predio-web/
├── public/                         # Archivos estáticos
│   ├── favicon.ico
│   └── assets/
│
├── src/
│   ├── main.tsx                   # Entry point
│   ├── App.tsx                    # Componente raíz
│   ├── router.tsx                 # Configuración de rutas
│   │
│   ├── components/                # Componentes reutilizables
│   │   ├── common/               # Componentes base
│   │   │   ├── Button.tsx
│   │   │   ├── Input.tsx
│   │   │   ├── DataGrid.tsx
│   │   │   ├── Modal.tsx
│   │   │   ├── Drawer.tsx
│   │   │   ├── Tabs.tsx
│   │   │   ├── Badge.tsx
│   │   │   ├── Skeleton.tsx
│   │   │   └── RHForm.tsx       # React Hook Form wrapper
│   │   │
│   │   └── layout/               # Componentes de layout
│   │       ├── MainLayout.tsx
│   │       ├── Sidebar.tsx
│   │       ├── Topbar.tsx
│   │       └── ExpandablePanel.tsx
│   │
│   ├── pages/                    # Páginas por módulo
│   │   ├── auth/
│   │   │   ├── LoginPage.tsx
│   │   │   └── components/
│   │   │
│   │   ├── dashboard/
│   │   │   └── DashboardPage.tsx
│   │   │
│   │   ├── security/             # Módulo de seguridad
│   │   │   ├── users/
│   │   │   │   ├── UsersListPage.tsx
│   │   │   │   ├── components/
│   │   │   │   │   ├── UserFilterBar.tsx
│   │   │   │   │   ├── UserInfoFormRHF.tsx
│   │   │   │   │   └── UserCredentialsFormRHF.tsx
│   │   │   │   └── hooks/
│   │   │   │       └── useUsersPage.ts
│   │   │   │
│   │   │   └── roles/
│   │   │       ├── RolesListPage.tsx
│   │   │       ├── components/
│   │   │       └── hooks/
│   │   │
│   │   ├── products/             # (Próximo módulo)
│   │   ├── inventory/            # (Próximo módulo)
│   │   └── sales/                # (Próximo módulo)
│   │
│   ├── hooks/                    # Custom hooks por módulo
│   │   ├── auth/
│   │   │   └── useAuth.ts
│   │   ├── security/
│   │   │   ├── useUsersPage.ts
│   │   │   └── useRolesPage.ts
│   │   └── common/
│   │       ├── useDebounce.ts
│   │       └── usePagination.ts
│   │
│   ├── services/                 # Servicios de API
│   │   ├── config.ts            # Configuración de Axios
│   │   ├── api.service.ts       # Cliente HTTP base
│   │   ├── interceptors/        # Interceptores HTTP
│   │   │   ├── auth.interceptor.ts
│   │   │   ├── error.interceptor.ts
│   │   │   └── response.interceptor.ts
│   │   │
│   │   └── endpoints/           # Servicios por módulo
│   │       ├── auth.service.ts
│   │       ├── security.service.ts
│   │       ├── products.service.ts
│   │       └── sales.service.ts
│   │
│   ├── types/                   # Tipos TypeScript
│   │   ├── api.types.ts         # ApiResponse, ErrorResponse, PageResponse
│   │   ├── auth.types.ts
│   │   ├── user.types.ts
│   │   ├── role.types.ts
│   │   └── common.types.ts
│   │
│   ├── mappers/                 # Mapeo DTO ↔ UI Model
│   │   ├── user.mapper.ts
│   │   ├── role.mapper.ts
│   │   └── common.mapper.ts
│   │
│   ├── constants/               # Constantes de la aplicación
│   │   ├── api.constants.ts     # Endpoints, timeouts
│   │   ├── auth.constants.ts    # Roles, permisos
│   │   ├── error.constants.ts   # Códigos de error (alineados con backend)
│   │   ├── validation.constants.ts  # Patrones, límites
│   │   └── routes.constants.ts  # Rutas de navegación
│   │
│   ├── utils/                   # Utilidades
│   │   ├── permissions.ts       # Sistema de permisos
│   │   ├── format.ts            # Formateo de datos
│   │   ├── validation.ts        # Validaciones custom
│   │   ├── date.ts              # Manejo de fechas
│   │   └── storage.ts           # localStorage wrapper
│   │
│   ├── context/                 # React Context
│   │   ├── AuthContext.tsx
│   │   └── ThemeContext.tsx
│   │
│   └── styles/                  # Estilos globales
│       ├── index.css           # Imports principales
│       ├── base.css            # Resets, base
│       ├── theme.css           # Variables CSS
│       ├── layout.css          # Layouts
│       └── utils.css           # Utilidades
│
├── .env.example                 # Variables de entorno
├── .eslintrc.cjs               # Configuración ESLint
├── tsconfig.json               # Configuración TypeScript
├── vite.config.ts              # Configuración Vite
└── package.json
```

---

## 5. Alineación con Backend

### 5.1 Estructura de Respuestas API

El backend usa estructuras estandarizadas que el frontend debe manejar:

#### 5.1.1 ApiResponse<T>

**Backend:**
```java
public class ApiResponse<T> {
    private Integer statusCode;
    private String message;
    private T body;
    private Map<String, Object> metadata;
}
```

**Frontend:**
```typescript
export interface ApiResponse<T> {
  statusCode: number;
  message: string;
  body: T;
  metadata?: Record<string, any>;
}
```

#### 5.1.2 PageResponse<T>

**Backend:**
```java
public class PageResponse<T> {
    private List<T> content;
    private Integer page;
    private Integer size;
    private Integer totalElements;
    private Integer totalPages;
    private Boolean last;
}
```

**Frontend:**
```typescript
export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}
```

#### 5.1.3 ErrorResponse

**Backend:**
```java
public class ErrorResponse {
    private String errorCode;
    private String message;
    private Integer status;
    private String timestamp;
    private List<ValidationError> validationErrors;
}
```

**Frontend:**
```typescript
export interface ErrorResponse {
  errorCode: string;
  message: string;
  status: number;
  timestamp: string;
  validationErrors?: ValidationError[];
}

export interface ValidationError {
  field: string;
  message: string;
  rejectedValue?: any;
}
```

### 5.2 Códigos de Error Sincronizados

El backend usa `ErrorCodes` centralizados. El frontend debe mantener los mismos códigos:

**Backend (`ErrorCodes.java`):**
```java
public static final String AUTH_INVALID_CREDENTIALS = "AUTH_001";
public static final String USER_NOT_FOUND = "USER_001";
public static final String USER_ALREADY_EXISTS = "USER_002";
```

**Frontend (`error.constants.ts`):**
```typescript
export const ERROR_CODES = {
  // Authentication
  AUTH_INVALID_CREDENTIALS: 'AUTH_001',
  AUTH_TOKEN_EXPIRED: 'AUTH_002',
  AUTH_TOKEN_INVALID: 'AUTH_003',
  
  // Users
  USER_NOT_FOUND: 'USER_001',
  USER_ALREADY_EXISTS: 'USER_002',
  USER_INACTIVE: 'USER_003',
  
  // Validation
  VAL_INVALID_FORMAT: 'VAL_001',
  VAL_REQUIRED_FIELD: 'VAL_002',
  
  // Business
  BUS_OPERATION_FAILED: 'BUS_001',
  
  // System
  SYS_INTERNAL_ERROR: 'SYS_001',
  SYS_SERVICE_UNAVAILABLE: 'SYS_002',
} as const;
```

### 5.3 Endpoints y Naming

**Backend:** `/api/v1/usuarios`  
**Frontend Service:** `usuarios.service.ts`

```typescript
// ✅ Correcto - alineado con backend
export const listUsuarios = (params: UsuarioFilterDTO): Promise<ApiResponse<PageResponse<UsuarioResponseDTO>>> => {
  return apiService.get('/api/v1/usuarios', { params });
};

// ❌ Incorrecto - nombres inconsistentes
export const getUsers = () => { ... }
```

### 5.4 DTOs y Naming Conventions

El backend usa sufijos para DTOs. El frontend debe mantenerlos:

**Backend:**
- `UsuarioCreateDTO`
- `UsuarioUpdateDTO`
- `UsuarioResponseDTO`
- `UsuarioFilterDTO`

**Frontend:**
```typescript
export interface UsuarioCreateDTO {
  username: string;
  password: string;
  email: string;
  // ...
}

export interface UsuarioResponseDTO {
  id: number;
  username: string;
  email: string;
  // ...
}
```

### 5.5 Validaciones Alineadas

**Backend (Jakarta Bean Validation):**
```java
@NotBlank(message = "El username es obligatorio")
@Size(min = 3, max = 50)
@Pattern(regexp = "^[a-zA-Z0-9_]+$")
private String username;
```

**Frontend (Zod):**
```typescript
const usuarioCreateSchema = z.object({
  username: z.string()
    .min(3, "El username debe tener al menos 3 caracteres")
    .max(50, "El username no puede exceder 50 caracteres")
    .regex(/^[a-zA-Z0-9_]+$/, "El username solo puede contener letras, números y guiones bajos"),
});
```

---

## 6. Patrones de Código

### 6.1 Patrón: Página con Hook Custom

**RolesListPage.tsx:**
```typescript
import { RoleFilterBar } from './components/RoleFilterBar';
import { DataGrid } from '@/components/common/DataGrid';
import { Drawer } from '@/components/common/Drawer';
import { useRolesPage } from './hooks/useRolesPage';

export const RolesListPage: React.FC = () => {
  const {
    // Estado
    roles,
    loading,
    error,
    selectedRole,
    drawerOpen,
    
    // Acciones
    handleFilter,
    handleCreate,
    handleEdit,
    handleDelete,
    handleSave,
    handleOpenDrawer,
    handleCloseDrawer,
  } = useRolesPage();

  if (loading) return <SkeletonTable />;
  if (error) return <ErrorMessage error={error} />;

  return (
    <div className="roles-page">
      <div className="roles-page__header">
        <h1>Roles</h1>
        <Button onClick={handleCreate}>+ Agregar Rol</Button>
      </div>

      <RoleFilterBar onFilter={handleFilter} />

      <DataGrid
        data={roles}
        columns={columns}
        onRowClick={handleEdit}
      />

      <Drawer
        isOpen={drawerOpen}
        onClose={handleCloseDrawer}
        title={selectedRole ? 'Editar Rol' : 'Nuevo Rol'}
      >
        <RoleForm
          role={selectedRole}
          onSubmit={handleSave}
        />
      </Drawer>
    </div>
  );
};
```

**useRolesPage.ts:**
```typescript
import { useState, useEffect } from 'react';
import { listRols, getRol, createRol, updateRol, deleteRol } from '@/services/endpoints/security.service';
import { useToast } from '@/hooks/common/useToast';
import type { Role, RoleFormData, RoleFilterDTO } from '@/types/role.types';

export const useRolesPage = () => {
  const [roles, setRoles] = useState<Role[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [selectedRole, setSelectedRole] = useState<Role | null>(null);
  const [drawerOpen, setDrawerOpen] = useState(false);
  
  const { showSuccess, showError } = useToast();

  // Cargar roles
  const fetchRoles = async (filters?: RoleFilterDTO) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await listRols(filters);
      setRoles(response.body.content);
    } catch (err) {
      setError('Error al cargar roles');
      showError('Error al cargar roles');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRoles();
  }, []);

  // Handlers
  const handleCreate = () => {
    setSelectedRole(null);
    setDrawerOpen(true);
  };

  const handleEdit = async (role: Role) => {
    try {
      const response = await getRol(role.id);
      setSelectedRole(response.body);
      setDrawerOpen(true);
    } catch (err) {
      showError('Error al cargar el rol');
    }
  };

  const handleSave = async (data: RoleFormData) => {
    try {
      if (selectedRole) {
        await updateRol(selectedRole.id, data);
        showSuccess('Rol actualizado exitosamente');
      } else {
        await createRol(data);
        showSuccess('Rol creado exitosamente');
      }
      
      setDrawerOpen(false);
      fetchRoles();
    } catch (err) {
      showError('Error al guardar el rol');
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('¿Está seguro de eliminar este rol?')) return;
    
    try {
      await deleteRol(id);
      showSuccess('Rol eliminado exitosamente');
      fetchRoles();
    } catch (err) {
      showError('Error al eliminar el rol');
    }
  };

  const handleFilter = (filters: RoleFilterDTO) => {
    fetchRoles(filters);
  };

  const handleOpenDrawer = (role?: Role) => {
    setSelectedRole(role || null);
    setDrawerOpen(true);
  };

  const handleCloseDrawer = () => {
    setDrawerOpen(false);
    setSelectedRole(null);
  };

  return {
    roles,
    loading,
    error,
    selectedRole,
    drawerOpen,
    handleFilter,
    handleCreate,
    handleEdit,
    handleDelete,
    handleSave,
    handleOpenDrawer,
    handleCloseDrawer,
  };
};
```

### 6.2 Patrón: Servicio de API

**security.service.ts:**
```typescript
import { apiService } from '../api.service';
import type { ApiResponse, PageResponse } from '@/types/api.types';
import type { 
  RoleResponseDTO, 
  RoleCreateDTO, 
  RoleUpdateDTO,
  RoleFilterDTO 
} from '@/types/role.types';

const BASE_PATH = '/api/v1/rols';

export const listRols = async (
  filters?: RoleFilterDTO
): Promise<ApiResponse<PageResponse<RoleResponseDTO>>> => {
  return apiService.get(BASE_PATH, { params: filters });
};

export const getRol = async (
  id: number
): Promise<ApiResponse<RoleResponseDTO>> => {
  return apiService.get(`${BASE_PATH}/${id}`);
};

export const createRol = async (
  data: RoleCreateDTO
): Promise<ApiResponse<RoleResponseDTO>> => {
  return apiService.post(BASE_PATH, data);
};

export const updateRol = async (
  id: number,
  data: RoleUpdateDTO
): Promise<ApiResponse<RoleResponseDTO>> => {
  return apiService.put(`${BASE_PATH}/${id}`, data);
};

export const deleteRol = async (
  id: number
): Promise<ApiResponse<void>> => {
  return apiService.delete(`${BASE_PATH}/${id}`);
};
```

### 6.3 Patrón: Formulario con React Hook Form

**RoleInfoFormRHF.tsx:**
```typescript
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Input } from '@/components/common/Input';
import { Button } from '@/components/common/Button';
import type { RoleFormData } from '@/types/role.types';

const roleSchema = z.object({
  nombre: z.string()
    .min(3, 'El nombre debe tener al menos 3 caracteres')
    .max(50, 'El nombre no puede exceder 50 caracteres'),
  descripcion: z.string()
    .max(200, 'La descripción no puede exceder 200 caracteres')
    .optional(),
  activo: z.boolean().default(true),
});

interface RoleInfoFormRHFProps {
  defaultValues?: Partial<RoleFormData>;
  onSubmit: (data: RoleFormData) => void;
  onCancel: () => void;
}

export const RoleInfoFormRHF: React.FC<RoleInfoFormRHFProps> = ({
  defaultValues,
  onSubmit,
  onCancel,
}) => {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RoleFormData>({
    resolver: zodResolver(roleSchema),
    defaultValues,
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="role-form">
      <Input
        label="Nombre"
        {...register('nombre')}
        error={errors.nombre?.message}
        required
      />

      <Input
        label="Descripción"
        {...register('descripcion')}
        error={errors.descripcion?.message}
        as="textarea"
      />

      <div className="form-actions">
        <Button
          type="button"
          variant="secondary"
          onClick={onCancel}
        >
          Cancelar
        </Button>
        <Button
          type="submit"
          disabled={isSubmitting}
        >
          {isSubmitting ? 'Guardando...' : 'Guardar'}
        </Button>
      </div>
    </form>
  );
};
```

---

## 7. Naming Conventions

### 7.1 Archivos y Carpetas

**Componentes:**
```
PascalCase.tsx
✅ Button.tsx
✅ UserFilterBar.tsx
✅ RoleInfoFormRHF.tsx
```

**Hooks:**
```
camelCase con prefijo "use"
✅ useAuth.ts
✅ useUsersPage.ts
✅ useDebounce.ts
```

**Servicios:**
```
camelCase.service.ts
✅ auth.service.ts
✅ security.service.ts
✅ products.service.ts
```

**Tipos:**
```
camelCase.types.ts
✅ user.types.ts
✅ role.types.ts
✅ api.types.ts
```

**Páginas:**
```
PascalCase + "Page" sufijo
✅ UsersListPage.tsx
✅ DashboardPage.tsx
✅ LoginPage.tsx
```

### 7.2 Variables y Funciones

**Convenciones:**
```typescript
// Variables y funciones: camelCase
const userName = 'admin';
const handleSubmit = () => {};

// Constantes globales: UPPER_SNAKE_CASE
const API_BASE_URL = 'http://localhost:8080';
const MAX_RETRY_ATTEMPTS = 3;

// Interfaces y Types: PascalCase
interface User {}
type RoleId = number;

// Enums: PascalCase
enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
}
```

### 7.3 Handlers de Eventos

**Convenciones:**
```typescript
// Handlers: prefijo "handle" + acción
const handleClick = () => {};
const handleSubmit = () => {};
const handleChange = () => {};
const handleDelete = (id: number) => {};

// Callbacks en props: prefijo "on" + acción
interface ButtonProps {
  onClick: () => void;
  onSubmit: (data: FormData) => void;
  onChange: (value: string) => void;
}
```

### 7.4 Props de Componentes

```typescript
// Props interface: ComponentName + "Props"
interface ButtonProps {
  variant?: 'primary' | 'secondary';
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  onClick?: () => void;
  children: React.ReactNode;
}

// Componente
export const Button: React.FC<ButtonProps> = ({
  variant = 'primary',
  size = 'md',
  disabled = false,
  onClick,
  children,
}) => {
  // ...
};
```

---

## 8. Manejo de Estado

### 8.1 Estrategia de Estado

**NO usamos librerías de estado global** (Redux, Zustand, Jotai)

**Razones:**
- Aplicación de tamaño medio
- Complejidad innecesaria
- React Context + Custom Hooks es suficiente
- Menos boilerplate

**Estrategia:**
```
Estado Local          → useState en componentes
Estado Compartido     → React Context (Auth, Theme)
Estado de Servidor    → Custom Hooks + Service Layer
Formularios           → React Hook Form
```

### 8.2 Estado Local (useState)

Usar para estado específico del componente:

```typescript
const MyComponent = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState<Item | null>(null);
  
  return (
    // ...
  );
};
```

### 8.3 Estado Global (Context)

Usar para estado que necesitan múltiples componentes:

**AuthContext.tsx:**
```typescript
import { createContext, useContext, useState, useEffect } from 'react';
import { storageService } from '@/utils/storage';
import type { User } from '@/types/user.types';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  login: (token: string, user: User) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const token = storageService.getToken();
    const storedUser = storageService.getUser();
    
    if (token && storedUser) {
      setUser(storedUser);
    }
  }, []);

  const login = (token: string, userData: User) => {
    storageService.setToken(token);
    storageService.setUser(userData);
    setUser(userData);
  };

  const logout = () => {
    storageService.clearAuth();
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};
```

### 8.4 Estado de Servidor (Custom Hooks)

Encapsular lógica de fetching en hooks:

```typescript
export const useUsers = (filters?: UserFilterDTO) => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchUsers = async () => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await listUsuarios(filters);
      setUsers(response.body.content);
    } catch (err) {
      setError('Error al cargar usuarios');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, [JSON.stringify(filters)]);

  return { users, loading, error, refetch: fetchUsers };
};
```

---

## 9. Integración con API

### 9.1 Cliente HTTP Base (api.service.ts)

```typescript
import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';
import { API_CONFIG } from '@/constants/api.constants';
import { authInterceptor } from './interceptors/auth.interceptor';
import { errorInterceptor } from './interceptors/error.interceptor';
import { responseInterceptor } from './interceptors/response.interceptor';

class ApiService {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_CONFIG.BASE_URL,
      timeout: API_CONFIG.TIMEOUT,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Request interceptors
    this.client.interceptors.request.use(
      authInterceptor,
      (error) => Promise.reject(error)
    );

    // Response interceptors
    this.client.interceptors.response.use(
      responseInterceptor,
      errorInterceptor
    );
  }

  async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.get<T>(url, config);
    return response.data;
  }

  async post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.post<T>(url, data, config);
    return response.data;
  }

  async put<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.put<T>(url, data, config);
    return response.data;
  }

  async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.delete<T>(url, config);
    return response.data;
  }
}

export const apiService = new ApiService();
```

---

## 10. Autenticación y Seguridad

### 10.1 Flujo de Autenticación JWT

```
1. Usuario ingresa credenciales
2. POST /api/v1/auth/login
3. Backend retorna { accessToken, refreshToken, usuario }
4. Frontend guarda en localStorage
5. Todas las requests incluyen: Authorization: Bearer <accessToken>
6. Si 401 Unauthorized:
   - Intentar refresh token
   - Si falla → logout automático
```

### 10.2 AuthContext

Ver sección 8.3 para implementación completa.

### 10.3 ProtectedRoute

```typescript
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';

interface ProtectedRouteProps {
  requiredPermissions?: string[];
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  requiredPermissions = [],
}) => {
  const { isAuthenticated, user } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredPermissions.length > 0 && user) {
    const hasPermission = requiredPermissions.some(permission =>
      user.permissions?.includes(permission)
    );

    if (!hasPermission) {
      return <Navigate to="/unauthorized" replace />;
    }
  }

  return <Outlet />;
};
```

### 10.4 Uso en Router

```typescript
import { createBrowserRouter } from 'react-router-dom';
import { ProtectedRoute } from '@/components/auth/ProtectedRoute';
import { PERMISSIONS } from '@/constants/auth.constants';

export const router = createBrowserRouter([
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/',
    element: <ProtectedRoute />,
    children: [
      {
        path: 'dashboard',
        element: <DashboardPage />,
      },
      {
        path: 'usuarios',
        element: (
          <ProtectedRoute requiredPermissions={[PERMISSIONS.USUARIOS_READ]} />
        ),
        children: [
          {
            index: true,
            element: <UsersListPage />,
          },
        ],
      },
    ],
  },
]);
```

---

## 11. Sistema de Permisos

### 11.1 Constantes de Permisos

**auth.constants.ts:**
```typescript
export const ROLES = {
  ADMIN: 'ROLE_ADMIN',
  SUPERVISOR: 'ROLE_SUPERVISOR',
  CONTADOR: 'ROLE_CONTADOR',
  OPERADOR: 'ROLE_OPERADOR',
} as const;

export const PERMISSIONS = {
  // Usuarios
  USUARIOS_CREATE: 'USUARIOS:CREATE',
  USUARIOS_READ: 'USUARIOS:READ',
  USUARIOS_UPDATE: 'USUARIOS:UPDATE',
  USUARIOS_DELETE: 'USUARIOS:DELETE',
  
  // Roles
  ROLES_CREATE: 'ROLES:CREATE',
  ROLES_READ: 'ROLES:READ',
  ROLES_UPDATE: 'ROLES:UPDATE',
  ROLES_DELETE: 'ROLES:DELETE',
  
  // Productos
  PRODUCTOS_CREATE: 'PRODUCTOS:CREATE',
  PRODUCTOS_READ: 'PRODUCTOS:READ',
  PRODUCTOS_UPDATE: 'PRODUCTOS:UPDATE',
  PRODUCTOS_DELETE: 'PRODUCTOS:DELETE',
  
  // Ventas
  VENTAS_CREATE: 'VENTAS:CREATE',
  VENTAS_READ: 'VENTAS:READ',
  VENTAS_UPDATE: 'VENTAS:UPDATE',
  VENTAS_DELETE: 'VENTAS:DELETE',
  VENTAS_APPROVE: 'VENTAS:APPROVE',
} as const;
```

### 11.2 Utils de Permisos

**permissions.ts:**
```typescript
import type { User } from '@/types/user.types';

/**
 * Verifica si el usuario tiene un permiso específico
 */
export const hasPermission = (
  user: User | null,
  permission: string
): boolean => {
  if (!user || !user.permissions) return false;
  return user.permissions.includes(permission);
};

/**
 * Verifica si el usuario tiene TODOS los permisos
 */
export const hasAllPermissions = (
  user: User | null,
  permissions: string[]
): boolean => {
  if (!user || !user.permissions) return false;
  return permissions.every(p => user.permissions!.includes(p));
};

/**
 * Verifica si el usuario tiene AL MENOS UNO de los permisos
 */
export const hasAnyPermission = (
  user: User | null,
  permissions: string[]
): boolean => {
  if (!user || !user.permissions) return false;
  return permissions.some(p => user.permissions!.includes(p));
};

/**
 * Verifica si el usuario tiene un rol específico
 */
export const hasRole = (
  user: User | null,
  role: string
): boolean => {
  if (!user || !user.roles) return false;
  return user.roles.some(r => r.nombre === role);
};

/**
 * Verifica si el usuario es administrador
 */
export const isAdmin = (user: User | null): boolean => {
  return hasRole(user, 'ROLE_ADMIN');
};
```

### 11.3 Hook usePermissions

```typescript
import { useAuth } from '@/context/AuthContext';
import { hasPermission, hasAllPermissions, hasAnyPermission, hasRole, isAdmin } from '@/utils/permissions';

export const usePermissions = () => {
  const { user } = useAuth();

  return {
    hasPermission: (permission: string) => hasPermission(user, permission),
    hasAllPermissions: (permissions: string[]) => hasAllPermissions(user, permissions),
    hasAnyPermission: (permissions: string[]) => hasAnyPermission(user, permissions),
    hasRole: (role: string) => hasRole(user, role),
    isAdmin: () => isAdmin(user),
    user,
  };
};
```

### 11.4 Componente Can

```typescript
import { usePermissions } from '@/hooks/common/usePermissions';

interface CanProps {
  permission?: string;
  permissions?: string[];
  requireAll?: boolean;
  fallback?: React.ReactNode;
  children: React.ReactNode;
}

export const Can: React.FC<CanProps> = ({
  permission,
  permissions = [],
  requireAll = false,
  fallback = null,
  children,
}) => {
  const { hasPermission, hasAllPermissions, hasAnyPermission } = usePermissions();

  let hasAccess = false;

  if (permission) {
    hasAccess = hasPermission(permission);
  } else if (permissions.length > 0) {
    hasAccess = requireAll
      ? hasAllPermissions(permissions)
      : hasAnyPermission(permissions);
  }

  if (!hasAccess) {
    return <>{fallback}</>;
  }

  return <>{children}</>;
};
```

### 11.5 Uso en Componentes

```typescript
import { Can } from '@/components/auth/Can';
import { PERMISSIONS } from '@/constants/auth.constants';

export const UsersPage = () => {
  return (
    <div>
      <h1>Usuarios</h1>

      {/* Solo mostrar si tiene permiso de crear */}
      <Can permission={PERMISSIONS.USUARIOS_CREATE}>
        <Button onClick={handleCreate}>+ Crear Usuario</Button>
      </Can>

      {/* Solo mostrar si tiene permiso de eliminar */}
      <Can permission={PERMISSIONS.USUARIOS_DELETE}>
        <Button onClick={handleDelete}>Eliminar</Button>
      </Can>

      {/* Mostrar si tiene CUALQUIERA de los permisos */}
      <Can permissions={[PERMISSIONS.USUARIOS_UPDATE, PERMISSIONS.USUARIOS_DELETE]}>
        <div className="actions">
          {/* ... */}
        </div>
      </Can>

      {/* Mostrar si tiene TODOS los permisos */}
      <Can
        permissions={[PERMISSIONS.USUARIOS_CREATE, PERMISSIONS.USUARIOS_DELETE]}
        requireAll
      >
        <div className="admin-actions">
          {/* ... */}
        </div>
      </Can>
    </div>
  );
};
```

---

## 12. Validaciones

### 12.1 Esquemas Zod

Mantener esquemas alineados con validaciones del backend.

**user.validation.ts:**
```typescript
import { z } from 'zod';
import { VALIDATION } from '@/constants/validation.constants';

export const usuarioCreateSchema = z.object({
  username: z.string()
    .min(VALIDATION.USERNAME_MIN_LENGTH, 'El username debe tener al menos 3 caracteres')
    .max(VALIDATION.USERNAME_MAX_LENGTH, 'El username no puede exceder 50 caracteres')
    .regex(VALIDATION.USERNAME_PATTERN, 'El username solo puede contener letras, números y guiones bajos'),
  
  password: z.string()
    .min(VALIDATION.PASSWORD_MIN_LENGTH, 'La contraseña debe tener al menos 8 caracteres')
    .max(VALIDATION.PASSWORD_MAX_LENGTH, 'La contraseña no puede exceder 100 caracteres')
    .regex(VALIDATION.PASSWORD_PATTERN, 'La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial'),
  
  email: z.string()
    .email('Email inválido')
    .max(VALIDATION.EMAIL_MAX_LENGTH, 'El email no puede exceder 100 caracteres'),
  
  primerNombre: z.string()
    .min(2, 'El primer nombre debe tener al menos 2 caracteres')
    .max(50, 'El primer nombre no puede exceder 50 caracteres'),
  
  primerApellido: z.string()
    .min(2, 'El primer apellido debe tener al menos 2 caracteres')
    .max(50, 'El primer apellido no puede exceder 50 caracteres'),
  
  dpi: z.string()
    .regex(VALIDATION.DPI_PATTERN, 'DPI inválido (formato: 1234567890101)')
    .optional(),
  
  telefono: z.string()
    .regex(VALIDATION.PHONE_PATTERN, 'Teléfono inválido (formato: +502 1234-5678)')
    .optional(),
});

export const usuarioUpdateSchema = usuarioCreateSchema.partial();
```

**validation.constants.ts:**
```typescript
export const VALIDATION = {
  // Username
  USERNAME_MIN_LENGTH: 3,
  USERNAME_MAX_LENGTH: 50,
  USERNAME_PATTERN: /^[a-zA-Z0-9_]+$/,
  
  // Password
  PASSWORD_MIN_LENGTH: 8,
  PASSWORD_MAX_LENGTH: 100,
  PASSWORD_PATTERN: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/,
  
  // Email
  EMAIL_MAX_LENGTH: 100,
  
  // DPI (Guatemala)
  DPI_PATTERN: /^\d{13}$/,
  
  // NIT (Guatemala)
  NIT_PATTERN: /^\d{7,8}-\d$/,
  
  // Phone (Guatemala)
  PHONE_PATTERN: /^\+502\s?\d{4}-?\d{4}$/,
} as const;
```

### 12.2 Uso con React Hook Form

```typescript
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { usuarioCreateSchema } from '@/validations/user.validation';

export const UserForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(usuarioCreateSchema),
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Input
        label="Username"
        {...register('username')}
        error={errors.username?.message}
      />
      {/* ... */}
    </form>
  );
};
```

---

## 13. Componentes UI

### 13.1 Librería de Componentes

**Estrategia híbrida:**
- Componentes base custom (Button, Input, Modal)
- Librerías especializadas para componentes complejos

**Componentes Base (Custom):**
- Button
- Input
- Textarea
- Select (básico)
- Checkbox
- Radio
- Badge
- Tag
- Modal
- Drawer
- Tabs
- Card

**Librerías Especializadas:**
- **DataGrid avanzado**: @tanstack/react-table
- **Loading states**: react-loading-skeleton
- **Toasts**: react-hot-toast
- **Date picker**: react-datepicker
- **Select avanzado**: react-select
- **Charts**: recharts

### 13.2 Componente Button

**Button.tsx:**
```typescript
import './Button.css';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger' | 'ghost';
  size?: 'sm' | 'md' | 'lg';
  fullWidth?: boolean;
  loading?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  children: React.ReactNode;
}

export const Button: React.FC<ButtonProps> = ({
  variant = 'primary',
  size = 'md',
  fullWidth = false,
  loading = false,
  leftIcon,
  rightIcon,
  children,
  disabled,
  className = '',
  ...props
}) => {
  const classes = [
    'btn',
    `btn--${variant}`,
    `btn--${size}`,
    fullWidth && 'btn--full-width',
    loading && 'btn--loading',
    className,
  ].filter(Boolean).join(' ');

  return (
    <button
      className={classes}
      disabled={disabled || loading}
      {...props}
    >
      {loading && <span className="btn__spinner" />}
      {!loading && leftIcon && <span className="btn__icon">{leftIcon}</span>}
      <span className="btn__label">{children}</span>
      {!loading && rightIcon && <span className="btn__icon">{rightIcon}</span>}
    </button>
  );
};
```

### 13.3 Componente Input

**Input.tsx:**
```typescript
import './Input.css';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement | HTMLTextAreaElement> {
  label?: string;
  error?: string;
  hint?: string;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  as?: 'input' | 'textarea';
}

export const Input: React.FC<InputProps> = ({
  label,
  error,
  hint,
  leftIcon,
  rightIcon,
  as = 'input',
  className = '',
  ...props
}) => {
  const Component = as;
  
  const classes = [
    'input',
    leftIcon && 'input--with-left-icon',
    rightIcon && 'input--with-right-icon',
    error && 'input--error',
    className,
  ].filter(Boolean).join(' ');

  return (
    <div className="input-wrapper">
      {label && (
        <label className="input-label">
          {label}
          {props.required && <span className="input-required">*</span>}
        </label>
      )}

      <div className="input-container">
        {leftIcon && <span className="input-icon input-icon--left">{leftIcon}</span>}
        
        <Component
          className={classes}
          {...props as any}
        />
        
        {rightIcon && <span className="input-icon input-icon--right">{rightIcon}</span>}
      </div>

      {error && <span className="input-error-message">{error}</span>}
      {!error && hint && <span className="input-hint">{hint}</span>}
    </div>
  );
};
```

### 13.4 Componente DataGrid (TanStack Table)

**DataGrid.tsx:**
```typescript
import { useReactTable, getCoreRowModel, flexRender } from '@tanstack/react-table';
import type { ColumnDef } from '@tanstack/react-table';
import './DataGrid.css';

interface DataGridProps<T> {
  data: T[];
  columns: ColumnDef<T>[];
  onRowClick?: (row: T) => void;
  loading?: boolean;
  emptyMessage?: string;
}

export function DataGrid<T>({
  data,
  columns,
  onRowClick,
  loading = false,
  emptyMessage = 'No hay datos disponibles',
}: DataGridProps<T>) {
  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

  if (loading) {
    return <SkeletonTable rows={5} columns={columns.length} />;
  }

  if (data.length === 0) {
    return (
      <div className="data-grid-empty">
        <p>{emptyMessage}</p>
      </div>
    );
  }

  return (
    <div className="data-grid">
      <table className="data-grid__table">
        <thead>
          {table.getHeaderGroups().map(headerGroup => (
            <tr key={headerGroup.id}>
              {headerGroup.headers.map(header => (
                <th key={header.id}>
                  {flexRender(
                    header.column.columnDef.header,
                    header.getContext()
                  )}
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody>
          {table.getRowModel().rows.map(row => (
            <tr
              key={row.id}
              onClick={() => onRowClick?.(row.original)}
              className={onRowClick ? 'data-grid__row--clickable' : ''}
            >
              {row.getVisibleCells().map(cell => (
                <td key={cell.id}>
                  {flexRender(
                    cell.column.columnDef.cell,
                    cell.getContext()
                  )}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
```

### 13.5 Notificaciones con React Hot Toast

**Installation:**
```bash
npm install react-hot-toast
```

**Setup en App.tsx:**
```typescript
import { Toaster } from 'react-hot-toast';

export const App = () => {
  return (
    <>
      <Router />
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 4000,
          style: {
            background: '#fff',
            color: '#333',
          },
          success: {
            duration: 3000,
            iconTheme: {
              primary: '#10b981',
              secondary: '#fff',
            },
          },
          error: {
            duration: 5000,
            iconTheme: {
              primary: '#ef4444',
              secondary: '#fff',
            },
          },
        }}
      />
    </>
  );
};
```

**Hook useToast:**
```typescript
import toast from 'react-hot-toast';

export const useToast = () => {
  return {
    showSuccess: (message: string) => toast.success(message),
    showError: (message: string) => toast.error(message),
    showWarning: (message: string) => toast(message, { icon: '⚠️' }),
    showInfo: (message: string) => toast(message, { icon: 'ℹ️' }),
  };
};
```

---

## 14. Diseño Responsive

### 14.1 Breakpoints

**theme.css:**
```css
:root {
  /* Breakpoints */
  --breakpoint-sm: 640px;
  --breakpoint-md: 768px;
  --breakpoint-lg: 1024px;
  --breakpoint-xl: 1280px;
  --breakpoint-2xl: 1536px;
}
```

**Media Queries:**
```css
/* Mobile First Approach */

/* Base: Mobile (< 640px) */
.container {
  padding: 1rem;
}

/* Small devices (≥ 640px) */
@media (min-width: 640px) {
  .container {
    padding: 1.5rem;
  }
}

/* Medium devices (≥ 768px) - Tablets */
@media (min-width: 768px) {
  .container {
    padding: 2rem;
  }
}

/* Large devices (≥ 1024px) - Desktops */
@media (min-width: 1024px) {
  .container {
    padding: 2.5rem;
  }
}
```

### 14.2 Layout Responsive

**MainLayout.tsx:**
```typescript
import { useState } from 'react';
import { Sidebar } from './Sidebar';
import { Topbar } from './Topbar';
import { Outlet } from 'react-router-dom';
import './MainLayout.css';

export const MainLayout = () => {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 768);
      if (window.innerWidth < 768) {
        setSidebarOpen(false);
      }
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <div className="main-layout">
      <Sidebar
        isOpen={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
        isMobile={isMobile}
      />

      <div className={`main-layout__content ${sidebarOpen ? 'main-layout__content--shifted' : ''}`}>
        <Topbar
          onMenuClick={() => setSidebarOpen(!sidebarOpen)}
        />

        <main className="main-layout__main">
          <Outlet />
        </main>
      </div>
    </div>
  );
};
```

**MainLayout.css:**
```css
.main-layout {
  display: flex;
  min-height: 100vh;
}

.main-layout__content {
  flex: 1;
  display: flex;
  flex-direction: column;
  transition: margin-left 0.3s ease;
}

/* Mobile: Sidebar como overlay */
@media (max-width: 767px) {
  .main-layout__content {
    margin-left: 0 !important;
  }
}

/* Desktop: Sidebar fijo */
@media (min-width: 768px) {
  .main-layout__content--shifted {
    margin-left: 250px;
  }
}

.main-layout__main {
  flex: 1;
  padding: 1rem;
}

@media (min-width: 768px) {
  .main-layout__main {
    padding: 2rem;
  }
}
```

### 14.3 DataGrid Responsive

```css
.data-grid {
  overflow-x: auto;
}

.data-grid__table {
  width: 100%;
  border-collapse: collapse;
  min-width: 600px; /* Permite scroll horizontal en móviles */
}

/* Mobile: Stack en cards */
@media (max-width: 767px) {
  .data-grid__table thead {
    display: none;
  }

  .data-grid__table tr {
    display: block;
    margin-bottom: 1rem;
    border: 1px solid var(--border-color);
    border-radius: 8px;
  }

  .data-grid__table td {
    display: flex;
    justify-content: space-between;
    padding: 0.75rem 1rem;
    border-bottom: 1px solid var(--border-color);
  }

  .data-grid__table td:before {
    content: attr(data-label);
    font-weight: 600;
    margin-right: 1rem;
  }
}
```

---

## 15. Patrones de Panel Expandible

### 15.1 Opción 1: Drawer (Recomendado)

**Características:**
- Se desliza desde el lado derecho
- Overlay oscuro sobre el contenido
- Ideal para formularios y detalles
- Mejor UX en tablets y desktops

**Drawer.tsx:**
```typescript
import { useEffect } from 'react';
import './Drawer.css';

interface DrawerProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  size?: 'sm' | 'md' | 'lg' | 'xl';
  footer?: React.ReactNode;
  children: React.ReactNode;
}

export const Drawer: React.FC<DrawerProps> = ({
  isOpen,
  onClose,
  title,
  size = 'md',
  footer,
  children,
}) => {
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }

    return () => {
      document.body.style.overflow = '';
    };
  }, [isOpen]);

  if (!isOpen) return null;

  return (
    <>
      <div
        className="drawer-overlay"
        onClick={onClose}
      />

      <div className={`drawer drawer--${size}`}>
        <div className="drawer__header">
          {title && <h2 className="drawer__title">{title}</h2>}
          <button
            className="drawer__close"
            onClick={onClose}
            aria-label="Cerrar"
          >
            ×
          </button>
        </div>

        <div className="drawer__body">
          {children}
        </div>

        {footer && (
          <div className="drawer__footer">
            {footer}
          </div>
        )}
      </div>
    </>
  );
};
```

**Drawer.css:**
```css
.drawer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 999;
  animation: fadeIn 0.3s ease;
}

.drawer {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  background: white;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  box-shadow: -4px 0 16px rgba(0, 0, 0, 0.1);
  animation: slideInRight 0.3s ease;
}

.drawer--sm { width: 400px; max-width: 90vw; }
.drawer--md { width: 600px; max-width: 90vw; }
.drawer--lg { width: 800px; max-width: 90vw; }
.drawer--xl { width: 1000px; max-width: 95vw; }

@keyframes slideInRight {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

.drawer__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.drawer__body {
  flex: 1;
  overflow-y: auto;
  padding: 1.5rem;
}

.drawer__footer {
  border-top: 1px solid var(--border-color);
  padding: 1rem 1.5rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}
```

### 15.2 Opción 2: Modal

**Características:**
- Centrado en la pantalla
- Ideal para confirmaciones y alertas
- Más compacto que drawer

**Modal.tsx:**
```typescript
import { useEffect } from 'react';
import './Modal.css';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  size?: 'sm' | 'md' | 'lg';
  footer?: React.ReactNode;
  children: React.ReactNode;
}

export const Modal: React.FC<ModalProps> = ({
  isOpen,
  onClose,
  title,
  size = 'md',
  footer,
  children,
}) => {
  useEffect(() => {
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape' && isOpen) {
        onClose();
      }
    };

    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <>
      <div className="modal-overlay" onClick={onClose} />

      <div className={`modal modal--${size}`}>
        <div className="modal__header">
          {title && <h2 className="modal__title">{title}</h2>}
          <button
            className="modal__close"
            onClick={onClose}
            aria-label="Cerrar"
          >
            ×
          </button>
        </div>

        <div className="modal__body">
          {children}
        </div>

        {footer && (
          <div className="modal__footer">
            {footer}
          </div>
        )}
      </div>
    </>
  );
};
```

### 15.3 Opción 3: Expandable Card

**Características:**
- Se expande in-place
- Ideal para detalles rápidos
- No requiere overlay

**ExpandableCard.tsx:**
```typescript
import { useState } from 'react';
import './ExpandableCard.css';

interface ExpandableCardProps {
  title: string;
  summary: React.ReactNode;
  children: React.ReactNode;
}

export const ExpandableCard: React.FC<ExpandableCardProps> = ({
  title,
  summary,
  children,
}) => {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className={`expandable-card ${isExpanded ? 'expandable-card--expanded' : ''}`}>
      <div
        className="expandable-card__header"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <div className="expandable-card__title">
          <h3>{title}</h3>
          <div className="expandable-card__summary">{summary}</div>
        </div>
        <button className="expandable-card__toggle">
          {isExpanded ? '−' : '+'}
        </button>
      </div>

      {isExpanded && (
        <div className="expandable-card__content">
          {children}
        </div>
      )}
    </div>
  );
};
```

### 15.4 Cuándo Usar Cada Uno

| Patrón | Mejor para | Casos de uso |
|--------|-----------|--------------|
| **Drawer** | Formularios detallados, edición de registros | Crear/editar usuario, rol, producto |
| **Modal** | Confirmaciones, alertas, formularios cortos | Eliminar registro, cambiar contraseña |
| **Expandable Card** | Detalles rápidos, información adicional | Ver detalles de pedido, historial |

---

## 16. Mapeo DTO ↔ UI Model

### 16.1 Por Qué Mapear

El backend envía DTOs diseñados para transporte de datos. El frontend puede necesitar:
- Formatear fechas
- Computar campos derivados
- Normalizar estructuras
- Agregar propiedades de UI

### 16.2 Ejemplo de Mapper

**user.mapper.ts:**
```typescript
import type { UsuarioResponseDTO, UsuarioCreateDTO } from '@/types/user.types';
import type { User, UserFormData } from '@/types/user.types';
import { formatDate } from '@/utils/date';

/**
 * Convierte UsuarioResponseDTO del backend a User del frontend
 */
export const mapUsuarioDTOToUser = (dto: UsuarioResponseDTO): User => {
  return {
    id: dto.id,
    username: dto.username,
    email: dto.email,
    primerNombre: dto.primerNombre,
    segundoNombre: dto.segundoNombre,
    primerApellido: dto.primerApellido,
    segundoApellido: dto.segundoApellido,
    
    // Campos computados
    nombreCompleto: [
      dto.primerNombre,
      dto.segundoNombre,
      dto.primerApellido,
      dto.segundoApellido,
    ].filter(Boolean).join(' '),
    
    // Formatear fechas
    fechaNacimiento: dto.fechaNacimiento ? formatDate(dto.fechaNacimiento) : null,
    
    // Normalizar roles y permisos
    roles: dto.roles || [],
    permissions: dto.permissions || [],
    
    // Estado
    activo: dto.activo,
    
    // Auditoría
    createdAt: formatDate(dto.createdAt),
    updatedAt: dto.updatedAt ? formatDate(dto.updatedAt) : null,
  };
};

/**
 * Convierte array de DTOs a array de Users
 */
export const mapUsuariosDTOToUsers = (dtos: UsuarioResponseDTO[]): User[] => {
  return dtos.map(mapUsuarioDTOToUser);
};

/**
 * Convierte UserFormData del formulario a UsuarioCreateDTO para el backend
 */
export const mapUserFormDataToCreateDTO = (formData: UserFormData): UsuarioCreateDTO => {
  return {
    username: formData.username,
    password: formData.password,
    email: formData.email,
    primerNombre: formData.primerNombre,
    segundoNombre: formData.segundoNombre || null,
    primerApellido: formData.primerApellido,
    segundoApellido: formData.segundoApellido || null,
    dpi: formData.dpi || null,
    nit: formData.nit || null,
    telefono: formData.telefono || null,
    fechaNacimiento: formData.fechaNacimiento || null,
    roles: formData.roles,
  };
};
```

### 16.3 Uso en Servicios

```typescript
import { mapUsuarioDTOToUser, mapUsuariosDTOToUsers } from '@/mappers/user.mapper';

export const listUsuarios = async (filters?: UsuarioFilterDTO) => {
  const response = await apiService.get<ApiResponse<PageResponse<UsuarioResponseDTO>>>(
    '/api/v1/usuarios',
    { params: filters }
  );

  // Mapear DTOs a Users
  return {
    ...response.body,
    content: mapUsuariosDTOToUsers(response.body.content),
  };
};

export const getUsuario = async (id: number) => {
  const response = await apiService.get<ApiResponse<UsuarioResponseDTO>>(
    `/api/v1/usuarios/${id}`
  );

  // Mapear DTO a User
  return mapUsuarioDTOToUser(response.body);
};
```

---

## 17. Sistema de Constantes

### 17.1 api.constants.ts

```typescript
export const API_CONFIG = {
  BASE_URL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  TIMEOUT: 30000, // 30 segundos
  RETRY_ATTEMPTS: 3,
  RETRY_DELAY: 1000, // 1 segundo
} as const;

export const API_ENDPOINTS = {
  // Auth
  AUTH_LOGIN: '/api/v1/auth/login',
  AUTH_REFRESH: '/api/v1/auth/refresh',
  AUTH_LOGOUT: '/api/v1/auth/logout',
  
  // Usuarios
  USUARIOS: '/api/v1/usuarios',
  
  // Roles
  ROLES: '/api/v1/rols',
  
  // Páginas
  PAGINAS: '/api/v1/pages',
  
  // Módulos
  MODULOS: '/api/v1/modules',
} as const;
```

### 17.2 routes.constants.ts

```typescript
export const ROUTES = {
  // Auth
  LOGIN: '/login',
  
  // Dashboard
  DASHBOARD: '/dashboard',
  
  // Usuarios
  USUARIOS: '/usuarios',
  USUARIOS_CREATE: '/usuarios/crear',
  USUARIOS_EDIT: '/usuarios/:id/editar',
  
  // Roles
  ROLES: '/roles',
  
  // Productos
  PRODUCTOS: '/productos',
  
  // Ventas
  VENTAS: '/ventas',
  
  // Inventario
  INVENTARIO: '/inventario',
} as const;
```

### 17.3 Uso de Constantes

```typescript
// ❌ Incorrecto - strings literales
navigate('/usuarios');
apiService.get('/api/v1/usuarios');

// ✅ Correcto - usar constantes
import { ROUTES } from '@/constants/routes.constants';
import { API_ENDPOINTS } from '@/constants/api.constants';

navigate(ROUTES.USUARIOS);
apiService.get(API_ENDPOINTS.USUARIOS);
```

---

## 18. Interceptores HTTP

### 18.1 Auth Interceptor (Request)

**auth.interceptor.ts:**
```typescript
import type { InternalAxiosRequestConfig } from 'axios';
import { storageService } from '@/utils/storage';

export const authInterceptor = (config: InternalAxiosRequestConfig) => {
  const token = storageService.getToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
};
```

### 18.2 Response Interceptor

**response.interceptor.ts:**
```typescript
import type { AxiosResponse } from 'axios';

export const responseInterceptor = (response: AxiosResponse) => {
  // Log de respuestas exitosas en desarrollo
  if (import.meta.env.DEV) {
    console.log(`[Response] ${response.config.method?.toUpperCase()} ${response.config.url}`, {
      status: response.status,
      data: response.data,
    });
  }

  return response;
};
```

### 18.3 Error Interceptor

**error.interceptor.ts:**
```typescript
import type { AxiosError } from 'axios';
import { storageService } from '@/utils/storage';
import { ERROR_CODES } from '@/constants/error.constants';
import toast from 'react-hot-toast';

export const errorInterceptor = async (error: AxiosError) => {
  const originalRequest = error.config;

  // Log de errores en desarrollo
  if (import.meta.env.DEV) {
    console.error('[API Error]', {
      url: originalRequest?.url,
      method: originalRequest?.method,
      status: error.response?.status,
      data: error.response?.data,
    });
  }

  // 401 Unauthorized - Token expirado
  if (error.response?.status === 401) {
    const errorData = error.response.data as any;

    // Si el error es por token expirado, intentar refresh
    if (errorData?.errorCode === ERROR_CODES.AUTH_TOKEN_EXPIRED) {
      try {
        const refreshToken = storageService.getRefreshToken();
        
        if (refreshToken) {
          // Intentar refresh
          const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/auth/refresh`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken }),
          });

          if (response.ok) {
            const data = await response.json();
            storageService.setToken(data.body.accessToken);
            
            // Reintentar request original
            if (originalRequest) {
              originalRequest.headers.Authorization = `Bearer ${data.body.accessToken}`;
              return axios(originalRequest);
            }
          }
        }
      } catch (refreshError) {
        // Refresh falló, hacer logout
        storageService.clearAuth();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    // Si no se pudo recuperar, hacer logout
    storageService.clearAuth();
    window.location.href = '/login';
    toast.error('Sesión expirada. Por favor, inicie sesión nuevamente.');
  }

  // 403 Forbidden - Sin permisos
  if (error.response?.status === 403) {
    toast.error('No tiene permisos para realizar esta acción');
  }

  // 404 Not Found
  if (error.response?.status === 404) {
    toast.error('Recurso no encontrado');
  }

  // 422 Validation Error
  if (error.response?.status === 422) {
    const errorData = error.response.data as any;
    
    if (errorData?.validationErrors) {
      // Mostrar primer error de validación
      const firstError = errorData.validationErrors[0];
      toast.error(firstError.message);
    }
  }

  // 500 Internal Server Error
  if (error.response?.status === 500) {
    toast.error('Error interno del servidor. Por favor, intente más tarde.');
  }

  // Network Error
  if (error.message === 'Network Error') {
    toast.error('Error de conexión. Verifique su conexión a internet.');
  }

  return Promise.reject(error);
};
```

---

## 19. Testing

### 19.1 Setup de Testing (Próxima Fase)

**Instalación:**
```bash
npm install -D vitest @testing-library/react @testing-library/jest-dom @testing-library/user-event happy-dom
```

**vitest.config.ts:**
```typescript
import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig({
  plugins: [react()],
  test: {
    environment: 'happy-dom',
    setupFiles: ['./src/test/setup.ts'],
    globals: true,
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
});
```

### 19.2 Test de Componente

**Button.test.tsx:**
```typescript
import { render, screen, fireEvent } from '@testing-library/react';
import { Button } from './Button';

describe('Button', () => {
  it('renders children correctly', () => {
    render(<Button>Click me</Button>);
    expect(screen.getByText('Click me')).toBeInTheDocument();
  });

  it('calls onClick when clicked', () => {
    const handleClick = vi.fn();
    render(<Button onClick={handleClick}>Click me</Button>);
    
    fireEvent.click(screen.getByText('Click me'));
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it('is disabled when disabled prop is true', () => {
    render(<Button disabled>Click me</Button>);
    expect(screen.getByText('Click me')).toBeDisabled();
  });

  it('shows loading state', () => {
    render(<Button loading>Click me</Button>);
    expect(screen.getByText('Click me')).toBeDisabled();
    expect(document.querySelector('.btn--loading')).toBeInTheDocument();
  });
});
```

### 19.3 Test de Hook

**useUsersPage.test.ts:**
```typescript
import { renderHook, waitFor } from '@testing-library/react';
import { useUsersPage } from './useUsersPage';
import * as securityService from '@/services/endpoints/security.service';

vi.mock('@/services/endpoints/security.service');

describe('useUsersPage', () => {
  it('fetches users on mount', async () => {
    const mockUsers = [
      { id: 1, username: 'user1', email: 'user1@test.com' },
    ];

    vi.spyOn(securityService, 'listUsuarios').mockResolvedValue({
      body: { content: mockUsers },
    } as any);

    const { result } = renderHook(() => useUsersPage());

    expect(result.current.loading).toBe(true);

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.users).toEqual(mockUsers);
  });
});
```

### 19.4 Cobertura Mínima

**Objetivo:**
- Componentes: 80%
- Hooks: 80%
- Utils: 90%
- Services: 70%

---

## 20. Optimización y Performance

### 20.1 Code Splitting

**router.tsx:**
```typescript
import { lazy, Suspense } from 'react';
import { createBrowserRouter } from 'react-router-dom';
import { SkeletonPage } from '@/components/common/SkeletonPage';

// Lazy load pages
const DashboardPage = lazy(() => import('@/pages/dashboard/DashboardPage'));
const UsersListPage = lazy(() => import('@/pages/security/users/UsersListPage'));
const RolesListPage = lazy(() => import('@/pages/security/roles/RolesListPage'));

export const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      {
        path: 'dashboard',
        element: (
          <Suspense fallback={<SkeletonPage />}>
            <DashboardPage />
          </Suspense>
        ),
      },
      {
        path: 'usuarios',
        element: (
          <Suspense fallback={<SkeletonPage />}>
            <UsersListPage />
          </Suspense>
        ),
      },
    ],
  },
]);
```

### 20.2 Memoization

**Uso de React.memo:**
```typescript
import { memo } from 'react';

export const DataGridRow = memo<DataGridRowProps>(
  ({ data, onEdit, onDelete }) => {
    return (
      <tr>
        {/* ... */}
      </tr>
    );
  },
  (prevProps, nextProps) => {
    // Custom comparison
    return prevProps.data.id === nextProps.data.id;
  }
);
```

**Uso de useMemo:**
```typescript
const filteredUsers = useMemo(() => {
  return users.filter(user =>
    user.username.toLowerCase().includes(searchTerm.toLowerCase())
  );
}, [users, searchTerm]);
```

**Uso de useCallback:**
```typescript
const handleDelete = useCallback((id: number) => {
  // ...
}, []);
```

### 20.3 Debounce en Búsquedas

**useDebounce.ts:**
```typescript
import { useState, useEffect } from 'react';

export const useDebounce = <T,>(value: T, delay: number = 500): T => {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
};
```

**Uso:**
```typescript
const [searchTerm, setSearchTerm] = useState('');
const debouncedSearchTerm = useDebounce(searchTerm, 500);

useEffect(() => {
  if (debouncedSearchTerm) {
    fetchUsers({ search: debouncedSearchTerm });
  }
}, [debouncedSearchTerm]);
```

### 20.4 Estrategia de Paginación

**Para catálogos pequeños (<1000 registros):**
- Cargar TODO en memoria
- Paginación client-side
- Filtrado client-side

**Para catálogos grandes (>1000 registros):**
- Paginación server-side
- Filtrado server-side
- Usar `PageResponse<T>` del backend

```typescript
const [page, setPage] = useState(0);
const [size, setSize] = useState(20);

useEffect(() => {
  fetchUsers({ page, size });
}, [page, size]);
```

---

## 21. Checklist por Módulo Nuevo

Al crear un módulo nuevo, seguir esta lista:

### 21.1 Preparación

- [ ] Revisar endpoints disponibles en Swagger del backend
- [ ] Revisar DTOs del backend
- [ ] Definir permisos necesarios
- [ ] Revisar mockups de UI

### 21.2 Tipos

- [ ] Crear archivo `types/[modulo].types.ts`
- [ ] Definir interfaces para DTOs del backend
- [ ] Definir interfaces para UI models
- [ ] Definir tipos de formularios

### 21.3 Constantes

- [ ] Agregar constantes de permisos en `auth.constants.ts`
- [ ] Agregar endpoints en `api.constants.ts`
- [ ] Agregar rutas en `routes.constants.ts`

### 21.4 Mappers

- [ ] Crear `mappers/[modulo].mapper.ts`
- [ ] Implementar mapeo DTO → UI Model
- [ ] Implementar mapeo Form Data → Create/Update DTO

### 21.5 Servicios

- [ ] Crear `services/endpoints/[modulo].service.ts`
- [ ] Implementar método `list` (con paginación)
- [ ] Implementar método `get` (obtener por ID)
- [ ] Implementar método `create`
- [ ] Implementar método `update`
- [ ] Implementar método `delete`
- [ ] Usar mappers en servicios

### 21.6 Validaciones

- [ ] Crear `validations/[modulo].validation.ts`
- [ ] Definir esquemas Zod alineados con backend
- [ ] Usar constantes de `validation.constants.ts`

### 21.7 Hooks

- [ ] Crear `hooks/[modulo]/use[Modulo]Page.ts`
- [ ] Implementar lógica de fetching
- [ ] Implementar handlers (create, update, delete)
- [ ] Manejar loading y error states
- [ ] Usar hook de toast para notificaciones

### 21.8 Componentes

- [ ] Crear carpeta `pages/[modulo]/`
- [ ] Crear `[Modulo]ListPage.tsx`
- [ ] Crear `components/[Modulo]FilterBar.tsx`
- [ ] Crear `components/[Modulo]InfoFormRHF.tsx`
- [ ] Aplicar diseño responsive
- [ ] Usar Drawer o Modal para formularios

### 21.9 Rutas

- [ ] Agregar rutas en `router.tsx`
- [ ] Configurar `ProtectedRoute` con permisos
- [ ] Configurar lazy loading con Suspense

### 21.10 Permisos

- [ ] Usar componente `<Can>` para mostrar/ocultar acciones
- [ ] Validar permisos en handlers si es necesario

### 21.11 Estilos

- [ ] Crear archivo CSS específico o usar clases globales
- [ ] Asegurar diseño responsive
- [ ] Probar en diferentes tamaños de pantalla

### 21.12 Testing (Próxima Fase)

- [ ] Test unitario del hook
- [ ] Test unitario de componentes clave
- [ ] Test de integración de formularios

---

## 22. Recursos y Referencias

### 22.1 Documentación Oficial

- [React](https://react.dev/)
- [TypeScript](https://www.typescriptlang.org/docs/)
- [Vite](https://vitejs.dev/)
- [React Router](https://reactrouter.com/)
- [React Hook Form](https://react-hook-form.com/)
- [Zod](https://zod.dev/)
- [TanStack Table](https://tanstack.com/table/latest)
- [React Hot Toast](https://react-hot-toast.com/)

### 22.2 Documentación del Proyecto

- **Directrices Backend**: `DIRECTRICES_TECNICAS_BACKEND.md`
- **Arquitectura Backend**: `DOCUMENTACION_ARQUITECTURA_BACKEND.md`
- **Mockups**: `Predio_Mijangos_WEB.pdf`
- **ERS**: `2__ERS_Levantamiento_Requerimientos_PredioMijangos.docx`
- **Arquitectura de Solución**: `ARQUITECTURA_DE_SOLUCIÓN.pdf`

### 22.3 Herramientas

- **Swagger UI**: Documentación API del backend
- **React DevTools**: Depuración de componentes
- **Redux DevTools**: (Si se agrega Redux en futuro)

---

## 📌 IMPORTANTE

**Este documento es la fuente de verdad para el desarrollo del frontend.**

- Cualquier decisión arquitectónica o patrón de código debe estar documentado aquí
- Mantener actualizado es responsabilidad de todo el equipo
- Consultar siempre antes de desviarse de las directrices establecidas
- Mantener alineación con backend en todo momento

---

**Documento mantenido por:** Equipo Técnico Predio Mijangos  
**Última revisión:** 21 de Octubre 2025  
**Versión:** 2.0.0
