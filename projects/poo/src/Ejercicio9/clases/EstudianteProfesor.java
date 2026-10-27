package Ejercicio9.clases;

import Ejercicio9.interfaces.Estudiante;
import Ejercicio9.interfaces.Profesor;

public class EstudianteProfesor extends Persona implements Estudiante, Profesor {
    @Override
    public boolean matricularCurso(Curso curso) {
        return false;
    }

    @Override
    public boolean entregarTarea(Curso curso, String tarea) {
        return false;
    }

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

    }

    @Override
    public void cerrarSesion() {

    }
}
