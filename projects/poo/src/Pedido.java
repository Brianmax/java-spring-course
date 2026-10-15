import java.util.ArrayList;

public class Pedido {
    // atributos
    String cliente;
    String producto;
    int cantidad;
    float precioUnitario;
    float precioTotal;

    Pedido(String cliente, String producto, int cantidad, float precioUnitario) {
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    Pedido() {}

    Pedido(String cliente) {
        this.cliente = cliente;
    }

    // metodos (acciones que realizan los objetos)
    // calcularPrecioTotal();

    float calcularPrecioTotal() {
        precioTotal = cantidad * precioUnitario;
        return precioTotal;
    }

    // simular un descuento
    float simularDescuento(float descuento) {
        return precioTotal * descuento;
    }

    // imprimir los detalles del pedido
    void imprimirDetalles() {
        System.out.println("Cliente: " + cliente);
        System.out.println("Producto: " + producto);
        System.out.println("Cantidad: " + cantidad);
        System.out.println("Precio Unitario: " + precioUnitario);
        System.out.println("Precio Total: " + precioTotal);
    }
}
