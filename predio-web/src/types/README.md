# 📘 Types - Documentación

> Tipos TypeScript para comunicación con la API del backend

---

## 📋 Índice

1. [Visión General](#visión-general)
2. [Tipos Principales](#tipos-principales)
3. [Guías de Uso](#guías-de-uso)
4. [Ejemplos Prácticos](#ejemplos-prácticos)
5. [Sincronización con Backend](#sincronización-con-backend)
6. [Mejores Prácticas](#mejores-prácticas)

---

## 🎯 Visión General

Este directorio contiene todos los tipos TypeScript del proyecto. Los tipos están **100% sincronizados** con el backend (Spring Boot) para garantizar consistencia en toda la aplicación.

### Archivos Principales

```
types/
├── api.d.ts          # Tipos base de API (ApiResponse, PageResponse, ErrorResponse)
├── auth.d.ts         # Tipos de autenticación y autorización
├── user.ts           # Tipos de usuarios
├── role.ts           # Tipos de roles y permisos
├── provider.ts       # Tipos de proveedores
└── index.ts          # Exportaciones centralizadas
```

---

## 📦 Tipos Principales

### 1. ApiResponse<T>

**Propósito**: Estructura estándar para TODAS las respuestas del backend

**Sincronizado con**: `ApiResponse.java`

```typescript
interface ApiResponse<T> {
  statusCode: number;        // Código HTTP (200, 400, 404, etc.)
  message: string;           // Mensaje descriptivo
  body: T;                   // Datos de la respuesta
  metadata?: Record<string, any>;  // Metadatos opcionales
}
```

**Uso**:
```typescript
// Respuesta simple
const response: ApiResponse<User> = await userService.getById(1);

// Respuesta paginada
const response: ApiResponse<PageResponse<User>> = await userService.list();

// Respuesta de error
const response: ApiResponse<ErrorResponse> = await userService.getById(999);
```

---

### 2. PageResponse<T>

**Propósito**: Estructura para listas paginadas

**Sincronizado con**: `PageResponse.java`

```typescript
interface PageResponse<T> {
  content: T[];          // Elementos de la página actual
  page: number;          // Número de página (0-indexed)
  size: number;          // Elementos por página
  totalElements: number; // Total de elementos
  totalPages: number;    // Total de páginas
  last: boolean;         // ¿Es la última página?
}
```

**Uso**:
```typescript
const response = await userService.list({ page: 0, size: 10 });
const users = response.body.content;          // Array de usuarios
const hasMore = !response.body.last;          // ¿Hay más páginas?
const total = response.body.totalElements;    // Total de usuarios
```

---

### 3. ErrorResponse

**Propósito**: Estructura detallada para errores

**Sincronizado con**: `ErrorResponse.java`

```typescript
interface ErrorResponse {
  errorCode: string;               // Código interno (USER_001, AUTH_002, etc.)
  message: string;                 // Mensaje del error
  status: number;                  // Código HTTP
  timestamp: string;               // Marca de tiempo ISO 8601
  validationErrors?: ValidationError[];  // Errores por campo (opcional)
}
```

**Uso**:
```typescript
try {
  await userService.create(data);
} catch (error) {
  const errorResponse = error.body as ErrorResponse;
  
  console.error(`[${errorResponse.errorCode}] ${errorResponse.message}`);
  
  if (errorResponse.validationErrors) {
    errorResponse.validationErrors.forEach(ve => {
      console.error(`  ${ve.field}: ${ve.message}`);
    });
  }
}
```

---

### 4. ValidationError

**Propósito**: Detalle de errores de validación por campo

```typescript
interface ValidationError {
  field: string;           // Campo que falló ("email", "password", etc.)
  message: string;         // Mensaje del error
  rejectedValue?: any;     // Valor rechazado (opcional)
}
```

**Uso con React Hook Form**:
```typescript
if (errorResponse.validationErrors) {
  errorResponse.validationErrors.forEach(error => {
    setError(error.field as any, {
      type: 'server',
      message: error.message
    });
  });
}
```

---

## 📖 Guías de Uso

### Servicios de API

**Siempre tipea las respuestas de servicios**:

```typescript
// ✅ Correcto
export const getUsuario = async (id: number): Promise<ApiResponse<UsuarioResponseDTO>> => {
  return apiService.get<ApiResponse<UsuarioResponseDTO>>(`/api/v1/usuarios/${id}`);
};

// ✅ Correcto - con paginación
export const listUsuarios = async (
  params: UsuarioFilterParams
): Promise<ApiResponse<PageResponse<UsuarioListDTO>>> => {
  return apiService.get<ApiResponse<PageResponse<UsuarioListDTO>>>(
    '/api/v1/usuarios',
    { params }
  );
};

// ❌ Incorrecto - sin tipos
export const getUsuario = async (id) => {
  return apiService.get(`/api/v1/usuarios/${id}`);
};
```

---

### Hooks Personalizados

**Usa los tipos en custom hooks**:

```typescript
interface UseUsersReturn {
  users: User[];
  loading: boolean;
  error: ErrorResponse | null;
  refetch: () => void;
}

export function useUsers(): UseUsersReturn {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<ErrorResponse | null>(null);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const response = await listUsuarios({ page: 0, size: 10 });
      
      if (isSuccessResponse(response)) {
        setUsers(response.body.content);
      }
    } catch (err: any) {
      setError(err.body as ErrorResponse);
    } finally {
      setLoading(false);
    }
  };

  return { users, loading, error, refetch: fetchUsers };
}
```

---

### Manejo de Errores

**Usa los type guards para verificar tipos de respuesta**:

```typescript
const response = await userService.getById(id);

// Type guard: isSuccessResponse
if (isSuccessResponse(response)) {
  console.log('Usuario:', response.body);  // body es User
} else {
  console.error('Error:', response.message);
}

// Type guard: isErrorResponse
if (isErrorResponse(response)) {
  const error = response.body as ErrorResponse;
  console.error(`[${error.errorCode}] ${error.message}`);
}

// Type guard: hasValidationErrors
if (isErrorResponse(response)) {
  const error = response.body as ErrorResponse;
  
  if (hasValidationErrors(error)) {
    error.validationErrors.forEach(ve => {
      console.error(`${ve.field}: ${ve.message}`);
    });
  }
}
```

---

## 💡 Ejemplos Prácticos

### Ejemplo 1: Listar Usuarios con Paginación

```typescript
import { ApiResponse, PageResponse } from '@/types/api.d';
import { User } from '@/types/user';

const UsersPage = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [pagination, setPagination] = useState({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0,
    hasMore: true
  });

  const fetchUsers = async (page: number = 0) => {
    const response: ApiResponse<PageResponse<User>> = await userService.list({
      page,
      size: 10
    });

    if (isSuccessResponse(response)) {
      setUsers(response.body.content);
      setPagination({
        page: response.body.page,
        size: response.body.size,
        totalElements: response.body.totalElements,
        totalPages: response.body.totalPages,
        hasMore: !response.body.last
      });
    }
  };

  return (
    <div>
      <UserList users={users} />
      <Pagination
        current={pagination.page}
        total={pagination.totalPages}
        onChange={fetchUsers}
      />
    </div>
  );
};
```

---

### Ejemplo 2: Crear Usuario con Validación

```typescript
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { ErrorResponse, hasValidationErrors } from '@/types/api.d';

const CreateUserForm = () => {
  const { register, handleSubmit, setError } = useForm({
    resolver: zodResolver(userSchema)
  });

  const onSubmit = async (data: UserCreateDTO) => {
    try {
      const response = await userService.create(data);
      
      if (isSuccessResponse(response)) {
        toast.success('Usuario creado exitosamente');
        navigate('/usuarios');
      }
    } catch (error: any) {
      const errorResponse = error.body as ErrorResponse;
      
      // Mostrar error general
      toast.error(errorResponse.message);
      
      // Mapear errores de validación a campos del formulario
      if (hasValidationErrors(errorResponse)) {
        errorResponse.validationErrors.forEach(ve => {
          setError(ve.field as any, {
            type: 'server',
            message: ve.message
          });
        });
      }
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      {/* campos del formulario */}
    </form>
  );
};
```

---

### Ejemplo 3: Interceptor de Errores

```typescript
import { ErrorResponse, isErrorResponse } from '@/types/api.d';
import { ERROR_CODES, shouldLogoutOnError } from '@/constants/error.constants';

// Interceptor de respuesta de Axios
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const apiError = error.response?.data as ApiResponse<ErrorResponse>;
    
    if (isErrorResponse(apiError)) {
      const errorData = apiError.body;
      
      // Log del error
      console.error(`[${errorData.errorCode}] ${errorData.message}`);
      
      // Auto-logout en errores de autenticación críticos
      if (shouldLogoutOnError(errorData.errorCode)) {
        localStorage.removeItem('accessToken');
        window.location.href = '/login';
      }
      
      // Mostrar notificación al usuario
      toast.error(errorData.message);
    }
    
    return Promise.reject(error);
  }
);
```

---

## 🔄 Sincronización con Backend

### Cambios que Requieren Actualización

**Cuando se modifique el backend, actualizar frontend**:

1. **ApiResponse.java** → `api.d.ts` (interface ApiResponse)
2. **PageResponse.java** → `api.d.ts` (interface PageResponse)
3. **ErrorResponse.java** → `api.d.ts` (interface ErrorResponse)
4. **ErrorCodes.java** → `error.constants.ts`

### Naming Conventions

| Backend (Java) | Frontend (TypeScript) |
|----------------|----------------------|
| `statusCode` | `statusCode` ✅ |
| `currentPage` | `page` ⚠️ |
| `pageSize` | `size` ⚠️ |

⚠️ **Nota**: `currentPage` y `pageSize` del backend se mapean a `page` y `size` en frontend por convención de JavaScript/TypeScript.

---

## ✅ Mejores Prácticas

### 1. Siempre Usa Tipos Genéricos

```typescript
// ✅ Correcto
const response: ApiResponse<User> = await getUser(id);

// ❌ Incorrecto
const response: any = await getUser(id);
```

---

### 2. Type Guards en Lugar de Casting

```typescript
// ✅ Correcto - usa type guards
if (isSuccessResponse(response)) {
  console.log(response.body);  // TypeScript sabe que body es User
}

// ❌ Incorrecto - casting directo
const user = response.body as User;  // Puede fallar si es error
```

---

### 3. Maneja Todos los Casos

```typescript
// ✅ Correcto - maneja éxito y error
const response = await getUser(id);

if (isSuccessResponse(response)) {
  setUser(response.body);
} else if (isErrorResponse(response)) {
  const error = response.body as ErrorResponse;
  showError(error.message);
}

// ❌ Incorrecto - solo maneja éxito
const response = await getUser(id);
setUser(response.body);  // ¿Qué pasa si es error?
```

---

### 4. Usa Tipos Auxiliares

```typescript
// ✅ Usa los tipos helpers definidos
import { PaginatedResponse, ErrorApiResponse } from '@/types/api.d';

const response: PaginatedResponse<User> = await listUsers();
// En lugar de: ApiResponse<PageResponse<User>>

const error: ErrorApiResponse = ...;
// En lugar de: ApiResponse<ErrorResponse>
```

---

### 5. Documenta Tipos Custom

```typescript
/**
 * DTO para crear un usuario
 * 
 * @see UsuarioCreateDTO.java
 */
export interface UserCreateDTO {
  /** Datos de la persona asociada */
  persona: PersonaCreateDTO;
  
  /** Username único del usuario */
  username: string;
  
  /** Contraseña (mínimo 8 caracteres) */
  password: string;
  
  /** IDs de roles a asignar */
  roleIds: number[];
}
```

---

## 🔗 Enlaces Relacionados

- [Constants](../constants/README.md) - Códigos de error y endpoints
- [Services](../services/README.md) - Servicios de API
- [Mappers](../mappers/README.md) - Transformación DTO ↔ Entity
- [Directrices Frontend](../../DIRECTRICES_TECNICAS_FRONTEND.md)

---

## 📝 Notas Finales

- **Sincronización**: Mantener tipos sincronizados con backend es CRÍTICO
- **Naming**: Usar camelCase en TypeScript (no snake_case)
- **Documentación**: Documentar todos los tipos custom con JSDoc
- **Validación**: Usar Zod o Yup para validación runtime si es necesario
- **Type Safety**: Aprovechar TypeScript al máximo, evitar `any`

---

**Última actualización**: 2025-10-30  
**Versión**: 2.0.0  
**Autor**: Equipo Técnico Predio Mijangos
