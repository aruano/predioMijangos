# 🏢 Predio Mijangos - Backend API

Sistema de gestión de inventario y ventas para distribuidora de partes automotrices.

## 📋 Tabla de Contenidos

- [Stack Tecnológico](#-stack-tecnológico)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Testing](#-testing)
- [Arquitectura](#-arquitectura)
- [Documentación API](#-documentación-api)

## 🛠 Stack Tecnológico

- **Java 17**
- **Spring Boot 3.5.5**
- **MySQL 8.0**
- **Flyway** (Migraciones de BD)
- **JWT** (Autenticación)
- **MapStruct** (Mapeo DTOs)
- **Lombok** (Reduce boilerplate)
- **SpringDoc OpenAPI** (Swagger)

## 📦 Requisitos Previos

- Java JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Git

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/aruano/predioMijangos.git
cd predioMijangos
```

### 2. Crear base de datos

```sql
CREATE DATABASE predio_mijangos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'predio_user'@'localhost' IDENTIFIED BY 'Predio2025!';
GRANT ALL PRIVILEGES ON predio_mijangos.* TO 'predio_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Instalar dependencias

```bash
mvn clean install
```

## ⚙️ Configuración

### Variables de Entorno (Producción)

Crea un archivo `.env` con:

```properties
DB_URL=jdbc:mysql://localhost:3306/predio_mijangos
DB_USERNAME=predio_user
DB_PASSWORD=tu_password_seguro
JWT_SECRET=tu_secret_key_base64
```

### Perfiles Disponibles

- **dev** (por defecto): Desarrollo local
- **prod**: Producción

## 🏃 Ejecución

### Modo Desarrollo

```bash
mvn spring-boot:run
```

### Modo Producción

```bash
mvn clean package -Pprod
java -jar target/predio-mijangos-api.jar --spring.profiles.active=prod
```

### Con Docker (Próximamente)

```bash
docker-compose up -d
```

## 🧪 Testing

### Ejecutar todos los tests

```bash
mvn test
```

### Tests con cobertura

```bash
mvn clean verify
```

## 🏗 Arquitectura

```
com.predio.mijangos/
├── config/              # Configuraciones (Security, CORS, etc.)
├── core/
│   ├── error/          # Manejo global de excepciones
│   └── response/       # ApiResponse estándar
├── security/           # JWT, Filtros, UserDetails
└── modules/            # Módulos de negocio
    ├── compras/
    │   ├── controller/
    │   ├── service/
    │   ├── repo/
    │   ├── dto/
    │   ├── mapper/
    │   └── domain/
    ├── geo/
    ├── personas/
    └── security/
```

### Capas por Módulo

- **Controller**: Endpoints REST
- **Service**: Lógica de negocio
- **Repository**: Acceso a datos (JPA)
- **DTO**: Data Transfer Objects
- **Mapper**: Conversión Entity ↔ DTO (MapStruct)
- **Domain**: Entidades JPA

## 📚 Documentación API

Una vez iniciada la aplicación, accede a:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

## 🔐 Seguridad

- JWT con expiración de 8 horas
- CORS configurado para Web y Móvil
- Roles: `ADMIN`, `OFICINA`, `SUPERVISOR`, `VENDEDOR`
- Contraseñas encriptadas con BCrypt

## 📄 Migraciones Flyway

Las migraciones están en: `src/main/resources/db/migration/`

Nomenclatura: `V{version}__{descripcion}.sql`

Ejemplo: `V1__schema_inicial.sql`

## 🤝 Contribución

1. Crea un branch: `git checkout -b feature/nueva-funcionalidad`
2. Commit: `git commit -m 'feat: agregar nueva funcionalidad'`
3. Push: `git push origin feature/nueva-funcionalidad`
4. Crea un Pull Request

### Convención de Commits

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` Nueva funcionalidad
- `fix:` Corrección de bug
- `refactor:` Refactorización
- `docs:` Documentación
- `test:` Tests
- `chore:` Tareas de mantenimiento

## 📞 Contacto

**Equipo Técnico Predio Mijangos**

---

**Versión:** 1.0.0  
**Última Actualización:** Octubre 2025