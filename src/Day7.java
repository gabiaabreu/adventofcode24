import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Day7 {

    public static void main(String[] args) throws Exception {
        File file = new File("./src/resources/day7_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        Map<Long, List<Long>> equations = new HashMap<>();

        String line;
        while ((line = reader.readLine()) != null) {
            var lineValues = Arrays.stream(line.split(": ")).toList();

            var key = Long.parseLong(lineValues.getFirst());
            var numbers = Arrays.stream(lineValues.get(1).split(" ")).map(Long::parseLong).toList();

            equations.put(key, numbers);
        }

        long result = 0;
        for (long equationResult : equations.keySet()) {
            List<Long> numbers = equations.get(equationResult);
            long first = numbers.getFirst();

            long allPossibilities = tryAllPossibilities(equationResult, first, numbers, 1);

            if (allPossibilities == equationResult) {
                result = result + allPossibilities;
            }
        }

        System.out.println("Part : " + result);
    }

    private static long tryAllPossibilities(
            long target, // resultado desejado
            long result, // resultado parcial
            List<Long> numbers, // lista de numeros
            int index // indice na lista de numeros
    ) {
        // CASO BASE (condicao de parada)
        // evita loop infinito
        if (index == numbers.size()) { // quer dizer que processou todos os numeros
            return result;
        }

        long nextNumber = numbers.get(index);

        // CASO RECURSIVO
        // chama a funcao dentro dela mesma
        // deve convergir ao caso base para chegar ao fim
        long sum = tryAllPossibilities(target, result + nextNumber, numbers, index + 1);
        long mul = tryAllPossibilities(target, result * nextNumber, numbers, index + 1);

        // part two
        long conc;
        conc = tryAllPossibilities(target, Long.parseLong(result + "" + nextNumber), numbers, index + 1);

        // se a soma/multiplicacao/concatenacao chegar no valor esperado, retorna o resultado
        if (sum == target || mul == target || conc == target) {
            return target;
        } else {
            return -1; // se nao, retorna -1
        }
    }
}
