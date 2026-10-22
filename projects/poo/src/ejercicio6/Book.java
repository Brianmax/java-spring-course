package ejercicio6;

public class Book extends LibraryItem{
    private String autor;


    public Book(String titulo, String itemId, String autor) {
        super(titulo, itemId);
        this.autor = autor;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Override
    public void mostrarDetalles() {
        super.mostrarDetalles();
        System.out.println("Autor: " + autor);
    }

    @Override
    public String toString() {
        return super.toString() + "\n" +
                "Autor: " + autor;
    }
}
