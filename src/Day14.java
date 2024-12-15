import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 {

    private static final int WIDE = 101;
    private static final int TALL = 103;

    public static void main(String[] args) throws Exception {
        File file = new File("./src/resources/day14_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<Robot> robots = getInput(reader);
        List<Robot> robotsPtTwo = robots.stream()
                .map(robot -> new Robot(robot.getX(), robot.getY(), robot.getWalkX(), robot.getWalkY()))
                .toList();

        // part one
        int[][] robotCount = new int[TALL][WIDE];
        for (var robot : robots) {
            passSeconds(robotCount, robot, 100);
        }

        var bathroomTiles = getBathroomTiles(robotCount, true);

        printMatrix(bathroomTiles);
        var safetyFactor = getSafetyFactor(robotCount);
        System.out.println("Bathroom safety factor: " + safetyFactor);

        // part two
        int[][] robotCountPtTwo = new int[TALL][WIDE];
        char[][] bathroomTilesPtTwo = new char[TALL][WIDE];
        for (int i = 0; i < 10000; i++) {
            passOneSecond(robotsPtTwo, robotCountPtTwo);

            bathroomTilesPtTwo = getBathroomTiles(robotCountPtTwo, false);

            if (containsTopOfTree(bathroomTilesPtTwo)) {
                printMatrix(bathroomTilesPtTwo);
                System.out.println("Seconds elapsed: " + i);
            }
        }
    }

    private static boolean containsTopOfTree(char[][] bathroomTiles) {
        // checks for pattern:
        // ..█..
        // .███.
        // █████

        for (int i = 0; i < TALL - 2; i++) {
            for (int j = 1; j < WIDE - 3; j++) {
                if (bathroomTiles[i][j + 2] == '█' &&
                        bathroomTiles[i + 1][j + 1] == '█' &&
                        bathroomTiles[i + 1][j + 2] == '█' &&
                        bathroomTiles[i + 1][j + 3] == '█' &&
                        bathroomTiles[i + 2][j] == '█' &&
                        bathroomTiles[i + 2][j + 1] == '█' &&
                        bathroomTiles[i + 2][j + 2] == '█' &&
                        bathroomTiles[i + 2][j + 3] == '█' &&
                        bathroomTiles[i + 2][j + 4] == '█') {
                    return true;
                }
            }
        }

        return false;
    }

    private static char[][] getBathroomTiles(int[][] robotCount, boolean numeric) {
        char[][] bathroomTiles = new char[TALL][WIDE];

        for (int i = 0; i < TALL; i++) {
            for (int j = 0; j < WIDE; j++) {
                if (robotCount[i][j] > 0) {
                    if (numeric) {
                        bathroomTiles[i][j] = (char) (robotCount[i][j] + '0');
                    } else {
                        bathroomTiles[i][j] = '█';
                    }
                } else {
                    bathroomTiles[i][j] = '.';
                }
            }
        }

        return bathroomTiles;
    }

    private static void passOneSecond(List<Robot> robots, int[][] robotCount) {
        for (int i = 0; i < TALL; i++) {
            Arrays.fill(robotCount[i], 0);
        }

        for (var robot : robots) {
            var currentX = robot.getX();
            var currentY = robot.getY();
            int newX = (currentX + robot.getWalkX() + TALL) % TALL;
            int newY = (currentY + robot.getWalkY() + WIDE) % WIDE;

            robot.setX(newX);
            robot.setY(newY);

            robotCount[newX][newY]++;
        }
    }

    private static void passSeconds(int[][] robotCount, Robot robot, int seconds) {
        for (int i = 0; i < seconds; i++) {
            var currentX = robot.getX();
            var currentY = robot.getY();
            int newX = (currentX + robot.getWalkX() + TALL) % TALL;
            int newY = (currentY + robot.getWalkY() + WIDE) % WIDE;

            robot.setX(newX);
            robot.setY(newY);
        }

        int finalX = robot.getX();
        int finalY = robot.getY();

        robotCount[finalX][finalY]++;
    }

    private static int getSafetyFactor(int[][] robotCount) {
        var middleRow = robotCount.length / 2;
        var middleCol = robotCount[0].length / 2;

        // 1 3
        // 2 4

        // first quadrant
        var firstCount = 0;
        for (int i = 0; i < middleRow; i++) {
            for (int j = 0; j < middleCol; j++) {
                if (robotCount[i][j] > 0) {
                    firstCount += robotCount[i][j];
                }
            }
        }

        // second quadrant
        var secondCount = 0;
        for (int i = middleRow + 1; i < robotCount.length; i++) {
            for (int j = 0; j < middleCol; j++) {
                if (robotCount[i][j] > 0) {
                    secondCount += robotCount[i][j];
                }
            }
        }

        // third quadrant
        var thirdCount = 0;
        for (int i = 0; i < middleRow; i++) {
            for (int j = middleCol + 1; j < robotCount[0].length; j++) {
                if (robotCount[i][j] > 0) {
                    thirdCount += robotCount[i][j];
                }
            }
        }

        // fourth quadrant
        var fourthCount = 0;
        for (int i = middleRow + 1; i < robotCount.length; i++) {
            for (int j = middleCol + 1; j < robotCount[0].length; j++) {
                if (robotCount[i][j] > 0) {
                    fourthCount += robotCount[i][j];
                }
            }
        }

        return firstCount * secondCount * thirdCount * fourthCount;
    }

    private static List<Robot> getInput(BufferedReader reader) throws IOException {
        List<Robot> robots = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            var split = line.split("v");

            var initialPosition = split[0].trim().replace("p=", "").split(",");
            var initialX = Integer.parseInt(initialPosition[1]);
            var initialY = Integer.parseInt(initialPosition[0]);

            var velocity = split[1].replace("=", "").split(",");
            var walkX = Integer.parseInt(velocity[1]);
            var walkY = Integer.parseInt(velocity[0]);

            robots.add(new Robot(initialX, initialY, walkX, walkY));
        }

        return robots;
    }

    private static class Robot {
        private int x;
        private int y;
        private int walkX;
        private int walkY;

        public Robot(int x, int y, int walkX, int walkY) {
            this.x = x;
            this.y = y;
            this.walkX = walkX;
            this.walkY = walkY;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWalkX() {
            return walkX;
        }

        public int getWalkY() {
            return walkY;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setWalkX(int walkX) {
            this.walkX = walkX;
        }

        public void setWalkY(int walkY) {
            this.walkY = walkY;
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (var values : matrix) { // matrix.length = line quantity
            for (int value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println();
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
