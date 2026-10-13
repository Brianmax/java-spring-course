package arrays;

import java.util.Random;

public class ArraysBidimensionales {
    public static void main(String[] args) {
        int[][] arrayBidimensional = new int[4][3];
        Random random = new Random();
        for(int i = 0; i < 4; i++) {
            for(int e = 0; e < 3; e++) {
                arrayBidimensional[i][e] = random.nextInt(100);
            }
        }

    }
}
