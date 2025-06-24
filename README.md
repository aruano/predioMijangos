# Predio Mijangos – Plataforma de Gestión Multiplataforma

**Proyecto de tesis de Ingeniería en Sistemas**  
Gestión de inventarios, ventas, compras y desmantelamiento de vehículos/repuestos.

---

## Descripción General

Este proyecto corresponde al desarrollo de una **plataforma multiplataforma** (web y móvil) para la gestión integral de un predio automotriz y distribuidora de repuestos, abarcando inventarios, ventas, compras, clientes, proveedores, vehículos y procesos de desmantelamiento.

**Tecnologías principales:**
- **Backend:** Java 17+, Spring Boot (microservicios)
- **Frontend Web:** Vaadin
- **Frontend Móvil:** React Native
- **Base de datos:** MySQL 8+
- **Infraestructura:** Local o Docker, pensado para despliegue en AWS a futuro

---

## Estructura del Monorepo

predio-mijangos/
│
├── backend/ # API RESTful Spring Boot (Java)
├── web/ # Frontend web (Vaadin)
├── mobile/ # App móvil (React Native)
├── db/ # Scripts SQL, modelo ER, migraciones
├── docs/ # Documentación y minutas
├── .gitignore
└── README.md

---

## Prerrequisitos

- **Java JDK 17+**
- **Node.js 18+** (recomendado usar NVM)
- **MySQL 8.x** (puedes usar Docker o instalación local)
- **NetBeans 15+** (para backend Java)
- **Visual Studio Code** (para web y móvil)
- **Docker Desktop** (opcional, para base de datos y despliegue rápido)

---

## Guía de Instalación y Ejecución Local

### 1. Clona el repositorio

git clone https://github.com/tuusuario/predio-mijangos.git
cd predio-mijangos

### 2. Configura la base de datos (MySQL)
Crea la base de datos predio_mijangos y el usuario con permisos adecuados.

Usa el script en db/ para crear las tablas iniciales.

Ajusta la conexión en backend/src/main/resources/application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/predio_mijangos
spring.datasource.username=predio_user
spring.datasource.password=********

### 3. Backend (Spring Boot)
cd backend
# En NetBeans o por terminal:
./mvnw spring-boot:run

### 4. Frontend Web (Vaadin)
cd web
npm install
npm start

### 5. App Móvil (React Native)
cd mobile
npm install
npx expo start
Documentación y Soporte
Checklist y cronograma de sprints

Mockups, casos de uso, y minutas en carpeta docs/

---

Créditos y referencias académicas
Proyecto de tesis para la carrera de Ingeniería en Sistemas.
Predio Mijangos, Guatemala – 2025.