import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Day10 {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static void main(String[] args) throws Exception {
        File file = new File("./src/resources/day10_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int[][] topographicMap = reader.lines()
                .map(line -> line.chars().map(Character::getNumericValue).toArray())
                .toArray(int[][]::new);

        List<Position> zeroPositions = new ArrayList<>();
        for (int i = 0; i < topographicMap.length; i++) {
            for (int j = 0; j < topographicMap[i].length; j++) {
                var target = topographicMap[i][j];

                if (target == 0) {
                    zeroPositions.add(new Position(i, j));
                }
            }
        }

        int score = 0;
        for (var x : zeroPositions) {
            score = score + checkAdjacents(x.getRow(), x.getCol(), topographicMap, 0, new HashSet<>());
        }
        System.out.println(score);
    }

    private static int checkAdjacents(int i, int j, int[][] matrix, int heightCount, Set<Position> visited) {
        var currentPosition = new Position(i, j);

        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length
                || visited.contains(currentPosition) || matrix[i][j] != heightCount) {
            return 0;
        }

        visited.add(currentPosition);

        if (heightCount == 9) {
            visited.remove(currentPosition);
            return 1;
        }

        int score = 0;
        for (int[] direction : DIRECTIONS) {
            int newi = i + direction[0];
            int newj = j + direction[1];
            score = score + checkAdjacents(newi, newj, matrix, heightCount + 1, visited);
        }

        visited.remove(currentPosition);
        return score;
    }

    private static class Position {
        private final int row;
        private final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getCol() {
            return col;
        }

        public int getRow() {
            return row;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] values : matrix) { // matrix.length = line quantity
            for (int value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------------");
    }
}
