import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
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

                    //  M   M
                    //    A
                    // S    S
                    if (matrix[i - 1][j - 1] == 'M' && matrix[i - 1][j + 1] == 'M'
                            && matrix[i + 1][j - 1] == 'S' && matrix[i + 1][j + 1] == 'S') {
                        crossedMasFound++;
                    }

                    //  S   S
                    //    A
                    // M    M
                    if (matrix[i - 1][j - 1] == 'S' && matrix[i - 1][j + 1] == 'S'
                            && matrix[i + 1][j - 1] == 'M' && matrix[i + 1][j + 1] == 'M') {
                        crossedMasFound++;
                    }

                    //  M   S
                    //    A
                    // M    S
                    if (matrix[i - 1][j - 1] == 'M' && matrix[i - 1][j + 1] == 'S'
                            && matrix[i + 1][j - 1] == 'M' && matrix[i + 1][j + 1] == 'S') {
                        crossedMasFound++;
                    }
                }
            }
        }
        System.out.println("number of crossed MAS found: " + crossedMasFound);
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

                    // SAMX
                    if (j - 3 >= 0
                            && matrix[i][j - 1] == 'M' && matrix[i][j - 2] == 'A' && matrix[i][j - 3] == 'S') {
                        xmasFound++;
                    }

                    // X
                    // M
                    // A
                    // S
                    if (i + 3 < matrix.length
                            && matrix[i + 1][j] == 'M' && matrix[i + 2][j] == 'A' && matrix[i + 3][j] == 'S') {
                        xmasFound++;
                    }

                    // S
                    // A
                    // M
                    // X
                    if (i - 3 >= 0
                            && matrix[i - 1][j] == 'M' && matrix[i - 2][j] == 'A' && matrix[i - 3][j] == 'S') {
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

                    // S
                    //   A
                    //     M
                    //       X
                    if (i - 3 >= 0 && j - 3 >= 0
                            && matrix[i - 1][j - 1] == 'M' && matrix[i - 2][j - 2] == 'A' && matrix[i - 3][j - 3] == 'S') {
                        xmasFound++;
                    }

                    //       X
                    //     M
                    //   A
                    // S
                    if (i + 3 < matrix.length && j - 3 >= 0
                            && matrix[i + 1][j - 1] == 'M' && matrix[i + 2][j - 2] == 'A' && matrix[i + 3][j - 3] == 'S') {
                        xmasFound++;
                    }

                    //       S
                    //     A
                    //   M
                    // X
                    if (i - 3 >= 0 && j + 3 < matrix[i].length
                            && matrix[i - 1][j + 1] == 'M' && matrix[i - 2][j + 2] == 'A' && matrix[i - 3][j + 3] == 'S') {
                        xmasFound++;
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
