import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {

    public static void main(String[] args) throws Exception {
        File file = new File("./src/resources/day8_input.txt");
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

        // finding digits and saving positions
        Map<Character, List<Position>> positionsMap = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                var target = matrix[i][j];

                if (Character.isLetterOrDigit(target)) {
                    var position = new Position(i, j);

                    // only adds to map if digit is absent
                    positionsMap.putIfAbsent(target, new ArrayList<>());
                    positionsMap.get(target).add(position);
                }
            }
        }

        List<Position> antinodes = new ArrayList<>();

        for (var entry : positionsMap.entrySet()) {
            System.out.println("Antenna frequency: " + entry.getKey() + " | Positions: " + entry.getValue());

            for (int i = 0; i < entry.getValue().size() - 1; i++) {

                // var antiNodesCreated = comparePositions(entry.getValue());
                var antiNodesCreated = comparePositionsRecursive(entry.getValue(), new ArrayList<>());

                for (var antinode : antiNodesCreated) {
                    if (antinode.getCol() >= 0 && antinode.getRow() >= 0
                            && antinode.getCol() < matrix.length && antinode.getRow() < matrix.length
                            && matrix[antinode.getRow()][antinode.getCol()] != entry.getKey()) {

                        antinodes.add(antinode);
                        matrix[antinode.getRow()][antinode.getCol()] = '#';
                    }
                }
            }
        }

        printMatrix(matrix);

        // cuidado, pois estou substituindo outras letras por #
        var count = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                var target = matrix[i][j];

                if (target == '#') {
                    count++;
                }
            }
        }

        System.out.println(count);
    }

    // recursive
    private static List<Position> comparePositionsRecursive(List<Position> positions, List<Position> antinodes) {
        // CASO BASE
        if (positions.size() <= 1) {
            return positions;
        }

        // CASO RECURSIVO
        for (int i = 1; i < positions.size(); i++) {
            Position firstPosition = positions.getFirst();
            Position secondPosition = positions.get(i);

            List<Position> createdAntinodes = createAntiNode(firstPosition, secondPosition);
            antinodes.addAll(createdAntinodes);
        }

        positions.removeFirst(); // ja comparei, entao posso remover
        comparePositionsRecursive(positions, antinodes);

        return antinodes;
    }

    // iterative
    private static List<Position> comparePositions(List<Position> positions) {
        List<Position> antinodes = new ArrayList<>();

        for (int i = 0; i < positions.size(); i++) {
            for (int j = positions.size() - 1; j >= 0; j--) {
                var createdNodes = createAntiNode(positions.get(i), positions.get(j));

                for (var node : createdNodes) {
                    antinodes.add(node);
                }
            }
        }

        return antinodes;
    }

    private static List<Position> createAntiNode(Position positionOne, Position positionTwo) {
        // (0,0) (2,2) -> (4,4)
        // downwards
        var rowDif = positionTwo.getRow() - positionOne.getRow();
        var columnDif = positionTwo.getCol() - positionOne.getCol();
        var newRow = positionTwo.getRow() + rowDif;
        var newColumn = positionTwo.getCol() + columnDif;

        // (2,5) (1,8) -> (0, 11)
        // upwards
        var otherRowDif = positionOne.getRow() - positionTwo.getRow();
        var otherColumnDif = positionOne.getCol() - positionTwo.getCol();
        var otherNewRow = positionOne.getRow() + otherRowDif;
        var otherNewColumn = positionOne.getCol() + otherColumnDif;

        return List.of(new Position(newRow, newColumn), new Position(otherNewRow, otherNewColumn));
    }

    private static class Position {
        private int row;
        private int col;

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

    private static void printMatrix(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) { // matrix.length = line quantity
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------------");
    }
}
