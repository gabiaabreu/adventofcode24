import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Day1 {

    public static void main(String[] args) throws Exception {

        // read file
        File file = new File("./src/resources/day1_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        // separate lists
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            var values = line.trim().split("\\s+");
            leftList.add(Integer.parseInt(values[0]));
            rightList.add(Integer.parseInt(values[1]));
        }

        // sort lists
        // cost -> O(n log n)
        Collections.sort(leftList);
        Collections.sort(rightList);

        var distanceSum = getDistanceSum(leftList, rightList);
        System.out.println("Total distance: " + distanceSum);

        var similarity = getSimilarity(leftList, rightList);
        System.out.println("Similarity: " + similarity);
    }

    // complexity O(n)
    private static int getDistanceSum(List<Integer> leftList, List<Integer> rightList) {
        var distanceSum = 0;
        for (int i = 0; i < leftList.size(); i++) {
            var distance = Math.abs(leftList.get(i) - rightList.get(i));
            distanceSum = distanceSum + distance;
        }
        return distanceSum;
    }

    // complexity O(n + m)
    private static int getSimilarity(List<Integer> leftList, List<Integer> rightList) {
        var similarity = 0;
        var numberMap = new HashMap<Integer, Integer>();

        for (Integer leftNumber : leftList) {
            if (numberMap.containsKey(leftNumber)) {
                var foundValue = numberMap.get(leftNumber);
                var timesShown = foundValue + 1;

                numberMap.put(leftNumber, timesShown);
            } else {
                numberMap.put(leftNumber, 1);
            }
        }

        for (Integer rightNumber : rightList) {
            if (numberMap.containsKey(rightNumber)) {
                var timesFoundOnLeft = numberMap.get(rightNumber);
                var toAdd = timesFoundOnLeft * rightNumber;

                similarity = similarity + toAdd;
            }
        }

        return similarity;
    }

    // complexity O(n * m)
//    private static int getSimilarity(List<Integer> leftList, List<Integer> rightList) {
//        var similarity = 0;
//        for (Integer leftNumber : leftList) {
//            for (Integer rightNumber : rightList) {
//                if (Objects.equals(rightNumber, leftNumber)) {
//                    similarity = similarity + leftNumber;
//                }
//            }
//        }
//        return similarity;
//    }
}
