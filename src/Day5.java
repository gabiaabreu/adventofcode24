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

            if (matcher.find()) {
                var match = Arrays.stream(matcher.group().split("\\|")).map(Integer::parseInt).toList();

                rules.add(match);
            } else if (!line.isBlank()) { // isblank eh mais abrangente q isempty
                records.add(Arrays.stream(line.split(",")).map(Integer::parseInt).toList());
            }
        }

//        for(var rule : rules) {
//            System.out.println(rule);
//        }

        // percorre lista de rules
        // junta todos os 1os elementos iguais em uma lista de chave valor
        // <97, [13,61,47]> (o 97 chave sempre vem antes dos numeros que estao no valor)
        var completeRuleMap = new HashMap<Integer, List<Integer>>();
        for (var rule : rules) {
            if (!completeRuleMap.containsKey(rule.get(0))) {
                completeRuleMap.put(rule.get(0), new ArrayList<>(List.of(rule.get(1))));
                // curiosidade: o List.of cria uma lista imutavel! para listas mutaveis usar o ArrayList
            } else {
                List<Integer> found = completeRuleMap.get(rule.getFirst());

                found.add(rule.get(1));
            }
        }

        // imprimindo map
//        for (Map.Entry<Integer, List<Integer>> entry : completeRuleMap.entrySet()) {
//            System.out.print("Key: " + entry.getKey() + " Values: ");
//            for (Integer value : entry.getValue()) {
//                System.out.print(value + " ");
//            }
//            System.out.println();
//        }

        // PART ONE

        // percorre lista de registros, percorre registro
        // olhar o ultimo numero do registro primeiro [32,58,33,26]
        // por exemplo, procuro a chave 26 <26, [24,61,47]> o 26 tem que vir antes desses
        // todos parecem estar ok pois nao estao nos valores
        // ai eu procuro a chave 33 <33, [58,61,47]> e os numeros que vem antes
        // ai ja eh um problema, pq o 58 veio antes e nao deveria. ja pausa e descarta o registro
        List<List<Integer>> ordemCerta = new ArrayList<>();
        for (var record : records) {
            var ordem = true;

            for (int i = record.size() - 1; i >= 0; i--) {
                var target = record.get(i);
                if (completeRuleMap.containsKey(target)) {
                    List<Integer> numbersThatShouldBeAfter = completeRuleMap.get(target);

                    if (!checkOtherValues(numbersThatShouldBeAfter, i, record)) {
                        ordem = false;
                    }
                }
            }

            if (ordem) {
                ordemCerta.add(record);
            }
        }

        var sumMiddleValues = 0;
        for (var registroCerto : ordemCerta) {
            System.out.println(registroCerto);

            var middle = (int) Math.floor((double) registroCerto.size() / 2);
            sumMiddleValues = sumMiddleValues + registroCerto.get(middle);
        }

        System.out.println(sumMiddleValues);

    }

    private static boolean checkOtherValues(List<Integer> numbersThatShouldBeDepois, Integer i, List<Integer> record) {
        for (int j = 0; j < i; j++) {
            if (numbersThatShouldBeDepois.contains(record.get(j))) {
                return false;
            }
        }
        return true;
    }

}
