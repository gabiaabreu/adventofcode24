import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day9 {

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("./src/resources/day9_input.txt"));

        List<Integer> values = Arrays.stream(input.split("")).map(Integer::parseInt).toList();

        List<String> diskMap = new ArrayList<>();
        List<Value> valuesAddedMap = new ArrayList<>();
        List<Dot> dotMap = new ArrayList<>();

        var dotCount = 0;
        var indexCount = 0;
        for (int i = 0; i < values.size(); i++) {
            long timesToAdd = values.get(i);

            if (isEven(i) && timesToAdd > 0) {
                valuesAddedMap.add(new Value(String.valueOf(i / 2), indexCount, timesToAdd));
            } else if (!isEven(i) && timesToAdd > 0) {
                dotMap.add(new Dot(indexCount, timesToAdd));
            }

            for (int j = 0; j < timesToAdd; j++) {
                indexCount++;
                if (isEven(i)) {
                    diskMap.add(String.valueOf(i / 2)); // id
                } else {
                    diskMap.add(".");
                    dotCount++;
                }
            }
        }

        printDiskMap(diskMap);

        dotMap.forEach(System.out::println);
        valuesAddedMap.forEach(System.out::println);

        // part one
//        List<String> newMap = new ArrayList<>(diskMap);
//        int iterations = isEven(dotCount) ? dotCount : dotCount + 1;
//        for (int i = 0; i < iterations; i++) {
//            newMap = moveValuesPartOne(newMap);
//        }
//
//        var sum = calculateChecksum(newMap);
//        System.out.println("Checksum: " + sum);

        // part two
        diskMap = doPartTwo(diskMap, valuesAddedMap, dotMap);
        printDiskMap(diskMap);

        var sum = calculateChecksumPartTwo(diskMap);
        System.out.println("Checksum: " + sum);

    }

    private static List<String> doPartTwo(List<String> diskMap,
                                          List<Value> valuesAdded,
                                          List<Dot> dotMap) {

        for (int j = valuesAdded.size() - 1; j >= 0; j--) {

            var valueObj = valuesAdded.get(j);
            var value = valueObj.getValue(); // ultimo valor colocado
            var valueSize = valueObj.getSize(); // tamanho dele
            var valueIndex = valueObj.getIndex(); // primeiro index

            long firstDotThatFits = -1;
            long remainingDots = 0;
            for (int i = 0; i < dotMap.size(); i++) {
                if (dotMap.get(i).getSize() >= valueSize) {
                    // se o tamanho do pontinho for = ou > que o tamanho do valor, coloca la
                    firstDotThatFits = dotMap.get(i).getIndex();

                    if (dotMap.get(i).getSize() - valueSize > 0) {
                        remainingDots = dotMap.get(i).getSize() - valueSize;
                    }

                    // excluir as dots usadas, ver se sobrou alguma, adicionar e ordenar novamente
                    dotMap.remove(i);
                    dotMap.add(new Dot(firstDotThatFits + valueSize, remainingDots));
                    dotMap.sort(Comparator.comparingLong(Dot::getIndex));

                    break;
                }
            }

            if (firstDotThatFits != -1) { // se for -1 quer dizer q nao achou lugar q cabe
                for (int i = 0; i < valueSize; i++) {
                    // substituir . pelo valor
                    diskMap.set((int) firstDotThatFits, value);
                    firstDotThatFits++;

                    // substituir o valor no final por .
                    diskMap.set((int) valueIndex, ".");
                    valueIndex++;
                }
            }
        }

        return diskMap;
    }

    private static long calculateChecksumPartTwo(List<String> diskMap) {

        long sum = 0;
        for (int i = 0; i < diskMap.size(); i++) {
            if (diskMap.get(i) != ".") {
                long value = Long.parseLong(diskMap.get(i));

                long partialSum = value * i;
                sum = sum + partialSum;
            }
        }

        return sum;
        // 8509897721111 too high
    }

    private static class Dot {

        private long index;
        private long size;

        public long getSize() {
            return size;
        }

        public long getIndex() {
            return index;
        }

        public Dot(long index, long size) {
            this.index = index;
            this.size = size;
        }

        @Override
        public String toString() {
            return "Index: " + index + ", Size: " + size;
        }
    }

    private static class Value {

        private String value;
        private long index;
        private long size;

        public Value(String value, long index, long size) {
            this.value = value;
            this.index = index;
            this.size = size;
        }

        public String getValue() {
            return value;
        }

        public long getIndex() {
            return index;
        }

        public long getSize() {
            return size;
        }

        @Override
        public String toString() {
            return "Value: " + value + ", Index: " + index + ", Size: " + size;
        }
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
