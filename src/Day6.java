import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

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

        // printMatrix(matrix);

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

        // part one
        var linePosition = firstGuardPosition.get(0);
        var columnPosition = firstGuardPosition.get(1);
        var partOneMatrix = matrix;
        partOneMatrix = partOne(partOneMatrix, linePosition, columnPosition);

        // looking for X footprints
        var xCount = 0;
        for (int i = 0; i < partOneMatrix.length; i++) {
            for (int j = 0; j < partOneMatrix[i].length; j++) {
                if (partOneMatrix[i][j] == 'X') {
                    xCount++;
                }
            }
        }
        // System.out.println(xCount);

        // part two
        var loopCount = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != '#' && matrix[i][j] != '^') {
                    var newMatrix = copyMatrix(matrix);
                    newMatrix[i][j] = '#';

                    var movingAroundResult = partOne(newMatrix, linePosition, columnPosition);
                    var loopSecretResult = new char[][]{{'?'}};

                    if (Arrays.deepEquals(movingAroundResult, loopSecretResult)) {
                        loopCount++;
                    }
                }
            }
        }

        System.out.println("Guard is looping in these alternate realities! :O");
        System.out.println("How many realities? " + loopCount);
    }

    private static char[][] partOne(char[][] partOneMatrix, int linePosition, int columnPosition) {
        int i = 0;

        Map<Integer, Integer> placesVisitedOnMapA = new HashMap<>();
        Map<Integer, Integer> placesVisitedOnMapB = new HashMap<>();
        Map<Integer, Integer> placesVisitedOnMapC = new HashMap<>();
        Map<Integer, Integer> placesVisitedOnMapD = new HashMap<>();

        List<Map<Integer, Integer>> allPlacesVisited = Arrays.asList(
                placesVisitedOnMapA,
                placesVisitedOnMapB,
                placesVisitedOnMapC,
                placesVisitedOnMapD
        );

        while (true) {
            var lastPlaceVisited = moveGuard(partOneMatrix, linePosition, columnPosition);
            if (lastPlaceVisited.isEmpty()) {
                break;
            }

            Map<Integer, Integer> targetMap = allPlacesVisited.get(i);
            if (targetMap.containsKey(lastPlaceVisited.getFirst())
                    && Objects.equals(targetMap.get(lastPlaceVisited.getFirst()), lastPlaceVisited.get(1))) {
                // guard has been here before!
                return new char[][]{{'?'}};
            }

            targetMap.put(lastPlaceVisited.getFirst(), lastPlaceVisited.get(1));

            var oldLinePosition = lastPlaceVisited.get(0);
            var oldColumnPosition = lastPlaceVisited.get(1);
            // printMatrix(partOneMatrix);

            partOneMatrix = rotateMatrix90DegreesToLeft(partOneMatrix);
            // printMatrix(partOneMatrix);

            // when a matrix rotates to the left, [i][j] becomes [n - 1 - j][i] (n = matrix size)
            columnPosition = oldLinePosition;
            linePosition = partOneMatrix.length - 1 - oldColumnPosition;


            i++;
            if (i == 4) {
                i = 0;
            }
        }

        return partOneMatrix;
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

    private static char[][] copyMatrix(char[][] original) {
        char[][] copy = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}
