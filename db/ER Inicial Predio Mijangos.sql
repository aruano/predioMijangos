CREATE TABLE `TBL_Usuario` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `usuario` varchar2 UNIQUE NOT NULL,
  `contraseña` varchar2 NOT NULL,
  `nombre` varchar2,
  `correo` varchar2,
  `telefono` varchar2
);

CREATE TABLE `TBL_Rol` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2,
  `descripcion` varchar2,
  `admin` boolean
);

CREATE TABLE `TBL_Usuario_Rol` (
  `id_usuario` integer AUTO_INCREMENT,
  `id_rol` integer,
  PRIMARY KEY (`id_usuario`, `id_rol`)
);

CREATE TABLE `TBL_Modulo` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2 UNIQUE NOT NULL
);

CREATE TABLE `TBL_Pagina` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_modulo` integer NOT NULL,
  `nombre` varchar2 UNIQUE NOT NULL,
  `movil` boolean,
  `icon_web` varchar2,
  `icon_movil` varchar2
);

CREATE TABLE `TBL_Pagina_Rol` (
  `id_pagina` integer AUTO_INCREMENT,
  `id_rol` integer,
  PRIMARY KEY (`id_pagina`, `id_rol`)
);

CREATE TABLE `TBL_Autorizaciones` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `motivo` varchar2,
  `respuesta` varchar2,
  `modulo` char(1) COMMENT 'I=Inventario, V=Ventas, C=Compras',
  `usuario_solicitante` integer,
  `usuario_aprobador` integer,
  `estado` char(1) COMMENT 'A=Aprobada, R=Rechazada',
  `fecha_solicitud` datetime,
  `fecha_respuesta` datetime
);

CREATE TABLE `TBL_Tipo_Vehiculo` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2
);

CREATE TABLE `TBL_Marca` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2
);

CREATE TABLE `TBL_Linea` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2,
  `id_marca` integer NOT NULL
);

CREATE TABLE `TBL_Categoria_Repuesto` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2
);

CREATE TABLE `TBL_Bodega` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2,
  `direccion` varchar2,
  `telefono` varchar2
);

CREATE TABLE `TBL_Ubicacion` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2,
  `id_bodega` integer
);

CREATE TABLE `TBL_Tipo_Precio` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2
);

CREATE TABLE `TBL_Producto` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `sku` varchar2 UNIQUE NOT NULL,
  `nombre` varchar2 NOT NULL,
  `id_tipo_vehiculo` integer,
  `id_linea` integer,
  `id_categoria` integer,
  `observaciones` varchar2,
  `activo` boolean,
  `existencia_min` integer
);

CREATE TABLE `TBL_Producto_Imagen` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_producto` integer NOT NULL,
  `imagen` varchar2 NOT NULL
);

CREATE TABLE `TBL_Precio_Producto` (
  `id_tipo_precio` integer AUTO_INCREMENT,
  `id_producto` integer,
  `monto` decimal,
  `id_usuario` integer,
  `fecha_creacion` datetime,
  PRIMARY KEY (`id_tipo_precio`, `id_producto`)
);

CREATE TABLE `TBL_Precio_Historico` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_tipo_precio` integer,
  `id_producto` integer,
  `monto` decimal,
  `id_usuario` integer,
  `fecha_creacion` datetime
);

CREATE TABLE `TBL_Inventario` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_producto` integer,
  `id_ubicacion` integer,
  `existencia` integer,
  `fecha_ult_actualizacion` datetime
);

CREATE TABLE `TBL_Ajuste_Inventario` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_inventario` integer,
  `nueva_existencia` integer,
  `id_solicitud` integer
);

CREATE TABLE `TBL_Log_Inventario` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_producto` integer,
  `tipo_registro` char(1) COMMENT 'E = Entrada y S = Salida',
  `tipo_documento` char(1) COMMENT 'C = Compra, V = Venta, D = Desmantelamiento, A = Ajuste, R = Devolución',
  `documento` varchar2,
  `cantidad` integer
);

CREATE TABLE `TBL_Proveedor` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2 UNIQUE,
  `direccion` varchar2,
  `telefono1` varchar2,
  `telefono2` varchar2
);

