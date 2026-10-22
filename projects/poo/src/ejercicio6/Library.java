package ejercicio6;

import java.util.ArrayList;

public class Library {
    private ArrayList<LibraryItem> items;
    private ArrayList<LibraryUser> usuarios;

    public Library() {
        items = new ArrayList<>();
        usuarios = new ArrayList<>();
    }

    public boolean agregarItem(LibraryItem itemAgregar) {
        // validar que el item no tenga un ID repetido

        // recorrer el arrayList de items y verficar que no se repita

        // en caso de que el id este repetido, rechazar la insercion y retornar falso

        // imprimir un mensaje de advertencia

        // extraemos el item id del item que deseamos ingresar
        String itemId = itemAgregar.getItemId();
        for(LibraryItem itemLibrary: items) {
            if(itemId.equals(itemLibrary.getItemId())) {
                System.out.println("\u001B[31mItem ID duplicado\u001B[0m");
                return false;
            }
        }
        // agregamos el item a el array
        items.add(itemAgregar);
        return true;
    }

    public boolean registrarUsuario(LibraryUser usuarioAgregar) {
        String usuarioId = usuarioAgregar.getUserId();
        for(LibraryUser libraryUser: usuarios) {
            if(usuarioId.equals(libraryUser.getUserId())) {
                System.out.println("Usuario ID duplicado");
                return false;
            }
        }
        usuarios.add(usuarioAgregar);
        return true;
    }

    public boolean prestamoItem(String itemId, String userId) {
        // primero: validar que el usuario y el item existan
        // segundo: realizar la operacion de prestamo
        // tercero: actualizar el estado del item

        LibraryItem libraryItem = buscarItem(itemId);
        LibraryUser libraryUser = buscarUsuario(userId);

        if(libraryItem == null || libraryUser == null) {
            System.out.println("Datos no encontrados");
            return false;
        }
        if(libraryItem.getPrestario() != null) {
            System.out.println("El libro no se encuentra disponible para prestamo");
            return false;
        }
        libraryItem.setPrestario(libraryUser);
        return true;
    }

    public boolean retornarItem(String itemId, String userId) {
        return false;
    }

    public void imprimirItemsBiblioteca() {
        System.out.println("================================");
        for(LibraryItem libraryItem: items) {
            System.out.println(libraryItem);
            System.out.println("================================");
        }
    }

    private LibraryItem buscarItem(String itemId) {
        for(LibraryItem libraryItem: items) {
            if(itemId.equals(libraryItem.getItemId())) {
                return libraryItem;
            }
        }
        return null;
    }

    private LibraryUser buscarUsuario(String userId) {
        for(LibraryUser libraryUser: usuarios) {
            if(userId.equals(libraryUser.getUserId())) {
                return libraryUser;
            }
        }
        return null;
    }

}
