-- Crear tabla Proveedores
CREATE TABLE IF NOT EXISTS TBL_Proveedor (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) UNIQUE,
  direccion VARCHAR(150),
  telefono VARCHAR(15),
  celular VARCHAR(15),
  correo VARCHAR(100),
  observaciones VARCHAR(300),
  activo BOOLEAN NOT NULL DEFAULT TRUE
);

DROP INDEX IF EXISTS idx_proveedor_nombre ON TBL_Proveedor;
CREATE INDEX idx_proveedor_nombre ON TBL_Proveedor (nombre);

DROP INDEX IF EXISTS idx_proveedor_activo ON TBL_Proveedor;
CREATE INDEX idx_proveedor_activo ON TBL_Proveedor (activo);