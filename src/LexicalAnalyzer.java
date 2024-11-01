import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {

    public List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isWhitespace(c)) {
                if (!token.isEmpty()) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                continue;
            }

            if (c == '(' || c == ')' || c == ';' || c == ',') {
                // Add the current token before the symbol
                if (!token.isEmpty()) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                // Add the symbol itself as a separate token
                tokens.add(String.valueOf(c));
                continue;
            }

            if (Character.isDigit(c)) {
                token.append(c);
                // Handle multi-digit numbers by checking the next character
                while (i + 1 < input.length() && Character.isDigit(input.charAt(i + 1))) {
                    i++;
                    token.append(input.charAt(i));
                }
                tokens.add(token.toString());
                token.setLength(0);
                continue;
            }

            if (isUppercaseLetter(c)) {
                token.append(c);
                if (i + 1 < input.length() && isUppercaseLetter(input.charAt(i + 1))) {
                    token.append(input.charAt(i + 1));
                    i++;
                }
                tokens.add(token.toString());
                token.setLength(0);
                continue;
            }

            token.append(c);
        }

        // Add the final token if there's any remaining text
        if (!token.isEmpty()) {
            tokens.add(token.toString());
        }

        return tokens;
    }

    public List<List<String>> splitIntoSentences(String input) {
        List<String> words = tokenize(input);
        List<List<String>> sentences = new ArrayList<>();
        List<String> sentence = new ArrayList<>();

        for (String word : words) {
            if (word.equals(".")) {
                if (!sentence.isEmpty()) {
                    sentences.add(new ArrayList<>(sentence));
                    sentence.clear();
                }
            } else {
                sentence.add(word);
            }
        }

        if (!sentence.isEmpty()) {
            sentences.add(sentence);
        }

        return sentences;
    }

    private boolean isUppercaseLetter(char c) {
        return (c >= 'A' && c <= 'Z');
    }
}
