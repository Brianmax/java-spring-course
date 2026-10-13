package loops.ejercicios;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Ejercicio8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el numero de elementos del array: ");

        int n = sc.nextInt();
        Random random = new Random();
        int[] array = new int[n];
        for(int i = 0; i < n; i++) {
            array[i] = random.nextInt(1000);
        }

        System.out.println(Arrays.toString(array));
        int tamArray = array.length;
        for(int i = tamArray - 1; i >= 0; i--) {
            int numero = array[i];
            System.out.print(numero + ", ");
        }
    }
}
