-- MySQL 8.0+: ejemplo reproducible de lessons/databases.html.
-- Ejecutar UNA vez en un servidor de clase; no elimina bases de datos existentes.
CREATE DATABASE tienda CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tienda;

CREATE TABLE Clientes (
    id CHAR(4) PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Pedidos (
    id CHAR(4) PRIMARY KEY,
    cliente_id CHAR(4) NOT NULL,
    detalle VARCHAR(120) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(12) NOT NULL,
    CONSTRAINT fk_pedidos_cliente FOREIGN KEY (cliente_id) REFERENCES Clientes(id),
    CONSTRAINT chk_pedidos_total CHECK (total >= 0),
    CONSTRAINT chk_pedidos_estado CHECK (estado IN ('PAGADO', 'PENDIENTE'))
) ENGINE = InnoDB;

-- Dos tablas adicionales: un producto puede aparecer en varios pedidos;
-- DetallePedido guarda cada producto, cantidad y precio en el momento de la compra.
CREATE TABLE Productos (
    id CHAR(4) PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    CONSTRAINT chk_productos_precio CHECK (precio >= 0)
) ENGINE = InnoDB;

CREATE TABLE DetallePedido (
    pedido_id CHAR(4) NOT NULL,
    producto_id CHAR(4) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (pedido_id, producto_id),
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (pedido_id) REFERENCES Pedidos(id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES Productos(id),
    CONSTRAINT chk_detalle_cantidad CHECK (cantidad > 0),
    CONSTRAINT chk_detalle_precio CHECK (precio_unitario >= 0)
) ENGINE = InnoDB;

INSERT INTO Clientes (id, nombre, correo) VALUES
    ('C-01', 'Ana', 'ana@nueva.pe'),
    ('C-02', 'Luis', 'luis@tienda.pe'),
    ('C-03', 'Rosa', 'rosa@tienda.pe');

INSERT INTO Productos (id, nombre, precio) VALUES
    ('P-01', 'Cuaderno', 12.00),
    ('P-02', 'Mochila', 45.00),
    ('P-03', 'Lapicero', 5.00);

INSERT INTO Pedidos (id, cliente_id, detalle, total, estado) VALUES
    ('A-17', 'C-01', '3 cuadernos x S/ 12', 36.00, 'PAGADO'),
    ('A-18', 'C-01', '1 mochila x S/ 45', 45.00, 'PENDIENTE'),
    ('A-20', 'C-02', '2 lapiceros x S/ 5', 10.00, 'PAGADO');

INSERT INTO DetallePedido (pedido_id, producto_id, cantidad, precio_unitario) VALUES
    ('A-17', 'P-01', 3, 12.00),
    ('A-18', 'P-02', 1, 45.00),
    ('A-20', 'P-03', 2, 5.00);
