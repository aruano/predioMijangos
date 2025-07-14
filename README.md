# Predio Mijangos – Plataforma de Gestión Multiplataforma

**Proyecto de tesis de Ingeniería en Sistemas**  
Gestión de inventarios, ventas, compras, clientes, proveedores y desmantelamiento de vehículos/repuestos.

---

## Descripción General

Este proyecto es una **plataforma multiplataforma** (web y móvil) para la gestión integral de un predio automotriz y distribuidora de repuestos, cubriendo procesos de inventarios, ventas, compras, clientes, proveedores, vehículos y desmantelamiento, con enfoque profesional y seguridad robusta.

**Tecnologías principales:**
- **Backend:** Java 17+, Spring Boot (microservicios, JWT Security)
- **Frontend Web:** Vaadin
- **Frontend Móvil:** React Native
- **Base de datos:** MySQL 8+
- **Infraestructura:** Local o Docker (pensado para despliegue en AWS a futuro)

---

## Estructura del Monorepo

predio-mijangos/
??? backend/ # API RESTful Spring Boot (Java)
??? web/ # Frontend web (Vaadin)
??? mobile/ # App móvil (React Native)
??? db/ # Scripts SQL, modelo ER, migraciones
??? docs/ # Documentación, minutas y mockups
??? .gitignore
??? README.md


---

## Características Destacadas

- **Arquitectura por capas y buenas prácticas.**
- **Seguridad con Spring Security + JWT:**  
  Control de acceso por roles (ADMIN, SUPERVISOR, VENDEDOR, SUPERVISOR_JEFE).
- **Respuestas API estandarizadas:**  
  Estructura JSON uniforme con `message`, `body`, `statusCode`, y `timestamp`.
- **Estructura profesional de paquetes:**  
  Separación clara de controllers, services, models, DTOs, repositories, exceptions, utilidades y configuración.
- **Soporte para integración web y móvil desde el primer día.**

---

## Prerrequisitos

- **Java JDK 17+**
- **Node.js 18+** (recomendado usar NVM)
- **MySQL 8.x** (Docker o instalación local)
- **NetBeans 15+** (para backend Java)
- **Visual Studio Code** (web y móvil)
- **Docker Desktop** (opcional, para despliegue y base de datos)

---

## Guía de Instalación y Ejecución Local

### 1. Clona el repositorio

git clone https://github.com/tuusuario/predio-mijangos.git
cd predio-mijangos

### 2. Configura la base de datos (MySQL)
- Crea la base de datos predio_mijangos y el usuario con permisos.
- Usa los scripts en la carpeta db/ para crear las tablas y poblar catálogos iniciales.
- Ajusta la conexión en backend/src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/predio_mijangos
spring.datasource.username=predio_user
spring.datasource.password=********

### 3. Ejecuta el Backend (Spring Boot)
cd backend
./mvnw spring-boot:run
O abre el proyecto en NetBeans y ejecuta desde el IDE.

### 4. Ejecuta el Frontend Web (Vaadin)
cd web
npm install
npm start

### 5. Ejecuta la App Móvil (React Native)
cd mobile
npm install
npx expo start
## Seguridad y autenticación
- Login y autenticación JWT:
El endpoint /auth/login retorna un token seguro para el frontend.

- Control de acceso por roles:
Rutas protegidas y acceso restringido según perfil.

- Manejo global de excepciones y respuestas estándar.

### Documentación y soporte
- Checklist, cronograma de sprints, mockups, casos de uso y minutas: carpeta docs/
- Modelo entidad-relación y scripts SQL: carpeta db/

## Créditos y referencias académicas
Proyecto de tesis para la carrera de Ingeniería en Sistemas
Predio Mijangos, Guatemala – 2025