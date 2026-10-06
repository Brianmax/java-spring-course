package loops;

public class Teoria {
    public static void main(String[] args) {
        for(int i = 1; i <=10; i++) {
            System.out.println(i);
        }

        for(int i = 1; i <=10; i+=2) {
            System.out.println(i);
        }

        int contador = 1;
        while(contador <=10) {
            System.out.println("Contador menor a 10");
            contador++;
        }

        System.out.println("===================OPERADOR ++ ==========================");
        int  a = 10;
        a++; // suma 1 al valor de a --> 11
        ++a; // representa lo mismo


        int b = 10;
        // ++ antes (++b==10) significa primero sumar y luego hacer la comparacion
        // ++ despues (b++==10) primero realiza la comparacion y luego suma
        if(b==10) {
            System.out.println("El valor de B es " + b);
            System.out.println("Ingresamos a la condicion if");
        } else {
            System.out.println("El valor de B ya no es 10");
        }

    }
}

// imprimir los numeros del 2 al 100
// imprimir solo los numeros pares
// imprimir los numeros del 100 al cero (reversa) 100, 99, 98, ..., 3, 2, 1, 0
// verificar si un numero es primo (solo divisible entre el mismo y la unidad)
// ejemplo 7 solo se puede dividir entre 7 y 1
// ejemplo 11 solo se puede dividir entre 11 y 1
