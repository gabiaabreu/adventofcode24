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

            var regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            var result = 0;
            List<String> matchesFound = new ArrayList<>();

            while (matcher.find()) {
                String match = matcher.group(); // mul(x,y)
                matchesFound.add(match);

                var x = Integer.parseInt(matcher.group(1));
                var y = Integer.parseInt(matcher.group(2));

                var multiplication = x * y;
                result = result + multiplication;
            }

            for (var found : matchesFound) {
                System.out.println("Found " + found);
            }

            System.out.println(result);
        }
}
