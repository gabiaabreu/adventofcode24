import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day13 {

    public static void main(String[] args) throws Exception {
        File file = new File("./src/resources/day13_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        var machineRules = getInputRules(reader);

        var totalTokensSpent = 0;
        for (var machine : machineRules) {
            MachineResult result = moveClaw(machine.getTargetX(), machine.getTargetY(),
                    machine.getaX(), machine.getaY(), machine.getbX(), machine.getbY());

            if(result.getTokensSpent() != -1) {
                totalTokensSpent += result.getTokensSpent();
            }
        }

        System.out.println(totalTokensSpent);
    }

    // BFS
    private static MachineResult moveClaw(int targetX, int targetY, int aX, int aY, int bX, int bY) {
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
                        var xValue = Integer.parseInt(values[0].replace("X=", ""));
                        var yValue = Integer.parseInt(values[1].trim().replace("Y=", ""));

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

    private static class MachineResult {
        int buttonAPresses;
        int buttonBPresses;
        int tokensSpent;

        MachineResult(int buttonAPresses, int buttonBPresses, int tokensSpent) {
            this.buttonAPresses = buttonAPresses;
            this.buttonBPresses = buttonBPresses;
            this.tokensSpent = tokensSpent;
        }

        public int getTokensSpent() {
            return tokensSpent;
        }
    }

    private static class MachineRules {
        int aX;
        int aY;
        int bX;
        int bY;
        int targetX;
        int targetY;

        public MachineRules() {

        }

        public int getaX() {
            return aX;
        }

        public int getaY() {
            return aY;
        }

        public int getbX() {
            return bX;
        }

        public int getbY() {
            return bY;
        }

        public int getTargetX() {
            return targetX;
        }

        public int getTargetY() {
            return targetY;
        }

        public void setaX(int aX) {
            this.aX = aX;
        }

        public void setaY(int aY) {
            this.aY = aY;
        }

        public void setbX(int bX) {
            this.bX = bX;
        }

        public void setbY(int bY) {
            this.bY = bY;
        }

        public void setTargetX(int targetX) {
            this.targetX = targetX;
        }

        public void setTargetY(int targetY) {
            this.targetY = targetY;
        }
    }

}
