package arrays;

import java.util.ArrayList;
import java.util.Arrays;

public class Teoria {
    public static void main(String[] args) {
        // declarar un array
        int[] numeros = new int[10];
        System.out.println("==============ARRAY ANTES DEL CAMBIO====================");
        System.out.println(Arrays.toString(numeros));
        numeros[5] = 24;
        System.out.println("==============ARRAY DESPUS DEL CAMBIO===================");
        System.out.println(Arrays.toString(numeros));

        // acceder y imprimir un elemento de un array
        System.out.println("=============IMPRIMIENDO UN VALOR DEL ARRAY================");
        System.out.println(numeros[5]);
        int var1 = numeros[5];
        System.out.println(var1);

        // crear un array segunda forma

        String[] nombres = {"George", "Sheyla", "Juan", "Maria", "Jose", "Julio", "Fiorela"};

        System.out.println(Arrays.toString(nombres));

        // crear array de strings de la manera 1

        String[] estudiantes = new String[20];
        estudiantes[0] = "George";

        // tener cuidado

//       estudiantes[20] = "Jose"; // error

        // Los arrays son estaticos --> No se puede aumentar ni disminuir el tamanio

    }
}
