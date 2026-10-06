package arrays;

import java.util.ArrayList;

public class TeoriaArrayList {
    public static void main(String[] args) {
        ArrayList<String> nombres = new ArrayList<>();
        // agregar elementos
        nombres.add("George");
        nombres.add("Sheyla");
        nombres.add("Jose");

        System.out.println("======================ARRAY ELEMENTOS========================");
        System.out.println(nombres);
        // obtener por indice
        System.out.println(nombres.get(1));

        // actualizar

        nombres.set(2, "Fiorela");
        System.out.println("===================ARRAY DESPUES DE LOS CAMBIOS===================");
        System.out.println(nombres);

        // remover
        // remueve por indice
        nombres.remove(0);

        System.out.println("===================ARRAY DESPUES DE REMOVER===================");
        System.out.println(nombres);

        // remueve por elemento. Si lo encuentra lo borra, si no no hace nada
        // [Sheyla, Fiorela]
        nombres.remove("Fiorela");
        System.out.println(nombres);
    }
}
