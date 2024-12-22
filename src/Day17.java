import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Day17 {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./src/resources/day17_input.txt");
        List<String> lines = new BufferedReader(new FileReader(file)).lines().toList();

        int registerA = Integer.parseInt(lines.getFirst().split(": ")[1]);
        int registerB = Integer.parseInt(lines.get(1).split(": ")[1]);
        int registerC = Integer.parseInt(lines.get(2).split(": ")[1]);

        List<String> program = Arrays.stream(lines.get(4).split(": ")[1].split(",")).toList();

        Map<Integer, Integer> comboOperandMap = new HashMap<>();
        comboOperandMap.put(0, 0);
        comboOperandMap.put(1, 1);
        comboOperandMap.put(2, 2);
        comboOperandMap.put(3, 3);
        comboOperandMap.put(4, registerA);
        comboOperandMap.put(5, registerB);
        comboOperandMap.put(6, registerC);

        List<String> outputs = new ArrayList<>();
        int nextMove = 2;
        for (int i = 0; i < program.size(); i = i + nextMove) {
            var instruction = program.get(i);

            if (i + 1 >= program.size()) {
                break;
            }
            int operand = Integer.parseInt(program.get(i + 1));

            switch (instruction) {
                case "0" -> {
                    var comboOperand = comboOperandMap.get(operand);

                    registerA = (int) (registerA / Math.pow(2, comboOperand));
                    comboOperandMap.replace(4, registerA);
                    nextMove = 2;
                }
                case "1" -> {
                    registerB = registerB ^ operand;
                    comboOperandMap.replace(5, registerB);
                    nextMove = 2;
                }
                case "2" -> {
                    registerB = comboOperandMap.get(operand) % 8;
                    comboOperandMap.replace(5, registerB);
                    nextMove = 2;
                }
                case "3" -> {
                    if (!(registerA == 0)) {
                        i = operand - 1;
                        nextMove = 1;
                    }
                }
                case "4" -> {
                    registerB = registerB ^ registerC;
                    comboOperandMap.replace(5, registerB);
                    nextMove = 2;
                }
                case "5" -> {
                    int result = comboOperandMap.get(operand) % 8;
                    outputs.add(String.valueOf(result));
                    System.out.println(result);
                    nextMove = 2;
                }
                case "6" -> {
                    var comboOperand = comboOperandMap.get(operand);

                    registerB = (int) (registerA / Math.pow(2, comboOperand));
                    comboOperandMap.replace(5, registerB);
                    nextMove = 2;
                }
                case "7" -> {
                    var comboOperand = comboOperandMap.get(operand);

                    registerC = (int) (registerA / Math.pow(2, comboOperand));
                    comboOperandMap.replace(6, registerC);
                    nextMove = 2;
                }
            }
        }

        var outputString = String.join(",", outputs);
        System.out.println(outputString);
    }
}
