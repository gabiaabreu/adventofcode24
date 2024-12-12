import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day12 {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static void main(String[] args) throws Exception {
        File file = new File("./src/resources/day12_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[][] garden = reader.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        printMatrix(garden);

        // map that stores letter + regions (each Region being a List<Position>)
        // <A, [[(0,0), (0,1)], [(3,3), (3,4)]]
        Map<Character, List<Region>> plantMap = new HashMap<>();
        boolean[][] visited = new boolean[garden.length][garden[0].length];

        // entering matrix
        for (int i = 0; i < garden.length; i++) {
            for (int j = 0; j < garden[0].length; j++) {
                // if not visited yet
                if (!visited[i][j]) {
                    Region region = new Region();
                    exploreGarden(i, j, garden, garden[i][j], visited, region);

                    plantMap.putIfAbsent(garden[i][j], new ArrayList<>());
                    // adds found region to key (letter)
                    plantMap.get(garden[i][j]).add(region);
                }
            }
        }

        // printPlantRegions(plantMap);

        var totalFenceCost = 0;
        for (var entry : plantMap.entrySet()) {
            var letter = entry.getKey();
            int regionNumber = 1;

            // calculates cost for each region of each letter
            for (Region region : entry.getValue()) {
                var cost = getFenceCost(region, garden);
                totalFenceCost = totalFenceCost + cost;

                System.out.println("Letter " + letter + " region " + regionNumber);
                System.out.println("Fence cost: " + cost + "\n");
                regionNumber++;
            }
        }

        System.out.println("Total fence cost: " + totalFenceCost);
    }

    private static void exploreGarden(int i, int j, char[][] matrix, char currentPlant, boolean[][] visited,
                                      Region region) {
        // base case - end recursion
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length
                || visited[i][j] || matrix[i][j] != currentPlant) {
            return;
        }

        // recursive case - mark as visited, add position to Region, keeps looking
        visited[i][j] = true;
        region.getPositions().add(new Position(i, j));

        for (int[] direction : DIRECTIONS) {
            int newi = i + direction[0];
            int newj = j + direction[1];
            exploreGarden(newi, newj, matrix, currentPlant, visited, region);
        }
    }

    private static int getFenceCost(Region region, char[][] matrix) {
        int perimeter = 0;
        int area = region.getPositions().size();

        for (Position pos : region.getPositions()) {
            int row = pos.row;
            int col = pos.col;

            for (int[] direction : DIRECTIONS) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                // if neighbor is out of bounds, means that current element is on borders (fence needed)
                // if neighbor is different plant (fence needed)
                if (newRow < 0 || newRow >= matrix.length || newCol < 0 || newCol >= matrix[0].length
                        || matrix[newRow][newCol] != matrix[row][col]) {
                    perimeter++;
                }
            }
        }

        return perimeter * area;
    }

    private static class Region {
        private List<Position> positions;

        Region() {
            this.positions = new ArrayList<>();
        }

        Region(List<Position> positions) {
            this.positions = positions;
        }

        public List<Position> getPositions() {
            return positions;
        }

        public void setPositions(List<Position> positions) {
            this.positions = positions;
        }
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

    private static void printPlantRegions(Map<Character, List<Region>> plantMap) {
        for (Map.Entry<Character, List<Region>> entry : plantMap.entrySet()) {
            System.out.println("Letra: " + entry.getKey());
            int regionNumber = 1;
            for (Region region : entry.getValue()) {
                System.out.println("Região " + regionNumber + ":");
                for (Position pos : region.getPositions()) {
                    System.out.println(pos);
                }
                regionNumber++;
            }
            System.out.println();
        }
    }

    private static void printMatrix(char[][] matrix) {
        for (var values : matrix) { // matrix.length = line quantity
            for (char value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------------");
    }
}
