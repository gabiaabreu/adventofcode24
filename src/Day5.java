import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {

    public static void main(String[] args) throws Exception {

        // read file
        File file = new File("./src/resources/day5_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<List<Integer>> rules = new ArrayList<>();
        List<List<Integer>> records = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            var ruleRegex = "^\\d{2}\\|\\d{2}$";
            Pattern pattern = Pattern.compile(ruleRegex);
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) { // looks for pattern NN|NN
                var match = Arrays.stream(matcher.group().split("\\|")).map(Integer::parseInt).toList();

                rules.add(match);
            } else if (!line.isBlank()) { // isBlank searchs for more cases than isEmpty
                records.add(Arrays.stream(line.split(",")).map(Integer::parseInt).toList());
            }
        }

        // assembling key-value map representing all combined rules, e.g.: <97, [13,61,47]>
        var completeRuleMap = new HashMap<Integer, List<Integer>>();
        for (var rule : rules) {
            if (!completeRuleMap.containsKey(rule.get(0))) {
                // fun fact: List.of() creates an immutable list! that's why we're using ArrayList
                completeRuleMap.put(rule.get(0), new ArrayList<>(List.of(rule.get(1))));
            } else {
                List<Integer> found = completeRuleMap.get(rule.getFirst());

                found.add(rule.get(1));
            }
        }

        // printing rule map
        for (Map.Entry<Integer, List<Integer>> entry : completeRuleMap.entrySet()) {
            System.out.print("Key: " + entry.getKey() + " Values: ");
            for (Integer value : entry.getValue()) {
                System.out.print(value + " ");
            }
            System.out.println();
        }

        // part one
        List<List<Integer>> correctRecords = new ArrayList<>();
        List<List<Integer>> fixedRecords = new ArrayList<>();

        for (var record : records) {
            var isOrdered = true;

            for (int i = record.size() - 1; i >= 0; i--) {
                var target = record.get(i);
                if (completeRuleMap.containsKey(target)) {
                    List<Integer> numbersThatShouldBeAfter = completeRuleMap.get(target);

                    if (!checkOtherValues(numbersThatShouldBeAfter, i, record)) {
                        isOrdered = false;
                    }
                }
            }

            if (isOrdered) {
                correctRecords.add(record);
            } else {
                // part two
                List<Integer> fixedRecord = sortRecord(record, completeRuleMap);
                fixedRecords.add(fixedRecord);
            }
        }

        // part one - result
        var sumCorrectMiddleValues = 0;
        for (var correct : correctRecords) {
            var middle = (int) Math.floor((double) correct.size() / 2);
            sumCorrectMiddleValues = sumCorrectMiddleValues + correct.get(middle);
        }
        System.out.println(sumCorrectMiddleValues);

        // part two - result
        var sumFixedMiddleValues = 0;
        for (var fixed : fixedRecords) {
            var middle = (int) Math.floor((double) fixed.size() / 2);
            sumFixedMiddleValues = sumFixedMiddleValues + fixed.get(middle);
        }
        System.out.println(sumFixedMiddleValues);
    }

    private static boolean checkOtherValues(List<Integer> numbersThatShouldBeAfter, Integer targetIndex, List<Integer> record) {
        for (int j = 0; j < targetIndex; j++) { // looks until target index if there's a number that shouldn't be there
            if (numbersThatShouldBeAfter.contains(record.get(j))) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> sortRecord(List<Integer> record, Map<Integer, List<Integer>> rulesMap) {
        Map<Integer, Set<Integer>> graph = new HashMap<>(); // dependencies graph
        Map<Integer, Integer> inDegree = new HashMap<>(); // dependency degree map <value, dependencyDegree>

        for (int value : record) {
            graph.put(value, new HashSet<>()); // adds values in record to graph with a new Set combined
            inDegree.put(value, 0); // how many numbers need to come before this value (dependencyDegree)
        }

        // graph is going to store dependencies - who depends on who (in this particular record)
        for (var rule : rulesMap.entrySet()) {
            int ruleKey = rule.getKey();

            for (int shouldAppearAfter : rule.getValue()) { // rule.getValue = numbers that should appear after key
                if (graph.containsKey(ruleKey) && graph.containsKey(shouldAppearAfter)) {
                    // adds dependency on shouldAppearAfter
                    graph.get(ruleKey).add(shouldAppearAfter);

                    // adds 1 degree of dependency for shouldAppearAfter
                    inDegree.put(shouldAppearAfter, inDegree.get(shouldAppearAfter) + 1);
                }
            }
        }

        // topological sort
        // this queue stores values that have no dependency on others (inDegree = 0)
        Queue<Integer> safeQueue = new LinkedList<>();
        for (Map.Entry<Integer, Integer> degree : inDegree.entrySet()) {
            if (degree.getValue() == 0) {
                safeQueue.add(degree.getKey());
            }
        }

        // builds sorted records following dependencies
        List<Integer> sortedRecord = new ArrayList<>();
        while (!safeQueue.isEmpty()) {
            int safeCurrent = safeQueue.poll();
            sortedRecord.add(safeCurrent); // adds to sorted record queue element with no dependencies

            for (int dependant : graph.get(safeCurrent)) { // for each dependant value on safeCurrent dependency map
                // minus 1 degree of dependency, because safeCurrent was already added to sorted record
                inDegree.put(dependant, inDegree.get(dependant) - 1);

                if (inDegree.get(dependant) == 0) { // if it's then safe, adds dependant to queue!
                    safeQueue.add(dependant);
                }
            }
        }

        return sortedRecord;
    }
}
