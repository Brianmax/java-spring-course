public class Main {
    public static void main(String[] args) {
        Pedido pedidoAna = new Pedido("A-17", "Ana", "Cuaderno", 2, 5);
        Pedido pedidoLuis = new Pedido("B-18", "Luis", "Carpeta", 1, 12);

        pedidoAna.imprimirDetalles();
        System.out.printf("Descuento simulado (20%%): $%d%n%n", pedidoAna.calcularDescuento(20));

        pedidoLuis.imprimirDetalles();
    }
}
