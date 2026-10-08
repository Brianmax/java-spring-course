package loops.ejercicios;

public class Ejercicio2 {
    public static void main(String[] args) {
        System.out.println("===================VERSION 1===========================");
        for(int i = 2; i <=100; i++) {
            if(i % 2 == 0) {
                System.out.println(i);
            }
        }
        System.out.println("===================VERSION 2===========================");
        for(int i = 2; i <= 100; i+=2) {
            System.out.println(i);
        }
    }
}
