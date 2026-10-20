package poo_teoria;

public class Alumno extends Persona {
    private double promedio;

    public Alumno(String nombre, String apellido, int edad, String email, double promedio) {
        super(nombre, apellido, edad, email);
        this.promedio = promedio;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }
}
