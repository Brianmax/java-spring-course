package variables.ejercicios;
import java.util.Scanner;

public class Ejercicio1 {
    public static void main(String[] args) {
        // Implementar un programa que calcule el area de un circulo
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese el valor del radio");
        System.out.println("Holllllaaa");

        double radio = sc.nextDouble();
        double areaCirculo = Math.PI * radio * radio;

        System.out.println(areaCirculo);
    }
}
