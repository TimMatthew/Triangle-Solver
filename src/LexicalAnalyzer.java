import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    public enum Token {
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
    private static final Pattern CIRCLE_PATTERN = Pattern.compile("[кК]ол[оуіа]+");
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
}
