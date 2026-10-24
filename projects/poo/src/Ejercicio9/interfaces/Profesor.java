package Ejercicio9.interfaces;

import Ejercicio9.clases.Curso;

public interface Profesor {
    boolean asignarTarea(Curso curso, String tarea);
    boolean calificarExamen(Curso curso, Estudiante estudiante, double nota);
}
