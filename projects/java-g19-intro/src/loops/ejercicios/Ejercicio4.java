package loops.ejercicios;

import java.util.Scanner;

public class Ejercicio4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese el numero a validar");
        int numero = sc.nextInt();
        int contadorDivisores = 0;
        for(int i = 1; i <= numero; i++) {
            if(numero % i == 0) {
                contadorDivisores++;
            }
        }
        if(contadorDivisores > 2) {
            System.out.println("No es un primo");
        } else {
            System.out.println("Si es un numero primo");
        }
    }
}
