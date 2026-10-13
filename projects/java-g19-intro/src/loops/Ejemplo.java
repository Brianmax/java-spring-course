package loops;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Ejemplo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println(n);

        Random random = new Random();
        int[] numeros = new int[n];

        for(int i = 0; i < n; i++) {
            int numAleatorio = random.nextInt(101);
            numeros[i] = numAleatorio;
        }

        System.out.println(Arrays.toString(numeros));

        int acumulado = 0;
        for(int i = 0; i < n; i++) {
            if(i % 2 == 0) {
                acumulado+=numeros[i];
            } else {
                acumulado-=numeros[i];
            }
        }
        System.out.println("El valor del acumulado es: " + acumulado);
    }
}
