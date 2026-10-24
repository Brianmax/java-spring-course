package interfaces;

public class Jugador implements Personaje{
    @Override
    public void atacar() {
        System.out.println("Personaje atacando...");
    }

    @Override
    public void regeneracion() {
        System.out.println("Jugador regenerandose...");
    }
}
