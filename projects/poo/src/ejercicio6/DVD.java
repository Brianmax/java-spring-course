package ejercicio6;

public class DVD extends LibraryItem{
    private String director;
    private int duracion;
    public DVD(String titulo, String itemId, String director, int duracion) {
        super(titulo, itemId);
        this.director = director;
        if(duracion <= 0) {
            System.out.println("No se pudo establecer la duracion debido a que el valor es invalido, ingreselo mas tarde");
        } else {
            this.duracion = duracion;
        }
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    @Override
    public void mostrarDetalles() {
        super.mostrarDetalles();
        System.out.println("Director: " + director);
        System.out.println("Duracion: " + duracion);
    }
}
