# Tienda en PostgreSQL · esquema propuesto

Esta versión implementa **las siete tablas del diseño ampliado**, agrupadas en esquemas PostgreSQL. Es un proyecto **separado** del ejemplo MySQL de `../tienda-mysql/`: no modifica sus archivos ni sus resultados. El diagrama conceptual está en [diagrama-entidad-relacion.md](../tienda-mysql/diagrama-entidad-relacion.md); aquí los nombres completos incluyen un esquema.

| Esquema | Tablas | Responsabilidad |
| --- | --- | --- |
| `personas` | `personas.clientes` | Quién compra. |
| `catalogo` | `catalogo.categorias`, `catalogo.productos` | Qué se vende. |
| `almacen` | `almacen.inventario` | Cuántas unidades quedan. |
| `ventas` | `ventas.pedidos`, `ventas.detalle_pedido`, `ventas.pagos` | Compras y pagos. |

## Crear y consultar

Necesitas PostgreSQL y el cliente `psql`. Utiliza un servidor de prácticas y un usuario autorizado a crear bases de datos. Desde la raíz del repositorio:

```sh
psql -h localhost -p 5432 -U TU_USUARIO -W -X -v ON_ERROR_STOP=1 -d postgres -f projects/tienda-postgres/crear-base.sql
psql -h localhost -p 5432 -U TU_USUARIO -W -X -v ON_ERROR_STOP=1 -d tienda -f projects/tienda-postgres/tienda.sql
psql -h localhost -p 5432 -U TU_USUARIO -W -X -v ON_ERROR_STOP=1 -d tienda -f projects/tienda-postgres/consultas.sql
```

Sustituye `TU_USUARIO` por tu usuario y `localhost` por el servidor correspondiente si es remoto. `-W` pide la contraseña sin guardarla en los archivos.

`crear-base.sql` **falla si `tienda` ya existe** y no borra nada. En ese caso, **no ejecutes `tienda.sql` sobre una base con datos ajenos**: usa una instancia nueva o elige otro nombre de base en la instrucción de creación y en `-d`. `tienda.sql` crea los cuatro esquemas, sus tablas y los datos dentro de una transacción; tampoco borra datos existentes.

No hay instrucciones MySQL como `USE` o `ENGINE`; en PostgreSQL se selecciona la **base** al conectar con `psql -d tienda`. Dentro de esa base, `CREATE SCHEMA` crea espacios de nombres para agrupar tablas; no crea bases de datos adicionales. Las consultas usan nombres completos como `ventas.pedidos` y `personas.clientes`, por lo que no necesitan cambiar `search_path`. Los identificadores SQL van en minúsculas para evitar comillas obligatorias en PostgreSQL. `VARCHAR(4)` conserva IDs como `C-01` sin el relleno de espacios de `CHAR(4)`.

## Resultados esperados

- A-17 → Ana → **S/ 36** (3 cuadernos a S/ 12).
- A-23 → Ana → **S/ 29** (2 cuadernos a S/ 12 y 1 lapicero a S/ 5).
- El `LEFT JOIN` de clientes y pedidos devuelve **cinco filas**: Ana tres, Luis una y Rosa una con pedido `NULL`.
- La comprobación de totales devuelve `coinciden = true` para A-17, A-18, A-20 y A-23.
- Los pagos confirmados son S/ 36 para A-17 y S/ 10 para A-20. A-18 y A-23 no tienen pagos registrados.

## Qué reglas faltan

Las claves foráneas y las restricciones `CHECK` impiden referencias inexistentes y valores negativos, pero **no** calculan `ventas.pedidos.total`, descuentan inventario, impiden pagos acumulados superiores al total ni cambian `ventas.pedidos.estado` en respuesta a un pago. Para confirmar una compra real, la aplicación deberá verificar existencias, actualizar inventario, líneas, total y estado de forma consistente mediante transacciones. Un pedido sin líneas también es posible en el esquema; la regla «un pedido confirmado debe tener líneas» necesita lógica adicional.

Los slides actuales y el SQL MySQL siguen mostrando el ejemplo anterior; **no se han migrado** a esta versión ampliada.
