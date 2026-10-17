package ejercicioProductos;

public class PedidoLinea {
    Producto producto;
    int cantidad;

    public PedidoLinea(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }
}
