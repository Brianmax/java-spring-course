package clasesAbstractas;

public abstract class Figura {
    protected String color;

    public Figura(String color) {
        this.color = color;
    }

    public abstract double area();
    public abstract double perimetro();

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
