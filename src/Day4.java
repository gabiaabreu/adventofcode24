import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Day4 {

    public static void main(String[] args) throws Exception {

        // read file
        File file = new File("./src/resources/day4_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<char[]> allLines = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            var lineValues = line.toCharArray();

            allLines.add(lineValues);
        }

        // creating matrix
        int lineSize = allLines.size(); // line quantity
        int columnSize = allLines.getFirst().length; // column size = how many letters in each line
        char[][] matrix = new char[lineSize][columnSize];

        // populating matrix
        for (int i = 0; i < lineSize; i++) {
            matrix[i] = allLines.get(i);
        }

        // print matrix
        // printMatrix(matrix);

        // part one
        findXmas(matrix);

        // part two
        findCrossedMas(matrix);
    }

    private static void findCrossedMas(char[][] matrix) {
        var crossedMasFound = 0;

        // starting from margin + 1 to find letter A (it cant be on border)
        for (int i = 1; i < matrix.length - 1; i++) { // each line
            for (int j = 1; j < matrix[i].length - 1; j++) { // each letter in line - element[i][j]
                if (matrix[i][j] == 'A') {
                    //  S   M
                    //    A
                    // S    M
                    if (matrix[i - 1][j - 1] == 'S' && matrix[i - 1][j + 1] == 'M'
                            && matrix[i + 1][j - 1] == 'S' && matrix[i + 1][j + 1] == 'M') {
                        crossedMasFound++;
                    }
                }
            }
        }

        // if i rotate matrix 90 degrees for 3 times and look again, i'll be able to find:
        //  M   M         S   S           M   S
        //    A             A               A
        // S    S        M    M           M   S
        var matrixCopy = matrix;
        for (int k = 0; k < 3; k++) {
            matrixCopy = rotateMatrix90Degrees(matrixCopy);

            for (int i = 1; i < matrixCopy.length - 1; i++) {
                for (int j = 1; j < matrixCopy[i].length - 1; j++) {
                    if (matrixCopy[i][j] == 'A') {
                        if (matrixCopy[i - 1][j - 1] == 'S' && matrixCopy[i - 1][j + 1] == 'M'
                                && matrixCopy[i + 1][j - 1] == 'S' && matrixCopy[i + 1][j + 1] == 'M') {
                            crossedMasFound++;
                        }
                    }
                }
            }
        }
        System.out.println("number of crossed MAS found: " + crossedMasFound);
    }

    public static char[][] rotateMatrix90Degrees(char[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        char[][] rotated = new char[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][m - 1 - i] = matrix[i][j];
            }
        }

        return rotated;
    }

    private static void findXmas(char[][] matrix) {
        var xmasFound = 0;
        for (int i = 0; i < matrix.length; i++) { // each line
            for (int j = 0; j < matrix[i].length; j++) { // each letter in line - element[i][j]
                if (matrix[i][j] == 'X') {
                    // XMAS
                    if (matrix[i].length > j + 3
                            && matrix[i][j + 1] == 'M' && matrix[i][j + 2] == 'A' && matrix[i][j + 3] == 'S') {
                        xmasFound++;
                    }

                    // X
                    //   M
                    //     A
                    //       S
                    if (i + 3 < matrix.length && j + 3 < matrix[i].length
                            && matrix[i + 1][j + 1] == 'M' && matrix[i + 2][j + 2] == 'A' && matrix[i + 3][j + 3] == 'S') {
                        xmasFound++;
                    }
                }
            }
        }

        // if i rotate matrix 90 degrees for 3 times and look again, i'll be able to find:
        // S A M X
        // X             S
        // M             A
        // A             M
        // S             X
        var matrixCopy = matrix;
        for (int k = 0; k < 3; k++) {
            matrixCopy = rotateMatrix90Degrees(matrixCopy);

            for (int i = 0; i < matrixCopy.length; i++) {
                for (int j = 0; j < matrixCopy[i].length; j++) {
                    if (matrixCopy[i][j] == 'X') {
                        if (matrixCopy[i].length > j + 3
                                && matrixCopy[i][j + 1] == 'M' && matrixCopy[i][j + 2] == 'A' && matrixCopy[i][j + 3] == 'S') {
                            xmasFound++;
                        }
                    }
                }
            }
        }

        // if i rotate matrix 90 degrees for 3 times and look again, i'll be able to find:
        // S                       X             S
        //   A                   M             A
        //     M               A             M
        //       X           S             X
        var diagonalMatrixCopy = matrix;
        for (int k = 0; k < 3; k++) {
            diagonalMatrixCopy = rotateMatrix90Degrees(diagonalMatrixCopy);

            for (int i = 0; i < diagonalMatrixCopy.length; i++) {
                for (int j = 0; j < diagonalMatrixCopy[i].length; j++) {
                    if (diagonalMatrixCopy[i][j] == 'X') {
                        if (i + 3 < diagonalMatrixCopy.length && j + 3 < diagonalMatrixCopy[i].length
                                && diagonalMatrixCopy[i + 1][j + 1] == 'M' && diagonalMatrixCopy[i + 2][j + 2] == 'A' && diagonalMatrixCopy[i + 3][j + 3] == 'S') {
                            xmasFound++;
                        }
                    }
                }
            }
        }

        System.out.println("number of XMAS found: " + xmasFound);
    }

    private static void printMatrix(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) { // matrix.length = line quantity
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
