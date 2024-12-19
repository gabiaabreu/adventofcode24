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
        File file = new File("./src/resources/day19_input.txt");
        List<String> lines = new BufferedReader(new FileReader(file)).lines().toList();

        List<String> patterns = Arrays.stream(lines.getFirst().split(", ")).toList();
        List<String> targets = lines.subList(2, lines.size());

        int possibleCount = 0;
        long possibleWays = 0;
        for (var target : targets) {
            Map<String, Long> cache = new HashMap<>(); // <substring, how many ways it can be made>

            // part two
            long ways = countWaysToFormDesign(target, patterns, cache);
            possibleWays = possibleWays + ways;

            if (ways > 0) { // part one
                possibleCount++;
            }
        }
        System.out.println(possibleCount + " desired designs can be made");
        System.out.println("Massive possible ways count: " + possibleWays);
    }

    // DP - dynamic programming
    // counts number of ways a design can be formed (using cache)
    // cache stores <substring, how many ways it can be made>
    private static long countWaysToFormDesign(String target, List<String> availablePatterns, Map<String, Long> cache) {
        if (target.isEmpty()) {
            return 1;
        }

        if (cache.containsKey(target)) { // then i already know if it's possible or not
            return cache.get(target);
        }

        long totalWays = 0;
        for (var pattern : availablePatterns) {
            if (target.startsWith(pattern)) {
                // recursion -> adds to totalWays count
                totalWays = totalWays + countWaysToFormDesign(target.substring(pattern.length()), availablePatterns, cache);
            }
        }

        cache.put(target, totalWays); // if not possible, adds to cache with value 0
        return totalWays;
    }

    // DP - dynamic programming
    // only checks if it's possible (using cache)
    // cache stores <substring, is it possible to make>
    private static boolean isPatternPossibleDP(String target, List<String> availablePatterns, Map<String, Boolean> cache) {
        if (target.isEmpty()) {
            return true;
        }

        if (cache.containsKey(target)) { // if target is in cache, I already know if that design can be made or not
            return cache.get(target);
        }

        for (var pattern : availablePatterns) {
            if (target.startsWith(pattern)) {
                if (isPatternPossibleDP(target.substring(pattern.length()), availablePatterns, cache)) {
                    cache.put(target, true); // if substring is possible, adds to cache with true value
                    return true;
                }
            }
        }

        cache.put(target, false); // can't possibly be made, so adds to cache with false value
        return false;
    }

    // DFS - works for small inputs
    // no cache
    private static boolean isPatternPossible(String target, List<String> availablePatterns) {
        if (target.isEmpty()) {
            return true;
        }

        for (var pattern : availablePatterns) {
            if (target.startsWith(pattern)) {
                // removes found pattern from target to keep exploring
                if (isPatternPossible(target.substring(pattern.length()), availablePatterns)) {
                    return true;
                }
            }
        }

        return false;
    }
}
