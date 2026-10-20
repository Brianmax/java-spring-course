package ejercicio6;

public class Main {
    public static void main(String[] args) {
        // Objetos Book
        Book libro1 = new Book("Clean Code", "B001", "Robert C. Martin");
        Book libro2 = new Book("Effective Java", "B002", "Joshua Bloch");
        Book libro3 = new Book("The Pragmatic Programmer", "B003", "Andrew Hunt");
        Book libro4 = new Book("Don Quijote de la Mancha", "B004", "Miguel de Cervantes");
        Book libro5 = new Book("Cien años de soledad", "B005", "Gabriel García Márquez");

        // Objetos DVD
        DVD dvd1 = new DVD("Inception", "D001", "Christopher Nolan", 148);
        DVD dvd2 = new DVD("The Matrix", "D002", "Lana Wachowski y Lilly Wachowski", 136);
        DVD dvd3 = new DVD("Interstellar", "D003", "Christopher Nolan", 169);
        DVD dvd4 = new DVD("Titanic", "D004", "James Cameron", 195);
        DVD dvd5 = new DVD("Jurassic Park", "D005", "Steven Spielberg", 127);

        Library library = new Library();
        // ingresar un item a la biblioteca
        library.agregarItem(libro1);
        library.agregarItem(dvd3);

        while(true) {
            System.out.println("Ingrese la opcion: ");

        }
    }
}
