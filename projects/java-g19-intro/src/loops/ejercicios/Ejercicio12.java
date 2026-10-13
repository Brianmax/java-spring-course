package loops.ejercicios;

import java.util.Scanner;

public class Ejercicio12 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String testCadena = "Java es un lenguaje de programacion";
        char letraContar = 'a';
        System.out.println(testCadena);

        char letra = testCadena.charAt(9);
        System.out.println(letra);

        int contador = 0;
        for(int i = 0; i < testCadena.length(); i++) {
            char letraCadena = testCadena.charAt(i);
            if(letraCadena == letraContar) {
                 contador++;
            }
        }
        System.out.println(contador);
    }
}
