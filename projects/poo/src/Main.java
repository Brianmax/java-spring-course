import poo_teoria.Persona;

public class Main {
    public static void main(String[] args) {
        Persona persona = new Persona(
                "George",
                "Maxi",
                29,
                "georgemaxi@email.com"
        );
        persona.imprimirDetalles();
        persona.setEdad(-39);
        System.out.println("===========================");
        persona.imprimirDetalles();

    }
}
