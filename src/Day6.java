import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Day6 {

    public static void main(String[] args) throws Exception {
        File file = new File("./src/resources/day6_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<char[]> allLines = new ArrayList<>();

        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            var lineValues = inputLine.toCharArray();

            allLines.add(lineValues);
        }

        // creating and filling matrix
        int size = allLines.size();
        char[][] matrix = new char[size][size];

        for (int i = 0; i < size; i++) {
            matrix[i] = allLines.get(i);
        }

        printMatrix(matrix);

        // entering matrix and finding start position
        List<Integer> firstGuardPosition = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == '^') {
                    firstGuardPosition.add(i);
                    firstGuardPosition.add(j);
                }
            }
        }

        var linePosition = firstGuardPosition.get(0);
        var columnPosition = firstGuardPosition.get(1);
        do {
            var lastPlaceVisited = moveGuard(matrix, linePosition, columnPosition);
            if (lastPlaceVisited.isEmpty()) {
                break;
            }

            var oldLinePosition = lastPlaceVisited.get(0);
            var oldColumnPosition = lastPlaceVisited.get(1);
            printMatrix(matrix);

            matrix = rotateMatrix90DegreesToLeft(matrix);
            printMatrix(matrix);

            // when a matrix rotates to the left, [i][j] becomes [n - 1 - j][i] (n = matrix size)
            columnPosition = oldLinePosition;
            linePosition = size - 1 - oldColumnPosition;
        } while (true);

        // looking for X footprints
        printMatrix(matrix);
        var xCount = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 'X') {
                    xCount++;
                }
            }
        }

        System.out.println(xCount);
    }

    private static List<Integer> moveGuard(char[][] matrix, int line, int column) {
        var lastLineVisited = 0;
        var lastColumnVisited = 0;
        List<Integer> lastPlaceVisited = new ArrayList<>();

        while (line >= 0 && matrix[line][column] != '#') {
            matrix[line][column] = 'X';

            lastLineVisited = line;
            lastColumnVisited = column;

            line--;

            if (line == 0 && matrix[line][column] != '#') {
                matrix[line][column] = 'X';
                return List.of();
            }
        }

        lastPlaceVisited.add(lastLineVisited);
        lastPlaceVisited.add(lastColumnVisited);
        return lastPlaceVisited;
    }

    public static char[][] rotateMatrix90DegreesToLeft(char[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        char[][] rotated = new char[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                rotated[n - 1 - j][i] = matrix[i][j];
            }
        }

        return rotated;
    }

    private static void printMatrix(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) { // matrix.length = line quantity
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }
}
