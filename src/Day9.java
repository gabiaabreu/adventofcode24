import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day9 {

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("./src/resources/day9_input.txt"));

        List<Integer> values = Arrays.stream(input.split("")).map(Integer::parseInt).toList();
        List<String> diskMap = new ArrayList<>();

        var dotCount = 0;
        for (int i = 0; i < values.size(); i++) {
            long timesToAdd = values.get(i);

            for (int j = 0; j < timesToAdd; j++) {
                if (isEven(i)) {
                    diskMap.add(String.valueOf(i / 2)); // id
                } else {
                    diskMap.add(".");
                    dotCount++;
                }
            }
        }

//        printDiskMap(diskMap);

        // part one
        List<String> newMap = new ArrayList<>(diskMap);
        int iterations = isEven(dotCount) ? dotCount : dotCount + 1;
        for (int i = 0; i < iterations; i++) {
            newMap = moveValuesPartOne(newMap);
        }

        printDiskMap(newMap);

        var sum = calculateChecksum(newMap);
        System.out.println("Checksum: " + sum);
    }

    private static long calculateChecksum(List<String> diskMap) {
        var firstDotIndex = diskMap.indexOf(".");
        var sublist = diskMap.subList(0, firstDotIndex);

        long sum = 0;

        for (int i = 0; i < sublist.size(); i++) {
            var id = i;
            var value = Long.parseLong(sublist.get(i));

            var partialSum = id * value;
            sum = sum + partialSum;
        }

        return sum;
    }

    private static List<String> moveValuesPartOne(List<String> diskMap) {
        var lastNumberIndex = 0;
        var firstDotIndex = diskMap.indexOf(".");

        for (int j = diskMap.size() - 1; j >= 0; j--) {
            if (!diskMap.get(j).equals(".")) {
                lastNumberIndex = j;
                break;
            }
        }

        var valueToMove = diskMap.get(lastNumberIndex);
        diskMap.set(firstDotIndex, valueToMove);
        diskMap.set(lastNumberIndex, ".");

//        printDiskMap(diskMap);

        return diskMap;
    }

    private static boolean isEven(int number) {
        return number % 2 == 0;
    }

    private static void printDiskMap(List<String> diskMap) {
        for (var x : diskMap) {
            System.out.print(x);
        }
        System.out.println();
    }
}
