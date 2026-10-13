package loops.ejercicios;

import java.util.Arrays;
import java.util.Scanner;

public class Ejercicio5Loops {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el valor de n");
        int n = sc.nextInt();
        int[] array = new int[n];

        for(int i = 0; i < n; i++) {
            System.out.println("Ingrese el elemento " + i + ":");
            array[i] = sc.nextInt();
        }
        System.out.println(Arrays.toString(array));
    }
}
