USE tienda;

-- El INNER JOIN de la lección: A-17 | Ana | 36.00.
SELECT p.id, c.nombre, p.total
FROM Pedidos AS p
INNER JOIN Clientes AS c ON p.cliente_id = c.id
WHERE p.id = 'A-17';

-- Ana aparece dos veces; Rosa aparece una vez con NULL como pedido.
SELECT c.nombre, p.id AS pedido_id
FROM Clientes AS c
LEFT JOIN Pedidos AS p ON p.cliente_id = c.id
ORDER BY c.id, p.id;

-- El filtro en ON mantiene a Rosa (y a Ana con solo su pedido pagado).
SELECT c.nombre, p.id AS pedido_pagado
FROM Clientes AS c
LEFT JOIN Pedidos AS p
    ON p.cliente_id = c.id AND p.estado = 'PAGADO'
ORDER BY c.id, p.id;

-- El filtro en WHERE, en cambio, excluye a Rosa.
SELECT c.nombre, p.id AS pedido_pagado
FROM Clientes AS c
LEFT JOIN Pedidos AS p ON p.cliente_id = c.id
WHERE p.estado = 'PAGADO'
ORDER BY c.id, p.id;

-- Nuevo: leer los productos de cada pedido y calcular cada subtotal.
SELECT p.id AS pedido_id, c.nombre AS cliente, pr.nombre AS producto,
       d.cantidad, d.precio_unitario,
       d.cantidad * d.precio_unitario AS subtotal
FROM Pedidos AS p
JOIN Clientes AS c ON c.id = p.cliente_id
JOIN DetallePedido AS d ON d.pedido_id = p.id
JOIN Productos AS pr ON pr.id = d.producto_id
ORDER BY p.id, pr.id;

-- Comprobar que los totales de los pedidos coinciden con sus líneas.
SELECT p.id, p.total, SUM(d.cantidad * d.precio_unitario) AS total_calculado
FROM Pedidos AS p
JOIN DetallePedido AS d ON d.pedido_id = p.id
GROUP BY p.id, p.total
ORDER BY p.id;

-- Prueba opcional de integridad: falla porque C-99 no existe.
-- INSERT INTO Pedidos (id, cliente_id, detalle, total, estado)
-- VALUES ('A-19', 'C-99', '1 cuaderno', 12.00, 'PENDIENTE');
