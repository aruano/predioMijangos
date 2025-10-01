-- ===== MÓDULOS / PÁGINAS / PERMISOS POR ROL =====

CREATE TABLE IF NOT EXISTS TBL_Modulo (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS TBL_Pagina (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_modulo INT NOT NULL,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  movil BOOLEAN,
  icon VARCHAR(100),
  redirect VARCHAR(100),
  CONSTRAINT FK_Pagina_Modulo FOREIGN KEY (id_modulo) REFERENCES TBL_Modulo(id)
);

CREATE TABLE IF NOT EXISTS TBL_Pagina_Rol (
  id_pagina INT NOT NULL,
  id_rol INT NOT NULL,
  PRIMARY KEY (id_pagina, id_rol),
  CONSTRAINT FK_Pagina_Rol_Pagina FOREIGN KEY (id_pagina) REFERENCES TBL_Pagina(id),
  CONSTRAINT FK_Pagina_Rol_Rol FOREIGN KEY (id_rol) REFERENCES TBL_Rol(id)
);

-- Opcional: SEED de ejemplo (ajústalo a tu preferencia)
INSERT INTO TBL_Modulo (id, nombre) VALUES
 (1,'Seguridad'), (2,'Compras'), (3,'Inventario'), (4,'Ventas'), (5, 'Vehículos')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO TBL_Pagina (id, id_modulo, nombre, movil, icon, redirect) VALUES
 (1,1,'Usuarios',false,'users', 'seguridad/usuarios'),
 (2,1,'Roles',false,'shield','seguridad/roles'),
 (3,2,'Proveedores',false,'compras/proveedores',''),
 (4,2,'Productos',false,'box', '')
ON DUPLICATE KEY UPDATE id_modulo = VALUES(id_modulo), movil = VALUES(movil),
  icon = VALUES(icon), redirect = VALUES(redirect);

-- Asignaciones de ejemplo: ADMIN ve todo; VENDEDOR ve Pre-ventas; OFICINA ve Inventario y Confirmación Ventas
INSERT INTO TBL_Pagina_Rol (id_pagina, id_rol) VALUES
 (1,2),(2,2),(3,2),(4,2),   -- ADMIN (id_rol=2)
 (3,3)                            -- OFICINA (id_rol=3)  Productos + Confirmación
ON DUPLICATE KEY UPDATE id_rol = VALUES(id_rol);
