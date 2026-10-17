package ejercicioProductos;

public class Main {
    public static void main(String[] args) {
        Cliente cliente1 = new Cliente(
                1,
                "Jose",
                "Lopez",
                "jlopez@email.com"
        );

        Cliente cliente2 = new Cliente(
                2,
                "Maria",
                "Garcia",
                "mgarcia@email.com"
        );

        Cliente cliente3 = new Cliente(
                3,
                "Carlos",
                "Ramirez",
                "cramirez@email.com"
        );

        Cliente cliente4 = new Cliente(
                4,
                "Ana",
                "Torres",
                "atorres@email.com"
        );


        Producto producto1 = new Producto(
                1,
                "Computadora",
                1999.99,
                "Computadora de escritorio",
                10
        );

        Producto producto2 = new Producto(
                2,
                "Laptop",
                1499.99,
                "Laptop para desarrollo de software",
                15
        );

        Producto producto3 = new Producto(
                3,
                "Teclado",
                59.99,
                "Teclado mecánico RGB",
                25
        );

        Producto producto4 = new Producto(
                4,
                "Mouse",
                29.99,
                "Mouse inalámbrico",
                30
        );

        Producto producto5 = new Producto(
                5,
                "Monitor",
                349.99,
                "Monitor LED de 27 pulgadas",
                8
        );

        Producto producto6 = new Producto(
                6,
                "Audifonos",
                89.99,
                "Audífonos con cancelación de ruido",
                12
        );


        Pedido pedido1 = new Pedido(101, cliente2);
        pedido1.ingresarProducto(producto2, 10);
        pedido1.removeProductos(2, 10);
    }
}
