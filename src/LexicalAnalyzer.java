import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LexicalAnalyzer {


    enum Tokens{
        CIRCLE,
        POINT,
        RADIUS,
        DIAMETER,
        CHORD,
    }


    // Define token patterns
    private static final Pattern POINT_PATTERN = Pattern.compile("[тТ]очк[аиу]+");
    private static final Pattern CIRCLE_PATTERN = Pattern.compile("[кК]ол[оа]+");
    private static final Pattern DIAMETER_PATTERN = Pattern.compile("[дД]іаметр[а]*");
    private static final Pattern RADIUS_PATTERN = Pattern.compile("[рР]адіус[а]*");
    private static final Pattern CHORD_PATTERN = Pattern.compile("[хХ]орд[аиу]");
    private static final Pattern COORDINATE_PATTERN = Pattern.compile("-*\\d+");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[A-Z]{1,2}");

    public List<List<String>> analyze(String text) {

        String[] sentences = text.split("\\.");

        // Step 2: Process each sentence into tokens
        List<List<String>> tree = new ArrayList<>();
        for (String sentence : sentences) {

            List<String> tokens = tokenize(sentence.trim());

            tokens = dropExtraTokens(tokens);

            List<List<String>> splitTokens;

            if(tokens.size()>5 && tokens.getFirst().matches("CHORD|RADIUS|DIAMETER")) {
                splitTokens = splitTokenGroups(tokens);  // Split token groups here
                tree.addAll(splitTokens);
            }
            else tree.add(tokens);
        }

        return tree;
    }

    private List<String> tokenize(String sentence) {
        List<String> tokens = new ArrayList<>();
        String[] words = sentence.split("\\s+|(?=[();,])|(?<=[();,])");

        for (String word : words) {
            if (POINT_PATTERN.matcher(word).matches() || (word.matches("центр[омі]+") && tokens.contains("CIRCLE"))) {
                tokens.add("POINT");
            }
            else if (CIRCLE_PATTERN.matcher(word).matches()) {
                tokens.add("CIRCLE");
            }
            else if (DIAMETER_PATTERN.matcher(word).matches()) {
                tokens.add("DIAMETER");
            }
            else if (RADIUS_PATTERN.matcher(word).matches()) {
                tokens.add("RADIUS");
            }
            else if (CHORD_PATTERN.matcher(word).matches()) {
                tokens.add("CHORD");
            }
            else if (COORDINATE_PATTERN.matcher(word).matches()) {
                tokens.add(word); // retain number as is
            }
            else if (IDENTIFIER_PATTERN.matcher(word).matches()) {

                // На випадок відрізків
                if(word.length()==2){
                    String id1 = String.valueOf(word.charAt(0)), id2 = String.valueOf(word.charAt(1));
                    tokens.add("POINT");
                    tokens.add(id1);
                    tokens.add("POINT");
                    tokens.add(id2);
                }
                else tokens.add(word); // retain identifier as is
            }
            else {
                tokens.add("EXTRA"); // mark as extra
            }
        }
        return tokens;
    }

    private List<String> dropExtraTokens(List<String> tokens) {
        List<String> filteredTokens = new ArrayList<>();
        for (String token : tokens) {
            if (!"EXTRA".equals(token)) {
                filteredTokens.add(token);
            }
        }
        return filteredTokens;
    }

    private List<List<String>> splitTokenGroups(List<String> tokens) {
        List<List<String>> result = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();

        String lastShapeToken = null;
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            // Check if the token is a shape token (CHORD, DIAMETER, RADIUS)
            if (token.equals("CHORD") || token.equals("DIAMETER") || token.equals("RADIUS")) {
                if (!currentGroup.isEmpty()) {
                    result.add(new ArrayList<>(currentGroup));
                    currentGroup.clear();
                }
                currentGroup.add(token);
                lastShapeToken = token;
            } else if (token.equals("POINT") && lastShapeToken != null && i + 1 < tokens.size() && tokens.get(i + 1).matches("[A-Z]")) {
                // Add each "POINT" followed by an identifier to the current group
                currentGroup.add("POINT");
                currentGroup.add(tokens.get(i + 1));
                i++; // Skip next identifier since it's been added already

                // Check if we have a complete shape with two points, add to result if so
                if (currentGroup.size() == 5) {
                    result.add(new ArrayList<>(currentGroup));
                    currentGroup.clear();
                    currentGroup.add(lastShapeToken);  // Start a new group with the shape token
                }
            } else {
                currentGroup.add(token);
            }
        }



        return result;
    }

}