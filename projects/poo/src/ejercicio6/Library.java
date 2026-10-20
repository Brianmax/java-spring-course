package ejercicio6;

import java.util.ArrayList;

public class Library {
    private ArrayList<LibraryItem> items;
    private ArrayList<LibraryUser> usuarios;

    public boolean agregarItem(LibraryItem item) {
        // validar que el item no tenga un ID repetido

        // recorrer el arrayList de items y verficar que no se repita

        // en caso de que el id este repetido, rechazar la insercion y retornar falso

        // imprimir un mensaje de advertencia
        return false;
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
