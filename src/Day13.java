import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Day13 {

    public static void main(String[] args) throws Exception {
        File file = new File("./src/resources/day13_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

//        String line;
//        while ((line = reader.readLine()) != null) {
//            var intList = Arrays.stream(line.trim().split("\\s+"))
//                    .map(Integer::valueOf)
//                    .toList();
//
//            reportsList.add(intList);
//        }


        var lala = findOptimalSolution(8400, 5400, 94, 34, 22, 67);
        var lala2 = findOptimalSolution(12748, 12176, 26, 66, 67, 21);
        var lala3 = findOptimalSolution(7870, 6450, 17, 86, 84, 37);
        var lala4 = findOptimalSolution(18641, 10279, 69, 23, 27, 71);
        var oi = 1;

    }

    private static class MachineResult {
        int buttonAPresses;
        int buttonBPresses;
        int tokensSpent;

        MachineResult(int buttonAPresses, int buttonBPresses, int tokensSpent) {
            this.buttonAPresses = buttonAPresses;
            this.buttonBPresses = buttonBPresses;
            this.tokensSpent = tokensSpent;
        }
    }

    // BFS
    private static MachineResult findOptimalSolution(int targetX, int targetY, int aX, int aY, int bX, int bY) {
        int maxPresses = 100;

        // Fila para explorar os estados
        Queue<State> queue = new LinkedList<>();
        // Set para evitar revisitar estados já processados
        Set<String> visited = new HashSet<>();

        // Adiciona o estado inicial (0 pressões, 0 tokens gastos, posição inicial 0,0)
        queue.offer(new State(0, 0, 0, 0, 0));  // [pressõesA, pressõesB, x, y, tokens]
        visited.add("0,0");

        while (!queue.isEmpty()) {
            var state = queue.poll();
            int aPresses = state.getButtonAPresses();
            int bPresses = state.getButtonBPresses();
            int x = state.getxPosition();
            int y = state.getyPosition();
            int tokens = state.getTokensSpent();

            if (x == targetX && y == targetY) {
                return new MachineResult(aPresses, bPresses, tokens);
            }

            // Verifica as próximas possibilidades (pressionar A ou B)
            if (aPresses < maxPresses) {
                int newX = x + aX;
                int newY = y + aY;
                int newTokens = tokens + 3;
                String newState = newX + "," + newY;

                if (!visited.contains(newState)) {
                    visited.add(newState);
                    queue.offer(new State(aPresses + 1, bPresses, newX, newY, newTokens));
                }
            }

            if (bPresses < maxPresses) {
                int newX = x + bX;
                int newY = y + bY;
                int newTokens = tokens + 1;
                String newState = newX + "," + newY;

                if (!visited.contains(newState)) {
                    visited.add(newState);
                    queue.offer(new State(aPresses, bPresses + 1, newX, newY, newTokens));
                }
            }
        }

        // Caso não consiga encontrar uma solução
        return new MachineResult(-1, -1, -1);
    }

    private static class State {
        int buttonAPresses;
        int buttonBPresses;
        int xPosition;
        int yPosition;
        int tokensSpent;

        State(int buttonAPresses, int buttonBPresses, int xPosition, int yPosition, int tokensSpent) {
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

        public int getxPosition() {
            return xPosition;
        }

        public int getyPosition() {
            return yPosition;
        }

        public int getTokensSpent() {
            return tokensSpent;
        }
    }

    // DFS
    // nao eh bom pra esse caso
    private static void findOptimalSolution(int buttonAPresses, int buttonBPresses, int currentX, int currentY,
                                            int targetX, int targetY, int tokensSpent) {
        // base case
        if (buttonAPresses >= 100 || buttonBPresses >= 100
                || currentX > targetX || currentY > targetY) {
            System.out.println("x: " + currentX + ", y: " + currentY);
            return;
        }

        // recursive case
        if (currentX == targetX && currentY == targetY) {
            return;
        }

        findOptimalSolution(buttonAPresses + 1, buttonBPresses,
                currentX + 94, currentY + 34, targetX, targetY,
                tokensSpent + 3);
        findOptimalSolution(buttonAPresses, buttonBPresses + 1,
                currentX + 34, currentY + 67, targetX, targetY,
                tokensSpent + 1);
    }

    private static int calculateTokens(int buttonA, int buttonB) {
        return buttonA * 3 + buttonB;
    }

    private static int calculateXAxis(int buttonA, int buttonB) {
        return buttonA * 94 + buttonB * 34;
    }

    private static int calculateYAxis(int buttonA, int buttonB) {
        return buttonA * 34 + buttonB * 67;
    }
}
