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

        // finding digits and saving correspondent positions
        Map<Character, List<Position>> positionsMap = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                var target = matrix[i][j];

                if (Character.isLetterOrDigit(target)) {
                    var position = new Position(i, j);

                    positionsMap.putIfAbsent(target, new ArrayList<>());
                    positionsMap.get(target).add(position);
                }
            }
        }

        for (var entry : positionsMap.entrySet()) {
            System.out.println("Antenna frequency: " + entry.getKey() + " | Placed at: " + entry.getValue());

            for (int i = 0; i < entry.getValue().size() - 1; i++) {
                var antiNodesCreated = comparePositionsRecursive(entry.getValue(), new ArrayList<>(), matrix.length);

                for (var antiNode : antiNodesCreated) {
                    matrix[antiNode.getRow()][antiNode.getCol()] = '#';
                }
            }
        }

        printMatrix(matrix);

        var count = 0;
        for (char[] chars : matrix) {
            for (char target : chars) {
                if (target == '#' || Character.isLetterOrDigit(target)) {
                    count++;
                }
            }
        }

        System.out.println("Antinodes found in map: " + count);
    }

    // recursive
    private static List<Position> comparePositionsRecursive(List<Position> positions, List<Position> antinodes, int size) {
        // CASO BASE
        if (positions.size() <= 1) {
            return positions;
        }

        // CASO RECURSIVO
        for (int i = 1; i < positions.size(); i++) {
            Position firstPosition = positions.getFirst();
            Position secondPosition = positions.get(i);

            List<Position> createdAntinodesOneDirection = createAntiNodes(firstPosition, secondPosition, size);
            List<Position> createdAntinodesOtherDirection = createAntiNodes(secondPosition, firstPosition, size);
            antinodes.addAll(createdAntinodesOneDirection);
            antinodes.addAll(createdAntinodesOtherDirection);
        }

        positions.removeFirst(); // ja comparei com todos os outros, entao posso remover
        comparePositionsRecursive(positions, antinodes, size);

        return antinodes;
    }

    private static List<Position> createAntiNodes(Position positionOne, Position positionTwo, int size) {
        List<Position> positionsCreated = new ArrayList<>();

        int rowDiff = positionTwo.getRow() - positionOne.getRow();
        int colDiff = positionTwo.getCol() - positionOne.getCol();

        int newRow = positionOne.getRow() + rowDiff;
        int newCol = positionOne.getCol() + colDiff;

        while (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
            positionsCreated.add(new Position(newRow, newCol));
            newRow = newRow + rowDiff;
            newCol = newCol + colDiff;
        }

        return positionsCreated;
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

    // iterative
//    private static List<Position> comparePositions(List<Position> positions) {
//        List<Position> antinodes = new ArrayList<>();
//
//        for (int i = 0; i < positions.size(); i++) {
//            for (int j = positions.size() - 1; j >= 0; j--) {
//                var createdNodes = createAntiNode(positions.get(i), positions.get(j));
//
//                antinodes.addAll(createdNodes);
//            }
//        }
//
//        return antinodes;
//    }

    private static void printMatrix(char[][] matrix) {
        for (char[] chars : matrix) { // matrix.length = line quantity
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------------");
    }
}
