import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class Pedido {
    // atributos
    int idPedido;
    Cliente cliente;
    ArrayList<PedidoLinea> productos;
    double precioTotal;
    int cantidadProductos;


    Pedido() {}

    public Pedido(int idPedido, Cliente cliente) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        productos = new ArrayList<>();
    }

    // crear un metodo que ingrese un producto al pedido
    void ingresarProducto(Producto producto, int qty) {
        if(qty > producto.stock) {
            System.out.println("El producto no tiene stock suficiente");
            System.out.println("Stock del producto: " + producto.stock);
            System.out.println("Cantidad requerida: " + qty);
            return;
        }
        PedidoLinea pedidoLinea = new PedidoLinea(producto, qty);
        productos.add(pedidoLinea);
        producto.stock -= qty;
        System.out.println("Se agrego " + qty + " unidades de " + producto.nombre);
    }


//     double calcularPrecioTotal() {
//        double acumulado = 0;
//        for(PedidoLinea p: productos) {
//            Producto producto = p.producto;
//            double precio = producto.precio;
//            int cantidad = p.cantidad;
//            acumulado = cantidad * precio;
//        }
//        precioTotal = acumulado;
//        return precioTotal;
//    }
}
