# 🏢 Predio Mijangos - Backend API

Sistema de gestión de inventario y ventas para distribuidora de partes automotrices.

**Versión:** 2.0.0  
**Última Actualización:** 21 de Octubre 2025

---

## 📖 Tabla de Contenidos

- [Documentación Principal](#-documentación-principal)
- [Stack Tecnológico](#-stack-tecnológico)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Testing](#-testing)
- [Arquitectura](#-arquitectura)
- [Documentación API](#-documentación-api)
- [Contribución](#-contribución)

---

## 📚 Documentación Principal

### ⭐ Lectura OBLIGATORIA antes de desarrollar

1. **[📘 Directrices Técnicas del Backend](../docs/DIRECTRICES_TECNICAS_BACKEND.md)**
   - Stack tecnológico completo
   - Estándares de base de datos (naming, índices, catálogos)
   - Patrones de código (BaseEntity, DTOs, Services, Controllers)
   - Naming conventions (Java, REST, SQL)
   - Seguridad y JWT (roles, permisos, configuración)
   - Testing (unitarios, integración, cobertura)
   - Configuración y despliegue (profiles, Docker, AWS)
   - **⚡ Checklist para módulos nuevos** (paso a paso)

2. **[📙 Documentación de Arquitectura Backend](../docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md)**
   - Explicación detallada de cada paquete (`core`, `security`, `modules`)
   - Descripción de todas las clases y sus responsabilidades
   - Flujos de datos principales (login, request autenticado, CRUD)
   - Diagramas de arquitectura (capas, seguridad, módulos)
   - Mejores prácticas implementadas (SOLID, Clean Code)

### 📋 Resumen Rápido

**¿Necesitas crear un nuevo módulo?**  
→ Ve al [Checklist de Módulo Nuevo](../docs/DIRECTRICES_TECNICAS_BACKEND.md#14-checklist-por-módulo-nuevo)

**¿Necesitas entender cómo funciona el sistema?**  
→ Lee los [Flujos de Datos](../docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md#5-flujos-de-datos-principales)

**¿Necesitas saber cómo usar una clase específica?**  
→ Busca en [Documentación de Arquitectura](../docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md)

---

## 🛠 Stack Tecnológico

### Core
- **Java 17 LTS**
- **Spring Boot 3.2.x**
- **Maven 3.9.x**

### Persistencia
- **Spring Data JPA** + **Hibernate 6.x**
- **MySQL 8.0**
- **Flyway** (versionado de BD)

### Seguridad
- **Spring Security 6.x**
- **JWT** (JSON Web Tokens)
  - Access Token: 8 horas
  - Refresh Token: 7 días
- **BCrypt** (encriptación de contraseñas, strength 12)

### Utilidades
- **MapStruct 1.5.x** (mapeo DTO ↔ Entity)
- **Lombok** (reduce boilerplate)
- **Jakarta Bean Validation** (validaciones)

### Documentación
- **SpringDoc OpenAPI 3.0** (Swagger UI)

### Testing
- **JUnit 5** (tests unitarios)
- **Mockito** (mocking)
- **Testcontainers** (tests de integración)
- **RestAssured** (tests E2E)

### Build y Deploy
- **Maven** (gestión de dependencias)
- **Docker** (contenedores)
- **AWS** (infraestructura)
  - EC2 (compute)
  - RDS MySQL Multi-AZ (database)
  - S3 (storage)
  - CloudFront (CDN)
  - CloudWatch (monitoring)

---

## 📦 Requisitos Previos

- ✅ **Java JDK 17+** ([AdoptOpenJDK](https://adoptium.net/) o [Oracle JDK](https://www.oracle.com/java/technologies/downloads/))
- ✅ **Maven 3.8+** (incluido con la mayoría de IDEs)
- ✅ **MySQL 8.0+** ([MySQL Community](https://dev.mysql.com/downloads/mysql/) o Docker)
- ✅ **Git** ([Download](https://git-scm.com/downloads))

### IDEs Recomendados
- **IntelliJ IDEA** (Community o Ultimate) ⭐ Recomendado
- **Eclipse** con Spring Tools
- **NetBeans 15+**
- **Visual Studio Code** con extensiones de Java

### Herramientas Opcionales
- **Docker Desktop** (para MySQL en contenedor)
- **Postman** o **Insomnia** (testing de API)
- **DBeaver** o **MySQL Workbench** (gestión de BD)
- **Git GUI** (GitKraken, SourceTree, GitHub Desktop)

---

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/aruano/predioMijangos.git
cd predioMijangos/backend
```

### 2. Crear base de datos

#### Opción A: MySQL Local

```sql
-- Conectar como root
mysql -u root -p

-- Crear base de datos
CREATE DATABASE predio_mijangos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear usuario con permisos
CREATE USER 'predio_user'@'localhost' IDENTIFIED BY 'Predio2025!';
GRANT ALL PRIVILEGES ON predio_mijangos.* TO 'predio_user'@'localhost';
FLUSH PRIVILEGES;

-- Verificar
SHOW DATABASES;
SELECT User, Host FROM mysql.user WHERE User = 'predio_user';
```

#### Opción B: MySQL con Docker

```bash
docker run -d \
  --name mysql-predio \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=predio_mijangos \
  -e MYSQL_USER=predio_user \
  -e MYSQL_PASSWORD=Predio2025! \
  -p 3307:3306 \
  mysql:8.0

# Verificar que está corriendo
docker ps

# Ver logs
docker logs mysql-predio
```

### 3. Instalar dependencias

```bash
# Compilar y descargar dependencias
mvn clean install

# Solo descargar dependencias (más rápido)
mvn dependency:resolve
```

---

## ⚙️ Configuración

### Profiles Disponibles

El proyecto usa Spring Profiles para diferentes entornos:

- **dev** (por defecto): Desarrollo local
- **test**: Testing automatizado
- **prod**: Producción (AWS)

### Variables de Entorno

#### Desarrollo Local (`application-dev.yml`)

Valores por defecto (no requiere cambios):
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3307/predio_mijangos
    username: predio_user
    password: Predio2025!
```

#### Producción (`.env` o variables de entorno)

```bash
# Base de datos
DB_URL=jdbc:mysql://rds-endpoint.region.rds.amazonaws.com:3306/predio_mijangos
DB_USERNAME=admin
DB_PASSWORD=SuperSecurePassword123!

# JWT
JWT_SECRET=YourBase64EncodedSecretKeyAtLeast256BitsLong

# AWS (para S3, SES, etc)
AWS_ACCESS_KEY_ID=AKIAIOSFODNN7EXAMPLE
AWS_SECRET_ACCESS_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
AWS_REGION=us-east-1
```

### Generar JWT Secret

```bash
# Linux/Mac
openssl rand -base64 64

# Windows (PowerShell)
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

---

## 🏃 Ejecución

### Modo Desarrollo (con hot reload)

```bash
# Opción 1: Maven
mvn spring-boot:run

# Opción 2: Maven con profile específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Opción 3: Desde el IDE
# Run > Run 'PredioMijangosApiApplication'
```

**El servidor iniciará en:** http://localhost:8080/api

**Endpoints importantes:**
- 🏠 **Root:** http://localhost:8080/api/
- 📚 **Swagger UI:** http://localhost:8080/api/swagger-ui.html
- 📋 **OpenAPI JSON:** http://localhost:8080/api/v3/api-docs
- ❤️ **Health Check:** http://localhost:8080/api/actuator/health

### Modo Producción

```bash
# 1. Compilar JAR optimizado
mvn clean package -DskipTests

# 2. Ejecutar JAR con profile de producción
java -jar target/predio-mijangos-api-1.0.0.jar --spring.profiles.active=prod

# 3. Con variables de entorno
java -jar target/predio-mijangos-api-1.0.0.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=${DB_URL} \
  --spring.datasource.username=${DB_USERNAME} \
  --spring.datasource.password=${DB_PASSWORD}
```

### Docker (próximamente)

```bash
# Build de imagen
docker build -t predio-mijangos-api:latest .

# Ejecutar contenedor
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=${DB_URL} \
  -e DB_USERNAME=${DB_USERNAME} \
  -e DB_PASSWORD=${DB_PASSWORD} \
  -e JWT_SECRET=${JWT_SECRET} \
  --name predio-api \
  predio-mijangos-api:latest
```

---

## 🧪 Testing

### Ejecutar Tests

```bash
# Todos los tests
mvn test

# Tests con cobertura
mvn clean verify

# Solo tests unitarios
mvn test -Dtest=**/*Test

# Solo tests de integración
mvn test -Dtest=**/*IntegrationTest

# Test específico
mvn test -Dtest=UsuarioServiceTest

# Con más detalle
mvn test -X
```

### Cobertura Requerida

| Capa | Cobertura Mínima |
|------|------------------|
| Services | 80% |
| Controllers | 70% |
| Mappers | 60% |
| Repositories | Query methods personalizados |

### Estructura de Tests

```
src/test/java/com/predio/mijangos/
├── unit/                    # Tests unitarios (Mockito)
│   ├── service/
│   │   ├── UsuarioServiceTest.java
│   │   └── ClienteServiceTest.java
│   └── mapper/
│       └── UsuarioMapperTest.java
│
├── integration/             # Tests de integración (Testcontainers)
│   ├── controller/
│   │   └── UsuarioControllerIntegrationTest.java
│   └── repository/
│       └── UsuarioRepositoryTest.java
│
└── e2e/                    # Tests end-to-end (RestAssured)
    └── AuthenticationE2ETest.java
```

---

## 🏗 Arquitectura

### Vista de Alto Nivel

```
com.predio.mijangos/
├── core/                           # 🌍 Funcionalidad transversal
│   ├── config/                    # Configuraciones globales
│   │   ├── SecurityConfig         # Spring Security + JWT + CORS
│   │   ├── SwaggerConfig          # OpenAPI/Swagger
│   │   └── AuditorAwareConfig     # Auditoría JPA
│   │
│   ├── constants/                 # Constantes centralizadas
│   │   ├── SecurityConstants      # Roles, permisos, JWT
│   │   ├── ErrorCodes            # Códigos de error
│   │   ├── ValidationConstants    # Patrones regex, validaciones
│   │   └── CacheConstants        # Configuración de caché
│   │
│   ├── response/                  # Respuestas estandarizadas
│   │   ├── ApiResponse<T>        # Respuesta estándar
│   │   ├── PageResponse<T>       # Respuesta paginada
│   │   └── ErrorResponse         # Detalle de errores
│   │
│   ├── exception/                 # Manejo de excepciones
│   │   ├── GlobalExceptionHandler # Interceptor global
│   │   ├── BusinessException      # Errores de negocio
│   │   ├── ResourceNotFoundException
│   │   └── ValidationException
│   │
│   ├── entity/
│   │   └── BaseEntity             # Auditoría + soft delete
│   │
│   └── controller/
│       └── RootController         # Endpoint raíz (/)
│
├── security/                       # 🔐 Autenticación y autorización
│   ├── jwt/
│   │   ├── JwtUtil                # Generación y validación de tokens
│   │   ├── JwtAuthenticationFilter # Interceptor HTTP
│   │   └── JwtAuthenticationEntryPoint
│   │
│   ├── model/
│   │   └── CustomUserDetails      # UserDetails extendido
│   │
│   ├── service/
│   │   ├── AuthenticationService  # Login, logout, refresh
│   │   ├── CustomUserDetailsService
│   │   └── RefreshTokenService
│   │
│   └── dto/
│       ├── LoginRequestDTO
│       ├── LoginResponseDTO
│       ├── RefreshTokenRequestDTO
│       └── LogoutRequestDTO
│
└── modules/                        # 📦 Módulos de negocio
    ├── auth/                       # Endpoints públicos de autenticación
    │   └── controller/
    │       └── AuthController
    │
    ├── geo/                        # Catálogos geográficos (Guatemala)
    │   ├── domain/                # Departamento, Municipio
    │   ├── dto/
    │   ├── mapper/
    │   ├── repository/
    │   ├── service/impl/
    │   └── controller/
    │
    ├── usuarios/                   # Gestión de usuarios, roles, permisos
    │   ├── domain/                # Usuario, Cliente, Persona, Rol, Modulo, Pagina
    │   ├── dto/
    │   ├── mapper/
    │   ├── repository/
    │   ├── service/impl/
    │   └── controller/
    │
    ├── compras/                    # (PRÓXIMAMENTE)
    ├── ventas/                     # (PRÓXIMAMENTE)
    ├── inventario/                 # (PRÓXIMAMENTE)
    ├── vehiculos/                  # (PRÓXIMAMENTE)
    └── reportes/                   # (PRÓXIMAMENTE)
```

### Capas por Módulo (DDD simplificado)

Cada módulo sigue esta estructura estándar:

```
modules/[nombre-modulo]/
├── domain/              # 📊 Entidades JPA
│   └── [Entidad].java
│
├── dto/                 # 📄 Data Transfer Objects
│   ├── [Entidad]CreateDTO.java
│   ├── [Entidad]UpdateDTO.java
│   ├── [Entidad]ResponseDTO.java
│   └── [Entidad]ListDTO.java
│
├── mapper/              # 🔄 Conversión Entity ↔ DTO (MapStruct)
│   └── [Entidad]Mapper.java
│
├── repository/          # 💾 Acceso a datos (Spring Data JPA)
│   └── [Entidad]Repository.java
│
├── service/             # 🧠 Lógica de negocio
│   ├── [Entidad]Service.java      (interface)
│   └── impl/
│       └── [Entidad]ServiceImpl.java
│
└── controller/          # 🌐 REST Endpoints
    └── [Entidad]Controller.java
```

> 📖 **Más detalles:** [Documentación de Arquitectura](../docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md)

---

## 📚 Documentación API

### Swagger UI (Interactiva)

Una vez iniciado el servidor, accede a:

**🔗 http://localhost:8080/api/swagger-ui.html**

Aquí podrás:
- ✅ Ver todos los endpoints disponibles
- ✅ Probar las APIs directamente
- ✅ Ver request/response examples
- ✅ Autenticarte con JWT
- ✅ Ver modelos de datos

### OpenAPI JSON

**🔗 http://localhost:8080/api/v3/api-docs**

Especificación OpenAPI 3.0 en formato JSON (útil para generadores de clientes).

### Autenticación en Swagger

1. Ir a **http://localhost:8080/api/swagger-ui.html**
2. Expandir **"auth-controller"** → **POST /api/v1/auth/login**
3. Probar con credenciales por defecto:
   ```json
   {
     "username": "admin",
     "password": "Admin123!"
   }
   ```
4. Copiar el **accessToken** de la respuesta
5. Hacer clic en **"Authorize"** (botón verde en la esquina superior derecha)
6. Pegar: `Bearer <accessToken>`
7. ¡Listo! Ya puedes probar endpoints protegidos

---

## 🔐 Seguridad

### Autenticación (JWT)

**Configuración:**
```yaml
app:
  security:
    jwt:
      secret: ${JWT_SECRET}
      expiration-minutes: 480      # 8 horas
      refresh-expiration-minutes: 10080  # 7 días
```

**Flow:**
1. Usuario hace login → recibe `accessToken` + `refreshToken`
2. Usuario incluye `accessToken` en header: `Authorization: Bearer <token>`
3. Cuando `accessToken` expira → usa `refreshToken` para obtener nuevo `accessToken`
4. `refreshToken` se guarda en BD y puede ser revocado

### Autorización (RBAC + Permisos Granulares)

**Roles:**
- `ROLE_ADMIN` - Acceso total
- `ROLE_SUPERVISOR` - Aprobaciones
- `ROLE_VENDEDOR` - Gestión de ventas
- `ROLE_BODEGUERO` - Gestión de inventario
- `ROLE_CONTADOR` - Reportes financieros
- `ROLE_OPERADOR` - Operaciones generales

**Permisos Granulares** (formato: `MODULO:ACCION`):
- `USUARIOS:CREATE`, `USUARIOS:READ`, `USUARIOS:UPDATE`, `USUARIOS:DELETE`
- `VENTAS:CREATE`, `VENTAS:READ`, `VENTAS:APPROVE`
- `INVENTARIO:CREATE`, `INVENTARIO:READ`, `INVENTARIO:TRANSFER`
- etc.

**Uso en Controllers:**
```java
// Por permiso específico (RECOMENDADO)
@PreAuthorize("hasAuthority('USUARIOS:CREATE')")
@PostMapping
public ResponseEntity<ApiResponse<UsuarioResponseDTO>> crear(...) { }

// Por rol
@PreAuthorize("hasRole('ROLE_ADMIN')")

// Múltiples permisos (OR)
@PreAuthorize("hasAnyAuthority('USUARIOS:READ', 'USUARIOS:UPDATE')")

// Múltiples permisos (AND)
@PreAuthorize("hasAuthority('USUARIOS:READ') and hasAuthority('VENTAS:READ')")
```

### Encriptación

- **Contraseñas:** BCrypt con strength 12
- **JWT:** Firmado con HMAC-SHA256
- **HTTPS:** Requerido en producción

### CORS

Configurado para permitir acceso desde:
- **Frontend Web:** http://localhost:3000 (desarrollo)
- **App Móvil:** http://localhost:19006 (Expo)
- **Producción:** dominio configurado en `application-prod.yml`

---

## 📄 Migraciones Flyway

### Ubicación

`src/main/resources/db/migration/`

### Nomenclatura

`V{version}__{descripcion}.sql`

**Ejemplos:**
- `V1__initial_schema.sql` - Esquema inicial
- `V2__add_audit_fields.sql` - Agregar campos de auditoría
- `V3__seed_geo_data.sql` - Datos iniciales de geografía
- `V4__create_indexes.sql` - Índices de performance
- `V5__seed_users.sql` - Usuarios iniciales

### Ejecución

Flyway ejecuta automáticamente al iniciar la aplicación:
- ✅ Verifica versión actual de BD
- ✅ Ejecuta migraciones pendientes en orden
- ✅ Registra en tabla `flyway_schema_history`

### Comandos Útiles

```bash
# Información de migraciones
mvn flyway:info

# Validar migraciones
mvn flyway:validate

# Limpiar BD (⚠️ CUIDADO: borra todos los datos)
mvn flyway:clean

# Reparar metadata de Flyway
mvn flyway:repair
```

---

## 🤝 Contribución

### Antes de Empezar

1. ✅ Leer [Directrices Técnicas](../docs/DIRECTRICES_TECNICAS_BACKEND.md)
2. ✅ Leer [Documentación de Arquitectura](../docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md)
3. ✅ Familiarizarse con [Checklist de Módulo Nuevo](../docs/DIRECTRICES_TECNICAS_BACKEND.md#14-checklist-por-módulo-nuevo)

### Flujo de Trabajo Git

```bash
# 1. Actualizar develop
git checkout develop
git pull origin develop

# 2. Crear branch desde develop
git checkout -b feature/nombre-funcionalidad
# Ejemplos:
# - feature/modulo-ventas
# - fix/validacion-dpi
# - refactor/usuario-service

# 3. Desarrollar siguiendo las directrices
# ...

# 4. Commit con Conventional Commits
git add .
git commit -m "feat(usuarios): agregar endpoint de búsqueda avanzada"

# 5. Push
git push origin feature/nombre-funcionalidad

# 6. Crear Pull Request en GitHub
# - Hacia: develop
# - Solicitar code review
# - Esperar aprobación
```

### Convención de Commits

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

**Formato:**
```
<tipo>(<scope>): <descripción>

[cuerpo opcional]

[footer opcional]
```

**Tipos:**
- `feat:` Nueva funcionalidad
- `fix:` Corrección de bug
- `refactor:` Refactorización (no cambia funcionalidad)
- `docs:` Cambios en documentación
- `test:` Agregar o modificar tests
- `chore:` Tareas de mantenimiento
- `style:` Cambios de formato (no afectan lógica)
- `perf:` Mejoras de rendimiento

**Ejemplos:**
```bash
git commit -m "feat(usuarios): agregar endpoint de cambio de contraseña"
git commit -m "fix(auth): corregir validación de refresh token expirado"
git commit -m "docs(readme): actualizar instrucciones de instalación"
git commit -m "refactor(roles): simplificar lógica de asignación de permisos"
git commit -m "test(usuarios): agregar tests de integración para CRUD"
git commit -m "perf(queries): optimizar query de búsqueda de productos"
```

### Code Review Checklist

Antes de solicitar review, verificar:

- [ ] ✅ Código sigue [Directrices Técnicas](../docs/DIRECTRICES_TECNICAS_BACKEND.md)
- [ ] ✅ Tests agregados/actualizados (cobertura mínima)
- [ ] ✅ Documentación actualizada (JavaDoc, README si es necesario)
- [ ] ✅ Sin código comentado innecesario
- [ ] ✅ Sin logs de debug (`System.out.println`)
- [ ] ✅ Migraciones Flyway incluidas si hay cambios en BD
- [ ] ✅ Swagger/OpenAPI actualizado
- [ ] ✅ Commits siguen Conventional Commits
- [ ] ✅ Build exitoso (`mvn clean install`)
- [ ] ✅ Tests pasan (`mvn test`)

---

## 📞 Contacto y Soporte

**Equipo Técnico Predio Mijangos**

- **GitHub Issues:** [Reportar bug o solicitar feature](https://github.com/aruano/predioMijangos/issues)
- **Email:** [correo del equipo]
- **Documentación:** Ver carpeta `docs/`

---

## 🔗 Enlaces Útiles

### Documentación del Proyecto
- 📘 [Directrices Técnicas Backend](../docs/DIRECTRICES_TECNICAS_BACKEND.md) ⭐
- 📙 [Arquitectura Backend Completa](../docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md) ⭐
- 📕 [Arquitectura de Solución](../docs/ARQUITECTURA_DE_SOLUCIÓN.pdf)
- 📗 [README Principal](../README.md)

### Herramientas en Desarrollo
- 🏠 **API Root:** http://localhost:8080/api/
- 📚 **Swagger UI:** http://localhost:8080/api/swagger-ui.html
- 📋 **OpenAPI Docs:** http://localhost:8080/api/v3/api-docs
- ❤️ **Health Check:** http://localhost:8080/api/actuator/health

### Documentación Técnica Externa
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [MapStruct Documentation](https://mapstruct.org/)
- [Flyway Documentation](https://flywaydb.org/documentation/)

---

## ⚠️ IMPORTANTE

**Antes de empezar a desarrollar, LEER:**

1. 📘 **[Directrices Técnicas del Backend](../docs/DIRECTRICES_TECNICAS_BACKEND.md)**
2. 📙 **[Documentación de Arquitectura Backend](../docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md)**

Estos documentos son la **fuente de verdad** para el desarrollo del backend.

---

**© 2025 Predio Mijangos. Todos los derechos reservados.**
