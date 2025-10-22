# 📚 Documentación de Arquitectura Backend - Predio Mijangos

**Versión:** 2.0.0  
**Última Actualización:** 21 de Octubre 2025  
**Autor:** Equipo Técnico Predio Mijangos

---

## 📋 Índice

1. [Introducción](#introducción)
2. [Paquete Core](#paquete-core)
3. [Paquete Security](#paquete-security)
4. [Paquete Modules](#paquete-modules)
5. [Flujos de Datos Principales](#flujos-de-datos-principales)
6. [Diagrama de Arquitectura](#diagrama-de-arquitectura)

---

## 1. Introducción

Este documento describe detalladamente la arquitectura del backend del sistema Predio Mijangos, explicando la función y responsabilidad de cada componente, clase y paquete del proyecto.

### 1.1 Estructura General

```
com.predio.mijangos/
├── core/              # Funcionalidades transversales y compartidas
├── security/          # Autenticación, autorización y JWT
├── modules/           # Módulos de negocio (usuarios, ventas, inventario, etc.)
└── PredioMijangosApiApplication.java
```

### 1.2 Principios Arquitectónicos

- **Monolito Modular**: Separación por dominios de negocio
- **Arquitectura en Capas**: Controller → Service → Repository → Domain
- **SOLID**: Aplicación estricta de principios SOLID
- **DRY**: Evitar duplicación mediante abstracciones en `core`
- **Separation of Concerns**: Cada clase tiene una única responsabilidad

---

## 2. Paquete Core

El paquete `core` contiene toda la funcionalidad transversal y compartida del sistema. Es la base sobre la cual se construyen todos los módulos de negocio.

### 2.1 core/config - Configuraciones

Contiene las configuraciones principales de Spring Boot y sus componentes.

#### 2.1.1 SecurityConfig.java

**Propósito**: Configuración central de Spring Security.

**Responsabilidades**:
- Configurar la cadena de filtros de seguridad HTTP
- Definir rutas públicas y protegidas
- Establecer política de sesiones (stateless para JWT)
- Configurar CORS para acceso desde frontend
- Integrar filtros personalizados (JWT)

**Componentes clave**:
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)
```
- Define qué rutas requieren autenticación
- Configura el filtro JWT antes del filtro de autenticación estándar
- Establece el manejador de excepciones de autenticación

```java
@Bean
public CorsConfigurationSource corsConfigurationSource()
```
- Define orígenes permitidos (localhost en dev, dominios en prod)
- Especifica métodos HTTP y headers permitidos
- Habilita credenciales (cookies, auth headers)

**Rutas públicas actuales**:
- `/`, `/health` - Información de la API
- `/api/auth/**` - Endpoints de autenticación
- `/swagger-ui/**`, `/v3/api-docs/**` - Documentación
- `/actuator/health` - Health checks

**Patrón de uso**: Esta clase es leída automáticamente por Spring Security al iniciar la aplicación.

---

#### 2.1.2 SwaggerConfig.java

**Propósito**: Configuración de OpenAPI 3.0 (Swagger) para documentación interactiva de la API.

**Responsabilidades**:
- Definir información general de la API (título, descripción, versión)
- Configurar esquemas de seguridad (JWT Bearer Token)
- Agrupar endpoints por tags
- Especificar servidores disponibles

**Componentes clave**:
```java
@Bean
public OpenAPI customOpenAPI()
```
- Define metadata de la API
- Configura el esquema de seguridad Bearer JWT
- Establece información de contacto y licencia

**Resultado**: Swagger UI disponible en `/api/swagger-ui.html`

---

#### 2.1.3 AuditorAwareConfig.java

**Propósito**: Configurar auditoría automática de JPA para tracking de usuarios.

**Responsabilidades**:
- Obtener el usuario actual autenticado desde el contexto de Spring Security
- Proveer el ID del usuario para los campos `createdBy` y `updatedBy`

**Componentes clave**:
```java
@Bean
public AuditorAware<Integer> auditorProvider()
```
- Extrae el `CustomUserDetails` del contexto de seguridad
- Retorna el ID del usuario autenticado
- Si no hay usuario autenticado, retorna `Optional.empty()`

**Integración**: Trabaja en conjunto con `@EnableJpaAuditing` y `BaseEntity`

---

### 2.2 core/constants - Constantes

Centralizan todos los valores constantes usados en la aplicación.

#### 2.2.1 SecurityConstants.java

**Propósito**: Constantes relacionadas con seguridad y JWT.

**Contenido**:
- **JWT**:
  - `TOKEN_PREFIX`: "Bearer "
  - `HEADER_STRING`: "Authorization"
  
- **Roles del Sistema**:
  - `ROLE_ADMIN`, `ROLE_SUPERVISOR`, `ROLE_VENDEDOR`, etc.
  
- **Permisos Granulares**:
  - Formato: `MODULO:ACCION` (ej: `USUARIOS:CREATE`, `VENTAS:READ`)
  
- **Endpoints Públicos**: Lista de URLs que no requieren autenticación

- **Configuración de Contraseñas**:
  - Longitud mínima, máxima
  - Requisitos de complejidad
  - BCrypt strength (12)

**Patrón de uso**: Importar constantes en lugar de usar strings literales

```java
// ❌ Incorrecto
if (role.equals("ROLE_ADMIN")) { ... }

// ✅ Correcto
if (role.equals(SecurityConstants.ROLE_ADMIN)) { ... }
```

---

#### 2.2.2 ErrorCodes.java

**Propósito**: Códigos de error estandarizados para toda la aplicación.

**Categorías**:
- **Autenticación**: `AUTH_001` - `AUTH_999`
- **Usuarios**: `USER_001` - `USER_999`
- **Validación**: `VAL_001` - `VAL_999`
- **Negocio**: `BUS_001` - `BUS_999`
- **Sistema**: `SYS_001` - `SYS_999`

**Beneficios**:
- Permite al frontend identificar errores específicos
- Facilita logging y debugging
- Soporta internacionalización de mensajes
- Tracking de errores en monitoring

**Ejemplo de uso**:
```java
throw new BusinessException(
    ErrorCodes.USER_ALREADY_EXISTS,
    "El usuario ya existe en el sistema"
);
```

---

#### 2.2.3 ValidationConstants.java

**Propósito**: Constantes para validación de datos.

**Contenido**:
- **Longitudes de campos**: MIN/MAX para nombre, username, email, etc.
- **Patrones Regex**:
  - `DPI_PATTERN`: Validación de DPI guatemalteco
  - `NIT_PATTERN`: Validación de NIT
  - `PHONE_PATTERN`: Teléfonos de Guatemala
  - `EMAIL_PATTERN`: Validación de email
  
- **Rangos numéricos**: Min/Max para precios, cantidades, etc.

- **Mensajes de validación**: Mensajes estandarizados para errores

**Patrón de uso**:
```java
@Pattern(
    regexp = ValidationConstants.DPI_PATTERN,
    message = ValidationConstants.INVALID_DPI_MESSAGE
)
private String dpi;
```

---

#### 2.2.4 CacheConstants.java

**Propósito**: Configuración de caché (Redis en Fase 2).

**Contenido**:
- **Nombres de caché**: `DEPARTAMENTOS_CACHE`, `USUARIOS_CACHE`, etc.
- **TTL (Time To Live)**: Duración del caché por categoría
  - Catálogos: 24 horas
  - Usuarios: 30 minutos
  - Permisos: 15 minutos
  
- **Prefijos de keys**: Para organización en Redis

**Ejemplo futuro**:
```java
@Cacheable(
    value = CacheConstants.DEPARTAMENTOS_CACHE,
    key = "#id"
)
public Departamento findById(Integer id) { ... }
```

---

### 2.3 core/controller - Controladores

#### 2.3.1 RootController.java

**Propósito**: Endpoint raíz de la API para información general y health check.

**Endpoints**:

**GET /**
- Retorna información de bienvenida de la API
- Lista enlaces útiles (Swagger, health, api-docs)
- Muestra endpoints principales disponibles
- **Acceso**: Público (no requiere autenticación)

**GET /health**
- Health check simple del servicio
- Retorna estado UP y timestamp
- **Acceso**: Público

**Uso**: Útil para verificar que el servicio está activo y conocer endpoints disponibles

---

### 2.4 core/entity - Entidades Base

#### 2.4.1 BaseEntity.java

**Propósito**: Clase base abstracta con auditoría y soft delete para todas las entidades del sistema.

**Características**:

**1. Auditoría Automática** (Spring Data JPA Auditing):
- `createdAt`: Fecha/hora de creación (auto)
- `updatedAt`: Fecha/hora de última modificación (auto)
- `createdBy`: ID del usuario creador (auto)
- `updatedBy`: ID del usuario que modificó (auto)

**2. Soft Delete**:
- `deletedAt`: Marca de eliminación lógica
- `@SQLDelete`: Intercepta DELETE y hace UPDATE del campo
- `@Where(clause = "deleted_at IS NULL")`: Filtra automáticamente registros eliminados

**3. Métodos de Utilidad**:
- `isNew()`: Determina si es INSERT o UPDATE
- `isDeleted()`: Verifica si está eliminado
- `softDelete()`: Marca como eliminado
- `restore()`: Restaura un registro eliminado

**Implementación**:
```java
@Entity
@Table(name = "TBL_Usuario")
public class Usuario extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Campos específicos de Usuario...
    
    @Override
    public Integer getId() {
        return id;
    }
}
```

**Beneficios**:
- **DRY**: No repetir código de auditoría en cada entidad
- **Trazabilidad**: Saber quién y cuándo modificó cada registro
- **Recuperación**: Los registros "eliminados" pueden restaurarse
- **Compliance**: Cumple requisitos de auditoría

---

### 2.5 core/exception - Manejo de Excepciones

#### 2.5.1 GlobalExceptionHandler.java

**Propósito**: Manejador centralizado de todas las excepciones de la aplicación.

**Anotación**: `@RestControllerAdvice` - Intercepta excepciones en todos los `@RestController`

**Excepciones Manejadas**:

**1. ResourceNotFoundException (404)**
- Recurso solicitado no existe
- Ejemplo: Usuario con ID inexistente

**2. BusinessException (400)**
- Error en lógica de negocio
- Ejemplo: No se puede eliminar un rol en uso

**3. ValidationException (400)**
- Error en validación personalizada
- Incluye mapa de errores por campo

**4. MethodArgumentNotValidException (400)**
- Errores de `@Valid` en DTOs
- Extrae errores de `BindingResult`

**5. DataIntegrityViolationException (409)**
- Violaciones de unique, foreign keys
- Personaliza mensajes según el tipo

**6. AuthenticationException (401)**
- Credenciales inválidas
- Login fallido

**7. AccessDeniedException (403)**
- Sin permisos para el recurso
- Usuario autenticado pero no autorizado

**8. Exception (500)**
- Cualquier error no contemplado
- Último recurso

**Patrón de respuesta**:
```json
{
  "statusCode": 400,
  "message": "Error descriptivo",
  "body": {
    "timestamp": "2025-10-21T10:30:00",
    "status": 400,
    "error": "BAD_REQUEST",
    "errorCode": "USER_001",
    "message": "El usuario ya existe",
    "errors": {
      "username": "Ya existe un usuario con este nombre"
    },
    "path": "/api/v1/usuarios"
  },
  "timestamp": "2025-10-21T10:30:00"
}
```

---

#### 2.5.2 BusinessException.java

**Propósito**: Excepción para errores de lógica de negocio.

**Uso**:
```java
if (usuarioRepository.existsByUsername(username)) {
    throw new BusinessException(
        ErrorCodes.USER_ALREADY_EXISTS,
        "El usuario " + username + " ya existe"
    );
}
```

**Capturada por**: `GlobalExceptionHandler` → respuesta HTTP 400

---

#### 2.5.3 ResourceNotFoundException.java

**Propósito**: Excepción cuando un recurso no se encuentra.

**Uso**:
```java
Usuario usuario = usuarioRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException(
        ErrorCodes.USER_NOT_FOUND,
        "Usuario no encontrado con ID: " + id
    ));
```

**Capturada por**: `GlobalExceptionHandler` → respuesta HTTP 404

---

#### 2.5.4 ValidationException.java

**Propósito**: Excepción para errores de validación personalizada con detalles por campo.

**Uso**:
```java
Map<String, String> errors = new HashMap<>();
errors.put("dpi", "El DPI no es válido");
errors.put("telefono", "El teléfono debe tener 8 dígitos");

throw new ValidationException(
    ErrorCodes.VALIDATION_ERROR,
    "Errores de validación",
    errors
);
```

**Capturada por**: `GlobalExceptionHandler` → respuesta HTTP 400 con detalles

---

### 2.6 core/response - Respuestas Estandarizadas

#### 2.6.1 ApiResponse.java

**Propósito**: Estructura estándar para TODAS las respuestas de la API.

**Estructura**:
```java
public class ApiResponse<T> {
    private Integer statusCode;
    private String message;
    private T body;              // Datos de la respuesta
    private LocalDateTime timestamp;
}
```

**Factory Methods**:

**Respuestas de Éxito**:
```java
// 200 OK con datos
ApiResponse.ok(data)
ApiResponse.ok("Mensaje personalizado", data)

// 201 CREATED
ApiResponse.created(data)
ApiResponse.created("Recurso creado", data)

// 204 NO CONTENT
ApiResponse.noContent("Eliminado exitosamente")
```

**Respuestas de Error**:
```java
// 400 BAD REQUEST
ApiResponse.badRequest("Datos inválidos")

// 401 UNAUTHORIZED
ApiResponse.unauthorized("Token inválido")

// 403 FORBIDDEN
ApiResponse.forbidden("Sin permisos")

// 404 NOT FOUND
ApiResponse.notFound("Recurso no encontrado")

// 409 CONFLICT
ApiResponse.conflict("El registro ya existe")

// 500 INTERNAL SERVER ERROR
ApiResponse.internalServerError("Error interno")
```

**Ejemplo de uso en Controller**:
```java
@PostMapping
public ResponseEntity<ApiResponse<UsuarioResponseDTO>> crear(
    @Valid @RequestBody UsuarioCreateDTO dto
) {
    UsuarioResponseDTO usuario = usuarioService.crear(dto);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.created("Usuario creado exitosamente", usuario));
}
```

**Beneficios**:
- **Consistencia**: Todas las respuestas tienen la misma estructura
- **Predecibilidad**: El frontend siempre sabe qué esperar
- **Facilita parsing**: Estructura clara para deserializar
- **Incluye metadata**: Timestamp y código de estado en el body

---

#### 2.6.2 PageResponse.java

**Propósito**: Estructura para respuestas paginadas.

**Estructura**:
```java
public class PageResponse<T> {
    private List<T> content;      // Datos de la página actual
    private int currentPage;      // Página actual (0-indexed)
    private int pageSize;         // Tamaño de página
    private long totalElements;   // Total de elementos
    private int totalPages;       // Total de páginas
    private boolean first;        // ¿Es la primera página?
    private boolean last;         // ¿Es la última página?
    private boolean empty;        // ¿Está vacía?
}
```

**Factory Method**:
```java
PageResponse.of(Page<T> page)  // Convierte Spring Page a PageResponse
```

**Ejemplo de uso**:
```java
@GetMapping
public ResponseEntity<ApiResponse<PageResponse<UsuarioListDTO>>> listar(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
) {
    Page<UsuarioListDTO> usuarios = usuarioService.listar(page, size);
    PageResponse<UsuarioListDTO> response = PageResponse.of(usuarios);
    
    return ResponseEntity.ok(
        ApiResponse.ok("Usuarios obtenidos exitosamente", response)
    );
}
```

**Respuesta JSON**:
```json
{
  "statusCode": 200,
  "message": "Usuarios obtenidos exitosamente",
  "body": {
    "content": [ /* array de usuarios */ ],
    "currentPage": 0,
    "pageSize": 10,
    "totalElements": 45,
    "totalPages": 5,
    "first": true,
    "last": false,
    "empty": false
  },
  "timestamp": "2025-10-21T10:30:00"
}
```

---

#### 2.6.3 ErrorResponse.java

**Propósito**: Estructura detallada para respuestas de error.

**Estructura**:
```java
public class ErrorResponse {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;        // Nombre del error (BAD_REQUEST, etc)
    private String errorCode;    // Código interno (USER_001, etc)
    private String message;      // Mensaje descriptivo
    private Map<String, String> errors;  // Errores por campo (opcional)
    private String path;         // Ruta del request
}
```

**Factory Methods**:
```java
// Error simple
ErrorResponse.of(status, errorCode, message, path)

// Error con detalles por campo
ErrorResponse.of(status, errorCode, message, errors, path)
```

**Usado internamente por**: `GlobalExceptionHandler`

---

## 3. Paquete Security

El paquete `security` maneja toda la autenticación y autorización del sistema mediante JWT.

### 3.1 security/jwt - JSON Web Tokens

#### 3.1.1 JwtUtil.java

**Propósito**: Utilidad central para creación, validación y extracción de información de tokens JWT.

**Configuración** (desde `application.yml`):
```yaml
app:
  security:
    jwt:
      secret: ${JWT_SECRET}
      expiration-minutes: 480      # 8 horas
      refresh-expiration-minutes: 10080  # 7 días
```

**Métodos principales**:

**Generación de Tokens**:
```java
// Token de acceso
String generateToken(UserDetails userDetails)

// Token de acceso con claims adicionales
String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)

// Refresh token
String generateRefreshToken(UserDetails userDetails)
```

**Validación**:
```java
// Validar token contra un usuario
Boolean validateToken(String token, UserDetails userDetails)

// Validar solo estructura y firma
Boolean validateToken(String token)
```

**Extracción de Información**:
```java
String extractUsername(String token)
Date extractExpiration(String token)
List<String> extractRoles(String token)
Boolean isRefreshToken(String token)
Long getTokenRemainingValidity(String token)
```

**Estructura de un JWT generado**:
```json
{
  "sub": "admin@prediomijangos.com",
  "roles": ["ROLE_ADMIN", "USUARIOS:READ", "USUARIOS:CREATE"],
  "iat": 1697900000,
  "exp": 1697928800
}
```

**Algoritmo**: HS256 (HMAC with SHA-256)

---

#### 3.1.2 JwtAuthenticationFilter.java

**Propósito**: Filtro que intercepta todas las requests HTTP para validar el token JWT.

**Posición en la cadena**: Antes de `UsernamePasswordAuthenticationFilter`

**Flujo de ejecución**:

1. **Extrae el token** del header `Authorization: Bearer <token>`
2. **Valida el token** con `JwtUtil`
3. **Extrae el username** del token
4. **Carga los detalles del usuario** con `CustomUserDetailsService`
5. **Valida el token contra el usuario** cargado
6. **Establece la autenticación** en el `SecurityContext`
7. **Continúa la cadena** de filtros

**Rutas que omite**:
- Rutas públicas definidas en `SecurityConstants.PUBLIC_ENDPOINTS`
- Swagger UI, API docs, actuator/health
- Endpoints de autenticación (`/api/auth/**`)

**Manejo de errores**:
- Tokens expirados → 401 UNAUTHORIZED
- Tokens inválidos → 401 UNAUTHORIZED
- Tokens malformados → 401 UNAUTHORIZED
- Sin token en rutas protegidas → 401 UNAUTHORIZED

---

#### 3.1.3 JwtAuthenticationEntryPoint.java

**Propósito**: Punto de entrada para errores de autenticación.

**Se ejecuta cuando**:
- Un usuario no autenticado intenta acceder a un recurso protegido
- El token JWT es inválido o ha expirado
- No se proporciona token en una ruta protegida

**Respuesta**:
```json
{
  "statusCode": 401,
  "message": "No autenticado - Token inválido o expirado",
  "timestamp": "2025-10-21T10:30:00"
}
```

**Registra el error** en logs para auditoría

---

### 3.2 security/model - Modelos de Seguridad

#### 3.2.1 CustomUserDetails.java

**Propósito**: Implementación de `UserDetails` de Spring Security con información extendida del usuario.

**Extiende**: `org.springframework.security.core.userdetails.UserDetails`

**Información que contiene**:
```java
- Integer id
- String username
- String email
- String password
- Boolean enabled (activo/inactivo)
- Boolean accountNonExpired
- Boolean accountNonLocked
- Boolean credentialsNonExpired
- Collection<GrantedAuthority> authorities (roles y permisos)
```

**Métodos clave**:
```java
// Obtener ID del usuario (útil para auditoría)
Integer getId()

// Verificar si el usuario está activo
boolean isEnabled()

// Obtener authorities (roles + permisos granulares)
Collection<? extends GrantedAuthority> getAuthorities()
```

**Conversión de roles y permisos**:
- Roles: `ROLE_ADMIN`, `ROLE_VENDEDOR`
- Permisos: `USUARIOS:CREATE`, `VENTAS:READ`

Ambos se almacenan como `SimpleGrantedAuthority` en la colección de authorities.

---

### 3.3 security/service - Servicios de Seguridad

#### 3.3.1 CustomUserDetailsService / CustomUserDetailsServiceImpl

**Propósito**: Cargar detalles del usuario desde la base de datos para Spring Security.

**Interfaz requerida**: `UserDetailsService`

**Método principal**:
```java
UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
```

**Flujo de ejecución**:
1. **Busca el usuario** por username en `UsuarioRepository`
2. **Verifica que exista**, sino lanza `UsernameNotFoundException`
3. **Carga los roles** del usuario desde la relación `Usuario → Rol`
4. **Carga los permisos** desde `Rol → Pagina` (permisos granulares)
5. **Construye las authorities** (roles + permisos)
6. **Crea y retorna** `CustomUserDetails`

**Usado por**:
- `DaoAuthenticationProvider` durante login
- `JwtAuthenticationFilter` durante validación de token

---

#### 3.3.2 AuthenticationService / AuthenticationServiceImpl

**Propósito**: Servicio de alto nivel para autenticación de usuarios.

**Métodos principales**:

**login**:
```java
LoginResponseDTO login(LoginRequestDTO request)
```
- Autentica al usuario con `AuthenticationManager`
- Genera access token y refresh token
- Carga información del usuario y sus roles
- Guarda el refresh token en BD
- Retorna `LoginResponseDTO` con tokens y datos del usuario

**logout**:
```java
void logout(LogoutRequestDTO request)
```
- Invalida el refresh token del usuario
- Elimina el token de la BD
- Registra el logout en logs

**refreshToken**:
```java
LoginResponseDTO refreshToken(RefreshTokenRequestDTO request)
```
- Valida el refresh token
- Verifica que no haya expirado
- Genera nuevo access token
- Opcionalmente rota el refresh token
- Retorna nuevos tokens

**getCurrentUser**:
```java
UserDetails getCurrentUser()
```
- Obtiene el usuario autenticado del `SecurityContext`
- Usado para operaciones que requieren el usuario actual

---

#### 3.3.3 RefreshTokenService / RefreshTokenServiceImpl

**Propósito**: Gestión de refresh tokens para renovación de acceso.

**Métodos principales**:

**createRefreshToken**:
```java
RefreshToken createRefreshToken(Integer usuarioId)
```
- Genera un token UUID único
- Calcula fecha de expiración (7 días)
- Guarda en BD asociado al usuario
- Retorna entidad `RefreshToken`

**validateRefreshToken**:
```java
RefreshToken validateRefreshToken(String token)
```
- Busca el token en BD
- Verifica que no haya expirado
- Si expiró, lo elimina de BD
- Retorna el `RefreshToken` válido

**deleteByUsuario**:
```java
void deleteByUsuario(Integer usuarioId)
```
- Elimina todos los refresh tokens de un usuario
- Usado durante logout

**Beneficios de Refresh Tokens**:
- Access tokens de corta duración (8 horas)
- Refresh tokens de larga duración (7 días)
- Mayor seguridad: si un access token es robado, expira pronto
- Revocación: se puede invalidar el refresh token

---

### 3.4 security/dto - DTOs de Autenticación

#### 3.4.1 LoginRequestDTO

**Propósito**: Request para login de usuario.

**Campos**:
```java
@NotBlank String username
@NotBlank String password
```

---

#### 3.4.2 LoginResponseDTO

**Propósito**: Response exitoso de login.

**Estructura**:
```java
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer",
  "expiresIn": 28800,  // segundos
  "usuario": {
    "id": 1,
    "username": "admin",
    "email": "admin@prediomijangos.com",
    "roles": ["ROLE_ADMIN"]
  }
}
```

**UsuarioInfoDTO anidado**:
- ID, username, email, nombre completo
- Lista de roles
- Estado (activo/inactivo)

---

#### 3.4.3 RefreshTokenRequestDTO

**Propósito**: Request para renovar access token.

**Campos**:
```java
@NotBlank String refreshToken
```

---

#### 3.4.4 LogoutRequestDTO

**Propósito**: Request para logout de usuario.

**Campos**:
```java
@NotBlank String refreshToken
```

---

## 4. Paquete Modules

El paquete `modules` contiene todos los módulos de negocio del sistema, organizados por dominio.

### 4.1 Estructura General de un Módulo

Todos los módulos siguen la misma estructura de capas:

```
modules/[nombre-modulo]/
├── domain/           # Entidades JPA
├── dto/             # Data Transfer Objects
├── mapper/          # MapStruct mappers
├── repository/      # Spring Data JPA repositories
├── service/         # Interfaces de servicio
│   └── impl/       # Implementaciones de servicio
└── controller/      # REST Controllers
```

### 4.2 Módulo: auth (Autenticación)

**Propósito**: Endpoints públicos para autenticación de usuarios.

#### 4.2.1 AuthController

**Endpoints**:

**POST /api/v1/auth/login**
- Autentica usuario con credenciales
- Retorna access token y refresh token
- **Body**: `LoginRequestDTO`
- **Response**: `LoginResponseDTO`

**POST /api/v1/auth/refresh**
- Renueva access token con refresh token
- **Body**: `RefreshTokenRequestDTO`
- **Response**: `LoginResponseDTO`

**POST /api/v1/auth/logout**
- Cierra sesión e invalida refresh token
- **Body**: `LogoutRequestDTO`
- **Response**: Mensaje de éxito

**Seguridad**: Todos los endpoints son públicos (no requieren autenticación)

---

### 4.3 Módulo: geo (Geográfico)

**Propósito**: Catálogos geográficos de Guatemala (departamentos y municipios).

#### 4.3.1 Entidades

**Departamento**:
```java
- Integer id
- String codigo  (GT-01, GT-02, etc)
- String nombre
```

**Municipio**:
```java
- Integer id
- Departamento departamento
- String codigo
- String nombre
```

#### 4.3.2 Controllers

**DepartamentoController**:
- `GET /api/v1/departamentos` - Lista todos los departamentos
- `GET /api/v1/departamentos/{id}` - Obtiene uno por ID

**MunicipioController**:
- `GET /api/v1/municipios` - Lista todos los municipios
- `GET /api/v1/municipios/{id}` - Obtiene uno por ID
- `GET /api/v1/municipios/departamento/{deptoId}` - Municipios por departamento

**Seguridad**: Endpoints públicos (datos de catálogo)

**Características**:
- Datos precargados con Flyway migration
- Solo lectura (no hay CREATE/UPDATE/DELETE)
- Usado por módulo de Personas para dirección

---

### 4.4 Módulo: usuarios (Gestión de Usuarios)

**Propósito**: CRUD completo de usuarios, clientes, personas, roles, módulos y páginas.

#### 4.4.1 Entidades

**Persona** (Entidad compartida):
```java
- Integer id
- String primerNombre, segundoNombre
- String primerApellido, segundoApellido
- String dpi
- String nit
- String telefono
- String email
- LocalDate fechaNacimiento
- String genero (MASCULINO/FEMENINO/OTRO)
- String direccion
- Municipio municipio
```
Base para Usuario y Cliente.

**Usuario**:
```java
- Integer id
- Persona persona
- String username (único)
- String password (encriptado)
- Boolean activo
- Set<Rol> roles
```

**Cliente**:
```java
- Integer id
- Persona persona
- String codigoCliente (único)
- TipoCliente tipoCliente (INDIVIDUAL/EMPRESA)
- Boolean creditoHabilitado
- BigDecimal limiteCredito
```

**Rol**:
```java
- Integer id
- String codigo (ROLE_ADMIN, ROLE_VENDEDOR)
- String nombre
- String descripcion
- Boolean activo
- Set<Pagina> paginas (permisos)
```

**Modulo**:
```java
- Integer id
- String nombre (Usuarios, Ventas, Inventario)
- String descripcion
- String icono
- Integer orden
- Boolean activo
```

**Pagina**:
```java
- Integer id
- Modulo modulo
- String nombre (Listado de Productos, Crear Venta)
- Boolean movil
- String iconWeb, iconMovil
- String redirectWeb, redirectMovil
```

#### 4.4.2 DTOs Organizados

**persona/**:
- `PersonaCreateDTO`, `PersonaUpdateDTO`
- `PersonaResponseDTO`, `PersonaListDTO`

**usuario/**:
- `UsuarioCreateDTO`, `UsuarioUpdateDTO`
- `UsuarioResponseDTO`, `UsuarioListDTO`
- `UsuarioRolesDTO`, `UsuarioEstadoDTO`
- `CambioPasswordDTO`

**cliente/**:
- `ClienteCreateDTO`, `ClienteUpdateDTO`
- `ClienteResponseDTO`, `ClienteListDTO`

**rol/**:
- `RolCreateDTO`, `RolUpdateDTO`
- `RolResponseDTO`, `RolSimpleDTO`
- `RolPaginasDTO`, `RolEstadoDTO`
- `PaginaSimpleDTO`

**modulo/**:
- `ModuloResponseDTO`, `ModuloSimpleDTO`

**pagina/**:
- `PaginaCreateDTO`, `PaginaUpdateDTO`
- `PaginaResponseDTO`
- `MenuModuloDTO`, `MenuPaginaDTO`

#### 4.4.3 Mappers (MapStruct)

- `PersonaMapper`
- `UsuarioMapper`
- `ClienteMapper`
- `RolMapper`
- `ModuloMapper`
- `PaginaMapper`

**Configuración**:
```java
@Mapper(componentModel = "spring")
```

**Ignoran campos de auditoría** (manejados por JPA Auditing):
```java
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
@Mapping(target = "createdBy", ignore = true)
@Mapping(target = "updatedBy", ignore = true)
@Mapping(target = "deletedAt", ignore = true)
```

#### 4.4.4 Services

**PersonaService**:
- CRUD completo de personas
- Validación de DPI y NIT únicos
- Búsqueda por múltiples criterios

**UsuarioService**:
- CRUD de usuarios
- Asignación de roles
- Cambio de contraseña
- Activar/Desactivar usuario
- Coordina con `PersonaService` para crear persona primero

**ClienteService**:
- CRUD de clientes
- Gestión de crédito
- Coordina con `PersonaService`

**RolService**:
- CRUD de roles
- Asignación de páginas (permisos)
- Activar/Desactivar rol

**ModuloService**:
- Listado de módulos del sistema
- Solo lectura (módulos fijos)

**PaginaService**:
- CRUD de páginas
- Generación de menú dinámico por usuario
- Filtra por permisos del usuario

#### 4.4.5 Controllers

**UsuarioController** - `/api/v1/usuarios`:
- `POST /` - Crear usuario
- `GET /` - Listar paginado
- `GET /{id}` - Obtener por ID
- `PUT /{id}` - Actualizar usuario
- `DELETE /{id}` - Eliminar (soft delete)
- `PATCH /{id}/estado` - Activar/Desactivar
- `PATCH /{id}/roles` - Asignar roles
- `PATCH /{id}/password` - Cambiar contraseña

**ClienteController** - `/api/v1/clientes`:
- CRUD completo similar a usuarios
- Endpoints adicionales para crédito

**PersonaController** - `/api/v1/personas`:
- CRUD completo
- Búsquedas por DPI, NIT, nombre

**RolController** - `/api/v1/roles`:
- CRUD completo
- `GET /{id}/paginas` - Obtener páginas del rol
- `PATCH /{id}/paginas` - Asignar páginas
- `PATCH /{id}/estado` - Activar/Desactivar

**ModuloController** - `/api/v1/modulos`:
- `GET /` - Listar módulos activos
- `GET /{id}` - Obtener módulo

**PaginaController** - `/api/v1/paginas`:
- CRUD completo
- `GET /menu` - Menú dinámico del usuario autenticado
- `GET /modulo/{moduloId}` - Páginas por módulo

**Seguridad**:
- Todos requieren autenticación (`@PreAuthorize`)
- Permisos granulares por endpoint:
  ```java
  @PreAuthorize("hasAuthority('USUARIOS:CREATE')")
  @PostMapping
  public ResponseEntity<ApiResponse<UsuarioResponseDTO>> crear(...)
  ```

---

### 4.5 Módulos Pendientes (Diseño Futuro)

#### 4.5.1 compras
- Proveedores
- Órdenes de compra
- Recepción de mercadería

#### 4.5.2 ventas
- Ventas (contado/crédito)
- Cotizaciones
- Facturas
- Pagos

#### 4.5.3 inventario
- Productos
- Bodegas
- Traslados
- Ajustes de inventario
- Kardex

#### 4.5.4 vehiculos
- Vehículos en stock
- Historial de propietarios
- Inspecciones

#### 4.5.5 reportes
- Reportes Excel
- Reportes PDF
- Dashboards
- Exportación de datos

---

## 5. Flujos de Datos Principales

### 5.1 Flujo de Autenticación (Login)

```
1. Usuario → POST /api/v1/auth/login
   Body: { username, password }

2. AuthController → AuthenticationService.login()

3. AuthenticationService:
   a. AuthenticationManager.authenticate()
      - DaoAuthenticationProvider
      - CustomUserDetailsService.loadUserByUsername()
      - Valida password con BCryptPasswordEncoder
   
   b. JwtUtil.generateToken(userDetails)
      - Crea JWT con username y roles
      - Expira en 8 horas
   
   c. JwtUtil.generateRefreshToken(userDetails)
      - Crea token UUID
      - Expira en 7 días
   
   d. RefreshTokenService.createRefreshToken(usuarioId)
      - Guarda en BD
   
   e. Construye LoginResponseDTO con:
      - accessToken
      - refreshToken
      - usuario { id, username, email, roles }

4. AuthController → ApiResponse.ok(response)

5. Usuario recibe tokens
```

### 5.2 Flujo de Request Autenticado

```
1. Usuario → GET /api/v1/usuarios
   Headers: Authorization: Bearer <accessToken>

2. JwtAuthenticationFilter:
   a. Extrae token del header
   b. Valida firma con JwtUtil
   c. Extrae username del token
   d. Carga UserDetails con CustomUserDetailsService
   e. Valida token contra UserDetails
   f. Establece Authentication en SecurityContext

3. SecurityConfig verifica permisos:
   @PreAuthorize("hasAuthority('USUARIOS:READ')")

4. Si autorizado → UsuarioController.listar()
   a. UsuarioService.listar(page, size)
   b. UsuarioRepository.findAll(pageable)
   c. UsuarioMapper.toListDTO(usuarios)
   d. PageResponse.of(page)

5. ApiResponse.ok(data)

6. Usuario recibe respuesta JSON
```

### 5.3 Flujo de Creación de Usuario

```
1. Cliente → POST /api/v1/usuarios
   Headers: Authorization: Bearer <token>
   Body: UsuarioCreateDTO {
     persona: { primerNombre, ... },
     username,
     password,
     roles: [roleIds]
   }

2. Spring Validation:
   @Valid UsuarioCreateDTO
   - Valida @NotBlank, @Email, @Pattern, etc
   - Si falla → MethodArgumentNotValidException
   - GlobalExceptionHandler → 400 BAD REQUEST

3. UsuarioController.crear():
   @PreAuthorize("hasAuthority('USUARIOS:CREATE')")

4. UsuarioService.crear(dto):
   a. Validar que username no exista
      - Si existe → BusinessException
   
   b. PersonaService.crear(dto.persona)
      - Validar DPI/NIT únicos
      - PersonaMapper.toEntity()
      - PersonaRepository.save()
      - Auditoría automática (createdAt, createdBy)
   
   c. UsuarioMapper.toEntity(dto)
      - Asocia persona creada
      - Encripta password con BCryptPasswordEncoder
   
   d. RolRepository.findAllById(dto.roleIds)
      - Carga roles
      - Asocia al usuario
   
   e. UsuarioRepository.save(usuario)
      - INSERT en TBL_Usuario
      - INSERT en TBL_Usuario_Rol (relación many-to-many)
      - Auditoría automática
   
   f. UsuarioMapper.toResponseDTO(usuario)

5. ApiResponse.created(response)

6. Cliente recibe 201 CREATED con datos del usuario
```

### 5.4 Flujo de Manejo de Excepciones

```
1. Cualquier Controller lanza excepción

2. GlobalExceptionHandler intercepta:
   @ExceptionHandler(XxxException.class)

3. Determina tipo de excepción:
   - BusinessException → 400
   - ResourceNotFoundException → 404
   - ValidationException → 400
   - DataIntegrityViolationException → 409
   - AuthenticationException → 401
   - AccessDeniedException → 403
   - Exception → 500

4. Construye ErrorResponse:
   {
     timestamp, status, error,
     errorCode, message, errors, path
   }

5. ApiResponse.error(status, message, errorResponse)

6. Logs del error

7. Cliente recibe respuesta de error estructurada
```

---

## 6. Diagrama de Arquitectura

### 6.1 Vista de Capas

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENT (Web/Mobile)                      │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   CONTROLLER LAYER                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │   Auth   │  │ Usuario  │  │ Cliente  │  │   Rol    │   │
│  │Controller│  │Controller│  │Controller│  │Controller│   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
│                     │                                        │
│              Validación DTOs                                 │
│              Security (@PreAuthorize)                        │
│              ApiResponse wrapping                            │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │   Auth   │  │ Usuario  │  │ Cliente  │  │   Rol    │   │
│  │ Service  │  │ Service  │  │ Service  │  │ Service  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
│                     │                                        │
│              Lógica de Negocio                               │
│              Transacciones (@Transactional)                  │
│              Orquestación entre servicios                    │
│              Mapeo DTO ↔ Entity (MapStruct)                 │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   REPOSITORY LAYER                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Usuario  │  │ Cliente  │  │   Rol    │  │  Pagina  │   │
│  │Repository│  │Repository│  │Repository│  │Repository│   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
│                     │                                        │
│              Spring Data JPA                                 │
│              Query Methods                                   │
│              Specifications (filtros dinámicos)              │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Usuario  │  │ Cliente  │  │   Rol    │  │  Pagina  │   │
│  │ (Entity) │  │ (Entity) │  │ (Entity) │  │ (Entity) │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
│                     │                                        │
│              Extienden BaseEntity                            │
│              JPA Annotations                                 │
│              Auditoría automática                            │
│              Soft Delete                                     │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      DATABASE (MySQL)                        │
│   TBL_Usuario, TBL_Cliente, TBL_Rol, TBL_Pagina, etc       │
└─────────────────────────────────────────────────────────────┘
```

### 6.2 Vista de Seguridad

```
┌─────────────────────────────────────────────────────────────┐
│                        REQUEST                               │
│          GET /api/v1/usuarios                                │
│          Header: Authorization: Bearer <JWT>                 │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│              JwtAuthenticationFilter                         │
│  1. Extrae token del header                                  │
│  2. Valida con JwtUtil                                       │
│  3. Carga UserDetails                                        │
│  4. Establece Authentication en SecurityContext              │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  SecurityConfig                              │
│  1. Verifica si la ruta requiere autenticación              │
│  2. Si sí, verifica que haya Authentication                  │
│  3. Si no → JwtAuthenticationEntryPoint (401)               │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     Controller                               │
│  @PreAuthorize("hasAuthority('USUARIOS:READ')")            │
│  1. Spring Security evalúa expresión                         │
│  2. Verifica authorities del usuario autenticado             │
│  3. Si no tiene permiso → AccessDeniedException (403)       │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   Service ejecuta                            │
│           AuditorAwareConfig provee usuarioId                │
│           para campos createdBy/updatedBy                    │
└─────────────────────────────────────────────────────────────┘
```

### 6.3 Vista de Módulos

```
┌─────────────────────────────────────────────────────────────┐
│                          CORE                                │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │  Config  │  │Constants │  │Exception │  │Response  │   │
│  │          │  │          │  │          │  │          │   │
│  │ Security │  │ Security │  │ Global   │  │  Api     │   │
│  │ Swagger  │  │ Error    │  │ Handler  │  │ Response │   │
│  │ Auditor  │  │Validation│  │ Custom   │  │  Page    │   │
│  │          │  │  Cache   │  │Exceptions│  │  Error   │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
│                                                              │
│  ┌──────────┐  ┌──────────┐                                │
│  │  Entity  │  │Controller│                                │
│  │          │  │          │                                │
│  │   Base   │  │   Root   │                                │
│  │  Entity  │  │          │                                │
│  └──────────┘  └──────────┘                                │
└─────────────────────────────────────────────────────────────┘
                              │
                ┌─────────────┴─────────────┐
                ▼                           ▼
┌──────────────────────────┐  ┌──────────────────────────┐
│        SECURITY          │  │        MODULES           │
│  ┌────────────────┐     │  │  ┌────────────────┐     │
│  │      JWT       │     │  │  │      auth      │     │
│  │  - JwtUtil     │     │  │  │  - AuthCtrl    │     │
│  │  - Filter      │     │  │  └────────────────┘     │
│  │  - EntryPoint  │     │  │                          │
│  └────────────────┘     │  │  ┌────────────────┐     │
│                          │  │  │      geo       │     │
│  ┌────────────────┐     │  │  │  - Depto       │     │
│  │     Model      │     │  │  │  - Municipio   │     │
│  │  - Custom      │     │  │  └────────────────┘     │
│  │    UserDetails │     │  │                          │
│  └────────────────┘     │  │  ┌────────────────┐     │
│                          │  │  │    usuarios    │     │
│  ┌────────────────┐     │  │  │  - Usuario     │     │
│  │    Service     │     │  │  │  - Cliente     │     │
│  │  - Auth        │     │  │  │  - Persona     │     │
│  │  - UserDetails │     │  │  │  - Rol         │     │
│  │  - RefreshToken│     │  │  │  - Modulo      │     │
│  └────────────────┘     │  │  │  - Pagina      │     │
│                          │  │  └────────────────┘     │
│  ┌────────────────┐     │  │                          │
│  │      DTO       │     │  │  (Futuros módulos:)      │
│  │  - Login       │     │  │  - compras               │
│  │  - Refresh     │     │  │  - ventas                │
│  │  - Logout      │     │  │  - inventario            │
│  └────────────────┘     │  │  - vehiculos             │
└──────────────────────────┘  │  - reportes              │
                              └──────────────────────────┘
```

---

## 7. Consideraciones Finales

### 7.1 Ventajas de esta Arquitectura

**Mantenibilidad**:
- Separación clara de responsabilidades
- Código organizado por dominio
- Fácil de navegar y entender

**Escalabilidad**:
- Módulos independientes
- Preparado para migración a microservicios
- Fácil agregar nuevos módulos

**Testabilidad**:
- Capas desacopladas
- Interfaces bien definidas
- Fácil hacer mocking

**Reutilización**:
- Core compartido por todos los módulos
- DTOs, excepciones, respuestas estandarizadas
- Mappers automáticos con MapStruct

**Seguridad**:
- JWT stateless
- Permisos granulares
- Auditoría completa
- Soft delete para compliance

### 7.2 Mejores Prácticas Implementadas

- ✅ SOLID principles
- ✅ Clean Code
- ✅ RESTful API design
- ✅ DTO pattern (no exponer entidades)
- ✅ Repository pattern
- ✅ Dependency Injection
- ✅ Exception handling centralizado
- ✅ Validation automática
- ✅ Documentation (JavaDoc + Swagger)
- ✅ Logging estructurado
- ✅ Auditoría automática
- ✅ Soft delete

### 7.3 Próximas Mejoras (Roadmap)

**Fase 2**:
- [ ] Caché con Redis
- [ ] Rate limiting
- [ ] WebSockets para notificaciones
- [ ] Spring Events para desacoplamiento

**Fase 3**:
- [ ] Elasticsearch para búsquedas avanzadas
- [ ] AWS S3 para almacenamiento de archivos
- [ ] Message Queue (RabbitMQ/SQS)
- [ ] Scheduled tasks con Quartz

---

**Documento generado el:** 21 de Octubre 2025  
**Versión del sistema:** 1.0.0  
**Mantenido por:** Equipo Técnico Predio Mijangos
