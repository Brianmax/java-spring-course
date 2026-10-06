package condicionales.ejercicios;

import java.util.Scanner;

public class Ejercicio1 {
    public static void main(String[] args) {
        // pedir un numero aleatoria a el usuario y determinar si es par o impar
        // %
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el numero");
        int numero = sc.nextInt();
        if(numero % 2 == 0) {
            System.out.println("El numero es par");
        } else {
            System.out.println("El numero es impar");
        }
    }
}
