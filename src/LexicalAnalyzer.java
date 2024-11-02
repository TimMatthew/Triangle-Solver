import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    enum Token {
        CIRCLE,
        POINT,
        RADIUS,
        DIAMETER,
        CHORD,
        ID,
        INTEGER,
        EXTRA
    }


    private static final Pattern POINT_PATTERN = Pattern.compile("[тТ]очк[аиу]+");
    private static final Pattern CIRCLE_PATTERN = Pattern.compile("[кК]ол[оіа]+");
    private static final Pattern DIAMETER_PATTERN = Pattern.compile("[дД]іаметр[аи]*");
    private static final Pattern RADIUS_PATTERN = Pattern.compile("[рР]адіус[аи]*");
    private static final Pattern CHORD_PATTERN = Pattern.compile("[хХ]орд[аиу]");
    private static final Pattern COORDINATE_PATTERN = Pattern.compile("-*\\d+");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[A-Z]{1,2}");

    public List<List<Pair>> analyze(String text) {
        String[] sentences = text.split("\\.");
        List<List<Pair>> tree = new ArrayList<>();

        for (String sentence : sentences) {
            List<Pair> tokens = tokenize(sentence.trim());
            tokens = dropExtraTokens(tokens);

            if (tokens.size() > 5 && tokens.get(0).getToken().toString().matches("CHORD|RADIUS|DIAMETER")) {
                tree.addAll(splitTokenGroups(tokens));
            } else {
                tree.add(tokens);
            }
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
            else if (DIAMETER_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.DIAMETER, word));
            }
            else if (RADIUS_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.RADIUS, word));
            }
            else if (CHORD_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.CHORD, word));
            }
            else if (COORDINATE_PATTERN.matcher(word).matches()) {
                tokens.add(new Pair(Token.INTEGER, word));
            }
            else if (IDENTIFIER_PATTERN.matcher(word).matches()) {

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
            if (token.getToken() == Token.CHORD || token.getToken() == Token.DIAMETER || token.getToken() == Token.RADIUS) {

                if (currentGroup.size() == 5) result.add(new ArrayList<>(currentGroup));

                if(!currentGroup.isEmpty() && !currentGroup.getFirst().equals(token)) currentGroup.clear();
                currentGroup.add(token);
                curShapeToken = token;
            }
            else if (token.getToken() == Token.POINT && curShapeToken != null && i + 1 < tokens.size()) {

                currentGroup.add(token);
                currentGroup.add(tokens.get(i + 1));
                i++;

                if (i + 1 < tokens.size() && tokens.get(i + 1).getToken() == Token.POINT && i + 2 < tokens.size() && tokens.get(i + 2).getToken() == Token.ID) {
                    currentGroup.add(tokens.get(i + 1));
                    currentGroup.add(tokens.get(i + 2));
                    i += 2;

                    if (currentGroup.size() == 5) {
                        result.add(new ArrayList<>(currentGroup));
                        currentGroup.clear();
                        currentGroup.add(curShapeToken);
                    }
                }
            }
            else currentGroup.add(token);

        }
        return result;
    }



}
