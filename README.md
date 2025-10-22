# Predio Mijangos – Plataforma de Gestión Multiplataforma

**Proyecto de tesis de Ingeniería en Sistemas**  
Gestión de inventarios, ventas, compras y desmantelamiento de vehículos/repuestos.

---

## 📖 Descripción General

Este proyecto corresponde al desarrollo de una **plataforma multiplataforma** (web y móvil) para la gestión integral de un predio automotriz y distribuidora de repuestos, abarcando inventarios, ventas, compras, clientes, proveedores, vehículos y procesos de desmantelamiento.

**Tecnologías principales:**
- **Backend:** Java 17+, Spring Boot 3.x (monolito modular)
- **Frontend Web:** React + TypeScript (próximamente)
- **Frontend Móvil:** React Native (próximamente)
- **Base de datos:** MySQL 8.0+
- **Infraestructura:** AWS (EC2, RDS, S3)

---

## 📂 Estructura del Proyecto

```
predio-mijangos/
│
├── backend/                  # API RESTful Spring Boot (Java)
│   ├── src/
│   ├── pom.xml
│   └── README.md            # Ver documentación del backend
│
├── predio-web/              # Frontend web (React + TypeScript)
│   └── (en desarrollo)
│
├── mobile/                  # App móvil (React Native)
│   └── (planificado)
│
├── docs/                    # 📚 Documentación del proyecto
│   ├── DOCUMENTACION_ARQUITECTURA_BACKEND.md     # ⭐ Arquitectura detallada
│   ├── DIRECTRICES_TECNICAS_BACKEND.md           # ⭐ Estándares y mejores prácticas
│   ├── ARQUITECTURA_DE_SOLUCIÓN.pdf
│   ├── Predio_Mijangos_WEB.pdf
│   ├── PREDIO_MIJANGOS_APP.pdf
│   └── ...
│
├── .gitignore
└── README.md                # Este archivo
```

---

## 📚 Documentación Principal

### 🎯 Documentación Obligatoria (Backend)

Antes de comenzar a desarrollar, **leer estos documentos**:

1. **[📘 Directrices Técnicas del Backend](docs/DIRECTRICES_TECNICAS_BACKEND.md)**
   - Stack tecnológico
   - Estándares de base de datos
   - Patrones de código (SOLID)
   - Naming conventions
   - Seguridad y JWT
   - Testing y despliegue
   - **Checklist para módulos nuevos**

2. **[📙 Documentación de Arquitectura Backend](docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md)**
   - Explicación detallada de cada paquete (`core`, `security`, `modules`)
   - Descripción de cada clase y su responsabilidad
   - Flujos de datos principales
   - Diagramas de arquitectura
   - Mejores prácticas implementadas

### 📋 Otros Documentos Importantes

- **[Arquitectura de Solución](docs/ARQUITECTURA_DE_SOLUCIÓN.pdf)** - Visión general del sistema
- **[Mockups Web](docs/Predio_Mijangos_WEB.pdf)** - Diseños de interfaz web
- **[Mockups App Móvil](docs/PREDIO_MIJANGOS_APP.pdf)** - Diseños de interfaz móvil

---

## 🔧 Prerrequisitos

### Backend
- **Java JDK 17+**
- **Maven 3.8+**
- **MySQL 8.0+**
- **IDE recomendado:** IntelliJ IDEA, Eclipse, o NetBeans 15+

### Frontend Web (próximamente)
- **Node.js 18+** (recomendado usar NVM)
- **npm o yarn**
- **IDE recomendado:** Visual Studio Code

### App Móvil (planificado)
- **Node.js 18+**
- **React Native CLI** o **Expo**
- **Android Studio** y/o **Xcode**

### Herramientas Opcionales
- **Docker Desktop** (para base de datos y contenedores)
- **Postman** o **Insomnia** (testing de API)
- **DBeaver** o **MySQL Workbench** (gestión de BD)

---

## 🚀 Guía de Instalación y Ejecución Local

### 1. Clonar el repositorio

```bash
git clone https://github.com/tuusuario/predio-mijangos.git
cd predio-mijangos
```

### 2. Configurar la base de datos (MySQL)

**Opción A: Local**

```sql
CREATE DATABASE predio_mijangos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'predio_user'@'localhost' IDENTIFIED BY 'Predio2025!';
GRANT ALL PRIVILEGES ON predio_mijangos.* TO 'predio_user'@'localhost';
FLUSH PRIVILEGES;
```

