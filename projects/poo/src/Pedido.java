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

    // remover unidades de un producto
    // cuando la cantidad llegue a 0, eliminar el producto completamente del pedido
    void removeProductos(int idProducto, int qtyRemove) {
        PedidoLinea pl = findProducto(idProducto);
        if(pl == null) {
            System.out.println("No existe el producto que se desea remover");
            return;
        }

        Producto pr = pl.producto;
        pr.stock += qtyRemove;
        if(qtyRemove < pl.cantidad) {
            pl.cantidad -= qtyRemove;
        } else if (qtyRemove == pl.cantidad) {
            // remover el pedidoLinea del pedido
            productos.remove(pl);
        }
    }

    PedidoLinea findProducto(int idProducto) {
        for(PedidoLinea pl: productos) {
            Producto pr = pl.producto;
            if(pr.id == idProducto) {
                return pl;
            }
        }
        return null;
    }
}
