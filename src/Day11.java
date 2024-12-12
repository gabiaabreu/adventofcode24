import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day11 {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("./src/resources/day11_input.txt"));
        long[] stones = Arrays.stream(input.split(" "))
                .mapToLong(Long::parseLong)
                .toArray();

        // map <number, count>
        Map<Long, Long> stoneMap = new HashMap<>();
        for (long stone : stones) {
            // tries to get key (stone number), if exists put value + 1, if doesnt exist insert 0 + 1
            // basically incrementing number count on map
            stoneMap.put(stone, stoneMap.getOrDefault(stone, 0L) + 1);
        }

        for (int i = 0; i < 75; i++) {
            // at first its input map, on other iterations only considers last line of rocks
            stoneMap = blinkOptimized(stoneMap);
        }

        // considering only last line of rocks, counts total of rocks present
        // remembering: key - rock number, value - count
        // so i just have to sum values (all stone counts = total count)
        long count = stoneMap.values().stream().mapToLong(Long::longValue).sum();
        System.out.println("75 blinks stone count: " + count);
    }

    private static Map<Long, Long> blinkOptimized(Map<Long, Long> stoneMap) {
        // map that holds next line of rocks after rules have been applied to current ones
        // resets on every iteration
        Map<Long, Long> nextStoneMap = new HashMap<>();

        for (Map.Entry<Long, Long> entry : stoneMap.entrySet()) {
            long target = entry.getKey();
            long count = entry.getValue();

            if (target == 0) {
                // if 0, increments 1 count on map
                nextStoneMap.put(1L, nextStoneMap.getOrDefault(1L, 0L) + count);
            } else if (isEven(String.valueOf(target).length())) {
                var string = String.valueOf(target);
                var firstHalf = Long.parseLong(string.substring(0, string.length() / 2));
                var secondHalf = Long.parseLong(string.substring(string.length() / 2));

                // increments count on both first and second half value
                nextStoneMap.put(firstHalf, nextStoneMap.getOrDefault(firstHalf, 0L) + count);
                nextStoneMap.put(secondHalf, nextStoneMap.getOrDefault(secondHalf, 0L) + count);
            } else {
                long result = target * 2024;

                // increments count on result value
                nextStoneMap.put(result, nextStoneMap.getOrDefault(result, 0L) + count);
            }
        }

        // returns only last line of rocks
        return nextStoneMap;
    }

    // non optimized - doesnt work on large number of iterations
    private static List<Long> blink(List<Long> stones) {
        // iterator - lets you iterate through current list without altering it
        ListIterator<Long> iterator = stones.listIterator();

        while (iterator.hasNext()) {
            long target = iterator.next();

            if (target == 0) {
                iterator.set(1L);
            } else if (isEven((int) Math.log10(target) + 1)) {
                var string = String.valueOf(target);
                var firstHalf = String.valueOf(target).substring(0, string.length() / 2);
                var secondHalf = String.valueOf(target).substring(string.length() / 2);

                iterator.set(Long.parseLong(firstHalf));
                iterator.add(Long.parseLong(secondHalf));
            } else {
                iterator.set(target * 2024);
            }
        }

        return stones;
    }

    private static boolean isEven(int number) {
        return number % 2 == 0;
    }

    private static void printStones(List<Long> stones) {
        for (var x : stones) {
            System.out.print(x + " ");
        }
        System.out.println();
    }
}
