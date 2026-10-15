public class Main {
    public static void main(String[] args) {
        Pedido pedido1 = new Pedido("Ana", "Cuaderno", 10, 1200);
        Pedido pedido2 = new Pedido("Luis", "Carpeta", 2, 499);

        float precioTotal = pedido1.calcularPrecioTotal();
        System.out.println("Total calculado: " + precioTotal);

        float descuento = pedido1.simularDescuento(0.2f);
        System.out.println(descuento);

        pedido1.imprimirDetalles();

        Pedido pedido3 = new Pedido();
        Pedido pedido4 = new Pedido("Cliente ocasional");
    }
}
