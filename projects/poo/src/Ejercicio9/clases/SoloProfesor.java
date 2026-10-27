package Ejercicio9.clases;

import Ejercicio9.interfaces.Estudiante;
import Ejercicio9.interfaces.Profesor;

public class SoloProfesor extends Persona implements Profesor {
    @Override
    public boolean asignarTarea(Curso curso, String tarea) {
        return false;
    }

    @Override
    public boolean calificarExamen(Curso curso, Estudiante estudiante, double nota) {
        return false;
    }

    @Override
    public void iniciarSesion() {
        System.out.println("Iniciar sesion profesor...");
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Cerrar sesion profesor...");
    }
}
