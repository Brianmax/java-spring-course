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

        LibraryUser usuario1 = new LibraryUser("jlopez", "U001");
        LibraryUser usuario2 = new LibraryUser("mgarcia", "U002");
        LibraryUser usuario3 = new LibraryUser("aperez", "U003");
        LibraryUser usuario4 = new LibraryUser("cramos", "U004");
        LibraryUser usuario5 = new LibraryUser("lcastro", "U005");


        Library library = new Library();
        library.agregarItem(dvd1);
        library.agregarItem(dvd2);
        library.agregarItem(libro4);
        library.agregarItem(dvd3);
        library.agregarItem(libro1);

        library.registrarUsuario(usuario1);
        library.registrarUsuario(usuario2);
        library.registrarUsuario(usuario3);
        library.registrarUsuario(usuario4);
        library.registrarUsuario(usuario5);


        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("===== BIBLIOTECA =====");
            System.out.println("1. Agregar item (DVD)");
            System.out.println("2. Agregar item (BOOK)");
            System.out.println("3. Registrar usuario");
            System.out.println("4. Prestar item");
            System.out.println("5. Retornar item");
            System.out.println("6. Imprimir items");
            System.out.println("0. Salir");
            System.out.print("Elige una opcion: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    // TODO: pedir los datos del dvd
                    System.out.print("Ingrese el ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Ingrese el titulo: ");
                    String titulo = scanner.nextLine();

                    System.out.print("Ingrese el director: ");
                    String director = scanner.nextLine();

                    System.out.print("Ingresa la duracion: ");
                    int duracion = Integer.parseInt(scanner.nextLine());

                    DVD dvd = new DVD(titulo, id, director, duracion);
                    library.agregarItem(dvd);
                    break;
                case "2":
                    // TODO: pedir los datos del book
                    System.out.print("Ingrese el ID: ");
                    String idLibro = scanner.nextLine();

                    System.out.print("Ingrese el titulo: ");
                    String tituloLibro = scanner.nextLine();

                    System.out.print("Ingrese el autor: ");
                    String autor = scanner.nextLine();

                    Book book = new Book(tituloLibro, idLibro, autor);
                    break;
                case "3":
                    // TODO: pedir itemId y userId
                    // y llamar a library.prestamoItem(...)
                    System.out.println("Elegiste: prestar item");
                    break;
                case "4":
                    // TODO: prestar item
                    System.out.println("Ingrese el ID del item");
                    String idItem = scanner.nextLine();
                    System.out.println("Ingrese el ID del usuario");
                    String idUsuario = scanner.nextLine();
                    library.prestamoItem(idItem, idUsuario);
                    break;
                case "5":
                    // TODO: retornar item
                    break;
                case "6":
                    library.imprimirItemsBiblioteca();
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
