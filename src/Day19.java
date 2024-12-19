import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {

    public static void main(String[] args) throws IOException {
        // white (w), blue (u), black (b), red (r), or green (g)
        // ggr - green stripe, green stripe, red stripe

        // to display the design rgrgr, you could use =:
        // two rg towels and then an r towel,
        // an rgr towel and then a gr towel,
        // or even a single massive rgrgr towel

        File file = new File("./src/resources/day19_input.txt");
        List<String> lines = new BufferedReader(new FileReader(file)).lines().toList();

        List<String> patterns = Arrays.stream(lines.getFirst().split(", ")).toList();
        List<String> targets = lines.subList(2, lines.size());

        // part one
        int possibleCount = 0;
//        for (var target : targets) {
//            Map<String, Boolean> memo = new HashMap<>();
//            if (isPatternPossibleDP(target, patterns, memo)) {
//                possibleCount++;
//            }
//        }
//        System.out.println(possibleCount + " desired designs can be made");

        long possibleWays = 0;
        for (var target : targets) {
            Map<String, Long> memo = new HashMap<>();
            long ways = countWaysToFormDesign(target, patterns, memo);
            possibleWays = possibleWays + ways;

            if(ways > 0) {
                possibleCount++;
            }
        }
        System.out.println(possibleCount + " desired designs can be made");
        System.out.println("Massive possible ways count: " + possibleWays);
    }

    private static long countWaysToFormDesign(String target, List<String> availablePatterns, Map<String, Long> memo) {
        if (target.isEmpty()) {
            return 1; // Caso base: uma forma de formar um design vazio
        }

        if (memo.containsKey(target)) {
            return memo.get(target); // Retorna o resultado memorizado
        }

        long totalWays = 0;

        for (var pattern : availablePatterns) {
            if (target.startsWith(pattern)) {
                // Soma todas as formas possíveis para o restante do target
                totalWays = totalWays + countWaysToFormDesign(target.substring(pattern.length()), availablePatterns, memo);
            }
        }

        memo.put(target, totalWays); // Memoriza o número de formas para o target atual
        return totalWays;
    }

    private static boolean isPatternPossibleDP(String target, List<String> availablePatterns, Map<String, Boolean> memo) {
        if (target.isEmpty()) {
            return true; // Caso base: o target vazio é sempre possível
        }

        if (memo.containsKey(target)) {
            return memo.get(target); // Retorna o resultado memorizado
        }

        for (var pattern : availablePatterns) {
            if (target.startsWith(pattern)) {
                // Se o prefixo corresponder, continue verificando o restante
                if (isPatternPossibleDP(target.substring(pattern.length()), availablePatterns, memo)) {
                    memo.put(target, true); // Memoriza o resultado
                    return true;
                }
            }
        }

        memo.put(target, false); // Memoriza como impossível formar esse target
        return false;
    }

    // DFS - works for small inputs
    private static boolean isPatternPossible(String target, List<String> availablePatterns) {
        if (target.isEmpty()) {
            return true;
        }

        for (var pattern : availablePatterns) {
            if (target.startsWith(pattern)) {
                if (isPatternPossible(target.substring(pattern.length()), availablePatterns)) {
                    return true;
                }
            }
        }

        return false;
    }
}
