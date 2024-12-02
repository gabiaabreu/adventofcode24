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

        // part one
        // check safe
        var safeCount = 0;
        List<int[]> unsafeReportsList = new ArrayList<>();

        for (var report : reportsList) {
            var reportList = Arrays.stream(report).boxed().toList(); // TODO: ja salvar como List la em cima
            if (isIncreasing(reportList) || isDecreasing(reportList)) {
                safeCount++;
            } else {
                unsafeReportsList.add(report);
            }
        }

        System.out.println("Safe count: " + safeCount);

        // part two
        // check conditional safe
        var conditionalSafeCount = 0;
        for (var report : unsafeReportsList) {
            if (isConditionalSafe(report)) {
                conditionalSafeCount++;
            }
        }

        System.out.println("Conditional safe count: " + conditionalSafeCount);

        var total = safeCount + conditionalSafeCount;
        System.out.println("Total safe count: " + total);

    }

    private static boolean isConditionalSafe(int[] report) {
        List<Integer> list = new ArrayList<>(Arrays.stream(report).boxed().toList());

        for (int i = 0; i < list.size(); i++) {
            // remove number in given index, create newList without it
            // if newList passes on isIncreasing/isDecreasing
            // then it's safe

            List<Integer> newList = new ArrayList<>(list);
            newList.remove(i);

            if (isIncreasing(newList) || isDecreasing(newList)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isIncreasing(List<Integer> report) {
        for (int i = 0; i < report.size() - 1; i++) {
            var current = report.get(i);
            var next = report.get(i + 1);

            if (current >= next || (next - current > 3)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isDecreasing(List<Integer> report) {
        for (int i = 0; i < report.size() - 1; i++) {
            var current = report.get(i);
            var next = report.get(i + 1);

            if (current <= next || (current - next > 3)) {
                return false;
            }
        }

        return true;
    }
}