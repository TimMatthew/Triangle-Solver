import java.util.*;

class SyntaxAnalyzer {
    private List<String> tokens;
    private int currentTokenIndex;
    private String currentToken;

    public SyntaxAnalyzer(String input, List<String> t) {
        this.tokens = t;
        this.currentTokenIndex = 0;
        this.currentToken = tokens.get(0);
    }

    // Tokenizer function with dot separated
    private List<String> tokenize(String input) {
        // Tokenize based on spaces, punctuation, and numbers
        // Ensure dot is separated even if attached to another token
        input = input.replaceAll("([(),;\\.])", " $1 ");
        return Arrays.asList(input.trim().split("\\s+"));
    }

    // Utility function to move to the next token
    private void nextToken() {
        currentTokenIndex++;
        if (currentTokenIndex < tokens.size()) {
            currentToken = tokens.get(currentTokenIndex);
        } else {
            currentToken = null;
        }
    }

    // Expect a specific token and advance
    private void expect(String expected) {
        if (!currentToken.equals(expected)) {
            throw new RuntimeException("Expected " + expected + ", but got: " + currentToken);
        }
        nextToken();
    }

    // Entry point for parsing
    public void parse() {
        while (currentToken != null) {
            parseTask();
            expect("."); // End of a sentence
        }
    }

    // Parse a single task (sentence)
    private void parseTask() {
        if (currentToken.equals("Позначте") || currentToken.equals("Позначити") || currentToken.equals("Познач")) {
            parseMarkPoint();
        } else if (currentToken.equals("Побудуйте") || currentToken.equals("Побудуй")) {
            parseDrawCircleOrRadiusOrLine();
        } else if (currentToken.equals("Провести")) {
            parseDrawDiameter();
        } else if (currentToken.equals("Проведіть") || currentToken.equals("Проведи")) {
            parseDrawSegmentOrChord();
        } else {
            throw new RuntimeException("Unexpected token: " + currentToken);
        }
    }

    // Parse the "MarkPoint" rule
    private void parseMarkPoint() {
        expect("Позначте");
        expect("точку");
        parsePointID();
        expect("з");
        expect("координатами");
        parseCoordinates();
    }

    // Parse the "DrawCircle" rule
    private void parseDrawCircleOrRadiusOrLine() {
        nextToken(); // Skip "Побудуйте" or "Побудуй"
        if (currentToken.equals("коло")) {
            parseDrawCircle();
        } else if (currentToken.equals("радіус")) {
            parseDrawRadius();
        } else if (currentToken.equals("пряму")) {
            parseDrawLine();
        } else {
            throw new RuntimeException("Unexpected token: " + currentToken);
        }
    }

    private void parseDrawCircle() {
        expect("коло");
        expect("з");
        expect("радіусом");
        parseNumber();
        expect("із");
        expect("центром");
        parsePointID();
    }

    private void parseDrawDiameter() {
        expect("Провести");
        expect("діаметр");
        parseTwoLetterID();
        expect("від");
        expect("точки");
        parsePointID();
    }

    private void parseDrawSegmentOrChord() {
        nextToken(); // Skip "Проведіть" or "Проведи"
        if (currentToken.equals("відрізок")) {
            parseDrawSegment();
        } else if (currentToken.equals("хорди")) {
            parseDrawChord();
        } else {
            throw new RuntimeException("Unexpected token: " + currentToken);
        }
    }

    private void parseDrawSegment() {
        expect("відрізок");
        parseTwoLetterList();
    }

    private void parseDrawChord() {
        expect("хорди");
        parseTwoLetterList();
    }

    private void parseDrawRadius() {
        expect("радіус");
        parseTwoLetterID();
    }

    private void parseDrawLine() {
        expect("пряму");
        parseTwoLetterID();
        expect(",");
        expect("дотичну");
        expect("до");
        expect("точки");
        parsePointID();
    }

    // Parse coordinates like (3;5)
    private void parseCoordinates() {
        expect("(");
        parseNumber();
        expect(";");
        parseNumber();
        expect(")");
    }

    // Parse a single point identifier (one uppercase letter)
    private void parsePointID() {
        if (!currentToken.matches("[A-Z]")) {
            throw new RuntimeException("Expected a point identifier (one uppercase letter), got: " + currentToken);
        }
        nextToken();
    }

    // Parse a two-letter identifier (e.g., PA)
    private void parseTwoLetterID() {
        if (!currentToken.matches("[A-Z][A-Z]")) {
            throw new RuntimeException("Expected a two-letter identifier, got: " + currentToken);
        }
        nextToken();
    }

    // Parse a list of two-letter identifiers (e.g., UP, UA)
    private void parseTwoLetterList() {
        parseTwoLetterID();
        while (currentToken.equals(",")) {
            nextToken();
            parseTwoLetterID();
        }
    }

    // Parse a number (integer)
    private void parseNumber() {
        if (!currentToken.matches("-?[0-9]+")) {
            throw new RuntimeException("Expected a number, got: " + currentToken);
        }
        nextToken();
    }

    // Helper to print the list of tokens (for debugging)
    public void printTokens() {
        System.out.println("Tokens: " + tokens);
    }
}
