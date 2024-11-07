import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    enum Token {
        CIRCLE,
        POINT,
        RADIUS,
        CHORD,
        DIAMETER,
        SEGMENT,
        ID,
        INT,
        EXTRA
    }


    private static final Pattern POINT_PATTERN = Pattern.compile("[тТ]очк[аиу]+");
    private static final Pattern CIRCLE_PATTERN = Pattern.compile("[кК]ол[оіа]+");
    private static final Pattern SEGMENT_PATTERN = Pattern.compile("[Вв]ідріз[оки]*");
    private static final Pattern RADIUS_PATTERN = Pattern.compile("[рР]адіус[аи]*");
    private static final Pattern DIAMETER_PATTERN = Pattern.compile("[дД]іаметр[иау]*");
    private static final Pattern CHORD_PATTERN = Pattern.compile("[хХ]орд[аиу]");
    private static final Pattern INT_PATTERN = Pattern.compile("-*\\d+");
    private static final Pattern ID_PATTERN = Pattern.compile("[A-Z]{1,2}");

    public List<List<Pair>> analyze(String text) {
        String[] sentences = text.split("\\.");
        List<List<Pair>> tree = new ArrayList<>();

        for (String sentence : sentences) {
            List<Pair> tokens = tokenize(sentence.trim());
            tokens = dropExtraTokens(tokens);

            if (tokens.size() > 8 && tokens.getFirst().getToken().toString().matches("CHORD|RADIUS|SEGMENT|DIAMETER"))
                tree.addAll(splitTokenGroups(tokens)); else
             tree.add(tokens);
        }
        return tree;
    }

    private List<Pair> tokenize(String sentence) {
        List<Pair> tokens = new ArrayList<>();
        String[] words = sentence.split("\\s+|(?=[();,])|(?<=[();,])");

        for (String word : words) {

            if (POINT_PATTERN.matcher(word).matches() || (word.matches("центр[омі]+") && tokens.stream().anyMatch(t -> t.getToken() == Token.CIRCLE))) {
                tokens.add(new Pair(Token.POINT, word));
            }
            else if (CIRCLE_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.CIRCLE, word));
            }
            else if (RADIUS_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.RADIUS, word));
            }
            else if (DIAMETER_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.DIAMETER, word));
            }
            else if (CHORD_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.CHORD, word));
            }
            else if (SEGMENT_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.SEGMENT, word));
            }
            else if (INT_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.INT, word));
            }
            else if (ID_PATTERN.matcher(word).matches()) {

                if (word.length() == 2) {
                    tokens.add(new Pair(Token.POINT, "точка"));
                    tokens.add(new Pair(Token.ID, String.valueOf(word.charAt(0))));
                    tokens.add(new Pair(Token.POINT, "точка"));
                    tokens.add(new Pair(Token.ID, String.valueOf(word.charAt(1))));
                }
                else tokens.add(new Pair(Token.ID, word));

            }
            else tokens.add(new Pair(Token.EXTRA, word));
        }
        return tokens;
    }

    private List<Pair> dropExtraTokens(List<Pair> tokens) {
        List<Pair> filteredTokens = new ArrayList<>();

        for (Pair token : tokens) {
            if (token.getToken() != Token.EXTRA) filteredTokens.add(token);
        }
        return filteredTokens;
    }

    private List<List<Pair>> splitTokenGroups(List<Pair> tokens) {
        List<List<Pair>> result = new ArrayList<>();
        List<Pair> currentGroup = new ArrayList<>();

        Pair curShapeToken = null;

        for (int i = 0; i < tokens.size(); i++) {
            Pair token = tokens.get(i);

            // Check if the current token is a shape token (CHORD, DIAMETER, RADIUS, SEGMENT)
            if (token.getToken() == Token.CHORD  || token.getToken() == Token.DIAMETER || token.getToken() == Token.RADIUS || token.getToken() == Token.SEGMENT) {

                // If the current group has 5 tokens, add it to the result
                if (currentGroup.size() == 5) {
                    result.add(new ArrayList<>(currentGroup));
                }

                // Clear the current group if it's not empty and the current token is different from the shape token
                if (!currentGroup.isEmpty() && !currentGroup.get(0).equals(token)) {
                    currentGroup.clear();
                }
                currentGroup.add(token);
                curShapeToken = token; // Update the current shape token

            } else if (token.getToken() == Token.POINT && curShapeToken != null && i + 1 < tokens.size()) {
                // Add the current point and the next ID token
                currentGroup.add(token);
                currentGroup.add(tokens.get(i + 1));
                i++; // Skip to the next token

                // Check for another point and an ID token
                if (i + 1 < tokens.size() && tokens.get(i + 1).getToken() == Token.POINT &&
                        i + 2 < tokens.size() && tokens.get(i + 2).getToken() == Token.ID) {

                    // Add the next point and ID to the current group
                    currentGroup.add(tokens.get(i + 1));
                    currentGroup.add(tokens.get(i + 2));
                    i += 2; // Move index forward

                    // If the current group has 5 tokens, add it to the result and reset the group
                    if (currentGroup.size() == 5) {
                        result.add(new ArrayList<>(currentGroup));
                        currentGroup.clear();
                        currentGroup.add(curShapeToken); // Start a new group with the shape token
                    }
                }
            } else {
                // Add other tokens directly to the current group
                currentGroup.add(token);
            }
        }

        // In case there's any remaining group that hasn't been added
        if (currentGroup.size() == 5) {
            result.add(new ArrayList<>(currentGroup));
        }

        return result;
    }
}
