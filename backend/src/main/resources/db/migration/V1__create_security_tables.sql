-- =====================================================
-- MIGRACIÓN V1: ESTRUCTURA CORRECTA DE SEGURIDAD
-- Proyecto: Predio Mijangos - Sistema de Gestión
-- Fecha: Octubre 2025
-- Descripción: Crea tablas de Persona, Usuario, Cliente,
--              Roles, Permisos, Módulos, Páginas y RefreshToken
-- =====================================================

-- =====================================================
-- 1. MÓDULO GEOGRÁFICO (Catálogos)
-- =====================================================

-- Tabla: Departamentos de Guatemala
CREATE TABLE TBL_Departamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Catálogo de departamentos de Guatemala';

-- Tabla: Municipios de Guatemala
CREATE TABLE TBL_Municipio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    id_departamento INT NOT NULL,
    
    CONSTRAINT fk_municipio_departamento 
        FOREIGN KEY (id_departamento) REFERENCES TBL_Departamento(id)
        ON DELETE RESTRICT,
    CONSTRAINT uk_municipio_nombre_depto 
        UNIQUE (nombre, id_departamento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Catálogo de municipios por departamento';

-- Índices para Municipio
CREATE INDEX idx_municipio_departamento ON TBL_Municipio(id_departamento);
CREATE INDEX idx_municipio_nombre ON TBL_Municipio(nombre);

-- =====================================================
-- 2. MÓDULO DE PERSONAS (Base Compartida)
-- =====================================================

-- Tabla: Persona (Base compartida para Usuario, Cliente, Proveedor)
CREATE TABLE TBL_Persona (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo_identificacion CHAR(15) NOT NULL COMMENT 'DPI, NIT, PASAPORTE',
    identificacion VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(100),
    telefono VARCHAR(15),
    id_municipio INT,
    direccion_domicilio VARCHAR(250),
    
    -- Auditoría
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    deleted_at TIMESTAMP NULL,
    
    CONSTRAINT fk_persona_municipio 
        FOREIGN KEY (id_municipio) REFERENCES TBL_Municipio(id)
        ON DELETE SET NULL,
    CONSTRAINT chk_tipo_identificacion 
        CHECK (tipo_identificacion IN ('DPI', 'NIT', 'PASAPORTE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Tabla base para información personal compartida';

-- Índices para Persona
CREATE INDEX idx_persona_identificacion ON TBL_Persona(identificacion);
CREATE INDEX idx_persona_nombres ON TBL_Persona(nombres);
CREATE INDEX idx_persona_apellidos ON TBL_Persona(apellidos);
CREATE INDEX idx_persona_correo ON TBL_Persona(correo);
CREATE INDEX idx_persona_created ON TBL_Persona(created_at);
CREATE INDEX idx_persona_deleted ON TBL_Persona(deleted_at);

-- =====================================================
-- 3. MÓDULO DE USUARIOS
-- =====================================================

-- Tabla: Usuario
CREATE TABLE TBL_Usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(100) NOT NULL UNIQUE COMMENT 'Código empleado o username',
    password VARCHAR(100) NOT NULL,
    id_persona INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    
    -- Auditoría
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    deleted_at TIMESTAMP NULL,
    
    CONSTRAINT fk_usuario_persona 
        FOREIGN KEY (id_persona) REFERENCES TBL_Persona(id)
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Usuarios del sistema con credenciales de acceso';

-- Índices para Usuario
CREATE INDEX idx_usuario_persona ON TBL_Usuario(id_persona);
CREATE INDEX idx_usuario_activo ON TBL_Usuario(activo);
CREATE INDEX idx_usuario_created ON TBL_Usuario(created_at);
CREATE INDEX idx_usuario_deleted ON TBL_Usuario(deleted_at);

-- Tabla: Cliente
CREATE TABLE TBL_Cliente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_persona INT NOT NULL,
    observaciones VARCHAR(100),
    
    -- Auditoría
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    deleted_at TIMESTAMP NULL,
    
    CONSTRAINT fk_cliente_persona 
        FOREIGN KEY (id_persona) REFERENCES TBL_Persona(id)
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Clientes del sistema';

-- Índices para Cliente
CREATE INDEX idx_cliente_persona ON TBL_Cliente(id_persona);
CREATE INDEX idx_cliente_created ON TBL_Cliente(created_at);
CREATE INDEX idx_cliente_deleted ON TBL_Cliente(deleted_at);

-- =====================================================
-- 4. MÓDULO DE ROLES Y PERMISOS
-- =====================================================

-- Tabla: Rol
CREATE TABLE TBL_Rol (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(100),
    admin BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    
    -- Auditoría
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    deleted_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Roles del sistema para control de acceso';

-- Índices para Rol
CREATE INDEX idx_rol_nombre ON TBL_Rol(nombre);
CREATE INDEX idx_rol_activo ON TBL_Rol(activo);
CREATE INDEX idx_rol_created ON TBL_Rol(created_at);
CREATE INDEX idx_rol_deleted ON TBL_Rol(deleted_at);

-- Tabla: Usuario_Rol (Relación Many-to-Many)
CREATE TABLE TBL_Usuario_Rol (
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,
    
    PRIMARY KEY (id_usuario, id_rol),
    
    CONSTRAINT fk_usuario_rol_usuario 
        FOREIGN KEY (id_usuario) REFERENCES TBL_Usuario(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_usuario_rol_rol 
        FOREIGN KEY (id_rol) REFERENCES TBL_Rol(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Relación entre usuarios y roles';

-- Índices para Usuario_Rol
CREATE INDEX idx_usuario_rol_usuario ON TBL_Usuario_Rol(id_usuario);
CREATE INDEX idx_usuario_rol_rol ON TBL_Usuario_Rol(id_rol);

-- =====================================================
-- 5. MÓDULO DE PÁGINAS Y PERMISOS
-- =====================================================

-- Tabla: Módulo
CREATE TABLE TBL_Modulo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    orden INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Módulos del sistema (Ventas, Inventario, etc)';

-- Índices para Módulo
CREATE INDEX idx_modulo_nombre ON TBL_Modulo(nombre);
CREATE INDEX idx_modulo_orden ON TBL_Modulo(orden);

-- Tabla: Página
CREATE TABLE TBL_Pagina (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_modulo INT NOT NULL,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    movil BOOLEAN DEFAULT FALSE,
    icon_web VARCHAR(100),
    icon_movil VARCHAR(100),
    redirect_web VARCHAR(100),
    redirect_movil VARCHAR(100),
    orden INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    
    CONSTRAINT fk_pagina_modulo 
        FOREIGN KEY (id_modulo) REFERENCES TBL_Modulo(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Páginas/Pantallas del sistema';

-- Índices para Página
CREATE INDEX idx_pagina_modulo ON TBL_Pagina(id_modulo);
CREATE INDEX idx_pagina_nombre ON TBL_Pagina(nombre);
CREATE INDEX idx_pagina_orden ON TBL_Pagina(orden);

-- Tabla: Pagina_Rol (Relación Many-to-Many)
CREATE TABLE TBL_Pagina_Rol (
    id_pagina INT NOT NULL,
    id_rol INT NOT NULL,
    
    PRIMARY KEY (id_pagina, id_rol),
    
    CONSTRAINT fk_pagina_rol_pagina 
        FOREIGN KEY (id_pagina) REFERENCES TBL_Pagina(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_pagina_rol_rol 
        FOREIGN KEY (id_rol) REFERENCES TBL_Rol(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Relación entre páginas y roles (permisos)';

-- Índices para Pagina_Rol
CREATE INDEX idx_pagina_rol_pagina ON TBL_Pagina_Rol(id_pagina);
CREATE INDEX idx_pagina_rol_rol ON TBL_Pagina_Rol(id_rol);

-- =====================================================
-- 6. MÓDULO DE REFRESH TOKENS
-- =====================================================

-- Tabla: RefreshToken
CREATE TABLE TBL_Refresh_Token (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL UNIQUE,
    id_usuario INT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    device_info VARCHAR(255),
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    revoked BOOLEAN DEFAULT FALSE,
    revoked_at TIMESTAMP NULL,
    
    CONSTRAINT fk_refresh_token_usuario 
        FOREIGN KEY (id_usuario) REFERENCES TBL_Usuario(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Tokens de refresco para autenticación JWT';

-- Índices para RefreshToken
CREATE INDEX idx_refresh_token_token ON TBL_Refresh_Token(token);
CREATE INDEX idx_refresh_token_usuario ON TBL_Refresh_Token(id_usuario);
CREATE INDEX idx_refresh_token_expiry ON TBL_Refresh_Token(expiry_date);
CREATE INDEX idx_refresh_token_revoked ON TBL_Refresh_Token(revoked);

-- =====================================================
-- 7. FOREIGN KEYS DE AUDITORÍA
-- =====================================================

-- FKs de auditoría para Persona
ALTER TABLE TBL_Persona
    ADD CONSTRAINT fk_persona_created_user 
        FOREIGN KEY (created_by) REFERENCES TBL_Usuario(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_persona_updated_user 
        FOREIGN KEY (updated_by) REFERENCES TBL_Usuario(id) ON DELETE SET NULL;

-- FKs de auditoría para Usuario
ALTER TABLE TBL_Usuario
    ADD CONSTRAINT fk_usuario_created_user 
        FOREIGN KEY (created_by) REFERENCES TBL_Usuario(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_usuario_updated_user 
        FOREIGN KEY (updated_by) REFERENCES TBL_Usuario(id) ON DELETE SET NULL;

-- FKs de auditoría para Cliente
ALTER TABLE TBL_Cliente
    ADD CONSTRAINT fk_cliente_created_user 
        FOREIGN KEY (created_by) REFERENCES TBL_Usuario(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_cliente_updated_user 
        FOREIGN KEY (updated_by) REFERENCES TBL_Usuario(id) ON DELETE SET NULL;

-- FKs de auditoría para Rol
ALTER TABLE TBL_Rol
    ADD CONSTRAINT fk_rol_created_user 
        FOREIGN KEY (created_by) REFERENCES TBL_Usuario(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_rol_updated_user 
        FOREIGN KEY (updated_by) REFERENCES TBL_Usuario(id) ON DELETE SET NULL;

-- =====================================================
-- FIN DE MIGRACIÓN V1
-- =====================================================