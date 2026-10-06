package variables.ejercicios;


import java.util.Scanner;

public class Ejercicio2 {
    public static void main(String[] args) {
        // dividir dos numeros y imprimir el resultado
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el primer numero");
        double num1 = sc.nextDouble();
        System.out.println("Ingrese el segundo numero");
        double num2 = sc.nextDouble();

        double resultado = num1 / num2;

        System.out.println(resultado);


    }
}
