import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Day11 {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("./src/resources/day11_input.txt"));
        List<Long> stones = new ArrayList<>(
                Arrays.stream(input.split(" "))
                        .map(Long::parseLong)
                        .toList()
        );

        printStones(stones);

        for(int i = 0; i < 6; i ++) {
            blink(stones);
            printStones(stones);
        }

        var count = stones.size();
        System.out.println("Stone count: " + count);
    }

    private static List<Long> blink(List<Long> stones) {
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