CREATE TABLE `TBL_Compra` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_proveedor` integer NOT NULL,
  `no_documento` varchar2,
  `fecha` date,
  `impuesto` money,
  `subtotal` money,
  `total` money,
  `observaciones` varchar2,
  `comprobante` varchar2,
  `fecha_creacion` datetime,
  `usuario` integer
);

CREATE TABLE `TBL_Compra_Detalle` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_compra` integer NOT NULL,
  `id_producto` integer,
  `cantidad` integer,
  `precio` money
);

CREATE TABLE `TBL_Vehiculo` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `placa` varchar2 UNIQUE NOT NULL,
  `id_tipo_vehiculo` integer,
  `id_ubicacion` integer,
  `id_linea` integer,
  `modelo` varchar2,
  `precio_venta` money,
  `estado` char(1) COMMENT 'D: Desmantelado, N: Nuevo, U: Usado',
  `activo` boolean,
  `observaciones` vachar2
);

CREATE TABLE `TBL_Compra_Vehiculos` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_proveedor` integer NOT NULL,
  `id_vehiculo` integer,
  `no_documento` varchar2,
  `fecha` date,
  `impuesto` money,
  `subtotal` money,
  `total` money,
  `observaciones` varchar2,
  `comprobante` varchar2,
  `fecha_creacion` datetime,
  `usuario` integer
);

CREATE TABLE `TBL_Vehiculo_Imagen` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_vehiculo` integer NOT NULL,
  `imagen` varchar2 NOT NULL
);

CREATE TABLE `TBL_Vehiculo_Documento` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_vehiculo` integer NOT NULL,
  `documento` varchar2 NOT NULL
);

CREATE TABLE `TBL_Vehiculo_Desmantelamiento` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_vehiculo` integer NOT NULL,
  `estado` char(1) COMMENT 'D: En Progreso, F: Finalizado',
  `observaciones` varchar2,
  `fecha_creacion` datetime,
  `fecha_finalizacion` datetime,
  `usuario` integer
);

CREATE TABLE `TBL_Vehiculo_Desmantelamiento_Detalle` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_desmantelamiento` integer NOT NULL,
  `id_producto` integer,
  `cantidad` integer
);

CREATE TABLE `TBL_Cliente` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `identificacion` varchar2 UNIQUE NOT NULL,
  `nombres` varchar2 NOT NULL,
  `apellidos` varchar2 NOT NULL,
  `correo` varchar2,
  `telefono` varchar2,
  `direccion_entrega` varchar2,
  `observaciones` varchar2
);

CREATE TABLE `TBL_Descuento` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `titulo` varchar2,
  `descripcion` varchar2,
  `porcentaje` integer,
  `activo` boolean
);

CREATE TABLE `TBL_Venta` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_cliente` integer NOT NULL,
  `id_autorizacion` integer,
  `id_descuento` integer,
  `no_documento` varchar2,
  `tipo_pago` char(1) COMMENT 'E = Efectivo, T = Tarjeta de Crédito / Débito, C = Pago al Crédito, R = Pago contra Entrega',
  `estado` char(1) COMMENT 'D = En Progreso, A = Aprobada, C = Congelada, P = Pagada, R = Rechazada, D = Devolución',
  `vendedor` integer,
  `impuesto` money,
  `descuento` money,
  `subtotal` money,
  `total` money,
  `fecha_creacion` datetime,
  `motivo` varchar2,
  `usuario_aprobador` integer,
  `fecha_aprobacion` datetime,
  `fecha_rechazo` datetime,
  `observaciones` varchar2
);

