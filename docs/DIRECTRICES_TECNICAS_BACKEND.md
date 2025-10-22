# Directrices Técnicas y Políticas de Desarrollo Backend

**Sistema de Gestión de Inventario y Ventas - Predio Mijangos**

**Versión:** 2.0.0  
**Última Actualización:** 21 de Octubre 2025  
**Autor:** Equipo Técnico Predio Mijangos

---

## 📋 Índice

1. [Información del Proyecto](#1-información-del-proyecto)
2. [Stack Tecnológico](#2-stack-tecnológico)
3. [Arquitectura General](#3-arquitectura-general)
4. [Estándares de Base de Datos](#4-estándares-de-base-de-datos)
5. [Estructura del Proyecto](#5-estructura-del-proyecto)
6. [Documentación de Arquitectura](#6-documentación-de-arquitectura)
7. [Patrones de Código](#7-patrones-de-código)
8. [Naming Conventions](#8-naming-conventions)
9. [Seguridad y Autenticación](#9-seguridad-y-autenticación)
10. [APIs y Contratos](#10-apis-y-contratos)
11. [Manejo de Errores](#11-manejo-de-errores)
12. [Testing](#12-testing)
13. [Configuración y Despliegue](#13-configuración-y-despliegue)
14. [Checklist por Módulo Nuevo](#14-checklist-por-módulo-nuevo)
15. [Recursos y Referencias](#15-recursos-y-referencias)
16. [Changelog del Documento](#16-changelog-del-documento)

---

## 1. Información del Proyecto

### 1.1 Descripción

Sistema de gestión integral para inventario de repuestos automotrices y ventas, con módulos web para administración y aplicaciones móviles para vendedores y personal operativo.

### 1.2 Objetivos Principales

- Gestión eficiente de inventario multi-bodega
- Control de ventas con crédito y contado
- Trazabilidad completa de operaciones
- Reportería en tiempo real
- Soporte para operaciones en zonas rurales (offline-first móvil)

### 1.3 Roles del Sistema

- **ADMIN**: Acceso total al sistema
- **SUPERVISOR**: Aprobaciones y supervisión
- **VENDEDOR**: Gestión de ventas y clientes (móvil)
- **BODEGUERO**: Gestión de inventario (móvil)
- **CONTADOR**: Reportes y análisis financiero
- **OPERADOR**: Operaciones generales (oficina)

---

## 2. Stack Tecnológico

### 2.1 Backend

```yaml
Lenguaje: Java 17 LTS
Framework: Spring Boot 3.2.x
Persistencia: Spring Data JPA + Hibernate
Base de Datos: MySQL 8.0
Migrations: Flyway
Security: Spring Security + JWT
Validation: Jakarta Bean Validation
Mapping: MapStruct 1.5.x
Documentation: SpringDoc OpenAPI 3.0 (Swagger)
Testing: JUnit 5, Mockito, Testcontainers
Build: Maven 3.9.x
```

### 2.2 Infraestructura (AWS)

- **Compute**: EC2 (t3.medium) con Auto Scaling
- **Database**: RDS MySQL Multi-AZ
- **Storage**: S3 (documentos, imágenes)
- **CDN**: CloudFront
- **Monitoring**: CloudWatch
- **Cache**: ElastiCache Redis (FASE 2)

---

## 3. Arquitectura General

### 3.1 Patrón Arquitectónico

**Monolito Modular** con separación por dominios (DDD simplificado)

**Justificación**:
- Equipo pequeño
- Facilita desarrollo inicial
- Menos complejidad operativa
- Posible migración a microservicios si escala

### 3.2 Estructura de Capas por Módulo

```
┌─────────────────────────────────┐
│     CONTROLLER LAYER            │ ← REST Endpoints
│     (DTOs, Validaciones)        │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│      SERVICE LAYER              │ ← Lógica de Negocio
│  (Transacciones, Orquestación)  │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│     REPOSITORY LAYER            │ ← Acceso a Datos
│   (JPA, Specifications)         │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│      DOMAIN LAYER               │ ← Entidades
│  (Entities, Value Objects)      │
└─────────────────────────────────┘
```

### 3.3 Principios SOLID Aplicados

- **Single Responsibility**: Un servicio = un agregado de negocio
- **Open/Closed**: Uso de interfaces y especificaciones
- **Liskov Substitution**: DTOs vs Entities separados
- **Interface Segregation**: Repositorios específicos
- **Dependency Inversion**: Inyección de dependencias

---

## 4. Estándares de Base de Datos

### 4.1 Naming Conventions - REGLAS DEFINITIVAS

**Tablas**:
```sql
-- Prefijo TBL_ para tablas principales
TBL_Usuario
TBL_Cliente
TBL_Producto

-- Prefijo TBL_Cat_ para catálogos
TBL_Cat_Estado_Venta
TBL_Cat_Tipo_Cliente

-- Tablas de relación Many-to-Many
TBL_Usuario_Rol
TBL_Producto_Bodega
```

**Columnas**:
```sql
-- snake_case
id, nombre, primer_nombre, fecha_nacimiento

-- Foreign Keys: id_[tabla] o id_[entidad]
id_usuario, id_cliente, id_producto

-- Booleanos: prefijo is_ o habilitado/activo
is_activo, habilitado, permite_edicion

-- Fechas: sufijo _at o fecha_
created_at, updated_at, deleted_at
fecha_nacimiento, fecha_venta
```

### 4.2 Campos de Auditoría (OBLIGATORIOS)

```sql
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
created_by INT NULL,
updated_by INT NULL,
deleted_at DATETIME NULL,  -- Soft delete

CONSTRAINT fk_created_by FOREIGN KEY (created_by) REFERENCES TBL_Usuario(id),
CONSTRAINT fk_updated_by FOREIGN KEY (updated_by) REFERENCES TBL_Usuario(id)
```

### 4.3 Índices

```sql
-- Unique constraints
UNIQUE KEY uk_usuario_username (username),
UNIQUE KEY uk_persona_dpi (dpi),

-- Índices de búsqueda frecuente
INDEX idx_usuario_email (email),
INDEX idx_venta_fecha (fecha_venta),

-- Índices compuestos
INDEX idx_producto_codigo_bodega (codigo_producto, id_bodega)
```

### 4.4 ENUM vs Catálogo

**Usar ENUM cuando**:
- Valores estables (no cambian)
- Pocas opciones (< 10)
- No requieren descripción extendida

```sql
CREATE TABLE TBL_Producto (
    estado ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO'
);
```

**Usar CATÁLOGO cuando**:
- Valores pueden crecer
- Necesitan descripción/metadatos
- Requieren reportes por estado

```sql
CREATE TABLE TBL_Cat_Estado_Venta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(20) UNIQUE NOT NULL,  -- 'PENDIENTE', 'PAGADA'
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200),
    permite_edicion BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    orden INT
);
```

---

## 5. Estructura del Proyecto

### 5.1 Estructura de Directorios

```
src/main/java/com/predio/mijangos/
├── core/                           # Configuraciones y utilidades globales
│   ├── config/
│   │   ├── SecurityConfig.java     # JWT, CORS, autenticación
│   │   ├── SwaggerConfig.java      # OpenAPI/Swagger
│   │   └── AuditorAwareConfig.java # Auditoría JPA
│   │
│   ├── constants/
│   │   ├── SecurityConstants.java  # Roles, permisos, JWT
│   │   ├── ErrorCodes.java         # Códigos de error
│   │   ├── ValidationConstants.java# Patrones regex, validaciones
│   │   └── CacheConstants.java     # Configuración de caché
│   │
│   ├── response/
│   │   ├── ApiResponse.java        # Respuesta estándar API
│   │   ├── PageResponse.java       # Respuesta paginada
│   │   └── ErrorResponse.java      # Respuesta de error
│   │
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java  # Manejador global
│   │   ├── BusinessException.java       # Excepciones de negocio
│   │   ├── ResourceNotFoundException.java
│   │   └── ValidationException.java
│   │
│   ├── entity/
│   │   └── BaseEntity.java         # Entidad base (auditoría + soft delete)
│   │
│   └── controller/
│       └── RootController.java     # Endpoint raíz de la API
│
├── security/                       # Autenticación y autorización
│   ├── jwt/
│   │   ├── JwtUtil.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtAuthenticationEntryPoint.java
│   │
│   ├── model/
│   │   └── CustomUserDetails.java
│   │
│   ├── service/
│   │   ├── AuthenticationService.java
│   │   ├── CustomUserDetailsService.java
│   │   ├── RefreshTokenService.java
│   │   └── impl/
│   │       ├── AuthenticationServiceImpl.java
│   │       ├── CustomUserDetailsServiceImpl.java
│   │       └── RefreshTokenServiceImpl.java
│   │
│   └── dto/
│       ├── LoginRequestDTO.java
│       ├── LoginResponseDTO.java
│       ├── RefreshTokenRequestDTO.java
│       └── LogoutRequestDTO.java
│
└── modules/                        # Módulos de negocio
    ├── auth/                       # Endpoints públicos de autenticación
    │   └── controller/
    │       └── AuthController.java
    │
    ├── geo/                        # Catálogos geográficos (departamentos, municipios)
    │   ├── domain/
    │   ├── dto/
    │   ├── mapper/
    │   ├── repository/
    │   ├── service/impl/
    │   └── controller/
    │
    ├── usuarios/                   # Gestión de usuarios, roles, permisos
    │   ├── domain/
    │   │   ├── Usuario.java
    │   │   ├── Cliente.java
    │   │   ├── Persona.java
    │   │   ├── Rol.java
    │   │   ├── Modulo.java
    │   │   ├── Pagina.java
    │   │   └── RefreshToken.java
    │   │
    │   ├── dto/
    │   │   ├── usuario/
    │   │   ├── cliente/
    │   │   ├── persona/
    │   │   ├── rol/
    │   │   ├── modulo/
    │   │   └── pagina/
    │   │
    │   ├── mapper/
    │   ├── repository/
    │   ├── service/impl/
    │   └── controller/
    │
    ├── compras/                    # (FUTURO) Módulo de compras y proveedores
    ├── ventas/                     # (FUTURO) Módulo de ventas
    ├── inventario/                 # (FUTURO) Módulo de inventario
    ├── vehiculos/                  # (FUTURO) Módulo de vehículos
    └── reportes/                   # (FUTURO) Módulo de reportería
```

### 5.2 Recursos (src/main/resources)

```
src/main/resources/
├── application.yml                 # Config principal
├── application-dev.yml             # Desarrollo
├── application-test.yml            # Testing
├── application-prod.yml            # Producción
│
├── db/migration/                   # Flyway migrations
│   ├── V1__initial_schema.sql     # Esquema inicial + FKs
│   ├── V2__add_audit_fields.sql   # Campos de auditoría
│   ├── V3__seed_geo_data.sql      # Departamentos/Municipios
│   ├── V4__create_indexes.sql     # Índices de performance
│   └── V5__seed_users.sql         # Usuarios iniciales
│
├── messages/                       # Internacionalización
│   ├── messages.properties
│   └── messages_es.properties
│
└── logback-spring.xml              # Configuración de logs
```

---

## 6. Documentación de Arquitectura

### 6.1 Documento Maestro de Arquitectura

**Ubicación**: `docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md`

Este documento contiene:
- Explicación detallada de cada paquete (core, security, modules)
- Descripción de cada clase y su responsabilidad
- Flujos de datos principales
- Diagramas de arquitectura
- Mejores prácticas implementadas

### 6.2 Paquete `core` - Funcionalidad Transversal

El paquete `core` contiene toda la funcionalidad compartida:

**config/**: Configuraciones de Spring Boot
- `SecurityConfig`: Spring Security + JWT + CORS
- `SwaggerConfig`: Documentación OpenAPI 3.0
- `AuditorAwareConfig`: Auditoría automática de usuarios

**constants/**: Valores constantes centralizados
- `SecurityConstants`: Roles, permisos, JWT
- `ErrorCodes`: Códigos de error estandarizados
- `ValidationConstants`: Patrones regex, longitudes, mensajes
- `CacheConstants`: Configuración de caché (Redis - Fase 2)

**response/**: Respuestas estandarizadas
- `ApiResponse<T>`: Estructura estándar para todas las respuestas
- `PageResponse<T>`: Respuestas paginadas
- `ErrorResponse`: Detalles de errores

**exception/**: Manejo centralizado de excepciones
- `GlobalExceptionHandler`: Intercepta todas las excepciones
- `BusinessException`: Errores de lógica de negocio
- `ResourceNotFoundException`: Recurso no encontrado
- `ValidationException`: Errores de validación con detalles

**entity/**: Entidad base
- `BaseEntity`: Auditoría automática + soft delete
  - `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `deletedAt`
  - Métodos: `isNew()`, `isDeleted()`, `softDelete()`, `restore()`

**controller/**: Controladores transversales
- `RootController`: Endpoint raíz (`/api/`) con información de la API

### 6.3 Paquete `security` - Autenticación y Autorización

**jwt/**: Manejo de JSON Web Tokens
- `JwtUtil`: Generación, validación y extracción de tokens
  - Access tokens: 8 horas
  - Refresh tokens: 7 días
  - Algoritmo: HS256
- `JwtAuthenticationFilter`: Intercepta requests HTTP
  - Extrae y valida tokens
  - Establece autenticación en SecurityContext
- `JwtAuthenticationEntryPoint`: Maneja errores de autenticación

**model/**: Modelos de seguridad
- `CustomUserDetails`: Implementación de `UserDetails`
  - Contiene ID, username, email, roles, permisos

**service/**: Servicios de autenticación
- `AuthenticationService`: Login, logout, refresh token
- `CustomUserDetailsService`: Carga usuarios desde BD
- `RefreshTokenService`: Gestión de refresh tokens

**dto/**: DTOs de autenticación
- `LoginRequestDTO`, `LoginResponseDTO`
- `RefreshTokenRequestDTO`, `LogoutRequestDTO`

### 6.4 Paquete `modules` - Módulos de Negocio

Cada módulo sigue la estructura estándar:

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

**Módulos implementados**:
- **auth**: Endpoints públicos de autenticación
- **geo**: Catálogos geográficos (Guatemala)
- **usuarios**: Gestión completa de usuarios, clientes, personas, roles, permisos

**Módulos futuros**:
- **compras**: Proveedores y órdenes de compra
- **ventas**: Ventas, cotizaciones, facturas
- **inventario**: Productos, bodegas, traslados
- **vehiculos**: Vehículos en stock
- **reportes**: Excel, PDF, dashboards

### 6.5 Flujos de Datos Clave

**Login**:
1. POST /api/v1/auth/login
2. AuthenticationService valida credenciales
3. Genera access token + refresh token
4. Retorna tokens + datos del usuario

**Request Autenticado**:
1. Cliente envía token en header `Authorization: Bearer <token>`
2. JwtAuthenticationFilter valida token
3. Establece autenticación en SecurityContext
4. Controller verifica permisos con `@PreAuthorize`
5. Service ejecuta lógica de negocio
6. Repository accede a BD
7. Respuesta envuelta en `ApiResponse<T>`

**Creación de Usuario**:
1. POST /api/v1/usuarios
2. Validación automática de DTOs
3. UsuarioService.crear():
   - Valida unicidad de username
   - PersonaService.crear() (coordina)
   - Encripta password
   - Asigna roles
   - Guarda en BD con auditoría
4. Retorna UsuarioResponseDTO

---

## 7. Patrones de Código

### 7.1 Entidad Base (Auditoría y Soft Delete)

```java
package com.predio.mijangos.core.entity;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE {h-schema}{h-table} SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public abstract class BaseEntity implements Persistable<Integer>, Serializable {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by")
    private Integer createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    public abstract Integer getId();
    
    @Override
    @Transient
    public boolean isNew() {
        return getId() == null;
    }
    
    public boolean isDeleted() {
        return deletedAt != null;
    }
    
    public void restore() {
        this.deletedAt = null;
    }
    
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
```

**Todas las entidades deben extender BaseEntity**.

---

### 7.2 Entidades (Domain)

```java
@Entity
@Table(name = "TBL_Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "TBL_Usuario_Rol",
        joinColumns = @JoinColumn(name = "id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();
    
    // NO incluir campos de auditoría aquí
    // BaseEntity ya los proporciona

    @Override
    public Integer getId() {
        return id;
    }

    // Métodos de utilidad
    @Transient
    public boolean isAdmin() {
        return roles.stream()
            .anyMatch(rol -> SecurityConstants.ROLE_ADMIN.equals(rol.getCodigo()));
    }
}
```

**Reglas**:
- Extender `BaseEntity`
- Usar Lombok: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
- Implementar `getId()` requerido por `Persistable<Integer>`
- No incluir campos de auditoría (heredados de BaseEntity)
- `@ManyToMany`: usar `@JoinTable`
- Lazy loading por defecto, excepto cuando se necesite EAGER

---

### 7.3 DTOs (Data Transfer Objects)

**Request DTOs (para crear/actualizar)**:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioCreateDTO {

    @NotNull(message = "Los datos de la persona son obligatorios")
    @Valid
    private PersonaCreateDTO persona;

    @NotBlank(message = "El username es obligatorio")
    @Size(
        min = ValidationConstants.USERNAME_MIN_LENGTH,
        max = ValidationConstants.USERNAME_MAX_LENGTH,
        message = "El username debe tener entre {min} y {max} caracteres"
    )
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(
        min = ValidationConstants.PASSWORD_MIN_LENGTH,
        message = "La contraseña debe tener al menos {min} caracteres"
    )
    @Pattern(
        regexp = ValidationConstants.PASSWORD_PATTERN,
        message = "La contraseña debe contener mayúsculas, minúsculas, números y caracteres especiales"
    )
    private String password;

    @NotEmpty(message = "Debe asignar al menos un rol")
    private Set<Integer> roleIds;
}
```

**Response DTOs**:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Integer id;
    private PersonaResponseDTO persona;
    private String username;
    private Boolean activo;
    private Set<RolSimpleDTO> roles;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // NO incluir: password, deletedAt, createdBy, updatedBy
}
```

**Reglas**:
- Usar Lombok
- **Request**: Validaciones completas (`@NotBlank`, `@Email`, `@Pattern`, etc)
- **Response**: Incluir solo datos necesarios, NO campos sensibles
- **List DTOs**: Versión simplificada para listados (menos campos)
- DTOs organizados en subcarpetas por entidad

---

### 7.4 Mappers (MapStruct)

```java
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // Entity → Response DTO
    @Mapping(target = "persona", source = "persona")
    @Mapping(target = "roles", source = "roles")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    // Create DTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)  // Se maneja en el servicio
    @Mapping(target = "password", ignore = true)  // Se encripta en el servicio
    @Mapping(target = "roles", ignore = true)    // Se asignan en el servicio
    @Mapping(target = "activo", constant = "true")
    // Campos de auditoría ignorados (manejados por JPA Auditing)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Usuario toEntity(UsuarioCreateDTO dto);

    // Update DTO → Entity (actualización parcial)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromDTO(UsuarioUpdateDTO dto, @MappingTarget Usuario usuario);

    // Lista de entidades → Lista de DTOs
    List<UsuarioListDTO> toListDTO(List<Usuario> usuarios);
}
```

**Reglas**:
- `@Mapper(componentModel = "spring")` para inyección de dependencias
- **SIEMPRE ignorar campos de auditoría** (manejados por JPA Auditing)
- Para actualizaciones: usar `@MappingTarget` para actualización parcial
- Mappers separados por entidad
- Métodos específicos para cada tipo de DTO

---

### 7.5 Repositories

```java
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su username.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Verifica si existe un usuario con el username dado.
     */
    boolean existsByUsername(String username);

    /**
     * Busca usuarios por rol.
     */
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.codigo = :roleCodigo")
    List<Usuario> findByRoleCodigo(@Param("roleCodigo") String roleCodigo);

    /**
     * Busca usuarios activos con paginación.
     */
    Page<Usuario> findByActivoTrue(Pageable pageable);

    /**
     * Búsqueda por múltiples criterios (nombre, username, email).
     */
    @Query("SELECT u FROM Usuario u " +
           "WHERE (:searchTerm IS NULL OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.persona.primerNombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.persona.primerApellido) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Usuario> searchUsuarios(@Param("searchTerm") String searchTerm, Pageable pageable);
}
```

**Reglas**:
- Extender `JpaRepository<Entity, ID>`
- Usar Query Methods cuando sea posible (`findByXxx`, `existsByXxx`)
- Para queries complejas: `@Query` con JPQL
- Siempre usar `Optional<T>` para búsquedas por ID
- Documentar cada método con JavaDoc

---

### 7.6 Services (Interfaces + Implementaciones)

**Interface**:

```java
public interface UsuarioService {

    /**
     * Crea un nuevo usuario.
     */
    UsuarioResponseDTO crear(UsuarioCreateDTO dto);

    /**
     * Obtiene un usuario por ID.
     */
    UsuarioResponseDTO obtenerPorId(Integer id);

    /**
     * Lista usuarios con paginación.
     */
    Page<UsuarioListDTO> listar(int page, int size);

    /**
     * Actualiza un usuario existente.
     */
    UsuarioResponseDTO actualizar(Integer id, UsuarioUpdateDTO dto);

    /**
     * Elimina un usuario (soft delete).
     */
    void eliminar(Integer id);

    /**
     * Activa o desactiva un usuario.
     */
    void cambiarEstado(Integer id, Boolean activo);

    /**
     * Asigna roles a un usuario.
     */
    UsuarioResponseDTO asignarRoles(Integer id, Set<Integer> roleIds);

    /**
     * Cambia la contraseña de un usuario.
     */
    void cambiarPassword(Integer id, CambioPasswordDTO dto);
}
```

**Implementación**:

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaService personaService;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDTO crear(UsuarioCreateDTO dto) {
        log.info("Creando nuevo usuario: {}", dto.getUsername());

        // 1. Validar que el username no exista
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessException(
                ErrorCodes.USER_ALREADY_EXISTS,
                "El usuario " + dto.getUsername() + " ya existe"
            );
        }

        // 2. Crear la persona primero
        PersonaResponseDTO personaCreada = personaService.crear(dto.getPersona());

        // 3. Mapear DTO a Entity
        Usuario usuario = usuarioMapper.toEntity(dto);
        
        // 4. Encriptar password
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        // 5. Asociar persona
        Persona persona = new Persona();
        persona.setId(personaCreada.getId());
        usuario.setPersona(persona);
        
        // 6. Asignar roles
        Set<Rol> roles = new HashSet<>(rolRepository.findAllById(dto.getRoleIds()));
        if (roles.size() != dto.getRoleIds().size()) {
            throw new BusinessException(
                ErrorCodes.INVALID_ROLE,
                "Uno o más roles especificados no existen"
            );
        }
        usuario.setRoles(roles);

        // 7. Guardar (auditoría automática)
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        log.info("Usuario creado exitosamente: ID={}", usuarioGuardado.getId());
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                ErrorCodes.USER_NOT_FOUND,
                "Usuario no encontrado con ID: " + id
            ));
        
        return usuarioMapper.toResponseDTO(usuario);
    }

    // ... resto de métodos
}
```

**Reglas**:
- Usar `@Service` + `@RequiredArgsConstructor` + `@Slf4j`
- `@Transactional` a nivel de clase (write operations)
- `@Transactional(readOnly = true)` para queries
- **Validaciones de negocio**: Lanzar `BusinessException`
- **Recursos no encontrados**: Lanzar `ResourceNotFoundException`
- **Logging**: Log inicio y fin de operaciones importantes
- **Coordinar servicios**: Si la operación involucra múltiples entidades
- **Mapper usage**: Siempre DTO → Entity → DTO

---

### 7.7 Controllers

```java
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuarios", description = "API para gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Crea un nuevo usuario.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('USUARIOS:CREATE')")
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe"),
        @ApiResponse(responseCode = "403", description = "Sin permisos para crear usuarios")
    })
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> crear(
            @Valid @RequestBody UsuarioCreateDTO dto) {
        
        log.info("POST /api/v1/usuarios - Creando usuario: {}", dto.getUsername());
        UsuarioResponseDTO usuario = usuarioService.crear(dto);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created("Usuario creado exitosamente", usuario));
    }

    /**
     * Lista usuarios con paginación.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('USUARIOS:READ')")
    @Operation(summary = "Listar usuarios", description = "Obtiene un listado paginado de usuarios")
    public ResponseEntity<ApiResponse<PageResponse<UsuarioListDTO>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/v1/usuarios - page={}, size={}", page, size);
        Page<UsuarioListDTO> usuarios = usuarioService.listar(page, size);
        PageResponse<UsuarioListDTO> response = PageResponse.of(usuarios);
        
        return ResponseEntity.ok(
            ApiResponse.ok("Usuarios obtenidos exitosamente", response)
        );
    }

    /**
     * Obtiene un usuario por ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIOS:READ')")
    @Operation(summary = "Obtener usuario", description = "Obtiene los detalles de un usuario")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> obtenerPorId(
            @PathVariable Integer id) {
        
        log.info("GET /api/v1/usuarios/{}", id);
        UsuarioResponseDTO usuario = usuarioService.obtenerPorId(id);
        
        return ResponseEntity.ok(
            ApiResponse.ok("Usuario obtenido exitosamente", usuario)
        );
    }

    /**
     * Actualiza un usuario existente.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIOS:UPDATE')")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioUpdateDTO dto) {
        
        log.info("PUT /api/v1/usuarios/{}", id);
        UsuarioResponseDTO usuario = usuarioService.actualizar(id, dto);
        
        return ResponseEntity.ok(
            ApiResponse.ok("Usuario actualizado exitosamente", usuario)
        );
    }

    /**
     * Elimina un usuario (soft delete).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIOS:DELETE')")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/v1/usuarios/{}", id);
        usuarioService.eliminar(id);
        
        return ResponseEntity.ok(
            ApiResponse.<Void>noContent("Usuario eliminado exitosamente")
        );
    }

    /**
     * Cambia el estado de un usuario (activar/desactivar).
     */
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('USUARIOS:UPDATE')")
    @Operation(summary = "Cambiar estado", description = "Activa o desactiva un usuario")
    public ResponseEntity<ApiResponse<Void>> cambiarEstado(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioEstadoDTO dto) {
        
        log.info("PATCH /api/v1/usuarios/{}/estado - activo={}", id, dto.getActivo());
        usuarioService.cambiarEstado(id, dto.getActivo());
        
        return ResponseEntity.ok(
            ApiResponse.<Void>ok("Estado del usuario actualizado exitosamente", null)
        );
    }
}
```

**Reglas**:
- `@RestController` + `@RequestMapping` + `@RequiredArgsConstructor` + `@Slf4j`
- **Documentación Swagger**: `@Tag`, `@Operation`, `@ApiResponses`
- **Seguridad**: `@PreAuthorize` con permisos granulares
- **Validación**: `@Valid` en `@RequestBody`
- **Logging**: Log de cada request con método HTTP y path
- **Respuestas**:
  - Crear: `201 CREATED` con `ApiResponse.created()`
  - Leer: `200 OK` con `ApiResponse.ok()`
  - Actualizar: `200 OK` con `ApiResponse.ok()`
  - Eliminar: `200 OK` con `ApiResponse.noContent()` o `204 NO CONTENT`
- **Códigos HTTP apropiados**
- **Paginación**: Parámetros `page` y `size` con defaults

---

## 8. Naming Conventions

### 8.1 Java

```java
// Clases: PascalCase
public class UsuarioService { }
public class ClienteController { }

// Interfaces: PascalCase (sin prefijo I)
public interface UsuarioRepository { }

// Métodos: camelCase (verbos)
public UsuarioResponseDTO crear(UsuarioCreateDTO dto) { }
public void eliminar(Integer id) { }

// Variables: camelCase
private String username;
private LocalDateTime createdAt;

// Constantes: UPPER_SNAKE_CASE
public static final String ROLE_ADMIN = "ROLE_ADMIN";
public static final int MAX_LOGIN_ATTEMPTS = 3;

// Packages: lowercase
package com.predio.mijangos.modules.usuarios;

// DTOs: sufijo DTO
UsuarioCreateDTO, UsuarioResponseDTO, UsuarioListDTO

// Exceptions: sufijo Exception
BusinessException, ResourceNotFoundException

// Mappers: sufijo Mapper
UsuarioMapper, ClienteMapper
```

### 8.2 REST Endpoints

```
# Recursos en plural
GET    /api/v1/usuarios
POST   /api/v1/usuarios
GET    /api/v1/usuarios/{id}
PUT    /api/v1/usuarios/{id}
DELETE /api/v1/usuarios/{id}

# Sub-recursos
GET    /api/v1/usuarios/{id}/roles
PATCH  /api/v1/usuarios/{id}/roles

# Acciones específicas (verbos)
PATCH  /api/v1/usuarios/{id}/estado
POST   /api/v1/usuarios/{id}/cambiar-password

# Búsquedas
GET    /api/v1/usuarios?search=juan&page=0&size=10

# Filtros
GET    /api/v1/productos?categoria=repuestos&activo=true
```

---

## 9. Seguridad y Autenticación

### 9.1 JWT Configuration

**application.yml**:
```yaml
app:
  security:
    jwt:
      secret: ${JWT_SECRET}
      expiration-minutes: 480      # 8 horas
      refresh-expiration-minutes: 10080  # 7 días
```

### 9.2 Roles y Permisos

**Roles** (nivel alto):
- `ROLE_ADMIN`
- `ROLE_SUPERVISOR`
- `ROLE_VENDEDOR`
- `ROLE_BODEGUERO`
- `ROLE_CONTADOR`
- `ROLE_OPERADOR`

**Permisos Granulares** (formato: `MODULO:ACCION`):
- `USUARIOS:CREATE`
- `USUARIOS:READ`
- `USUARIOS:UPDATE`
- `USUARIOS:DELETE`
- `VENTAS:CREATE`
- `VENTAS:READ`
- `INVENTARIO:CREATE`
- etc.

### 9.3 Uso en Controllers

```java
// Por rol
@PreAuthorize("hasRole('ROLE_ADMIN')")

// Por permiso granular (RECOMENDADO)
@PreAuthorize("hasAuthority('USUARIOS:CREATE')")

// Múltiples permisos (OR)
@PreAuthorize("hasAnyAuthority('USUARIOS:READ', 'USUARIOS:UPDATE')")

// Múltiples permisos (AND)
@PreAuthorize("hasAuthority('USUARIOS:READ') and hasAuthority('VENTAS:READ')")

// Roles + Permisos
@PreAuthorize("hasRole('ROLE_ADMIN') or hasAuthority('USUARIOS:DELETE')")
```

### 9.4 Estructura de Token JWT

```json
{
  "sub": "admin@prediomijangos.com",
  "roles": [
    "ROLE_ADMIN",
    "USUARIOS:READ",
    "USUARIOS:CREATE",
    "VENTAS:READ"
  ],
  "iat": 1697900000,
  "exp": 1697928800
}
```

---

## 10. APIs y Contratos

### 10.1 Estructura de Respuesta Estándar

**Éxito (200, 201)**:
```json
{
  "statusCode": 200,
  "message": "Usuario creado exitosamente",
  "body": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com"
  },
  "timestamp": "2025-10-21T10:30:00"
}
```

**Paginación (200)**:
```json
{
  "statusCode": 200,
  "message": "Usuarios obtenidos exitosamente",
  "body": {
    "content": [...],
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

**Error (400, 404, 500)**:
```json
{
  "statusCode": 400,
  "message": "Errores de validación",
  "body": {
    "timestamp": "2025-10-21T10:30:00",
    "status": 400,
    "error": "BAD_REQUEST",
    "errorCode": "VAL_001",
    "message": "Errores de validación en los campos",
    "errors": {
      "username": "El username es obligatorio",
      "email": "El formato del email es inválido"
    },
    "path": "/api/v1/usuarios"
  },
  "timestamp": "2025-10-21T10:30:00"
}
```

### 10.2 Códigos de Estado HTTP

- **200 OK**: Operación exitosa (GET, PUT, PATCH)
- **201 CREATED**: Recurso creado (POST)
- **204 NO CONTENT**: Sin contenido (DELETE exitoso)
- **400 BAD REQUEST**: Validación fallida
- **401 UNAUTHORIZED**: No autenticado
- **403 FORBIDDEN**: No autorizado (sin permisos)
- **404 NOT FOUND**: Recurso no encontrado
- **409 CONFLICT**: Conflicto (ej: unique constraint)
- **500 INTERNAL SERVER ERROR**: Error del servidor

---

## 11. Manejo de Errores

### 11.1 Excepciones Personalizadas

Todas heredan de `RuntimeException`:

- **BusinessException**: Errores de lógica de negocio (400)
- **ResourceNotFoundException**: Recurso no encontrado (404)
- **ValidationException**: Validación personalizada (400)

### 11.2 GlobalExceptionHandler

Centraliza el manejo de TODAS las excepciones:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleResourceNotFound(...)
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(...)
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(...)
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationErrors(...)
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDataIntegrityViolation(...)
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAuthenticationException(...)
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAccessDenied(...)
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGenericException(...)
}
```

### 11.3 Uso en Services

```java
// Recurso no encontrado
Usuario usuario = usuarioRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException(
        ErrorCodes.USER_NOT_FOUND,
        "Usuario no encontrado con ID: " + id
    ));

// Error de negocio
if (usuarioRepository.existsByUsername(username)) {
    throw new BusinessException(
        ErrorCodes.USER_ALREADY_EXISTS,
        "El usuario " + username + " ya existe"
    );
}

// Validación personalizada
Map<String, String> errors = new HashMap<>();
if (!isValidDPI(dpi)) {
    errors.put("dpi", "El DPI no es válido");
}
if (!errors.isEmpty()) {
    throw new ValidationException(
        ErrorCodes.VALIDATION_ERROR,
        "Errores de validación",
        errors
    );
}
```

---

## 12. Testing

### 12.1 Estructura de Tests

```
src/test/java/com/predio/mijangos/
├── unit/                # Tests unitarios (MockMvc, Mockito)
│   ├── service/
│   └── mapper/
│
├── integration/         # Tests de integración (Testcontainers)
│   ├── controller/
│   └── repository/
│
└── e2e/                # Tests end-to-end (RestAssured)
```

### 12.2 Test Unitario (Service)

```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PersonaService personaService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    @DisplayName("Crear usuario exitosamente")
    void crearUsuario_Success() {
        // Given
        UsuarioCreateDTO dto = UsuarioCreateDTO.builder()
            .username("testuser")
            .password("Test123!")
            .build();

        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.save(any())).thenReturn(new Usuario());

        // When
        UsuarioResponseDTO result = usuarioService.crear(dto);

        // Then
        assertNotNull(result);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Crear usuario con username duplicado lanza BusinessException")
    void crearUsuario_DuplicateUsername_ThrowsException() {
        // Given
        UsuarioCreateDTO dto = UsuarioCreateDTO.builder()
            .username("existing")
            .build();

        when(usuarioRepository.existsByUsername("existing")).thenReturn(true);

        // When & Then
        assertThrows(BusinessException.class, () -> usuarioService.crear(dto));
        verify(usuarioRepository, never()).save(any());
    }
}
```

### 12.3 Test de Integración (Controller)

```java
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@Testcontainers
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @Test
    @WithMockUser(authorities = {"USUARIOS:CREATE"})
    void crearUsuario_Success() throws Exception {
        // Given
        UsuarioCreateDTO dto = UsuarioCreateDTO.builder()
            .username("testuser")
            .password("Test123!")
            .build();

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.body.username").value("testuser"));
    }
}
```

### 12.4 Cobertura de Testing

**Mínimo requerido**:
- **Servicios**: 80%
- **Controllers**: 70%
- **Mappers**: 60%
- **Repositories**: Query methods personalizados

---

## 13. Configuración y Despliegue

### 13.1 Profiles

```yaml
# application.yml (común)
spring:
  application:
    name: predio-mijangos-api

---
# application-dev.yml (desarrollo)
spring:
  datasource:
    url: jdbc:mysql://localhost:3307/predio_mijangos
    username: predio_user
    password: Predio2025!
  jpa:
    show-sql: true

---
# application-test.yml (testing)
spring:
  datasource:
    url: jdbc:tc:mysql:8.0:///testdb
  jpa:
    hibernate:
      ddl-auto: create-drop

---
# application-prod.yml (producción)
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    show-sql: false
```

### 13.2 Variables de Entorno (Contraseñas solo de demostración)

```bash
# Base de datos
DB_URL=jdbc:mysql://rds-endpoint:3306/predio_mijangos
DB_USERNAME=admin
DB_PASSWORD=SecurePassword123!

# JWT
JWT_SECRET=SuperSecretKeyMinLength32Chars!

# AWS
AWS_ACCESS_KEY_ID=AKIAIOSFODNN7EXAMPLE
AWS_SECRET_ACCESS_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
AWS_REGION=us-east-1

# Email
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=noreply@prediomijangos.com
MAIL_PASSWORD=AppPassword123!
```

### 13.3 Docker (para desarrollo)

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 14. Checklist por Módulo Nuevo

Al crear un módulo nuevo, seguir esta lista:

### 14.1 Database
- [ ] Crear migration SQL con Flyway
- [ ] Definir tablas con prefijo `TBL_`
- [ ] Agregar campos de auditoría (created_at, updated_at, etc)
- [ ] Definir índices y constraints
- [ ] Agregar datos semilla si es necesario

### 14.2 Domain
- [ ] Crear entidades extendiendo `BaseEntity`
- [ ] Usar anotaciones JPA correctas
- [ ] Implementar método `getId()`
- [ ] Definir relaciones (OneToMany, ManyToOne, ManyToMany)
- [ ] Agregar métodos de utilidad (`@Transient`)

### 14.3 DTOs
- [ ] Crear DTOs por operación (Create, Update, Response, List)
- [ ] Agregar validaciones (`@NotBlank`, `@Email`, etc)
- [ ] Usar constantes de `ValidationConstants`
- [ ] Documentar con JavaDoc

### 14.4 Mappers
- [ ] Crear mapper con MapStruct
- [ ] Ignorar campos de auditoría
- [ ] Mapeos específicos por tipo de DTO
- [ ] Método para listas

### 14.5 Repository
- [ ] Extender `JpaRepository<Entity, Integer>`
- [ ] Query methods para búsquedas comunes
- [ ] `@Query` para búsquedas complejas
- [ ] Documentar cada método

### 14.6 Service
- [ ] Crear interface con contrato de métodos
- [ ] Implementar con `@Service` + `@Transactional`
- [ ] Validaciones de negocio
- [ ] Logging de operaciones importantes
- [ ] Lanzar excepciones apropiadas

### 14.7 Controller
- [ ] `@RestController` con path base
- [ ] Endpoints RESTful estándar (GET, POST, PUT, DELETE)
- [ ] Paginación en listados
- [ ] `@PreAuthorize` para control de acceso
- [ ] Retornar `ApiResponse<T>` en todos los endpoints
- [ ] Status HTTP apropiados (200, 201, 204, etc)

### 14.8 Testing
- [ ] Test unitario del servicio (mocks)
- [ ] Test de integración del controller (Testcontainers)
- [ ] Cobertura mínima 70%
- [ ] Tests de validaciones
- [ ] Tests de casos de error
- [ ] Tests de permisos/seguridad

### 14.9 Documentación
- [ ] JavaDoc en clases y métodos públicos
- [ ] Comentarios inline para lógica compleja
- [ ] Actualizar README.md si agrega funcionalidad nueva
- [ ] Documentar decisiones arquitectónicas importantes

### 14.10 Seguridad
- [ ] Definir permisos finos (`MODULO:ACCION`)
- [ ] Configurar `@PreAuthorize` en endpoints sensibles
- [ ] Validar ownership cuando aplique
- [ ] Sanitizar inputs
- [ ] No exponer datos sensibles en responses

---

## 15. Recursos y Referencias

### 15.1 Documentación Oficial

- [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)
- [Flyway](https://flywaydb.org/documentation/)
- [MapStruct](https://mapstruct.org/)
- [SpringDoc OpenAPI](https://springdoc.org/)

### 15.2 Documentación del Proyecto

- **Arquitectura Backend**: `docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md`
  - Explicación detallada de cada paquete
  - Flujos de datos principales
  - Diagramas de arquitectura
  - Mejores prácticas implementadas

### 15.3 Herramientas

- **Swagger UI**: Documentación API - `/api/swagger-ui.html`
- **Actuator**: Monitoreo - `/api/actuator/health`
- **H2 Console**: Solo en dev

---

## 16. Changelog del Documento

| Versión | Fecha      | Cambios                                                                 |
|---------|------------|-------------------------------------------------------------------------|
| 2.0.0   | 21 Oct 2025| - Agregada sección 6: Documentación de Arquitectura<br>- Actualizada estructura de paquetes con implementación real<br>- Documentados paquetes core, security y modules<br>- Agregados flujos de datos<br>- Actualizado con patrones implementados (BaseEntity, ApiResponse, GlobalExceptionHandler)<br>- Incluida configuración JWT actualizada |
| 1.0.0   | 02 Oct 2025| Versión inicial - Consolidación de todas las directrices               |

---

## 📌 IMPORTANTE

**Este documento es la fuente de verdad para el desarrollo del backend.**

- Cualquier decisión arquitectónica o patrón de código debe estar documentado aquí
- Mantener actualizado es responsabilidad de todo el equipo
- Consultar siempre antes de desviarse de las directrices establecidas
- Para información detallada de implementación, consultar `docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md`

---

**Documento mantenido por:** Equipo Técnico Predio Mijangos  
**Última revisión:** 21 de Octubre 2025
