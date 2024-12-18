import utils.Position;

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

        // part one
        int partOneScore = 0;
        for (var startPosition : zeroPositions) {
            boolean[][] visited = new boolean[topographicMap.length][topographicMap[0].length];
            int partialScore = exploreTrailPartOne(startPosition.getRow(),
                    startPosition.getCol(), topographicMap, 0, visited);
            partOneScore += partialScore;
        }
        System.out.println("Part one trailheads score sum: " + partOneScore);

        // part two
        int partTwoScore = 0;
        for (var startPosition : zeroPositions) {
            partTwoScore = partTwoScore + exploreTrailPartTwo(startPosition.getRow(),
                    startPosition.getCol(), topographicMap, 0);
        }
        System.out.println("Part two trailheads score sum: " + partTwoScore);
    }

    private static int exploreTrailPartOne(int i, int j, int[][] matrix, int currentHeight, boolean[][] visited) {
        // base case: index out of bounds, position already visited or value not on current height
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length
                || visited[i][j] || matrix[i][j] != currentHeight) {
            return 0;
        }

        visited[i][j] = true;

        if (currentHeight == 9) {
            return 1;
        }

        // recursive case
        int score = 0;
        for (int[] direction : DIRECTIONS) {
            int newi = i + direction[0];
            int newj = j + direction[1];
            score = score + exploreTrailPartOne(newi, newj, matrix, currentHeight + 1, visited);
        }

        // mark position as not visited (backtracking)
        visited[i][j] = false;
        return score;
    }

    private static int exploreTrailPartTwo(int i, int j, int[][] matrix, int currentHeight) {
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length
                || matrix[i][j] != currentHeight) {
            return 0;
        }

        if (currentHeight == 9) {
            return 1;
        }

        int score = 0;
        for (int[] direction : DIRECTIONS) {
            int newi = i + direction[0];
            int newj = j + direction[1];
            score = score + exploreTrailPartTwo(newi, newj, matrix, currentHeight + 1);
        }

        return score;
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
