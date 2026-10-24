package atributosStatic;

public class Main {
    public static void main(String[] args) {
        Bloque bloque1 = new Bloque("Tierra");
        Bloque bloque2 = new Bloque("Tierra");
        Bloque bloque3 = new Bloque("Tierra");

        System.out.println(bloque1.getCantidad());
        System.out.println(bloque2.getCantidad());
        System.out.println(bloque3.getCantidad());

        Math.pow(2,4);
        if(Bloque.cantidad >= 100000){
            // operacion de limpieza
        }
    }
}
