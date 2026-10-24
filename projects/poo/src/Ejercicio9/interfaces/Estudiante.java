package Ejercicio9.interfaces;

import Ejercicio9.clases.Curso;

public interface Estudiante {
    boolean matricularCurso(Curso curso);
    boolean entregarTarea(Curso curso, String tarea);
}
