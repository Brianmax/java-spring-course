package ejercicio6;

public class LibraryItem {
    private String titulo;
    private String itemId; // unique
    private LibraryUser prestario;

    public LibraryItem(String titulo, String itemId) {
        this.titulo = titulo;
        this.itemId = itemId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public LibraryUser getPrestario() {
        return prestario;
    }

    public void setPrestario(LibraryUser prestario) {
        this.prestario = prestario;
    }

    public void mostrarDetalles() {
        System.out.println("Item ID: " + itemId);
        System.out.println("Titulo: " + titulo);
        System.out.println("Disponible: " + prestario == null ? "SI": "NO" );
    }


}
