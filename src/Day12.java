import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

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

        var totalFenceCost = 0;
        var totalFenceCostWithDiscount = 0;
        // entering matrix
        for (int i = 0; i < garden.length; i++) {
            for (int j = 0; j < garden[0].length; j++) {
                // if not visited yet
                if (!visited[i][j]) {
                    Region region = new Region();
                    exploreGarden(i, j, garden, garden[i][j], visited, region);

                    // adds found region to key (letter)
                    plantMap.putIfAbsent(garden[i][j], new ArrayList<>());
                    plantMap.get(garden[i][j]).add(region);

                    // calculate region fence cost
                    var cost = getFenceCost(region, garden);
                    totalFenceCost = totalFenceCost + cost;

                    var discountCost = getFenceCostPartTwo(region);
                    totalFenceCostWithDiscount = totalFenceCostWithDiscount + discountCost;
                }
            }
        }

        // printPlantRegions(plantMap, garden);
        System.out.println("Total fence cost: " + totalFenceCost);
        System.out.println("Total fence cost with discount: " + totalFenceCostWithDiscount);
    }

    private static void exploreGarden(int i, int j, char[][] matrix, char currentPlant,
                                      boolean[][] visited, Region region) {
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
            int row = pos.getRow();
            int col = pos.getCol();

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

    private static int getFenceCostPartTwo(final Region region) {
        int totalCorners = 0;
        int area = region.getPositions().size();

        for (final Position position : region.getPositions()) {
            int corners = 0;
            if (!hasLeftNeighbour(position, region) && !hasUpperNeighbour(position, region)
                    || !hasUpperLeftNeighbour(position, region) && hasLeftNeighbour(position, region)
                    && hasUpperNeighbour(position, region)) {
                corners++;
            }
            if (!hasRightNeighbour(position, region) && !hasUpperNeighbour(position, region)
                    || !hasUpperRightNeighbour(position, region) && hasRightNeighbour(position, region)
                    && hasUpperNeighbour(position, region)) {
                corners++;
            }
            if (!hasRightNeighbour(position, region) && !hasLowerNeighbour(position, region)
                    || !hasLowerRightNeighbour(position, region) && hasRightNeighbour(position, region)
                    && hasLowerNeighbour(position, region)) {
                corners++;
            }
            if (!hasLeftNeighbour(position, region) && !hasLowerNeighbour(position, region)
                    || !hasLowerLeftNeighbour(position, region) && hasLeftNeighbour(position, region)
                    && hasLowerNeighbour(position, region)) {
                corners++;
            }
            totalCorners += corners;
        }

        return totalCorners * area;
    }

    private static boolean hasLeftNeighbour(final Position p, final Region region) {
        return region.getPositions().contains(new Position(p.getRow() - 1, p.getCol()));
    }

    private static boolean hasRightNeighbour(final Position p, final Region region) {
        return region.getPositions().contains(new Position(p.getRow() + 1, p.getCol()));
    }

    private static boolean hasUpperNeighbour(final Position p, final Region region) {
        return region.getPositions().contains(new Position(p.getRow(), p.getCol() - 1));
    }

    private static boolean hasLowerNeighbour(final Position p, final Region region) {
        return region.getPositions().contains(new Position(p.getRow(), p.getCol() + 1));
    }

    private static boolean hasLowerLeftNeighbour(final Position p, final Region region) {
        return region.getPositions().contains(new Position(p.getRow() - 1, p.getCol() + 1));
    }

    private static boolean hasUpperRightNeighbour(final Position p, final Region region) {
        return region.getPositions().contains(new Position(p.getRow() + 1, p.getCol() - 1));
    }

    private static boolean hasUpperLeftNeighbour(final Position p, final Region region) {
        return region.getPositions().contains(new Position(p.getRow() - 1, p.getCol() - 1));
    }

    private static boolean hasLowerRightNeighbour(final Position p, final Region region) {
        return region.getPositions().contains(new Position(p.getRow() + 1, p.getCol() + 1));
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

    private static void printPlantRegions(Map<Character, List<Region>> plantMap, char[][] garden) {
        for (var entry : plantMap.entrySet()) {
            var letter = entry.getKey();
            System.out.println("Plant: " + letter);

            int regionNumber = 1;
            for (Region region : entry.getValue()) {
                System.out.println("Região " + regionNumber + ":");

                var cost = getFenceCost(region, garden);

                var discountCost = getFenceCostPartTwo(region);

                System.out.println("Letter " + letter + " region " + regionNumber);
                System.out.println("Fence cost: " + cost);
                System.out.println("Fence cost with discount: " + discountCost + "\n");

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
