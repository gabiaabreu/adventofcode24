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

                var antiNodesCreated = comparePositionsRecursive(entry.getValue(), new ArrayList<>(), matrix.length);

                for (var antinode : antiNodesCreated) {
                    if (antinode.getCol() >= 0 && antinode.getRow() >= 0
                            && antinode.getCol() < matrix.length && antinode.getRow() < matrix.length) {

                        antinodes.add(antinode);
                        matrix[antinode.getRow()][antinode.getCol()] = '#';
                    }
                }
            }
        }

        printMatrix(matrix);

        // part two
        var count = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                var target = matrix[i][j];

                if (target == '#' || Character.isLetterOrDigit(target)) {
                    count++;
                }
            }
        }

        System.out.println(count);
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

            List<Position> createdAntinodes = createAntiNode(firstPosition, secondPosition, size);
            antinodes.addAll(createdAntinodes);
        }

        positions.removeFirst(); // ja comparei, entao posso remover
        comparePositionsRecursive(positions, antinodes, size);

        return antinodes;
    }

    private static List<Position> createAntiNode(Position positionOne, Position positionTwo, int size) {
        // (0,0) (2,2) -> (4,4)
        // downwards
        List<Position> createdDownwards = createDownwards(positionOne, positionTwo, size);

        // (2,5) (1,8) -> (0, 11)
        // upwards
        List<Position> createdUpwards = createUpwards(positionOne, positionTwo, size);

        List<Position> positionsCreated = new ArrayList<>();
        positionsCreated.addAll(createdDownwards);
        positionsCreated.addAll(createdUpwards);

        return positionsCreated;
    }

    private static List<Position> createUpwards(Position positionOne, Position positionTwo, int size) {
        List<Position> positionsCreated = new ArrayList<>();

        var rowDif = positionOne.getRow() - positionTwo.getRow();
        var columnDif = positionOne.getCol() - positionTwo.getCol();

        var newRow = positionOne.getRow() + rowDif;
        var newColumn = positionOne.getCol() + columnDif;

        while (newRow >= 0 && newRow < size && newColumn >= 0 && newColumn < size) {
            positionsCreated.add(new Position(newRow, newColumn));

            newRow = newRow + rowDif;
            newColumn = newColumn + columnDif;
        }

        return positionsCreated;
    }

    private static List<Position> createDownwards(Position positionOne, Position positionTwo, int size) {
        List<Position> positionsCreated = new ArrayList<>();

        var rowDif = positionTwo.getRow() - positionOne.getRow();
        var columnDif = positionTwo.getCol() - positionOne.getCol();

        var newRow = positionTwo.getRow() + rowDif;
        var newColumn = positionTwo.getCol() + columnDif;

        while (newRow >= 0 && newRow < size && newColumn >= 0 && newColumn < size) {
            positionsCreated.add(new Position(newRow, newColumn));

            newRow = newRow + rowDif;
            newColumn = newColumn + columnDif;
        }

        return positionsCreated;
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
