package loops.ejercicios;

import java.util.ArrayList;
import java.util.LinkedList;

public class Ejercicio5 {
    public static void main(String[] args) {
        int[] array = {20,32,43,83,8,-42,56,23,90,-100};
        ArrayList<Integer> numerosEnteros = new ArrayList<>();
        LinkedList<Integer> numerosLinkedList = new LinkedList<>();
        numerosEnteros.add(21);
        numerosEnteros.add(19);
        numerosEnteros.add(78);
        numerosEnteros.add(83);

        numerosLinkedList.add(21);
        numerosLinkedList.add(19);
        numerosLinkedList.add(78);
        numerosLinkedList.add(83);


        int acumuladoArray=0;
        int tamArray = array.length;
        for (int i=0; i < tamArray; i++) {
            acumuladoArray += array[i];
        }
        System.out.println("El total de la suma es: " + acumuladoArray);
        int tamArrayList = numerosEnteros.size();
        int acumuladoArrayList = 0;
        for(int i = 0; i < tamArrayList; i++) {
            acumuladoArrayList += numerosEnteros.get(i);
        }
    }
}
