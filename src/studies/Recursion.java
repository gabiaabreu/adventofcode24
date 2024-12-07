package studies;

import java.util.ArrayList;
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
}
