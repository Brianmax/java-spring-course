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
                System.out.println("\\u001B[31mItem ID duplicado\\u001B[0m");
                return false;
            }
        }
        // agregamos el item a el array
        items.add(itemAgregar);
        return true;
    }

    public boolean registrarUsuario(LibraryUser usuario) {
        // Aplicar una logica similar al metodo agregarItem
        return false;
    }

    public boolean prestamoItem(String itemId, String userId) {
        // primero: validar que el usuario y el item existan
        // segundo: realizar la operacion de prestamo
        // tercero: actualizar el estado del item
        return false;
    }

}
