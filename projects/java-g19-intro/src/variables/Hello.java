package variables;

public class Hello {
    public static void main(String[] args) {
        System.out.println("variables.Hello World!");
        System.out.println("Hola Mundo!" + " En Java");

        // String nombreCompleto = "George Maxi"; error no se pueden tener dos variables con el mismo nombre
        // comentario de todo un bloque
        /*
        String var1 = "Test";
        int var2 = 232;
        boolean var3 = false;
        */

        String nombreCompleto = "George Maxi Ccapa";
        int edad = 29;
        // M significa masculino y F feminino
        char sexo = 'M';
        float estatura = 1.7f;
        String ciudad = "Arequipa";

        System.out.println("Hola soy " + nombreCompleto + " tengo " + edad + " y soy de " + ciudad);

        // operaciones con variables

        int a = 10;
        int b = 20;

        int resultado = a + b; // 30
        System.out.println(resultado);

        String cadena1 = "Hola ";
        String cadena2 = "mundo";
        // concatenacion de cadenas
        // Solo se puede usar el operador + para operaciones con String
        // operadores como -, *, / no funcionan y daran error
        String respuestaCadenas = cadena1 + cadena2;
        System.out.println(respuestaCadenas);

        // division

        float numero1 = 30;
        float numero2 = 9;
        // int / int --> int
        // float / int --> float
        // int / float --> float
        // float / float --> float
//        int respuesta = numero1 / numero2; error
        System.out.println("El resultado es: " + numero1/numero2);


        int var1 = 10;
        var1 = 20;
        var1 = var1+5; // var1+=5

        int mensajesSinLeer = 3;
        // te llega un nuevo mensaje
        mensajesSinLeer = mensajesSinLeer + 1; // 4
        // lees todos los mensajes
        mensajesSinLeer = 0;
    }
}