CREATE TABLE `TBL_Venta_Detalle` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_venta` integer NOT NULL,
  `id_producto` integer NOT NULL,
  `id_solicitud_mod` integer,
  `cantidad` integer,
  `precio` money
);

CREATE TABLE `TBL_Tipo_Pago` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar2
);

CREATE TABLE `TBL_Pagos` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `id_venta` integer NOT NULL,
  `tipo_pago` integer NOT NULL,
  `monto` money,
  `comprobante` varchar2,
  `fecha_creacion` datetime
);

ALTER TABLE `TBL_Usuario_Rol` ADD CONSTRAINT `FK_Usuario_Rol` FOREIGN KEY (`id_usuario`) REFERENCES `TBL_Usuario` (`id`);

ALTER TABLE `TBL_Usuario_Rol` ADD CONSTRAINT `FK_Rol_Usuario` FOREIGN KEY (`id_rol`) REFERENCES `TBL_Rol` (`id`);

ALTER TABLE `TBL_Pagina` ADD CONSTRAINT `FK_Pagina_Modulo` FOREIGN KEY (`id_modulo`) REFERENCES `TBL_Modulo` (`id`);

ALTER TABLE `TBL_Pagina_Rol` ADD CONSTRAINT `FK_Pagina_Rol` FOREIGN KEY (`id_pagina`) REFERENCES `TBL_Pagina` (`id`);

ALTER TABLE `TBL_Pagina_Rol` ADD CONSTRAINT `FK_Rol_Pagina` FOREIGN KEY (`id_rol`) REFERENCES `TBL_Rol` (`id`);

ALTER TABLE `TBL_Linea` ADD CONSTRAINT `FK_Marca_Linea` FOREIGN KEY (`id_marca`) REFERENCES `TBL_Marca` (`id`);

ALTER TABLE `TBL_Ubicacion` ADD CONSTRAINT `FK_Bodega_Ubicacion` FOREIGN KEY (`id_bodega`) REFERENCES `TBL_Bodega` (`id`);

ALTER TABLE `TBL_Producto` ADD CONSTRAINT `FK_Tipo_Vehiculo_Producto` FOREIGN KEY (`id_tipo_vehiculo`) REFERENCES `TBL_Tipo_Vehiculo` (`id`);

ALTER TABLE `TBL_Producto` ADD CONSTRAINT `FK_Linea_Producto` FOREIGN KEY (`id_linea`) REFERENCES `TBL_Linea` (`id`);

ALTER TABLE `TBL_Producto` ADD CONSTRAINT `FK_Categoria_Repuesto_Producto` FOREIGN KEY (`id_categoria`) REFERENCES `TBL_Categoria_Repuesto` (`id`);

ALTER TABLE `TBL_Precio_Producto` ADD CONSTRAINT `FK_Precio_Producto` FOREIGN KEY (`id_tipo_precio`) REFERENCES `TBL_Tipo_Precio` (`id`);

ALTER TABLE `TBL_Precio_Producto` ADD CONSTRAINT `FK_Producto_Precio` FOREIGN KEY (`id_producto`) REFERENCES `TBL_Producto` (`id`);

ALTER TABLE `TBL_Producto_Imagen` ADD CONSTRAINT `FK_Producto_Imagen` FOREIGN KEY (`id_producto`) REFERENCES `TBL_Producto` (`id`);

ALTER TABLE `TBL_Inventario` ADD CONSTRAINT `FK_Ubicacion_Producto` FOREIGN KEY (`id_ubicacion`) REFERENCES `TBL_Ubicacion` (`id`);

ALTER TABLE `TBL_Inventario` ADD CONSTRAINT `FK_Producto_Ubicacion` FOREIGN KEY (`id_producto`) REFERENCES `TBL_Producto` (`id`);

ALTER TABLE `TBL_Ajuste_Inventario` ADD CONSTRAINT `FK_Ajuste_Inventario` FOREIGN KEY (`id_inventario`) REFERENCES `TBL_Inventario` (`id`);

ALTER TABLE `TBL_Ajuste_Inventario` ADD CONSTRAINT `FK_Autorizacion_Ajuste` FOREIGN KEY (`id_solicitud`) REFERENCES `TBL_Autorizaciones` (`id`);

ALTER TABLE `TBL_Log_Inventario` ADD CONSTRAINT `FK_Producto_Log_Inventario` FOREIGN KEY (`id_producto`) REFERENCES `TBL_Producto` (`id`);

ALTER TABLE `TBL_Compra` ADD CONSTRAINT `FK_Proveedor_Compra` FOREIGN KEY (`id_proveedor`) REFERENCES `TBL_Proveedor` (`id`);

ALTER TABLE `TBL_Compra_Detalle` ADD CONSTRAINT `FK_Compra_Detalle` FOREIGN KEY (`id_compra`) REFERENCES `TBL_Compra` (`id`);

ALTER TABLE `TBL_Compra_Detalle` ADD CONSTRAINT `FK_Producto_Compra_Detalle` FOREIGN KEY (`id_producto`) REFERENCES `TBL_Producto` (`id`);

ALTER TABLE `TBL_Vehiculo` ADD CONSTRAINT `FK_Tipo_Vehiculo_Vehiculo` FOREIGN KEY (`id_tipo_vehiculo`) REFERENCES `TBL_Tipo_Vehiculo` (`id`);

ALTER TABLE `TBL_Vehiculo` ADD CONSTRAINT `FK_Linea_Vehiculo` FOREIGN KEY (`id_linea`) REFERENCES `TBL_Linea` (`id`);

ALTER TABLE `TBL_Vehiculo` ADD CONSTRAINT `FK_Ubicacion_Vehiculo` FOREIGN KEY (`id_ubicacion`) REFERENCES `TBL_Ubicacion` (`id`);

ALTER TABLE `TBL_Compra_Vehiculos` ADD CONSTRAINT `FK_Proveedor_Vehiculo` FOREIGN KEY (`id_proveedor`) REFERENCES `TBL_Proveedor` (`id`);

ALTER TABLE `TBL_Compra_Vehiculos` ADD CONSTRAINT `FK_Vehiculo_Compra` FOREIGN KEY (`id_vehiculo`) REFERENCES `TBL_Vehiculo` (`id`);

ALTER TABLE `TBL_Vehiculo_Imagen` ADD CONSTRAINT `FK_Vehiculo_Imagen` FOREIGN KEY (`id_vehiculo`) REFERENCES `TBL_Vehiculo` (`id`);

ALTER TABLE `TBL_Vehiculo_Documento` ADD CONSTRAINT `FK_Vehiculo_Documento` FOREIGN KEY (`id_vehiculo`) REFERENCES `TBL_Vehiculo` (`id`);

ALTER TABLE `TBL_Vehiculo_Desmantelamiento` ADD CONSTRAINT `FK_Vehiculo_Desmantelamiento` FOREIGN KEY (`id_vehiculo`) REFERENCES `TBL_Vehiculo` (`id`);

ALTER TABLE `TBL_Vehiculo_Desmantelamiento_Detalle` ADD CONSTRAINT `FK_Vehiculo_Desmantelamiento_Detalle` FOREIGN KEY (`id_desmantelamiento`) REFERENCES `TBL_Vehiculo_Desmantelamiento` (`id`);

ALTER TABLE `TBL_Vehiculo_Desmantelamiento_Detalle` ADD CONSTRAINT `FK_Producto_Desmantelamiento` FOREIGN KEY (`id_producto`) REFERENCES `TBL_Producto` (`id`);

ALTER TABLE `TBL_Venta` ADD CONSTRAINT `FK_Cliente_Venta` FOREIGN KEY (`id_cliente`) REFERENCES `TBL_Cliente` (`id`);

ALTER TABLE `TBL_Venta` ADD CONSTRAINT `FK_Descuento_Venta` FOREIGN KEY (`id_descuento`) REFERENCES `TBL_Descuento` (`id`);

ALTER TABLE `TBL_Venta_Detalle` ADD CONSTRAINT `FK_Venta_Detalle` FOREIGN KEY (`id_venta`) REFERENCES `TBL_Venta` (`id`);

ALTER TABLE `TBL_Venta_Detalle` ADD CONSTRAINT `FK_Producto_Venta_Detalle` FOREIGN KEY (`id_producto`) REFERENCES `TBL_Producto` (`id`);

ALTER TABLE `TBL_Venta` ADD CONSTRAINT `FK_Autorizacion_Venta_Devolucion` FOREIGN KEY (`id_autorizacion`) REFERENCES `TBL_Autorizaciones` (`id`);

ALTER TABLE `TBL_Venta_Detalle` ADD CONSTRAINT `FK_Autorizacion_Venta_Detalle` FOREIGN KEY (`id_solicitud_mod`) REFERENCES `TBL_Autorizaciones` (`id`);

ALTER TABLE `TBL_Pagos` ADD CONSTRAINT `FK_Venta_Pago` FOREIGN KEY (`id_venta`) REFERENCES `TBL_Venta` (`id`);

ALTER TABLE `TBL_Pagos` ADD CONSTRAINT `FK_Tipo_Pago` FOREIGN KEY (`tipo_pago`) REFERENCES `TBL_Tipo_Pago` (`id`);
