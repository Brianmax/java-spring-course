package Ejercicio9;

import Ejercicio9.clases.EstudianteProfesor;
import Ejercicio9.clases.SoloEstudiante;
import Ejercicio9.clases.SoloProfesor;
import Ejercicio9.interfaces.Estudiante;
import Ejercicio9.interfaces.Profesor;

public class Main {
    public static void main(String[] args) {
        SoloEstudiante soloEstudiante = new SoloEstudiante();
        Estudiante estudiante = new SoloEstudiante();
        Profesor profesor1 = new SoloProfesor();
        Profesor profesor2 = new EstudianteProfesor();
    }
}
