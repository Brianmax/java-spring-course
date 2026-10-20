package poo_teoria;

public class Profesor extends Persona{
    private double salario;
    public Profesor(String nombre, String apellido, int edad, String email, double salario) {
        super(nombre, apellido, edad, email);
        this.salario = salario;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
