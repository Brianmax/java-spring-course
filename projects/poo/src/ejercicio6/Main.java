package ejercicio6;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("===== BIBLIOTECA =====");
            System.out.println("1. Agregar item (DVD)");
            System.out.println("2. Agregar item (BOOK)");
            System.out.println("3. Registrar usuario");
            System.out.println("4. Prestar item");
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
