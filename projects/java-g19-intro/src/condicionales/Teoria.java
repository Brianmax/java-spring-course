package condicionales;

import java.util.Scanner;

public class Teoria {
    public static void main(String[] args) {
        // operadores de comparacion
        // resultado booleano
        // valor numerico OPERADOR valor numerico -> respuesta booleana
        System.out.println(5 > 3);
        System.out.println(5 >= 5);
        System.out.println(5 == 5);
        System.out.println(5 == 3);
        System.out.println(5 != 4);

        // operadores logicos
        // AND, OR, NOT
        // resultado booleano
        // valor booleano OPERADOR valor booleano -> respuesta booleana

        // AND

        System.out.println("OPERADORES DE COMPARACION");
        System.out.println("================OPERADOR AND=======================");
        System.out.println(true && true);
        System.out.println(true && false);

        System.out.println("================OPERADOR OR========================");
        System.out.println(true || false);
        System.out.println(false || true);
        System.out.println(false || false);

        System.out.println("================OPERADOR NOT======================");
        System.out.println(!true);
        System.out.println(!false);


        System.out.println("===============CONDICIONALES======================");
        System.out.println("Ingrese la edad del usuario");
        Scanner sc = new Scanner(System.in);
        int edad = sc.nextInt();
        if(edad >= 18) {
            System.out.println("Enviar la pelicula como recomendacion");
        } else {
            System.out.println("Buscar otra pelicula como recomendacion");
        }


    }
}
