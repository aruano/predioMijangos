-- Persona + usuario admin (hash BCrypt de "admin123" ya listo)
INSERT INTO TBL_Departamento (id, nombre) VALUES
 (1,'Guatemala'),(2,'El Progreso'),(3,'Sacatepéquez'),(4,'Chimaltenango'),
 (5,'Escuintla'),(6,'Santa Rosa'),(7,'Sololá'),(8,'Totonicapán'),
 (9,'Quetzaltenango'),(10,'Suchitepéquez'),(11,'Retalhuleu'),(12,'San Marcos'),
 (13,'Huehuetenango'),(14,'Quiché'),(15,'Baja Verapaz'),(16,'Alta Verapaz'),
 (17,'Petén'),(18,'Izabal'),(19,'Zacapa'),(20,'Chiquimula'),
 (21,'Jalapa'),(22,'Jutiapa')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Guatemala (1)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Guatemala',1),('Santa Catarina Pinula',1),('San José Pinula',1),('San José del Golfo',1),
 ('Palencia',1),('Chinautla',1),('San Pedro Ayampuc',1),('Mixco',1),
 ('San Pedro Sacatepéquez',1),('San Juan Sacatepéquez',1),('San Raymundo',1),('Chuarrancho',1),
 ('Fraijanes',1),('Amatitlán',1),('Villa Nueva',1),('Villa Canales',1),('San Miguel Petapa',1)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- El Progreso (2)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Guastatoya',2),('Morazán',2),('San Agustín Acasaguastlán',2),('San Antonio La Paz',2),
 ('San Cristóbal Acasaguastlán',2),('Sanarate',2),('Sansare',2),('El Jícaro',2)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Sacatepéquez (3)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Antigua Guatemala',3),('Ciudad Vieja',3),('Jocotenango',3),('Pastores',3),
 ('Sumpango',3),('Santo Domingo Xenacoj',3),('Santiago Sacatepéquez',3),
 ('San Bartolomé Milpas Altas',3),('San Lucas Sacatepéquez',3),('San Antonio Aguas Calientes',3),
 ('Santa Catarina Barahona',3),('Santa Lucía Milpas Altas',3),('Magdalena Milpas Altas',3),
 ('Santa María de Jesús',3),('Alotenango',3),('San Miguel Dueñas',3)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Chimaltenango (4)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Chimaltenango',4),('Acatenango',4),('El Tejar',4),('Parramos',4),
 ('Patzicía',4),('Patzún',4),('Pochuta',4),('San Andrés Itzapa',4),
 ('San José Poaquil',4),('San Juan Comalapa',4),('San Martín Jilotepeque',4),
 ('Santa Apolonia',4),('Santa Cruz Balanyá',4),('Tecpán Guatemala',4),
 ('San Pedro Yepocapa',4),('Zaragoza',4)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Escuintla (5)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Escuintla',5),('Guanagazapa',5),('Iztapa',5),('La Democracia',5),('La Gomera',5),
 ('Masagua',5),('Nueva Concepción',5),('Palín',5),('San José',5),('San Vicente Pacaya',5),
 ('Santa Lucía Cotzumalguapa',5),('Sipacate',5),('Siquinalá',5),('Tiquisate',5)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Santa Rosa (6)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Cuilapa',6),('Barberena',6),('Casillas',6),('Chiquimulilla',6),('Guazacapán',6),
 ('Nueva Santa Rosa',6),('Oratorio',6),('Pueblo Nuevo Viñas',6),('San Juan Tecuaco',6),
 ('San Rafael Las Flores',6),('Santa Cruz Naranjo',6),('Santa María Ixhuatán',6),
 ('Santa Rosa de Lima',6),('Taxisco',6)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Sololá (7)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Sololá',7),('Concepción',7),('Nahualá',7),('Panajachel',7),('San Andrés Semetabaj',7),
 ('San Antonio Palopó',7),('San José Chacayá',7),('San Juan La Laguna',7),
 ('San Lucas Tolimán',7),('San Marcos La Laguna',7),('San Pablo La Laguna',7),
 ('San Pedro La Laguna',7),('Santa Catarina Ixtahuacán',7),('Santa Catarina Palopó',7),
 ('Santa Clara La Laguna',7),('Santa Cruz La Laguna',7),('Santa Lucía Utatlán',7),
 ('Santa María Visitación',7),('Santiago Atitlán',7)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Totonicapán (8)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Totonicapán',8),('Momostenango',8),('San Andrés Xecul',8),('San Bartolo',8),
 ('San Cristóbal Totonicapán',8),('San Francisco El Alto',8),
 ('Santa Lucía La Reforma',8),('Santa María Chiquimula',8)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Quetzaltenango (9)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Quetzaltenango',9),('Almolonga',9),('Cabricán',9),('Cajolá',9),('Cantel',9),
 ('Coatepeque',9),('Colomba',9),('Concepción Chiquirichapa',9),('El Palmar',9),
 ('Flores Costa Cuca',9),('Génova',9),('Huitán',9),('La Esperanza',9),('Olintepeque',9),
 ('Palestina de Los Altos',9),('Salcajá',9),('San Carlos Sija',9),('San Francisco La Unión',9),
 ('San Juan Ostuncalco',9),('San Martín Sacatepéquez',9),('San Mateo',9),
 ('San Miguel Sigüilá',9),('Sibilia',9),('Zunil',9)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Suchitepéquez (10)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Mazatenango',10),('Chicacao',10),('Cuyotenango',10),('Patulul',10),('Pueblo Nuevo',10),
 ('Río Bravo',10),('Samayac',10),('San Antonio Suchitepéquez',10),('San Bernardino',10),
 ('San Francisco Zapotitlán',10),('San Gabriel',10),('San José El Ídolo',10),
 ('San José La Máquina',10),('San Lorenzo',10),('San Miguel Panán',10),
 ('San Pablo Jocopilas',10),('Santa Bárbara',10),('Santo Domingo Suchitepéquez',10),
 ('Santo Tomás La Unión',10),('Zunilito',10),('San Juan Bautista',10)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Retalhuleu (11)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Retalhuleu',11),('Champerico',11),('El Asintal',11),('Nuevo San Carlos',11),
 ('San Andrés Villa Seca',11),('San Felipe',11),('San Martín Zapotitlán',11),
 ('San Sebastián',11),('Santa Cruz Muluá',11)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- San Marcos (12)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('San Marcos',12),('Ayutla',12),('Catarina',12),('Comitancillo',12),('Concepción Tutuapa',12),
 ('El Quetzal',12),('El Tumbador',12),('Esquipulas Palo Gordo',12),('Ixchiguán',12),
 ('La Blanca',12),('La Reforma',12),('Malacatán',12),('Nuevo Progreso',12),('Ocós',12),
 ('Pajapita',12),('Río Blanco',12),('San Antonio Sacatepéquez',12),('San Cristóbal Cucho',12),
 ('San José El Rodeo',12),('San José Ojetenam',12),('San Lorenzo',12),('San Miguel Ixtahuacán',12),
 ('San Pablo',12),('San Pedro Sacatepéquez',12),('San Rafael Pie de la Cuesta',12),
 ('Sibinal',12),('Sipacapa',12),('Tacaná',12),('Tajumulco',12),('Tejutla',12)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Huehuetenango (13)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Huehuetenango',13),('Aguacatán',13),('Chiantla',13),('Colotenango',13),('Concepción Huista',13),
 ('Cuilco',13),('Jacaltenango',13),('La Democracia',13),('La Libertad',13),
 ('Malacatancito',13),('Nentón',13),('San Antonio Huista',13),('San Gaspar Ixchil',13),
 ('San Ildefonso Ixtahuacán',13),('San Juan Atitán',13),('San Juan Ixcoy',13),
 ('San Mateo Ixtatán',13),('San Miguel Acatán',13),('San Pedro Necta',13),
 ('San Pedro Soloma',13),('San Rafael La Independencia',13),('San Rafael Petzal',13),
 ('San Sebastián Coatán',13),('San Sebastián Huehuetenango',13),('Santa Ana Huista',13),
 ('Santa Bárbara',13),('Santa Cruz Barillas',13),('Santa Eulalia',13),
 ('Santiago Chimaltenango',13),('Tectitán',13),('Todos Santos Cuchumatán',13),
 ('Unión Cantinil',13)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Quiché (14)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Santa Cruz del Quiché',14),('Canillá',14),('Chajul',14),('Chicamán',14),('Chiché',14),
 ('Chichicastenango',14),('Chinique',14),('Cunén',14),('Ixcán',14),('Joyabaj',14),
 ('Nebaj',14),('Pachalum',14),('Patzité',14),('Sacapulas',14),('San Andrés Sajcabajá',14),
 ('San Antonio Ilotenango',14),('San Bartolomé Jocotenango',14),('San Juan Cotzal',14),
 ('San Pedro Jocopilas',14),('Uspantán',14),('Zacualpa',14)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Baja Verapaz (15)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Salamá',15),('Cubulco',15),('Granados',15),('Purulhá',15),('Rabinal',15),
 ('San Jerónimo',15),('San Miguel Chicaj',15),('Santa Cruz el Chol',15)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Alta Verapaz (16)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Cobán',16),('Santa Cruz Verapaz',16),('San Cristóbal Verapaz',16),('Tactic',16),
 ('Tamahú',16),('Tucurú',16),('Panzós',16),('Senahú',16),('San Pedro Carchá',16),
 ('San Juan Chamelco',16),('Lanquín',16),('Santa María Cahabón',16),('Chisec',16),
 ('Chahal',16),('Fray Bartolomé de las Casas',16),('Santa Catalina La Tinta',16),('Raxruhá',16)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Petén (17)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Flores',17),('San Benito',17),('San Andrés',17),('San José',17),('San Luis',17),
 ('Santa Ana',17),('Dolores',17),('Sayaxché',17),('Melchor de Mencos',17),
 ('Poptún',17),('La Libertad',17),('Las Cruces',17),('San Francisco',17),('El Chal',17)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Izabal (18)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Puerto Barrios',18),('Livingston',18),('El Estor',18),('Morales',18),('Los Amates',18)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Zacapa (19)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Zacapa',19),('Cabañas',19),('Estanzuela',19),('Gualán',19),('Huité',19),
 ('La Unión',19),('Río Hondo',19),('San Diego',19),('San Jorge',19),
 ('Teculután',19),('Usumatlán',19)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Chiquimula (20)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Chiquimula',20),('Camotán',20),('Concepción Las Minas',20),('Esquipulas',20),
 ('Ipala',20),('Jocotán',20),('Olopa',20),('Quezaltepeque',20),
 ('San Jacinto',20),('San José la Arada',20),('San Juan Ermita',20)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Jalapa (21)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Jalapa',21),('San Pedro Pinula',21),('San Luis Jilotepeque',21),('San Manuel Chaparrón',21),
 ('San Carlos Alzatate',21),('Monjas',21),('Mataquescuintla',21)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Jutiapa (22)
