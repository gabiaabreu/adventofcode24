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

        List<int[]> reportsList = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            var intArray = Arrays.stream(line.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
            reportsList.add(intArray);
        }
//        for (int[] array : reportsList) {
//            System.out.println(Arrays.toString(array));
//        }

        // check increasing
        var safeCount = 0;
        for (var report : reportsList) {
            if (isIncreasing(report) || isDecreasing(report)) {
                safeCount++;
            }
        }

        System.out.println(safeCount);
    }

    private static boolean isIncreasing(int[] report) {
        for (int i = 0; i < report.length - 1; i++) {
            var current = report[i];
            var next = report[i + 1];

            if (current >= next || (next - current > 3)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isDecreasing(int[] report) {
        for (int i = 0; i < report.length - 1; i++) {
            var current = report[i];
            var next = report[i + 1];

            if (current <= next || (current - next > 3)) {
                return false;
            }
        }

        return true;
    }
}