import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day15 {
    // @ - robot
    // O - box
    // # - wall
    // . - free space

    public static void main(String[] args) throws IOException {
        File file = new File("./src/resources/day15_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<String> lines = reader.lines().toList();

        char[][] warehouse = lines.stream()
                .filter(s -> s.startsWith("#"))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        List<String> directions = Arrays.stream(lines.stream().filter(s -> s.startsWith("<") || s.startsWith(">")
                || s.startsWith("^") || s.startsWith("v")).collect(Collectors.joining()).split("")).toList();

        var robotPosition = new Position(-1, -1);
        for (int i = 0; i < warehouse.length; i++) {
            for (int j = 0; j < warehouse[0].length; j++) {
                if (warehouse[i][j] == '@') {
                    robotPosition = new Position(i, j);
                }
            }
        }

        var count = 1;
        for (String direction : directions) {
            System.out.println("Rodada " + count + ", direction: " + direction);
            robotPosition = move(direction, warehouse, robotPosition);
            printMatrix(warehouse); // Exibe o estado atualizado do armazém após cada movimento

            count++;
        }

    }

    private static Position move(String direction, char[][] warehouse, Position currentPosition) {
        if (Objects.equals(direction, ">")) {
            return moveRight(warehouse, currentPosition);
        }
        if (Objects.equals(direction, "<")) {
            return moveLeft(warehouse, currentPosition);
        }
        if (Objects.equals(direction, "^")) {
            return moveUp(warehouse, currentPosition);
        }
        if (Objects.equals(direction, "v")) {
            return moveDown(warehouse, currentPosition);
        } else return new Position(-1, -1);
    }

    private static Position moveRight(char[][] warehouse, Position currentPosition) {
        var i = currentPosition.getRow();
        var j = currentPosition.getCol();

        List<Integer> boxPositions = new ArrayList<>();

        // moving right, so walking through line >>>
        var wallPosition = -1;
        var noSpacesToMove = true;
        for (int k = j; k < warehouse[0].length; k++) {
            if (warehouse[i][k] == 'O') {
                if (!boxPositions.isEmpty() && k - boxPositions.getLast() > 1) {
                    wallPosition = k;
                    break;
                } else {
                    boxPositions.add(k);
                }
            }
            if (warehouse[i][k] == '#') {
                // found wall
                wallPosition = k;
                break;
            }
            if (warehouse[i][k] == '.') {
                noSpacesToMove = false;
            }
        }

        if (noSpacesToMove) {
            return new Position(i, j);
        }

        if (boxPositions.size() == 1 && (boxPositions.getFirst() == wallPosition - 1)) {
            warehouse[i][j] = '.';
            warehouse[i][j + 1] = '@';
            return new Position(i, j + 1);
        }

        if (!boxPositions.isEmpty()) {
            var newRobotCol = wallPosition - boxPositions.size() - 1;
            warehouse[i][newRobotCol] = '@';
            warehouse[i][j] = '.';

            for (var p = 0; p < boxPositions.size(); p++) {
                warehouse[i][wallPosition - p - 1] = 'O';

                if (boxPositions.get(p) < newRobotCol) {
                    warehouse[i][boxPositions.get(p)] = '.';
                }
            }

            return new Position(i, newRobotCol);
        } else {
            warehouse[i][j] = '.';
            warehouse[i][j + 1] = '@';
            return new Position(i, j + 1);
        }
    }

    private static Position moveLeft(char[][] warehouse, Position currentPosition) {
        var i = currentPosition.getRow();
        var j = currentPosition.getCol();

        List<Integer> boxPositions = new ArrayList<>();

        // moving left, so walking through line <<<
        var wallPosition = -1;
        var noSpacesToMove = true;
        for (int k = j; k >= 0; k--) {
            if (warehouse[i][k] == 'O') {
                if (!boxPositions.isEmpty() && boxPositions.get(boxPositions.size() - 1) - k > 1) {
                    wallPosition = k;
                    break;
                } else {
                    boxPositions.add(k);
                }
            }
            if (warehouse[i][k] == '#') {
                // found wall
                wallPosition = k;
                break;
            }
            if (warehouse[i][k] == '.') {
                noSpacesToMove = false;
            }
        }

        if (noSpacesToMove) {
            return new Position(i, j);
        }

        if (boxPositions.size() == 1 && (boxPositions.getFirst() == wallPosition + 1)) {
            warehouse[i][j] = '.';
            warehouse[i][j - 1] = '@';
            return new Position(i, j - 1);
        }

        if (!boxPositions.isEmpty()) {
            var newRobotCol = wallPosition + boxPositions.size() + 1;
            warehouse[i][newRobotCol] = '@';
            warehouse[i][j] = '.';

            for (var p = 0; p < boxPositions.size(); p++) {
                warehouse[i][wallPosition + p + 1] = 'O';

                if (boxPositions.get(p) > newRobotCol) {
                    warehouse[i][boxPositions.get(p)] = '.';
                }
            }

            return new Position(i, newRobotCol);
        } else {
            warehouse[i][j] = '.';
            warehouse[i][j - 1] = '@';
            return new Position(i, j - 1);
        }
    }

    private static Position moveUp(char[][] warehouse, Position currentPosition) {
        var i = currentPosition.getRow();
        var j = currentPosition.getCol();

        List<Integer> boxPositions = new ArrayList<>();

        // moving up, so walking through column ^^^
        var wallPosition = -1;
        var noSpacesToMove = true;
        for (int k = i; k >= 0; k--) {
            if (warehouse[k][j] == 'O') {
                if (!boxPositions.isEmpty() && boxPositions.get(boxPositions.size() - 1) - k > 1) {
                    wallPosition = k;
                    break;
                } else {
                    boxPositions.add(k);
                }
            }
            if (warehouse[k][j] == '#') {
                // found wall
                wallPosition = k;
                break;
            }
            if (warehouse[k][j] == '.') {
                noSpacesToMove = false;
            }
        }

        if (noSpacesToMove) {
            return new Position(i, j);
        }

        if (boxPositions.size() == 1 && (boxPositions.getFirst() - wallPosition == 1)) {
            warehouse[i][j] = '.';
            warehouse[i - 1][j] = '@';
            return new Position(i - 1, j);
        }

        if (!boxPositions.isEmpty()) {
            var newRobotRow = wallPosition + boxPositions.size() + 1;
            warehouse[newRobotRow][j] = '@';
            warehouse[i][j] = '.';

            for (var p = 0; p < boxPositions.size(); p++) {
                warehouse[wallPosition + p + 1][j] = 'O';

                if (boxPositions.get(p) > newRobotRow) {
                    warehouse[boxPositions.get(p)][j] = '.';
                }
            }

            return new Position(newRobotRow, j);
        } else {
            warehouse[i][j] = '.';
            warehouse[i - 1][j] = '@';
            return new Position(i - 1, j);
        }
    }

    private static Position moveDown(char[][] warehouse, Position currentPosition) {
        var i = currentPosition.getRow();
        var j = currentPosition.getCol();

        List<Integer> boxPositions = new ArrayList<>();

        // moving down, so walking through column vvv
        var wallPosition = -1;
        var noSpacesToMove = true;
        for (int k = i; k < warehouse.length; k++) {
            if (warehouse[k][j] == 'O') {
                if (!boxPositions.isEmpty() && k - boxPositions.get(boxPositions.size() - 1) > 1) {
                    wallPosition = k;
                    break;
                } else {
                    boxPositions.add(k);
                }
            }
            if (warehouse[k][j] == '#') {
                // found wall
                wallPosition = k;
                break;
            }
            if (warehouse[k][j] == '.') {
                noSpacesToMove = false;
            }
        }

        if (noSpacesToMove) {
            return new Position(i, j);
        }

        if (boxPositions.size() == 1 && (wallPosition - boxPositions.getFirst() == 1)) {
            warehouse[i][j] = '.';
            warehouse[i + 1][j] = '@';
            return new Position(i + 1, j);
        }

        if (!boxPositions.isEmpty()) {
            var newRobotRow = wallPosition - boxPositions.size() - 1;
            warehouse[newRobotRow][j] = '@';
            warehouse[i][j] = '.';

            for (var p = 0; p < boxPositions.size(); p++) {
                warehouse[wallPosition - p - 1][j] = 'O';

                if (boxPositions.get(p) < newRobotRow) {
                    warehouse[boxPositions.get(p)][j] = '.';
                }
            }

            return new Position(newRobotRow, j);
        } else {
            warehouse[i][j] = '.';
            warehouse[i + 1][j] = '@';
            return new Position(i + 1, j);
        }
    }


    private static void printMatrix(char[][] matrix) {
        for (var values : matrix) { // matrix.length = line quantity
            for (char value : values) {
                System.out.print(value);
            }
            System.out.println();
        }
        System.out.println();
    }
}