**Opción B: Docker**

```bash
docker run -d \
  --name mysql-predio \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=predio_mijangos \
  -e MYSQL_USER=predio_user \
  -e MYSQL_PASSWORD=Predio2025! \
  -p 3307:3306 \
  mysql:8.0
```

### 3. Backend (Spring Boot)

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

El backend estará disponible en: **http://localhost:8080/api**

**Documentación interactiva (Swagger):** http://localhost:8080/api/swagger-ui.html

> 📖 **Ver más detalles:** [backend/README.md](backend/README.md)

### 4. Frontend Web (en desarrollo)

```bash
cd predio-web
npm install
npm start
```

### 5. App Móvil (planificado)

```bash
cd mobile
npm install
npx expo start
```

---

## 🏗️ Arquitectura del Sistema

### Backend: Monolito Modular

```
com.predio.mijangos/
├── core/                    # Funcionalidad transversal
│   ├── config/             # Configuraciones (Security, Swagger, Auditoría)
│   ├── constants/          # Constantes (Security, Errors, Validation)
│   ├── entity/             # BaseEntity (auditoría + soft delete)
│   ├── exception/          # Manejo global de excepciones
│   └── response/           # ApiResponse, PageResponse, ErrorResponse
│
├── security/                # Autenticación y autorización
│   ├── jwt/                # JWT Utils, Filter, EntryPoint
│   ├── model/              # CustomUserDetails
│   ├── service/            # Authentication, RefreshToken
│   └── dto/                # Login, Logout, RefreshToken DTOs
│
└── modules/                 # Módulos de negocio
    ├── auth/               # Endpoints públicos de autenticación
    ├── geo/                # Catálogos geográficos (Guatemala)
    ├── usuarios/           # Gestión de usuarios, roles, permisos
    ├── compras/            # (próximamente)
    ├── ventas/             # (próximamente)
    ├── inventario/         # (próximamente)
    └── reportes/           # (próximamente)
```

> 📖 **Documentación completa:** [docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md](docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md)

### Principios Arquitectónicos

- ✅ **SOLID Principles**
- ✅ **Clean Architecture** (Capas bien definidas)
- ✅ **Domain-Driven Design** (simplificado)
- ✅ **API RESTful** (estándares HTTP)
- ✅ **Security First** (JWT, RBAC, permisos granulares)
- ✅ **Auditoría Completa** (quién y cuándo modificó cada registro)
- ✅ **Soft Delete** (recuperación de registros)

---

## 🔐 Seguridad

- **Autenticación:** JWT (JSON Web Tokens)
  - Access Token: 8 horas
  - Refresh Token: 7 días
- **Autorización:** RBAC (Role-Based Access Control) + permisos granulares
- **Roles del sistema:**
  - `ROLE_ADMIN` - Acceso total
  - `ROLE_SUPERVISOR` - Aprobaciones y supervisión
  - `ROLE_VENDEDOR` - Gestión de ventas (móvil)
  - `ROLE_BODEGUERO` - Gestión de inventario (móvil)
  - `ROLE_CONTADOR` - Reportes y análisis
  - `ROLE_OPERADOR` - Operaciones generales (oficina)
- **Permisos granulares:** `MODULO:ACCION` (ej: `USUARIOS:CREATE`, `VENTAS:READ`)
- **Encriptación:** BCrypt para contraseñas (strength 12)
- **CORS:** Configurado para web y móvil

---

## 📊 Base de Datos

- **Motor:** MySQL 8.0
- **Migraciones:** Flyway (versionado automático)
- **Naming Conventions:**
  - Tablas: `TBL_NombreTabla`
  - Catálogos: `TBL_Cat_NombreCatalogo`
  - Columnas: `snake_case`
  - Foreign Keys: `id_tabla_referenciada`
- **Auditoría:** Todos los registros tienen `created_at`, `updated_at`, `created_by`, `updated_by`
- **Soft Delete:** Campo `deleted_at` para eliminación lógica

