# Programación orientada a objetos · Pedidos

Un ejemplo pequeño en Java 21 para practicar las piezas básicas de la programación orientada a objetos: una clase, objetos, atributos privados, constructor y métodos.

## Ejecutar

Abre esta carpeta en IntelliJ IDEA con un JDK 21 y ejecuta `Main`. También puedes compilar desde una terminal:

```sh
javac -d out src/Main.java src/Pedido.java
java -cp out Main
```

## Qué observar

- `Pedido` describe cada pedido; `pedidoAna` y `pedidoLuis` son objetos distintos.
- El constructor recibe los datos y los guarda en atributos privados.
- `calcularTotal()` multiplica la cantidad por el precio unitario.
- `calcularDescuento(20)` calcula el 20 % del total; el ejemplo de Ana da $2 sobre un total de $10.
- `imprimirDetalles()` muestra los datos y el total del pedido.
- Los importes se expresan en dólares enteros para mantener los cálculos sencillos: Ana pide 2 cuadernos a $5 ($10) y Luis 1 carpeta a $12 ($12).

Como práctica, crea una clase `Computadora` con marca, memoria RAM y almacenamiento, un constructor y un método `mostrarDetalles()`.
