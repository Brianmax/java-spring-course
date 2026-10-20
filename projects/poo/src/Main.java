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


        Alumno estudiante1 = new Alumno(
                "Sheyla",
                "Suares",
                29,
                "shsuares@email.com",
                18
        );

        Profesor profesor1 = new Profesor(
                "George",
                "Maxi",
                29,
                "gmaxi@email.com",
                999.99
        );


        estudiante1.imprimirDetalles();
        System.out.println("=============================");
        profesor1.imprimirDetalles();

        // una variable de la superclase, puede almacenar
        // un objeto de una subclase
        Persona estudiante2 = new Alumno(
                "Jose",
                "Martinez",
                29,
                "jmartinez@jjdjd.com",
                19
        );

        Persona profesor2 = new Profesor(
                "James",
                "Larson",
                21,
                "jlarson@jfdka.com",
                999.99
        );
        System.out.println("============================================");
        estudiante2.imprimirDetalles();
        System.out.println("============================================");
        profesor2.imprimirDetalles();

        ArrayList<Persona> usuarios = new ArrayList<>();
        usuarios.add(estudiante1);
        usuarios.add(profesor1);
        usuarios.add(estudiante2);
        usuarios.add(profesor2);
    }
}
