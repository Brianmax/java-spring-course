-- PostgreSQL: ejecutar conectado a una base NUEVA llamada tienda.
-- No modifica la base MySQL de projects/tienda-mysql/.
-- Falla si los esquemas o tablas ya existen. La transaccion evita dejarlos a medias.
BEGIN;

CREATE SCHEMA personas;
CREATE SCHEMA catalogo;
CREATE SCHEMA almacen;
CREATE SCHEMA ventas;

CREATE TABLE personas.clientes (
    id VARCHAR(4) PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE catalogo.categorias (
    id VARCHAR(6) PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE catalogo.productos (
    id VARCHAR(4) PRIMARY KEY,
    categoria_id VARCHAR(6) NOT NULL REFERENCES catalogo.categorias(id),
    nombre VARCHAR(80) NOT NULL,
    precio NUMERIC(10, 2) NOT NULL CHECK (precio >= 0)
);

-- Una fila como maximo por producto; puede no haber fila si todavia no
-- se ha registrado su inventario.
CREATE TABLE almacen.inventario (
    producto_id VARCHAR(4) PRIMARY KEY REFERENCES catalogo.productos(id),
    existencias INTEGER NOT NULL CHECK (existencias >= 0)
);

CREATE TABLE ventas.pedidos (
    id VARCHAR(4) PRIMARY KEY,
    cliente_id VARCHAR(4) NOT NULL REFERENCES personas.clientes(id),
    fecha TIMESTAMPTZ NOT NULL,
    estado VARCHAR(12) NOT NULL CHECK (estado IN ('PAGADO', 'PENDIENTE')),
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0)
);

CREATE TABLE ventas.detalle_pedido (
    pedido_id VARCHAR(4) NOT NULL REFERENCES ventas.pedidos(id),
    producto_id VARCHAR(4) NOT NULL REFERENCES catalogo.productos(id),
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10, 2) NOT NULL CHECK (precio_unitario >= 0),
    PRIMARY KEY (pedido_id, producto_id)
);

-- Se permiten varios pagos por pedido (por ejemplo, pagos parciales).
CREATE TABLE ventas.pagos (
    id VARCHAR(4) PRIMARY KEY,
    pedido_id VARCHAR(4) NOT NULL REFERENCES ventas.pedidos(id),
    monto NUMERIC(10, 2) NOT NULL CHECK (monto > 0),
    estado VARCHAR(12) NOT NULL CHECK (estado IN ('CONFIRMADO', 'PENDIENTE', 'RECHAZADO')),
    fecha TIMESTAMPTZ NOT NULL
);

INSERT INTO personas.clientes (id, nombre, correo) VALUES
    ('C-01', 'Ana', 'ana@nueva.pe'),
    ('C-02', 'Luis', 'luis@tienda.pe'),
    ('C-03', 'Rosa', 'rosa@tienda.pe');

INSERT INTO catalogo.categorias (id, nombre) VALUES
    ('CAT-01', 'Papeleria'),
    ('CAT-02', 'Accesorios');

INSERT INTO catalogo.productos (id, categoria_id, nombre, precio) VALUES
    ('P-01', 'CAT-01', 'Cuaderno', 12.00),
    ('P-02', 'CAT-02', 'Mochila', 45.00),
    ('P-03', 'CAT-01', 'Lapicero', 5.00);

-- Existencias de ejemplo ya registradas: no son un historico de movimientos.
INSERT INTO almacen.inventario (producto_id, existencias) VALUES
    ('P-01', 35),
    ('P-02', 10),
    ('P-03', 100);

INSERT INTO ventas.pedidos (id, cliente_id, fecha, estado, total) VALUES
    ('A-17', 'C-01', '2026-10-01 10:00:00-05', 'PAGADO', 36.00),
    ('A-18', 'C-01', '2026-10-02 11:00:00-05', 'PENDIENTE', 45.00),
    ('A-20', 'C-02', '2026-10-03 12:00:00-05', 'PAGADO', 10.00),
    ('A-23', 'C-01', '2026-10-04 13:00:00-05', 'PENDIENTE', 29.00);

-- El precio_unitario conserva el precio al comprar, aunque cambie
-- catalogo.productos.precio en el futuro.
INSERT INTO ventas.detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario) VALUES
    ('A-17', 'P-01', 3, 12.00),
    ('A-18', 'P-02', 1, 45.00),
    ('A-20', 'P-03', 2, 5.00),
    ('A-23', 'P-01', 2, 12.00),
    ('A-23', 'P-03', 1, 5.00);

INSERT INTO ventas.pagos (id, pedido_id, monto, estado, fecha) VALUES
    ('G-01', 'A-17', 36.00, 'CONFIRMADO', '2026-10-01 10:05:00-05'),
    ('G-02', 'A-20', 10.00, 'CONFIRMADO', '2026-10-03 12:05:00-05');

COMMIT;