> 📖 **Estándares completos:** [docs/DIRECTRICES_TECNICAS_BACKEND.md#4-estándares-de-base-de-datos](docs/DIRECTRICES_TECNICAS_BACKEND.md#4-estándares-de-base-de-datos)

---

## 🧪 Testing

### Backend

```bash
# Ejecutar todos los tests
mvn test

# Tests con cobertura
mvn clean verify

# Cobertura mínima requerida
# - Servicios: 80%
# - Controllers: 70%
# - Mappers: 60%
```

**Tipos de tests:**
- ✅ Tests unitarios (Mockito)
- ✅ Tests de integración (Testcontainers)
- ✅ Tests E2E (RestAssured)

---

## 📦 Despliegue

### Desarrollo Local
Ver secciones de instalación arriba.

### Staging / Producción (AWS)

**Infraestructura:**
- **Compute:** EC2 (t3.medium) con Auto Scaling
- **Database:** RDS MySQL Multi-AZ
- **Storage:** S3 (documentos, imágenes)
- **CDN:** CloudFront
- **Monitoring:** CloudWatch
- **Cache:** ElastiCache Redis (Fase 2)

**CI/CD:**
- GitHub Actions (configuración próximamente)
- Deploy automático a staging en merge a `develop`
- Deploy manual a producción desde `main`

---

## 🤝 Contribución

### Flujo de Trabajo Git

1. **Crear branch desde develop:**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/nombre-funcionalidad
   ```

2. **Desarrollar siguiendo las directrices:**
   - Leer [Directrices Técnicas](docs/DIRECTRICES_TECNICAS_BACKEND.md)
   - Seguir [Checklist para Módulos Nuevos](docs/DIRECTRICES_TECNICAS_BACKEND.md#14-checklist-por-módulo-nuevo)

3. **Commit con Conventional Commits:**
   ```bash
   git commit -m "feat(usuarios): agregar endpoint de búsqueda avanzada"
   git commit -m "fix(auth): corregir validación de refresh token"
   git commit -m "docs(arquitectura): actualizar diagrama de flujos"
   ```

4. **Push y crear Pull Request:**
   ```bash
   git push origin feature/nombre-funcionalidad
   ```
   - Crear PR hacia `develop`
   - Solicitar code review
   - Esperar aprobación antes de merge

### Convención de Commits

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` Nueva funcionalidad
- `fix:` Corrección de bug
- `refactor:` Refactorización de código
- `docs:` Cambios en documentación
- `test:` Agregar o modificar tests
- `chore:` Tareas de mantenimiento
- `style:` Cambios de formato (no afectan lógica)
- `perf:` Mejoras de rendimiento

**Formato:**
```
<tipo>(<scope>): <descripción corta>

[cuerpo opcional]

[footer opcional]
```

**Ejemplos:**
```
feat(usuarios): agregar endpoint para cambio de contraseña
fix(auth): corregir expiración de refresh tokens
docs(readme): actualizar instrucciones de instalación
refactor(roles): simplificar lógica de asignación de permisos
```

---

## 📖 Recursos y Documentación Adicional

### Documentación del Proyecto
- 📘 [Directrices Técnicas Backend](docs/DIRECTRICES_TECNICAS_BACKEND.md) ⭐
- 📙 [Arquitectura Backend](docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md) ⭐
- 📕 [Arquitectura de Solución](docs/ARQUITECTURA_DE_SOLUCIÓN.pdf)
- 📗 [README Backend](backend/README.md)

### Documentación Técnica
- [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [MapStruct](https://mapstruct.org/)
- [Flyway](https://flywaydb.org/documentation/)

### Herramientas
- **Swagger UI:** http://localhost:8080/api/swagger-ui.html
- **Actuator Health:** http://localhost:8080/api/actuator/health
- **API Base:** http://localhost:8080/api

---

## 👥 Equipo

**Proyecto de tesis - Ingeniería en Sistemas**

**Desarrolladores:**
- Equipo Técnico Predio Mijangos

**Institución:**
- Universidad [Nombre]
- Guatemala

---

## 📄 Licencia

Este proyecto es parte de una tesis académica para la carrera de Ingeniería en Sistemas.

© 2025 Predio Mijangos. Todos los derechos reservados.

---

## 📞 Contacto y Soporte

Para dudas sobre el proyecto:
- **Email:** [correo del equipo]
- **Issues:** [GitHub Issues](https://github.com/tuusuario/predio-mijangos/issues)

---

**Versión:** 2.0.0  
**Última Actualización:** 21 de Octubre 2025

---

## ⚠️ IMPORTANTE

**Antes de empezar a desarrollar, leer:**

1. 📘 **[Directrices Técnicas del Backend](docs/DIRECTRICES_TECNICAS_BACKEND.md)**
2. 📙 **[Documentación de Arquitectura Backend](docs/DOCUMENTACION_ARQUITECTURA_BACKEND.md)**

Estos documentos son la **fuente de verdad** para el desarrollo del proyecto.
