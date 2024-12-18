package utils;

public class MatrixUtils {

    public static void printMatrix(char[][] matrix) {
        for (var values : matrix) {
            for (char value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] values : matrix) {
            for (int value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
