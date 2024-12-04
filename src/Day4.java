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

        int lineSize = allLines.size(); // quantas linhas tera a matriz = quantidade de registros
        int columnSize = allLines.getFirst().length; // quantas colunas = quant de valores de cada registro
        char[][] matrix = new char[lineSize][columnSize];

        for (int i = 0; i < lineSize; i++) {
            matrix[i] = allLines.get(i);
        }

        // print matrix
        for (int i = 0; i < matrix.length; i++) { // matrix.length = quantidade de linhas
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        var xmasFound = 0;
        for (int i = 0; i < matrix.length; i++) { // cada linha
            for (int j = 0; j < matrix[i].length; j++) { // cada elemento da linha - elemento[i][j]
                if (matrix[i][j] == 'X') {
                    // XMAS
                    if (matrix[i].length > j + 3) {
                        if (matrix[i][j + 1] == 'M' && matrix[i][j + 2] == 'A' && matrix[i][j + 3] == 'S') {
                            xmasFound++;
                        }
                    }

                    // SAMX
                    if (j - 3 >= 0) {
                        if (matrix[i][j - 1] == 'M' && matrix[i][j - 2] == 'A' && matrix[i][j - 3] == 'S') {
                            xmasFound++;
                        }
                    }

                    // X
                    // M
                    // A
                    // S
                    if (i + 3 < matrix.length) {
                        if (matrix[i + 1][j] == 'M' && matrix[i + 2][j] == 'A' && matrix[i + 3][j] == 'S') {
                            xmasFound++;
                        }
                    }

                    // S
                    // A
                    // M
                    // X
                     if (i - 3 >= 0) {
                        if (matrix[i - 1][j] == 'M' && matrix[i - 2][j] == 'A' && matrix[i - 3][j] == 'S') {
                            xmasFound++;
                        }
                    }

                    // X
                    //   M
                    //     A
                    //       S
                    if (i + 3 < matrix.length && j + 3 < matrix[i].length) {
                        if (matrix[i + 1][j + 1] == 'M' && matrix[i + 2][j + 2] == 'A' && matrix[i + 3][j + 3] == 'S') {
                            xmasFound++;
                        }
                    }

                    // S
                    //   A
                    //     M
                    //       X
                    if (i - 3 >= 0 && j - 3 >= 0) {
                        if (matrix[i - 1][j - 1] == 'M' && matrix[i - 2][j - 2] == 'A' && matrix[i - 3][j - 3] == 'S') {
                            xmasFound++;
                        }
                    }

                    //       X
                    //     M
                    //   A
                    // S
                    if (i + 3 < matrix.length && j - 3 >= 0) {
                        if (matrix[i + 1][j - 1] == 'M' && matrix[i + 2][j - 2] == 'A' && matrix[i + 3][j - 3] == 'S') {
                            xmasFound++;
                        }
                    }

                    //       S
                    //     A
                    //   M
                    // X
                    if (i - 3 >= 0 && j + 3 < matrix[i].length) {
                        if (matrix[i - 1][j + 1] == 'M' && matrix[i - 2][j + 2] == 'A' && matrix[i - 3][j + 3] == 'S') {
                            xmasFound++;
                        }
                    }
                }

            }
        }

        System.out.println("number of XMAS found: " + xmasFound);

    }
}
