# Programación orientada a objetos · Pedidos

Un ejemplo introductorio en Java 21 para practicar clases, objetos, atributos, constructores y métodos a partir de pedidos.

## Ejecutar

Abre esta carpeta en IntelliJ IDEA con un JDK 21 y ejecuta `Main`. También puedes compilar desde una terminal:

```sh
javac -d out src/Main.java src/Pedido.java
java -cp out Main
```

## Qué observar

- `Pedido` describe un pedido y cada variable de tipo `Pedido` referencia un objeto distinto.
- Los atributos guardan los datos de cliente, producto, cantidad y precios.
- Los constructores permiten crear pedidos con datos completos o iniciar uno vacío.
- `calcularPrecioTotal()` multiplica la cantidad por el precio unitario.
- `simularDescuento(0.2f)` calcula el 20 % del total guardado en el pedido.
- `imprimirDetalles()` muestra los datos del pedido.

El ejemplo conserva los datos del código: Ana pide 10 cuadernos a 1200 cada uno; Luis pide 2 carpetas a 499 cada una. El código de clase trabaja con `float` y muestra el total de Ana y el descuento simulado.

Como práctica, crea una clase `Computadora` con marca, memoria RAM y almacenamiento, un constructor y un método `mostrarDetalles()`.
