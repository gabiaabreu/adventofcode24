package studies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Recursion {

    public static void main(String[] args) throws Exception {

        // soma de 1 ate N
        System.out.println(sumOneToN(5));

        // palindromo
        var word = "radar";

        List<Character> charList = new ArrayList<>();
        for (char c : word.toCharArray()) {
            charList.add(c);
        }

        System.out.println(isPalindrome(charList));

        // maior numero
        List<Integer> numberList = new ArrayList<>();
        numberList.add(52);
        numberList.add(33);
        numberList.add(78);
        numberList.add(92);
        numberList.add(10);

        System.out.println(biggestNumber(numberList));

        // soma de uma lista de numeros
        List<Integer> toSum = new ArrayList<>();
        toSum.add(52);
        toSum.add(33);
        toSum.add(78);
        toSum.add(10);
        System.out.println(sumNumbers(toSum, 0));

        // conta elementos
        List<String> toCount = new ArrayList<>();
        toCount.add("1");
        toCount.add("2");
        toCount.add("3");
        toCount.add("4");

        System.out.println(countItems(toCount, 0));

        // busca binaria - array
        int[] numArray = {0, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 16}; // ordered array
        System.out.println("Number 13 found at index: " + binarySearch(numArray, 13, 0, numArray.length));

        List<Integer> integerList = Arrays.stream(numArray)
                .boxed()
                .toList();

        // busca binaria - list
        System.out.println("Number 13 found at index: " + binarySearchSublist(integerList, 13, 0));
    }

    private static int biggestNumber(List<Integer> numbers) {
        // caso base - quando so sobrar 1 numero, eh ele
        if (numbers.size() == 1) {
            return numbers.getFirst();
        }

        // caso recursivo - compara 2 primeiros numeros e remove o menor, depois compara novamente
        if (numbers.getFirst() < numbers.get(1)) {
            numbers.removeFirst();
        } else {
            numbers.remove(1);
        }

        return biggestNumber(numbers);
    }


    private static boolean isPalindrome(List<Character> letters) {
        // caso base - se as outras letras forem todas iguais, pode ignorar a do meio
        if (letters.size() <= 1) {
            return true;
        }

        // caso recursivo
        if (Objects.equals(letters.getFirst(), letters.getLast())) {
            letters.removeFirst();
            letters.removeLast();

            return isPalindrome(letters);
        }
        return false;
    }

    private static int sumOneToN(int n) {
        // caso base - se chegou na soma de 0 retorna 0
        if (n == 0) {
            return n;
        }

        // caso recursivo - soma de n + (n-1)
        return n + sumOneToN(n - 1);
    }

    // livro aprendendo algoritmos - seçao 4
    private static int sumNumbers(List<Integer> numbers, int sum) {
        // caso base
        if (numbers.isEmpty()) {
            return sum;
        }

        // caso recursivo
        sum = sum + numbers.getFirst();
        numbers.removeFirst();
        return sumNumbers(numbers, sum);
    }

    private static int countItems(List<String> items, int count) {
        if (items.isEmpty()) {
            return count;
        }

        count++;
        items.removeFirst();
        return countItems(items, count);
    }

    private static int binarySearch(int[] array, int target, int beginning, int end) {
        if (beginning > end) {
            return -1;
        }

        int middle = beginning + (end - beginning) / 2;
        int currentNumber = array[middle];

        if (currentNumber == target) {
            return middle;
        } else if (currentNumber > target) { // chutei alto
            return binarySearch(array, target, beginning, middle - 1);
        } else { // chutei baixo
            return binarySearch(array, target, middle + 1, end);
        }
    }

    public static int binarySearchSublist(List<Integer> list, int target, int offset) {
        if (list.isEmpty()) {
            return -1;
        }
        if (list.size() == 1) {
            if (list.getFirst() == target) {
                return offset;
            } else {
                return -1;
            }
        }

        int middle = list.size() / 2;
        int currentNumber = list.get(middle);

        if (currentNumber == target) {
            return offset + middle;
        } else if (currentNumber > target) {
            return binarySearchSublist(list.subList(0, middle), target, offset);
        } else {
            return binarySearchSublist(list.subList(middle + 1, list.size()), target, offset + middle + 1);
        }
    }
}
