public class Producto {
    int id;
    String nombre;
    double precio;
    String descripcion;
    int stock;

    public Producto(int id, String nombre, double precio, String descripcion, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
    }

    public Producto() {}


}
