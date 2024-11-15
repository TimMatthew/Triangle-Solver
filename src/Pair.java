public class Pair {
    private final LexicalAnalyzer.Token token;
    private final String value;

    public Pair(LexicalAnalyzer.Token token, String value) {
        this.token = token;
        this.value = value;
    }

    public LexicalAnalyzer.Token getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(" + token + ", " + value + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair Pair = (Pair) o;
        return token == Pair.token && value.equals(Pair.value);
    }



    @Override
    public int hashCode() {
        return 31 * token.hashCode() + value.hashCode();
    }
}
