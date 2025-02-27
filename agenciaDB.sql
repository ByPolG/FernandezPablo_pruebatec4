-- Eliminamos la base de datos si ya existe y vla volvemos a crear
DROP DATABASE IF EXISTS agencia_db;
CREATE DATABASE agencia_db;
USE agencia_db;

-- Creamos la tabla de usuarios
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    contrasenia VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'USER') NOT NULL
);

-- Creamos la tabla de hoteles
CREATE TABLE IF NOT EXISTS hotel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_hotel VARCHAR(255) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    lugar VARCHAR(255),
    precio_por_noche DECIMAL(10,2) NOT NULL,
    disponible_desde DATE,
    disponible_hasta DATE,
    reservado BOOLEAN NOT NULL DEFAULT FALSE
);

-- Creamos la tabla de habitaciones
CREATE TABLE IF NOT EXISTS habitacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_habitacion VARCHAR(50) NOT NULL UNIQUE,
    hotel_id BIGINT NOT NULL,
    tipo ENUM('Single', 'Doble', 'Triple', 'Multiple') NOT NULL,
    precio_habitacion DECIMAL(10,2) NOT NULL CHECK (precio_habitacion > 0),
    capacidad INT NOT NULL CHECK (capacidad > 0),
    estado ENUM('DISPONIBLE', 'NO_DISPONIBLE') NOT NULL DEFAULT 'DISPONIBLE',
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    FOREIGN KEY (hotel_id) REFERENCES hotel(id) ON DELETE CASCADE
);

-- Creamos la tabla de vuelos
CREATE TABLE IF NOT EXISTS vuelo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_vuelo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    origen VARCHAR(255) NOT NULL,
    destino VARCHAR(255) NOT NULL,
    tipo_asiento ENUM('Economy', 'Business') NOT NULL,
    precio_vuelo DECIMAL(10,2) NOT NULL CHECK (precio_vuelo > 0),
    fecha_ida DATE NOT NULL,
    fecha_vuelta DATE NULL,
    estado ENUM('DISPONIBLE', 'NO_DISPONIBLE') NOT NULL DEFAULT 'DISPONIBLE'
);

-- Creamos la tabla de reservas de hotel
CREATE TABLE IF NOT EXISTS reserva_hotel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    cantidad_personas INT NOT NULL CHECK (cantidad_personas > 0),
    fecha_entrada DATE NOT NULL,
    fecha_salida DATE NOT NULL,
    tipo_habitacion VARCHAR(50) NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL CHECK (monto_total >= 0),
    FOREIGN KEY (hotel_id) REFERENCES hotel(id) ON DELETE CASCADE
);

-- Creamos la tabla de reservas de vuelo
CREATE TABLE IF NOT EXISTS reserva_vuelo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vuelo_id BIGINT NOT NULL,
    origen VARCHAR(255) NOT NULL,
    destino VARCHAR(255) NOT NULL,
    fecha_ida DATE NOT NULL,
    cantidad_personas INT NOT NULL CHECK (cantidad_personas > 0),
    monto_total DECIMAL(10,2) NOT NULL CHECK (monto_total >= 0),
    FOREIGN KEY (vuelo_id) REFERENCES vuelo(id) ON DELETE CASCADE
);

-- Creamos la tabla intermedia para la relación muchos a muchos entre usuarios y reservas de vuelo
CREATE TABLE IF NOT EXISTS reserva_vuelo_usuario (
    reserva_vuelo_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    PRIMARY KEY (reserva_vuelo_id, usuario_id),
    FOREIGN KEY (reserva_vuelo_id) REFERENCES reserva_vuelo(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Creamos la tabla intermedia para la relación muchos a muchos entre reservas de hotel y usuarios
CREATE TABLE IF NOT EXISTS reserva_hotel_huesped (
    reserva_hotel_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    PRIMARY KEY (reserva_hotel_id, usuario_id),
    FOREIGN KEY (reserva_hotel_id) REFERENCES reserva_hotel(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Insertamos datos en las tablas principales
INSERT INTO usuario (nombre, contrasenia, rol)
VALUES
('admin', 'admin123', 'ADMIN'),
('usuario1', 'pass123', 'USER'),
('usuario2', 'pass456', 'USER');

INSERT INTO hotel (codigo_hotel, nombre, lugar, precio_por_noche, disponible_desde, disponible_hasta, reservado)
VALUES
('HOT001', 'Hotel Playacartaya', 'Cancún', 150.00, '2025-01-01', '2025-12-31', FALSE),
('HOT002', 'Hotel NH', 'Madrid', 120.00, '2025-02-01', '2025-12-31', FALSE),
('HOT003', 'Hotel Costa', 'Barcelona', 180.00, '2025-03-01', '2025-12-31', TRUE);

INSERT INTO habitacion (codigo_habitacion, hotel_id, tipo, precio_habitacion, capacidad, estado, fecha_inicio, fecha_fin)
VALUES
('HAB001', 1, 'Single', 100.00, 1, 'DISPONIBLE', '2025-01-01', '2025-12-31'),
('HAB002', 1, 'Doble', 150.00, 2, 'DISPONIBLE', '2025-01-01', '2025-12-31'),
('HAB003', 2, 'Triple', 200.00, 3, 'NO_DISPONIBLE', '2025-02-01', '2025-12-31'),
('HAB004', 3, 'Single', 120.00, 1, 'DISPONIBLE', '2025-03-01', '2025-12-31');

INSERT INTO vuelo (codigo_vuelo, nombre, origen, destino, tipo_asiento, precio_vuelo, fecha_ida, fecha_vuelta, estado)
VALUES
('VL001', 'Vuelo A', 'Madrid', 'Cancún', 'Economy', 300.00, '2025-06-01', '2025-06-10', 'DISPONIBLE'),
('VL002', 'Vuelo B', 'Barcelona', 'Paris', 'Business', 500.00, '2025-07-01', '2025-07-05', 'DISPONIBLE'),
('VL003', 'Vuelo C', 'Madrid', 'Londres', 'Economy', 250.00, '2025-05-15', '2025-05-18', 'NO_DISPONIBLE');

