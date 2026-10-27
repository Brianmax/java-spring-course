package Ejercicio9.clases;

import Ejercicio9.interfaces.Estudiante;

public class SoloEstudiante extends Persona implements Estudiante {
    @Override
    public boolean matricularCurso(Curso curso) {
        return false;
    }

    @Override
    public boolean entregarTarea(Curso curso, String tarea) {
        return false;
    }

    @Override
    public void iniciarSesion() {
        System.out.println("Iniciando sesion profesor...");
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Cerrando sesion profesor...");
    }
}
