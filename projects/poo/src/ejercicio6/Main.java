package ejercicio6;

import java.util.Scanner;

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

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("===== BIBLIOTECA =====");
            System.out.println("1. Agregar item");
            System.out.println("2. Registrar usuario");
            System.out.println("3. Prestar item");
            System.out.println("0. Salir");
            System.out.print("Elige una opcion: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    // TODO: pedir los datos del libro o DVD
                    // y llamar a library.agregarItem(...)
                    System.out.println("Elegiste: agregar item");
                    break;
                case "2":
                    // TODO: pedir los datos del usuario
                    // y llamar a library.registrarUsuario(...)
                    System.out.println("Elegiste: registrar usuario");
                    break;
                case "3":
                    // TODO: pedir itemId y userId
                    // y llamar a library.prestamoItem(...)
                    System.out.println("Elegiste: prestar item");
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida. Intenta de nuevo.");
                    break;
            }
        }

        scanner.close();
        System.out.println("Fin del programa.");
    }
}
