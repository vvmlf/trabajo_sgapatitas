CREATE TABLE usuario (
	id_usuario INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(80),
	usuario VARCHAR(30) UNIQUE,
	clave VARCHAR(60),
	rol VARCHAR(20)
);

CREATE TABLE animal (

	id_animal INT AUTO_INCREMENT PRIMARY KEY,
	especie VARCHAR(30),
	edad_aprox INT,
	sexo VARCHAR(10),
	fecha_ingreso DATE,
	descripcion VARCHAR(255),
	estado VARCHAR(25) DEFAULT 'En evaluacion'
);


CREATE TABLE adoptante (
	id_adoptante INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(60),
	apellido VARCHAR(60),
	dni VARCHAR(15) UNIQUE,
	telefono VARCHAR(20),
	formulario_idoneidad VARCHAR(500),
	fecha_registro DATE
);

CREATE TABLE solicitud_adopcion (
	id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
	id_animal INT,
	id_adoptante INT,
	id_usuario INT,
	fecha DATE,
	estado VARCHAR(20) DEFAULT 'pendiente',
	CONSTRAINT fk_solicitud_animal FOREIGN KEY (id_animal) REFERENCES animal(id_animal),
	CONSTRAINT fk_solicitud_adoptante FOREIGN KEY (id_adoptante) REFERENCES adoptante(id_adoptante),
	CONSTRAINT fk_solicitud_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);



INSERT INTO usuario (nombre, usuario, clave, rol) VALUES
('Esteban Fuertes', 'efuertes', 'admin2026', 'administrador'),
('Luis Miguel Rodriguez', 'lmrodriguez', 'vet2026', 'veterinario');
 
INSERT INTO animal (especie, edad_aprox, sexo, fecha_ingreso, descripcion, estado) VALUES
('perro', 2, 'macho', '2026-08-01', 'mestizo tamano mediano, rescatado en la ruta 20', 'En evaluacion'),
('gato', 1, 'hembra', '2026-08-05', 'gata atigrada, muy sociable', 'En evaluacion'),
('perro', 4, 'hembra', '2026-07-20', 'labrador mezcla, buena con ninos', 'disponible');
 
 
INSERT INTO adoptante (nombre, apellido, dni, telefono, formulario_idoneidad, fecha_registro) VALUES
('Sofia', 'Aguirre', '31555222', '3514445566', 'vive en casa con patio, sin otras mascotas', '2026-08-10'),
('Diego', 'Torres', '29888111', '3512223311', 'departamento, ya tuvo perros antes', '2026-08-15'),
('Lorena', 'Vega', '33777444', '3513334455', 'casa con jardin, consulta general', '2026-08-18');
 
INSERT INTO solicitud_adopcion (id_animal, id_adoptante, id_usuario, fecha, estado) VALUES
(3, 1, 1, '2026-08-20', 'pendiente'),
(3, 2, 1, '2026-08-21', 'pendiente');
 
SELECT * FROM animal WHERE estado = 'disponible';
 
 
