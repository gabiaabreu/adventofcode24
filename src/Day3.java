import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

    public static void main(String[] args) throws Exception {

        // read entire file
        String input = Files.readString(Path.of("./src/resources/day3_input.txt"));
        input = input.replaceAll("\\r?\\n", ""); // remove line breaks -> game changer

        // part two
        var regexDoDont = "do\\(\\)(.*?)don't\\(\\)";
        List<String> doSections = new ArrayList<>();

        var parts = input.split("don't\\(\\)", 2);
        var firstPartTilDont = parts[0];
        var rest = parts[1];

        Pattern pattern = Pattern.compile(regexDoDont);
        Matcher matcher = pattern.matcher(rest);

        while (matcher.find()) {
            String match = matcher.group();

            doSections.add(match);
        }

        var textToConsider = firstPartTilDont + doSections;

        // part one
        var mulRegex = "mul\\((\\d{1,3}),(\\d{1,3})\\)";
        Pattern mulPattern = Pattern.compile(mulRegex);
        Matcher mulMatcher = mulPattern.matcher(textToConsider);

        var result = 0;
        List<String> matchesFound = new ArrayList<>();

        while (mulMatcher.find()) {
            String match = mulMatcher.group(); // mul(x,y)
            matchesFound.add(match);

            var x = Integer.parseInt(mulMatcher.group(1));
            var y = Integer.parseInt(mulMatcher.group(2));

            var multiplication = x * y;
            result = result + multiplication;
        }

        for (var found : matchesFound) {
            System.out.println("Found " + found);
        }

        System.out.println(result);
    }
}
