package clasesAbstractas;

// no se puede instanciar un objeto de la clase abstracta
// tiene constructor
// tiene atributos
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
