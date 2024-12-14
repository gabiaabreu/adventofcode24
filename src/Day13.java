import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class Day13 {

    public static void main(String[] args) throws Exception {
        File file = new File("./src/resources/day13_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        var machineRules = getInputRules(reader);

        BigInteger totalTokensSpentPartOne = BigInteger.ZERO;
        BigInteger totalTokensSpentPartTwo = BigInteger.ZERO;
        for (var machine : machineRules) {
            // part one - BFS solution
            MachineResult result = moveClawBFS(machine.getTargetX() - 10000000000000L,
                    machine.getTargetY() - 10000000000000L, machine.getaX(),
                    machine.getaY(), machine.getbX(), machine.getbY());

            if (!result.getTokensSpent().equals(BigInteger.valueOf(-1))) {
                totalTokensSpentPartOne = totalTokensSpentPartOne.add(result.getTokensSpent());
            }

            // part two - linear system solution
            MachineResult resultPartTwo = solveSystem(BigInteger.valueOf(machine.getaX()),
                    BigInteger.valueOf(machine.getaY()), BigInteger.valueOf(machine.getbX()),
                    BigInteger.valueOf(machine.getbY()), BigInteger.valueOf(machine.getTargetX()),
                    BigInteger.valueOf(machine.getTargetY()));

            if (!resultPartTwo.getTokensSpent().equals(BigInteger.valueOf(-1))) {
                totalTokensSpentPartTwo = totalTokensSpentPartTwo.add(resultPartTwo.getTokensSpent());
            }
        }

        System.out.println(totalTokensSpentPartOne);
        System.out.println(totalTokensSpentPartTwo);
    }

    private static MachineResult solveSystem(BigInteger aX, BigInteger aY, BigInteger bX, BigInteger bY,
                                            BigInteger prizeX, BigInteger prizeY) {
        // calculating system determinant
        BigInteger determinant = aX.multiply(bY).subtract(bX.multiply(aY));

        // solving linear system
        BigInteger aPresses = (prizeX.multiply(bY).subtract(prizeY.multiply(bX))).divide(determinant);
        BigInteger bPresses = (aX.multiply(prizeY).subtract(aY.multiply(prizeX))).divide(determinant);

        // comparing result with target positions
        if (!(aPresses.multiply(aX).add(bPresses.multiply(bX)).equals(prizeX)
                && aPresses.multiply(aY).add(bPresses.multiply(bY)).equals(prizeY))) {
            return new MachineResult(BigInteger.valueOf(-1), BigInteger.valueOf(-1), BigInteger.valueOf(-1));
        }

        BigInteger tokens = aPresses.multiply(BigInteger.valueOf(3L)).add(bPresses);
        return new MachineResult(aPresses, bPresses, tokens);
    }

    // BFS - Breadth-First Search - Busca em Largura
    private static MachineResult moveClawBFS(long targetX, long targetY, long aX, long aY, long bX, long bY) {
        long maxPresses = 100;

        Queue<State> stateQueue = new LinkedList<>();
        Set<Position> visitedPositions = new HashSet<>();

        // adds initial state
        stateQueue.offer(new State(0, 0, 0, 0, 0));
        visitedPositions.add(new Position(0, 0));

        while (!stateQueue.isEmpty()) {
            var state = stateQueue.poll();
            int aPresses = state.getButtonAPresses();
            int bPresses = state.getButtonBPresses();
            var x = state.getxPosition();
            var y = state.getyPosition();
            int tokens = state.getTokensSpent();

            // if current position = target position
            if (x == targetX && y == targetY) {
                return new MachineResult(BigInteger.valueOf(aPresses),
                        BigInteger.valueOf(bPresses), BigInteger.valueOf(tokens));
            }

            // press A
            if (aPresses < maxPresses) {
                var newX = x + aX;
                var newY = y + aY;
                int newTokens = tokens + 3;

                if (!visitedPositions.contains(new Position(newX, newY))) {
                    visitedPositions.add(new Position(newX, newY));
                    stateQueue.offer(new State(aPresses + 1, bPresses, newX, newY, newTokens));
                }
            }

            // press B
            if (bPresses < maxPresses) {
                var newX = x + bX;
                var newY = y + bY;
                int newTokens = tokens + 1;

                if (!visitedPositions.contains(new Position(newX, newY))) {
                    visitedPositions.add(new Position(newX, newY));
                    stateQueue.offer(new State(aPresses, bPresses + 1, newX, newY, newTokens));
                }
            }
        }

        // no solution found
        return new MachineResult(BigInteger.valueOf(-1), BigInteger.valueOf(-1), BigInteger.valueOf(-1));
    }

    // DFS - not proper solution for this problem
    //    private static void moveClawDFS(int buttonAPresses, int buttonBPresses, int currentX, int currentY,
    //                                    int targetX, int targetY, int tokensSpent) {
    //        // base case
    //        if (buttonAPresses >= 100 || buttonBPresses >= 100
    //                || currentX > targetX || currentY > targetY) {
    //            System.out.println("x: " + currentX + ", y: " + currentY);
    //            return;
    //        }
    //
    //        // recursive case
    //        if (currentX == targetX && currentY == targetY) {
    //            return;
    //        }
    //
    //        moveClawDFS(buttonAPresses + 1, buttonBPresses,
    //                currentX + 94, currentY + 34, targetX, targetY,
    //                tokensSpent + 3);
    //        moveClawDFS(buttonAPresses, buttonBPresses + 1,
    //                currentX + 34, currentY + 67, targetX, targetY,
    //                tokensSpent + 1);
    //    }

    private static List<MachineRules> getInputRules(BufferedReader reader) throws IOException {
        List<MachineRules> machineRules = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) {
                break;
            }

            var rule = new MachineRules();
            for (int i = 0; i < 3 && line != null; i++) {
                String ruleName = line.split(":")[0].trim();
                String ruleValue = line.split(":")[1].trim();

                switch (ruleName) {
                    case "Button A" -> {
                        var values = ruleValue.trim().split(",");
                        var axValue = Integer.parseInt(values[0].replace("X+", ""));
                        var ayValue = Integer.parseInt(values[1].trim().replace("Y+", ""));

                        rule.setaX(axValue);
                        rule.setaY(ayValue);
                    }
                    case "Button B" -> {
                        var values = ruleValue.trim().split(",");
                        var bxValue = Integer.parseInt(values[0].replace("X+", ""));
                        var byValue = Integer.parseInt(values[1].trim().replace("Y+", ""));

                        rule.setbX(bxValue);
                        rule.setbY(byValue);
                    }
                    case "Prize" -> {
                        var values = ruleValue.trim().split(",");
                        var xValue = 10000000000000L + Long.parseLong(values[0].replace("X=", ""));
                        var yValue = 10000000000000L + Long.parseLong(values[1].trim().replace("Y=", ""));

                        rule.setTargetX(xValue);
                        rule.setTargetY(yValue);
                    }
                }
                line = reader.readLine();
            }
            machineRules.add(rule);
        }

        return machineRules;
    }

    private static class State {
        int buttonAPresses;
        int buttonBPresses;
        long xPosition;
        long yPosition;
        int tokensSpent;

        State(int buttonAPresses, int buttonBPresses, long xPosition, long yPosition, int tokensSpent) {
            this.buttonAPresses = buttonAPresses;
            this.buttonBPresses = buttonBPresses;
            this.xPosition = xPosition;
            this.yPosition = yPosition;
            this.tokensSpent = tokensSpent;
        }

        public int getButtonAPresses() {
            return buttonAPresses;
        }

        public int getButtonBPresses() {
            return buttonBPresses;
        }

        public long getxPosition() {
            return xPosition;
        }

        public long getyPosition() {
            return yPosition;
        }

        public int getTokensSpent() {
            return tokensSpent;
        }
    }

    private static class MachineResult {
        BigInteger buttonAPresses;
        BigInteger buttonBPresses;
        BigInteger tokensSpent;

        MachineResult(BigInteger buttonAPresses, BigInteger buttonBPresses, BigInteger tokensSpent) {
            this.buttonAPresses = buttonAPresses;
            this.buttonBPresses = buttonBPresses;
            this.tokensSpent = tokensSpent;
        }

        public BigInteger getTokensSpent() {
            return tokensSpent;
        }
    }

    private static class MachineRules {
        long aX;
        long aY;
        long bX;
        long bY;
        long targetX;
        long targetY;

        public MachineRules() {
        }

        public long getaX() {
            return aX;
        }

        public long getaY() {
            return aY;
        }

        public long getbX() {
            return bX;
        }

        public long getbY() {
            return bY;
        }

        public long getTargetX() {
            return targetX;
        }

        public long getTargetY() {
            return targetY;
        }

        public void setaX(long aX) {
            this.aX = aX;
        }

        public void setaY(long aY) {
            this.aY = aY;
        }

        public void setbX(long bX) {
            this.bX = bX;
        }

        public void setbY(long bY) {
            this.bY = bY;
        }

        public void setTargetX(long targetX) {
            this.targetX = targetX;
        }

        public void setTargetY(long targetY) {
            this.targetY = targetY;
        }
    }

    private static class Position {
        private final long row;
        private final long col;

        Position(long row, long col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }

        @Override
        public boolean equals(Object o) { // super important for .contains() to work
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

}
