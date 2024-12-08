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

//        printMatrix(matrix);

        // percorrendo a matrix, achando os caracteres e guardando suas posicoes
        Map<Character, List<Position>> positionsMap = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                var target = matrix[i][j];

                if (Character.isLetterOrDigit(target)) {
                    var position = new Position(i, j);

                    // so adiciona uma nova key caso nao exista
                    positionsMap.putIfAbsent(target, new ArrayList<>());
                    positionsMap.get(target).add(position);
                }
            }
        }

        List<Position> antinodes = new ArrayList<>();

        for (var entry : positionsMap.entrySet()) {
            System.out.println("Antenna frequency: " + entry.getKey() + " | Positions: " + entry.getValue());

            for (int i = 0; i < entry.getValue().size() - 1; i++) {
                var firstPosition = entry.getValue().get(i);
                var secondPosition = entry.getValue().get(i + 1);
//                var thirdPosition = entry.getValue().get(i + 2);
                var antiNodesCreated = createAntiNode(firstPosition, secondPosition);
//                var moreAntinodes = createAntiNode(firstPosition, thirdPosition);

                for (var antinode : antiNodesCreated) {

                    antinodes.add(antinode);
                    matrix[antinode.getRow()][antinode.getCol()] = '#';
                }

//                for (var antinode : moreAntinodes) {
//
//                    if(antinode.getCol() >= 0 && antinode.getRow()>= 0 && antinode.getCol() < matrix.length && antinode.getRow() < matrix.length){
//                        antinodes.add(antinode);
//                        matrix[antinode.getRow()][antinode.getCol()] = '#';
//
//                    }
//                }
            }

        }

        printMatrix(matrix);

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

    private static List<Position> createAntiNode(Position positionOne, Position positionTwo) {
        // esse ta certo, nao mexer
        // (0,0) (2,2) -> (4,4)
        var column = positionTwo.getCol() - positionOne.getCol();
        var row = positionTwo.getRow() - positionOne.getRow();
        var newColumn = positionTwo.getCol() + column;
        var newRow = positionTwo.getRow() + row;

        // (1,8) (2,5) -> (0, 11)
        var c = positionOne.getCol() - positionTwo.getCol();
        var r = positionOne.getRow() - positionTwo.getRow();
        var newnewColumn = positionOne.getCol() + c;
        var newnewRow = positionOne.getRow() + r;

        // (4,4) (2,5) -> (0,6)
        return List.of(new Position(newRow, newColumn), new Position(newnewRow, newnewColumn));
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

        public void setRow(int row) {
            this.row = row;
        }

        public void setCol(int col) {
            this.col = col;
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
