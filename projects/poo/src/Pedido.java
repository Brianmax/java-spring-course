public class Pedido {
    private final String numero;
    private final String cliente;
    private final String producto;
    private final int cantidad;
    private final int precioUnitarioDolares;

    public Pedido(String numero, String cliente, String producto, int cantidad, int precioUnitarioDolares) {
        if (cantidad <= 0 || precioUnitarioDolares < 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva y el precio no puede ser negativo.");
        }
        this.numero = numero;
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitarioDolares = precioUnitarioDolares;
    }

    public int calcularTotal() {
        return cantidad * precioUnitarioDolares;
    }

    public int calcularDescuento(int porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        return calcularTotal() * porcentaje / 100;
    }

    public void imprimirDetalles() {
        System.out.printf("Pedido %s | Cliente: %s%n", numero, cliente);
        System.out.printf("Producto: %s | Cantidad: %d | Precio unitario: $%d%n", producto, cantidad, precioUnitarioDolares);
        System.out.printf("Total: $%d%n", calcularTotal());
    }
}
