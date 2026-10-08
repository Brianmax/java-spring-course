package loops.ejercicios;

public class Ejercicio6 {
    public static void main(String[] args) {
        // imprimir las tablas de multiplicar del 1 al 10
        for(int i = 1; i <=10; i++) {
            System.out.println("Tabla de multiplicar " + i);
            for(int e = 1; e <=10; e++) {
                System.out.println(i + " * " + e + " = " + i*e);
            }
        }
    }
}
