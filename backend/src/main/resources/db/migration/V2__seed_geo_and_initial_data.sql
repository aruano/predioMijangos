-- =====================================================
-- MIGRACIÓN V2: DATOS SEMILLA
-- Proyecto: Predio Mijangos
-- Descripción: Inserta datos iniciales de departamentos,
--              municipios, roles y usuario administrador
-- =====================================================

-- =====================================================
-- 1. DEPARTAMENTOS DE GUATEMALA
-- =====================================================

INSERT INTO TBL_Departamento (nombre) VALUES
('Alta Verapaz'),
('Baja Verapaz'),
('Chimaltenango'),
('Chiquimula'),
('El Progreso'),
('Escuintla'),
('Guatemala'),
('Huehuetenango'),
('Izabal'),
('Jalapa'),
('Jutiapa'),
('Petén'),
('Quetzaltenango'),
('Quiché'),
('Retalhuleu'),
('Sacatepéquez'),
('San Marcos'),
('Santa Rosa'),
('Sololá'),
('Suchitepéquez'),
('Totonicapán'),
('Zacapa');

-- =====================================================
-- 2. MUNICIPIOS (Muestra de los más importantes)
-- =====================================================

-- Guatemala
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
('Guatemala', (SELECT id FROM TBL_Departamento WHERE nombre = 'Guatemala')),
('Mixco', (SELECT id FROM TBL_Departamento WHERE nombre = 'Guatemala')),
('Villa Nueva', (SELECT id FROM TBL_Departamento WHERE nombre = 'Guatemala')),
('San Miguel Petapa', (SELECT id FROM TBL_Departamento WHERE nombre = 'Guatemala')),
('Villa Canales', (SELECT id FROM TBL_Departamento WHERE nombre = 'Guatemala'));

-- Sacatepéquez
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
('Antigua Guatemala', (SELECT id FROM TBL_Departamento WHERE nombre = 'Sacatepéquez')),
('Ciudad Vieja', (SELECT id FROM TBL_Departamento WHERE nombre = 'Sacatepéquez')),
('Jocotenango', (SELECT id FROM TBL_Departamento WHERE nombre = 'Sacatepéquez')),
('San Antonio Aguas Calientes', (SELECT id FROM TBL_Departamento WHERE nombre = 'Sacatepéquez')),
('Santa Catarina Barahona', (SELECT id FROM TBL_Departamento WHERE nombre = 'Sacatepéquez')),
('Sumpango', (SELECT id FROM TBL_Departamento WHERE nombre = 'Sacatepéquez'));

-- Escuintla
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
('Escuintla', (SELECT id FROM TBL_Departamento WHERE nombre = 'Escuintla')),
('Santa Lucía Cotzumalguapa', (SELECT id FROM TBL_Departamento WHERE nombre = 'Escuintla')),
('La Democracia', (SELECT id FROM TBL_Departamento WHERE nombre = 'Escuintla'));

-- Quetzaltenango
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
('Quetzaltenango', (SELECT id FROM TBL_Departamento WHERE nombre = 'Quetzaltenango')),
('Coatepeque', (SELECT id FROM TBL_Departamento WHERE nombre = 'Quetzaltenango')),
('Salcajá', (SELECT id FROM TBL_Departamento WHERE nombre = 'Quetzaltenango'));

-- =====================================================
-- 3. MÓDULOS DEL SISTEMA
-- =====================================================

INSERT INTO TBL_Modulo (nombre, descripcion, orden, activo) VALUES
('Seguridad', 'Gestión de usuarios, roles y permisos', 1, TRUE),
('Inventario', 'Gestión de productos y stock', 2, TRUE),
('Ventas', 'Gestión de ventas y facturación', 3, TRUE),
('Compras', 'Gestión de compras y proveedores', 4, TRUE),
('Vehículos', 'Gestión de vehículos y desmantelamiento', 5, TRUE),
('Reportes', 'Reportes y estadísticas', 6, TRUE),
('Clientes', 'Gestión de clientes', 7, TRUE);

-- =====================================================
-- 4. PÁGINAS DEL SISTEMA (Ejemplos principales)
-- =====================================================

