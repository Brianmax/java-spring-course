package atributosStatic;

public class Bloque {
    private String nombre;
    static int cantidad = 0;

    public Bloque(String nombre) {
        this.nombre = nombre;
        cantidad++;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