INSERT INTO TBL_Municipio (nombre, id_departamento) VALUES
 ('Jutiapa',22),('Agua Blanca',22),('Asunción Mita',22),('Atescatempa',22),('Comapa',22),
 ('Conguaco',22),('El Adelanto',22),('El Progreso',22),('Jalpatagua',22),('Jerez',22),
 ('Moyuta',22),('Pasaco',22),('Quesada',22),('San José Acatempa',22),
 ('Santa Catarina Mita',22),('Yupiltepeque',22),('Zapotitlán',22)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

INSERT INTO TBL_Persona (id, tipo_identificacion, identificacion, nombres, apellidos, correo, telefono, id_municipio, direccion_domicilio)
VALUES (1,'DPI','2683464450901','Administrador','General','admin@predio.com','33494579', 1, 'Chimaltenango')
ON DUPLICATE KEY UPDATE correo=VALUES(correo);

INSERT INTO TBL_Rol (id, nombre, descripcion, admin) VALUES
(1,'VENDEDOR','Rol encargado de funciones de vendedor y bodeguero',false),
(2,'ADMIN','Administrador del sistema',true),
(3,'OFICINA','Personal de control y gestión',false),
(4,'SUPERVISOR','Supervisor de oficina',true)
ON DUPLICATE KEY UPDATE descripcion=VALUES(descripcion);

INSERT INTO TBL_Usuario (id, usuario, password, id_persona, activo)
VALUES (1,'admin','$2b$10$IpZkoaSErr9Y2WCg7c41K.e5cvGouFu1K4dvDaxMOmucAnDXxeOYG',1,true)
ON DUPLICATE KEY UPDATE activo=VALUES(activo);

INSERT INTO TBL_Usuario_Rol (id_usuario, id_rol) VALUES (1, 2)
ON DUPLICATE KEY UPDATE id_rol=VALUES(id_rol);
