import poo_teoria.Alumno;
import poo_teoria.Persona;
import poo_teoria.Profesor;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Persona persona = new Persona(
                "George",
                "Maxi",
                29,
                "georgemaxi@email.com"
        );
//        persona.imprimirDetalles();
        persona.setEdad(-39);
        System.out.println("===========================");
//        persona.imprimirDetalles();
        ArrayList arrayList = new ArrayList();


        Alumno alumno = new Alumno(
                "Sheyla",
                "Suares",
                29,
                "shsuares@email.com",
                18
        );

        Profesor profesor = new Profesor(
                "George",
                "Maxi",
                29,
                "gmaxi@email.com",
                999.99
        );


        alumno.imprimirDetalles();
        System.out.println("=============================");
        profesor.imprimirDetalles();

    }
    // Profesor: double salario
}
