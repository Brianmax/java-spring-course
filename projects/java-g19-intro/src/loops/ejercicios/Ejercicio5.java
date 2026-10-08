package loops.ejercicios;

public class Ejercicio5 {
    public static void main(String[] args) {
        int[] array = {20,32,43,83,8,-42,56,23,90,-100};

        int acumalado=0;
        for (int i=0; i < array.length; i++) {
            acumalado += array[i];
        }
        System.out.println("El total de la suma es: " + acumalado);
    }
}
