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
        ACTION_POINT,
        ACTION_CIRCLE,
        ACTION_LINE,
        EXTRA,
        COMMA,
        DOT,
        TEXT_END
    }


    private static final Pattern POINT_PATTERN = Pattern.compile("[тТ]очк[аиу]+|центр[омі]+");
    private static final Pattern CIRCLE_PATTERN = Pattern.compile("[кК]ол[оуіа]+");
    private static final Pattern SEGMENT_PATTERN = Pattern.compile("[Вв]ідріз[оки]*");
    private static final Pattern RADIUS_PATTERN = Pattern.compile("[рР]адіус[аиом]*");
    private static final Pattern DIAMETER_PATTERN = Pattern.compile("[дД]іаметр[иау]*");
    private static final Pattern CHORD_PATTERN = Pattern.compile("[хХ]орд[аиу]");
    private static final Pattern INT_PATTERN = Pattern.compile("-*\\d+");
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private static final Pattern DOT_PATTER = Pattern.compile("\\.");
    private static final Pattern TEXT_END_PATTER = Pattern.compile("#");
    private static final Pattern ID_PATTERN = Pattern.compile("[A-Z](\\d+)?([A-Z](\\d+)?)?");
    private static final Pattern ACTION_POINT_PATTERN = Pattern.compile("Познач(те|ити)*");
    private static final Pattern ACTION_LINE_PATTERN = Pattern.compile("Прове(сти|діть|ди)*");
    private static final Pattern ACTION_CIRCLE_PATTERN = Pattern.compile("Побуду(вати|й|йте)*");



    public List<Pair> analyze(String text) {
        List<Pair> tree = new ArrayList<>();
        return tokenize(text, tree);
    }

    private List<Pair> tokenize(String sentence, List<Pair> tree) {
        StringBuilder word = new StringBuilder();
        char[] charArray = sentence.toCharArray();
        boolean isHashPending = false;  

        for (char c : charArray) {
            
            if (Character.isWhitespace(c) || c == '#' || c == '(' || c == ')' || c == ';' || c == ',' || c == '.' || c == ':') {
                if (!word.isEmpty()) {
                    processWord(word.toString(), tree);
                    word.setLength(0);
                }

                if (c == '#') isHashPending = true;
                else if(c == '.') processWord(String.valueOf(c), tree);
                else if(c == ',') processWord(String.valueOf(c), tree);
                else if (!Character.isWhitespace(c)) tree.add(new Pair(Token.EXTRA, String.valueOf(c)));
            }
            else word.append(c);

            if (isHashPending) {
                tree.add(new Pair(Token.TEXT_END, "#"));
                isHashPending = false;  
            }
        }

        
        if (!word.isEmpty()) processWord(word.toString(), tree);
        tree.removeIf(token -> token.getValue().isBlank());
        return tree;
    }


    private void processWord(String word, List<Pair> tree) {
        if (POINT_PATTERN.matcher(word).matches()) {
            tree.add(new Pair(Token.POINT, word));
        }
        else if (CIRCLE_PATTERN.matcher(word).matches()) {
            tree.add(new Pair(Token.CIRCLE, word));
        }
        else if (RADIUS_PATTERN.matcher(word).matches()) {
            tree.add(new Pair(Token.RADIUS, word));
        }
        else if (DIAMETER_PATTERN.matcher(word).matches()) {
            tree.add(new Pair(Token.DIAMETER, word));
        }
        else if (CHORD_PATTERN.matcher(word).matches()) {
            tree.add(new Pair(Token.CHORD, word));
        }
        else if (SEGMENT_PATTERN.matcher(word).matches()) tree.add(new Pair(Token.SEGMENT, word));

        else if (INT_PATTERN.matcher(word).matches()) tree.add(new Pair(Token.INT, word));
        else if (ID_PATTERN.matcher(word).matches())
        {

            if (word.length() > 1) {
                StringBuilder id1 = new StringBuilder(), id2 = new StringBuilder();
                boolean isId2 = false, isId1 = true;

                for (char c : word.toCharArray()) {

                    if (Character.isLetter(c)) {

                        if (!isId2 && !id1.isEmpty()) {
                            isId2 = true;
                            isId1 = false;
                            id2.append(c);
                        }
                        else id1.append(c);

                    }
                    else if (Character.isDigit(c) && isId2) id2.append(c);
                    else if (Character.isDigit(c) && isId1) id1.append(c);
                }
                tree.add(new Pair(Token.ID, id1.toString()));
                tree.add(new Pair(Token.ID, id2.toString()));
            }
            else tree.add(new Pair(Token.ID, word));
        }
        else if (ACTION_POINT_PATTERN.matcher(word).matches()) tree.add(new Pair(Token.ACTION_POINT, word));
        else if (ACTION_CIRCLE_PATTERN.matcher(word).matches()) tree.add(new Pair(Token.ACTION_CIRCLE, word));
        else if (ACTION_LINE_PATTERN.matcher(word).matches()) tree.add(new Pair(Token.ACTION_LINE, word));
        else if (COMMA_PATTERN.matcher(word).matches()) tree.add(new Pair(Token.COMMA, word));
        else if (DOT_PATTER.matcher(word).matches()) tree.add(new Pair(Token.DOT, word));
        else if (DOT_PATTER.matcher(word).matches()) tree.add(new Pair(Token.DOT, word));
        else tree.add(new Pair(Token.EXTRA, word));

    }


}
