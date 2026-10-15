public class Main {
    public static void main(String[] args) {
        Pedido pedido1 = new Pedido("Ana", "Cuaderno", 10, 1200);
        Pedido pedido2 = new Pedido("Luis", "Carpeta", 2, 499);

//        pedido1.cliente = "Ana";
//        pedido2.cliente = "Luis";
//        pedido1.cantidad = 12;
//        pedido2.cantidad = 4;
//        pedido1.precioUnitario = 1200;
//        pedido2.precioUnitario = 499;
//        pedido1.producto = "Cuaderno";
//        pedido2.producto = "Carpeta";
        // para pedido 1
        float precioTotal = pedido1.precioUnitario * pedido1.cantidad;
        pedido1.precioTotal = precioTotal;
        // para pedido 2
        pedido2.precioTotal = pedido2.precioUnitario * pedido2.cantidad;
        pedido1.calcularPrecioTotal();
        float cantidadDescontar = pedido1.simularDescuento(0.2f);
        System.out.println(cantidadDescontar);
        pedido1.imprimirDetalles();

        Pedido pedido3 = new Pedido();
        Pedido pedido4 = new Pedido("Jose");
    }
    // crear una clase Computadora
    // int ram, int almacenamiento, string marca, string procesador
    // metodo para mostrar los detalles de la computadora
    // construtor
    // crear objetos
}
