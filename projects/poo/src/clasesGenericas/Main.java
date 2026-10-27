package clasesGenericas;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Tupla tupla1 = new Tupla(3,5);
        TuplaFloat tupla2 = new TuplaFloat(9.5f, 7.2f);
        TuplaString tupla3 = new TuplaString("George", "Maxi");
        System.out.println(tupla1);

        TuplaGenerica<Integer> tuplaGenerica1 = new TuplaGenerica<>(3,5);
        TuplaGenerica<Float> tuplaGenerica2 = new TuplaGenerica<>(9.5f, 7.2f);
        TuplaGenerica<String> tuplaGenerica3 =  new TuplaGenerica<>("George", "Maxi");

        // Implementar un arrayList generico

    }
}
