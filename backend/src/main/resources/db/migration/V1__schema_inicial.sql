-- ==== DEPARTAMENTOS ====
CREATE TABLE IF NOT EXISTS TBL_Departamento (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL
);

-- ==== MUNICIPIOS ====
CREATE TABLE IF NOT EXISTS TBL_Municipio (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  id_departamento INT,
  CONSTRAINT fk_municipio_departamento FOREIGN KEY (id_departamento) REFERENCES TBL_Departamento(id)
);

DROP INDEX IF EXISTS idx_municipio_nombre ON TBL_Municipio;
CREATE INDEX idx_municipio_nombre ON TBL_Municipio (nombre);

-- ==== PERSONAS ====
CREATE TABLE TBL_Persona (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tipo_identificacion VARCHAR(20),
  identificacion VARCHAR(50),
  nombres VARCHAR(100),
  apellidos VARCHAR(100),
  correo VARCHAR(120),
  telefono VARCHAR(30),
  id_municipio INT,
  direccion_domicilio VARCHAR(200),
  CONSTRAINT fk_persona_municipio FOREIGN KEY (id_municipio) REFERENCES TBL_Municipio(id)
);

DROP INDEX IF EXISTS idx_persona_identificacion ON TBL_Persona;
CREATE INDEX idx_persona_identificacion ON TBL_Persona (identificacion);

DROP INDEX IF EXISTS idx_nombres_identificacion ON TBL_Persona;
CREATE INDEX idx_nombres_identificacion ON TBL_Persona (nombres);

DROP INDEX IF EXISTS idx_apellidos_identificacion ON TBL_Persona;
CREATE INDEX idx_apellidos_identificacion ON TBL_Persona (apellidos);

-- ==== USUARIOS ====
CREATE TABLE TBL_Usuario (
  id INT PRIMARY KEY AUTO_INCREMENT,
  usuario VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(120) NOT NULL,
  id_persona BIGINT,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_usuario_persona FOREIGN KEY (id_persona) REFERENCES TBL_Persona(id)
);

-- ==== ROLES ====
CREATE TABLE TBL_Rol (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL UNIQUE,
  descripcion VARCHAR(200),
  admin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE TBL_Usuario_Rol (
  id_usuario INT NOT NULL,
  id_rol INT NOT NULL,
  PRIMARY KEY (id_usuario, id_rol),
  CONSTRAINT fk_ur_usuario FOREIGN KEY (id_usuario) REFERENCES TBL_Usuario(id),
  CONSTRAINT fk_ur_rol FOREIGN KEY (id_rol) REFERENCES TBL_Rol(id)
);