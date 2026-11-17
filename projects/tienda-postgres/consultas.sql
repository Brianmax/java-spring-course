-- Ejecutar conectado a la base tienda creada con tienda.sql.

-- Pedido inicial de la leccion: A-17 | Ana | 36.00.
SELECT p.id, c.nombre, p.total
FROM ventas.pedidos AS p
JOIN personas.clientes AS c ON c.id = p.cliente_id
WHERE p.id = 'A-17';

-- Uno a muchos: Ana aparece tres veces, Luis una y Rosa una con NULL.
SELECT c.nombre, p.id AS pedido_id
FROM personas.clientes AS c
LEFT JOIN ventas.pedidos AS p ON p.cliente_id = c.id
ORDER BY c.id, p.id;

-- El filtro en ON conserva a todos los clientes, incluida Rosa.
SELECT c.nombre, p.id AS pedido_pagado
FROM personas.clientes AS c
LEFT JOIN ventas.pedidos AS p
    ON p.cliente_id = c.id AND p.estado = 'PAGADO'
ORDER BY c.id, p.id;

-- En WHERE desaparecen Rosa y los clientes sin pedidos pagados.
SELECT c.nombre, p.id AS pedido_pagado
FROM personas.clientes AS c
LEFT JOIN ventas.pedidos AS p ON p.cliente_id = c.id
WHERE p.estado = 'PAGADO'
ORDER BY c.id, p.id;

-- A-23 tiene dos lineas: 2 cuadernos (24.00) y 1 lapicero (5.00).
SELECT p.id AS pedido_id, c.nombre AS cliente, pr.nombre AS producto,
       d.cantidad, d.precio_unitario,
       d.cantidad * d.precio_unitario AS subtotal
FROM ventas.pedidos AS p
JOIN personas.clientes AS c ON c.id = p.cliente_id
JOIN ventas.detalle_pedido AS d ON d.pedido_id = p.id
JOIN catalogo.productos AS pr ON pr.id = d.producto_id
WHERE p.id = 'A-23'
ORDER BY pr.id;

-- Comprobar todos los totales. coinciden = true para los cuatro pedidos.
-- Un pedido sin lineas apareceria con total_calculado = 0.
SELECT p.id, p.total,
       COALESCE(SUM(d.cantidad * d.precio_unitario), 0) AS total_calculado,
       p.total = COALESCE(SUM(d.cantidad * d.precio_unitario), 0) AS coinciden
FROM ventas.pedidos AS p
LEFT JOIN ventas.detalle_pedido AS d ON d.pedido_id = p.id
GROUP BY p.id, p.total
ORDER BY p.id;

-- Categoria e inventario actual de cada producto.
SELECT pr.id, pr.nombre, cat.nombre AS categoria,
       i.existencias
FROM catalogo.productos AS pr
JOIN catalogo.categorias AS cat ON cat.id = pr.categoria_id
LEFT JOIN almacen.inventario AS i ON i.producto_id = pr.id
ORDER BY pr.id;

-- Agregar pagos ANTES de unirlos a pedidos evita duplicar importes
-- cuando un pedido tiene varias lineas y varios pagos.
SELECT p.id, p.total,
       COALESCE(pc.total_confirmado, 0) AS total_pagado,
       p.total - COALESCE(pc.total_confirmado, 0) AS saldo
FROM ventas.pedidos AS p
LEFT JOIN (
    SELECT pedido_id, SUM(monto) AS total_confirmado
    FROM ventas.pagos
    WHERE estado = 'CONFIRMADO'
    GROUP BY pedido_id
) AS pc ON pc.pedido_id = p.id
ORDER BY p.id;

-- Prueba opcional de integridad: falla porque C-99 no existe.
-- INSERT INTO ventas.pedidos (id, cliente_id, fecha, estado, total)
-- VALUES ('A-19', 'C-99', '2026-10-05 10:00:00-05', 'PENDIENTE', 12.00);