-- Módulo Seguridad
INSERT INTO TBL_Pagina (id_modulo, nombre, descripcion, movil, icon_web, redirect_web, orden, activo) VALUES
((SELECT id FROM TBL_Modulo WHERE nombre = 'Seguridad'), 'Usuarios', 'Gestión de usuarios', FALSE, 'users', '/usuarios', 1, TRUE),
((SELECT id FROM TBL_Modulo WHERE nombre = 'Seguridad'), 'Roles', 'Gestión de roles', FALSE, 'shield', '/roles', 2, TRUE),
((SELECT id FROM TBL_Modulo WHERE nombre = 'Seguridad'), 'Permisos', 'Gestión de permisos', FALSE, 'lock', '/permisos', 3, TRUE);

-- Módulo Inventario
INSERT INTO TBL_Pagina (id_modulo, nombre, descripcion, movil, icon_web, redirect_web, orden, activo) VALUES
((SELECT id FROM TBL_Modulo WHERE nombre = 'Inventario'), 'Productos', 'Catálogo de productos', TRUE, 'package', '/productos', 1, TRUE),
((SELECT id FROM TBL_Modulo WHERE nombre = 'Inventario'), 'Ajustes', 'Ajustes de inventario', FALSE, 'edit', '/inventario/ajustes', 2, TRUE),
((SELECT id FROM TBL_Modulo WHERE nombre = 'Inventario'), 'Ubicaciones', 'Gestión de ubicaciones', FALSE, 'map-pin', '/ubicaciones', 3, TRUE);

-- Módulo Ventas
INSERT INTO TBL_Pagina (id_modulo, nombre, descripcion, movil, icon_web, redirect_web, orden, activo) VALUES
((SELECT id FROM TBL_Modulo WHERE nombre = 'Ventas'), 'Nueva Venta', 'Crear nueva venta', TRUE, 'shopping-cart', '/ventas/nueva', 1, TRUE),
((SELECT id FROM TBL_Modulo WHERE nombre = 'Ventas'), 'Historial Ventas', 'Historial de ventas', TRUE, 'list', '/ventas/historial', 2, TRUE),
((SELECT id FROM TBL_Modulo WHERE nombre = 'Ventas'), 'Devoluciones', 'Gestión de devoluciones', FALSE, 'rotate-ccw', '/ventas/devoluciones', 3, TRUE);

-- Módulo Clientes
INSERT INTO TBL_Pagina (id_modulo, nombre, descripcion, movil, icon_web, redirect_web, orden, activo) VALUES
((SELECT id FROM TBL_Modulo WHERE nombre = 'Clientes'), 'Listado Clientes', 'Listado de clientes', TRUE, 'users', '/clientes', 1, TRUE),
((SELECT id FROM TBL_Modulo WHERE nombre = 'Clientes'), 'Nuevo Cliente', 'Registrar nuevo cliente', TRUE, 'user-plus', '/clientes/nuevo', 2, TRUE);

-- =====================================================
-- 5. ROLES INICIALES
-- =====================================================

INSERT INTO TBL_Rol (nombre, descripcion, admin, activo) VALUES
('ADMIN', 'Administrador con acceso total', TRUE, TRUE),
('SUPERVISOR', 'Supervisor con permisos de supervisión', FALSE, TRUE),
('OPERADOR', 'Personal operativo de logística', FALSE, TRUE),
('VENDEDOR', 'Vendedor con acceso a ventas', FALSE, TRUE),
('BODEGUERO', 'Encargado de bodega e inventario', FALSE, TRUE);

-- =====================================================
-- 6. ASIGNACIÓN DE PÁGINAS A ROLES
-- =====================================================

-- ADMIN: Acceso a todas las páginas
INSERT INTO TBL_Pagina_Rol (id_pagina, id_rol)
SELECT p.id, r.id 
FROM TBL_Pagina p
CROSS JOIN TBL_Rol r
WHERE r.nombre = 'ADMIN';

