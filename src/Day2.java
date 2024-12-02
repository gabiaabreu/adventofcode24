import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {

    public static void main(String[] args) throws Exception {

        // read file
        File file = new File("./src/resources/day2_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<List<Integer>> reportsList = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            var intList = Arrays.stream(line.trim().split("\\s+"))
                    .map(Integer::valueOf)
                    .toList();

            reportsList.add(intList);
        }

        // part one - check safe
        var safeCount = 0;
        var conditionalSafeCount = 0;

        for (var report : reportsList) {
            if (isSortedSafely(report)) {
                safeCount++;
            } else {
                // part two - check conditional safe
                if (isConditionalSafe(report)) {
                    conditionalSafeCount++;
                }
            }
        }

        var total = safeCount + conditionalSafeCount;
        System.out.println("Safe count: " + safeCount);
        System.out.println("Conditional safe count: " + conditionalSafeCount);
        System.out.println("Total safe count: " + total);
    }

    private static boolean isConditionalSafe(List<Integer> report) {
        for (int i = 0; i < report.size(); i++) {
            // removes value in given index, creates newList without it
            // if newList passes on isIncreasing/isDecreasing
            // then it's safe
            List<Integer> newList = new ArrayList<>(report);
            newList.remove(i);

            if (isSortedSafely(newList)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSortedSafely(List<Integer> report) {
        var increasing = false;
        var decreasing = false;

        for (int i = 0; i < report.size() - 1; i++) {
            var current = report.get(i);
            var next = report.get(i + 1);

            // absolute difference can't be greater than 3
            // numbers can't be equal
            if (current.equals(next) || Math.abs(next - current) > 3) {
                return false;
            }

            if (current > next) {
                decreasing = true;
            }
            if (next > current) {
                increasing = true;
            }
        }
        return increasing ^ decreasing;
    }
}