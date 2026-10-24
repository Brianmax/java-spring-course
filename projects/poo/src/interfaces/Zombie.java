package interfaces;


public class Zombie implements Personaje {
    @Override
    public void atacar() {
        System.out.println("Zombie atacando...");
    }

    @Override
    public void regeneracion() {
        System.out.println("Zombie regenerandose...");
    }
}
