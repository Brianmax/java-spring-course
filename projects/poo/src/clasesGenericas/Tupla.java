package clasesGenericas;

public class Tupla {
    private int x;
    private int y;

    public Tupla(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
