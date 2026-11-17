# Base de datos `tienda` · práctica MySQL

Este ejemplo convierte las tablas de [la lección de bases de datos](../../lessons/databases.html) en una base real de **MySQL 8.0 o posterior**. Necesitas MySQL Server en ejecución; MySQL Workbench es un cliente opcional, no el servidor. No hay una aplicación Java conectada todavía.

## Tablas y relaciones

| Tabla | Qué guarda | Relación |
| --- | --- | --- |
| `Clientes` | ID, nombre y correo | Ana C-01, Luis C-02 y Rosa C-03 |
| `Pedidos` | ID, cliente, resumen, total y estado | `cliente_id` apunta a `Clientes.id` |
| `Productos` **(nueva)** | ID, nombre y precio actual | Cuaderno P-01, mochila P-02, lapicero P-03 |
| `DetallePedido` **(nueva)** | Producto, cantidad y precio por pedido | Apunta a `Pedidos.id` y `Productos.id` |

`Pedidos.detalle` se conserva para que la consulta de la diapositiva funcione sin cambios; `DetallePedido` es la versión estructurada del mismo contenido. El `precio_unitario` de cada línea guarda el precio de compra aunque más tarde cambie `Productos.precio`. El campo `total` es el total guardado en el pedido; **no se recalcula automáticamente** al modificar líneas. La última consulta de `consultas.sql` permite compararlo con los subtotales.

## Crear la base de datos

En MySQL Workbench, conecta con un usuario que pueda crear bases y tablas, abre `tienda.sql` y ejecuta **todo el archivo una sola vez**. En una terminal, desde la raíz de este repositorio:

```sh
mysql -h localhost -P 3306 -u TU_USUARIO -p < projects/tienda-mysql/tienda.sql
```

El comando pide la contraseña sin guardarla en el repositorio. **Usa un servidor de prácticas**: el script crea la base `tienda` y falla si ya existe; no la borra ni sobrescribe datos. Para volver a empezar, utiliza una instancia nueva o pide permiso antes de eliminar una base existente. Si los estudiantes comparten un servidor, cada uno necesita una instancia separada o un nombre de base propio (cambiando `CREATE DATABASE` y `USE` en ambos archivos).

## Comprobar y practicar

Abre `consultas.sql` en Workbench y ejecútalo, o usa:

```sh
mysql -h localhost -P 3306 -u TU_USUARIO -p < projects/tienda-mysql/consultas.sql
```

- La primera consulta devuelve `A-17 | Ana | 36.00` (soles).
- La consulta `LEFT JOIN` devuelve cuatro filas: Ana dos veces, Luis una y Rosa con pedido `NULL`.
- Los tres totales calculados deben coincidir con los guardados: A-17 = 36, A-18 = 45 y A-20 = 10.
- Descomenta la sentencia final **solo si quieres demostrar** el rechazo de `C-99` por la clave foránea; da error deliberadamente.

Reto: agrega un segundo producto a A-17 y actualiza el total y el resumen de `Pedidos` para que reflejen la compra. Luego repite la consulta de totales.
