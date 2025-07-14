# Predio Mijangos � Plataforma de Gesti�n Multiplataforma

**Proyecto de tesis de Ingenier�a en Sistemas**  
Gesti�n de inventarios, ventas, compras, clientes, proveedores y desmantelamiento de veh�culos/repuestos.

---

## Descripci�n General

Este proyecto es una **plataforma multiplataforma** (web y m�vil) para la gesti�n integral de un predio automotriz y distribuidora de repuestos, cubriendo procesos de inventarios, ventas, compras, clientes, proveedores, veh�culos y desmantelamiento, con enfoque profesional y seguridad robusta.

**Tecnolog�as principales:**
- **Backend:** Java 17+, Spring Boot (microservicios, JWT Security)
- **Frontend Web:** Vaadin
- **Frontend M�vil:** React Native
- **Base de datos:** MySQL 8+
- **Infraestructura:** Local o Docker (pensado para despliegue en AWS a futuro)

---

## Estructura del Monorepo

predio-mijangos/
??? backend/ # API RESTful Spring Boot (Java)
??? web/ # Frontend web (Vaadin)
??? mobile/ # App m�vil (React Native)
??? db/ # Scripts SQL, modelo ER, migraciones
??? docs/ # Documentaci�n, minutas y mockups
??? .gitignore
??? README.md


---

## Caracter�sticas Destacadas

- **Arquitectura por capas y buenas pr�cticas.**
- **Seguridad con Spring Security + JWT:**  
  Control de acceso por roles (ADMIN, SUPERVISOR, VENDEDOR, SUPERVISOR_JEFE).
- **Respuestas API estandarizadas:**  
  Estructura JSON uniforme con `message`, `body`, `statusCode`, y `timestamp`.
- **Estructura profesional de paquetes:**  
  Separaci�n clara de controllers, services, models, DTOs, repositories, exceptions, utilidades y configuraci�n.
- **Soporte para integraci�n web y m�vil desde el primer d�a.**

---

## Prerrequisitos

- **Java JDK 17+**
- **Node.js 18+** (recomendado usar NVM)
- **MySQL 8.x** (Docker o instalaci�n local)
- **NetBeans 15+** (para backend Java)
- **Visual Studio Code** (web y m�vil)
- **Docker Desktop** (opcional, para despliegue y base de datos)

---

## Gu�a de Instalaci�n y Ejecuci�n Local

### 1. Clona el repositorio

git clone https://github.com/tuusuario/predio-mijangos.git
cd predio-mijangos

### 2. Configura la base de datos (MySQL)
- Crea la base de datos predio_mijangos y el usuario con permisos.
- Usa los scripts en la carpeta db/ para crear las tablas y poblar cat�logos iniciales.
- Ajusta la conexi�n en backend/src/main/resources/application.properties:

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

### 5. Ejecuta la App M�vil (React Native)
cd mobile
npm install
npx expo start
## Seguridad y autenticaci�n
- Login y autenticaci�n JWT:
El endpoint /auth/login retorna un token seguro para el frontend.

- Control de acceso por roles:
Rutas protegidas y acceso restringido seg�n perfil.

- Manejo global de excepciones y respuestas est�ndar.

### Documentaci�n y soporte
- Checklist, cronograma de sprints, mockups, casos de uso y minutas: carpeta docs/
- Modelo entidad-relaci�n y scripts SQL: carpeta db/

## Cr�ditos y referencias acad�micas
Proyecto de tesis para la carrera de Ingenier�a en Sistemas
Predio Mijangos, Guatemala � 2025