-- VENDEDOR: Solo ventas y clientes
INSERT INTO TBL_Pagina_Rol (id_pagina, id_rol)
SELECT p.id, r.id 
FROM TBL_Pagina p
CROSS JOIN TBL_Rol r
WHERE r.nombre = 'VENDEDOR' 
AND p.nombre IN ('Nueva Venta', 'Historial Ventas', 'Listado Clientes', 'Nuevo Cliente', 'Productos');

-- BODEGUERO: Inventario y productos
INSERT INTO TBL_Pagina_Rol (id_pagina, id_rol)
SELECT p.id, r.id 
FROM TBL_Pagina p
CROSS JOIN TBL_Rol r
WHERE r.nombre = 'BODEGUERO' 
AND p.nombre IN ('Productos', 'Ajustes', 'Ubicaciones');

-- =====================================================
-- 7. USUARIO ADMINISTRADOR INICIAL
-- =====================================================

-- Crear persona para el administrador
INSERT INTO TBL_Persona (tipo_identificacion, identificacion, nombres, apellidos, correo, telefono, id_municipio)
VALUES (
    'DPI',
    '1234567890101',
    'Administrador',
    'Sistema',
    'admin@prediomijangos.com',
    '50212345678',
    (SELECT id FROM TBL_Municipio WHERE nombre = 'Guatemala' LIMIT 1)
);

-- Crear usuario administrador
-- Password: Admin123! (BCrypt hash)
INSERT INTO TBL_Usuario (usuario, password, id_persona, activo)
VALUES (
    'ADMIN',
    '$2a$10$rO.qZ3KL1VqKvhEqL5qXWeYZxZ3sQGE1gkJOHQOJX6gKJMYvQHYqG',
    (SELECT id FROM TBL_Persona WHERE identificacion = '1234567890101'),
    TRUE
);

-- Asignar rol ADMIN al usuario
INSERT INTO TBL_Usuario_Rol (id_usuario, id_rol)
VALUES (
    (SELECT id FROM TBL_Usuario WHERE usuario = 'ADMIN'),
    (SELECT id FROM TBL_Rol WHERE nombre = 'ADMIN')
);

-- =====================================================
-- 8. USUARIOS DE PRUEBA ADICIONALES
-- =====================================================

-- Vendedor 001
INSERT INTO TBL_Persona (tipo_identificacion, identificacion, nombres, apellidos, correo, telefono)
VALUES ('DPI', '2345678901012', 'Carlos', 'Vendedor', 'vendedor@prediomijangos.com', '50223456789');

INSERT INTO TBL_Usuario (usuario, password, id_persona, activo)
VALUES (
    'VEND001',
    '$2a$10$rO.qZ3KL1VqKvhEqL5qXWeYZxZ3sQGE1gkJOHQOJX6gKJMYvQHYqG',
    (SELECT id FROM TBL_Persona WHERE identificacion = '2345678901012'),
    TRUE
);

INSERT INTO TBL_Usuario_Rol (id_usuario, id_rol)
VALUES (
    (SELECT id FROM TBL_Usuario WHERE usuario = 'VEND001'),
    (SELECT id FROM TBL_Rol WHERE nombre = 'VENDEDOR')
);

-- Bodeguero 001
INSERT INTO TBL_Persona (tipo_identificacion, identificacion, nombres, apellidos, correo, telefono)
VALUES ('DPI', '3456789012013', 'Juan', 'Bodeguero', 'bodeguero@prediomijangos.com', '50234567890');

INSERT INTO TBL_Usuario (usuario, password, id_persona, activo)
VALUES (
    'BOD001',
    '$2a$10$rO.qZ3KL1VqKvhEqL5qXWeYZxZ3sQGE1gkJOHQOJX6gKJMYvQHYqG',
    (SELECT id FROM TBL_Persona WHERE identificacion = '3456789012013'),
    TRUE
);

INSERT INTO TBL_Usuario_Rol (id_usuario, id_rol)
VALUES (
    (SELECT id FROM TBL_Usuario WHERE usuario = 'BOD001'),
    (SELECT id FROM TBL_Rol WHERE nombre = 'BODEGUERO')
);

-- =====================================================
-- NOTAS:
-- Password para todos los usuarios de prueba: Admin123!
-- =====================================================