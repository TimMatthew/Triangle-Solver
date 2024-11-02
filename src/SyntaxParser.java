import java.util.List;
/*
 Текст розподілений на речення.
 Слова в реченнях були замінені на відповідні пари (токен, з-ння).
 Тож граматика побудована для речення

    S -> Sentence
    Sentence -> MarkPoint | DrawCircle | DrawDiameter | DrawRadius | DrawChord | '.' | #
    MarkPoint -> 'POINT' ID INTEGER INTEGER
    DrawCircle -> 'CIRCLE' INTEGER 'POINT' ID (INTEGER INTEGER)
    DrawRadius -> 'RADIUS' 'POINT' ID 'POINT' ID
    DrawChord -> 'CHORD' 'POINT' ID 'POINT' ID
    DrawSegment -> 'SEGMENT' 'POINT' ID 'POINT' ID
    ID -> [A-Z]
    INTEGER -> [0-9]+
*/


class SyntaxParser {
    private List<List<Pair>> sentences;


    public SyntaxParser(List<List<Pair>> sentences) {
        this.sentences = sentences;
    }

    public void parse(List<CommandNode> syntaxTree, List<CommandNode> idTable) {
        for (List<Pair> sentence : sentences) {
            Pair curPair = sentence.getFirst();
            int pos = 0;

            switch (curPair.getToken()) {
                case POINT -> {
                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String id = curPair.getValue();

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.INTEGER, curPair.getToken());
                    int x = Integer.parseInt(curPair.getValue());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.INTEGER, curPair.getToken());
                    int y = Integer.parseInt(curPair.getValue());

                    MarkPointNode markPointNode = new MarkPointNode(id, x, y);
                    idTable.add(markPointNode);
                    syntaxTree.add(markPointNode);
                }

                case CIRCLE -> {
                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.INTEGER, curPair.getToken());
                    int r = Integer.parseInt(curPair.getValue());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String center = curPair.getValue();

                    if (sentence.size() == 6) {
                        pos++;
                        curPair = sentence.get(pos);
                        expect(LexicalAnalyzer.Token.INTEGER, curPair.getToken());
                        int x = Integer.parseInt(curPair.getValue());

                        pos++;
                        curPair = sentence.get(pos);
                        expect(LexicalAnalyzer.Token.INTEGER, curPair.getToken());
                        int y = Integer.parseInt(curPair.getValue());

                        syntaxTree.add(new MarkPointNode(center, x, y));
                        DrawCircleNode circleNode = new DrawCircleNode(r, center, x, y);
                        circleNode.setCenterNode(new MarkPointNode(center, x, y));
                        syntaxTree.add(circleNode);
                        idTable.add(circleNode);
                    } else {
                        syntaxTree.add(new DrawCircleNode(r, center));
                        idTable.add(new DrawCircleNode(r, center));
                    }
                }

                case RADIUS -> {
                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String idStart = curPair.getValue();

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String idEnd = curPair.getValue();

                    String radId = idStart + idEnd;

                    idTable.add(new DrawRadiusNode(radId, idStart, idEnd));
                    syntaxTree.add(new DrawRadiusNode(radId, idStart, idEnd));
                }

                case CHORD -> {
                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String idStart = curPair.getValue();

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String idEnd = curPair.getValue();

                    String chordId = idStart + idEnd;

                    idTable.add(new DrawChordNode(chordId, idStart, idEnd));
                    syntaxTree.add(new DrawChordNode(chordId, idStart, idEnd));
                }

                case SEGMENT -> {
                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String idStart = curPair.getValue();

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String idEnd = curPair.getValue();

                    String segmentId = idStart + idEnd;

                    idTable.add(new DrawSegmentNode(segmentId, idStart, idEnd));
                    syntaxTree.add(new DrawSegmentNode(segmentId, idStart, idEnd));
                }
            }
        }
    }



    public void expect(LexicalAnalyzer.Token expected, LexicalAnalyzer.Token retrieved){

        if(retrieved != expected){
            throw new RuntimeException("Unexpected token! Expected: " + expected + "Got: " + retrieved);
        }
    }




























//    public ASTNode parseSentence(List<String> tokens) {
//        this.tokens = tokens;
//        this.position = 0;
//
//        if (matchRegex("Познач[те|ити]*", "точку")) {
//            return parseMarkPoint();
//        }
//
//        else if (matchRegex("Побуду[йте|й|вати]+", "коло")) {
//            return parseDrawCircle();
//        }
//        else if (position < tokens.size() && tokens.get(position).matches("Прове[ди|діть|сти]+")) {
//            position++; // Move past "Проведи"
//
//
//            if (position < tokens.size()) {
//                String token = tokens.get(position++);
//
//                if(token.matches("діаметр[и]*")){
//                    return parseDrawDiameter();
//                }
//                else if (token.matches("хорд[и]*")){
//                    return parseDrawChord();
//                }
//                else if (token.matches("радіус[и]*")){
//                    return parseDrawRadius();
//                }
//            }
//            else System.out.println("Unrecognized sentence structure: " + tokens);
//        }
//        else System.out.println("Unrecognized sentence structure: " + tokens);
//
//        return null;
    }

//    private boolean matchRegex(String regex, String nextWord) {
//        if (position < tokens.size() && tokens.get(position).matches(regex)) {
//            position++;
//            if (position < tokens.size() && tokens.get(position).equals(nextWord)) {
//                position++;
//                return true;
//            }
//            position--;
//        }
//        return false;
//    }
//
//    private MarkPointNode parseMarkPoint() {
//        String pointID = tokens.get(position++);
//        expect("з");
//        expect("координатами");
//        expect("(");
//        int x = parseNumber();
//        expect(";");
//        int y = parseNumber();
//        expect(")");
//        return new MarkPointNode(pointID, x, y);
//    }
//
//    private DrawCircleNode parseDrawCircle() {
//        expect("з");
//        expect("радіусом");
//        int radius = parseNumber();
//        expect("із");
//        expect("центром");
//        String center = tokens.get(position++);
//        return new DrawCircleNode(center, radius);
//    }
//
//    private DrawRadiusNode parseDrawRadius() {
//        String radiusID = tokens.get(position++);
//        return new DrawRadiusNode(radiusID);
//    }
//
//    private DrawDiameterNode parseDrawDiameter() {
//        String diameterID = tokens.get(position++);
//        return new DrawDiameterNode(diameterID);
//    }
//
//    private DrawLineNode parseDrawLine() {
//        String lineID = tokens.get(position++);
//        expect(",");
//        expect("дотичну");
//        expect("до");
//        expect("точки");
//        String pointID = tokens.get(position++);
//        return new DrawLineNode(lineID, pointID);
//    }
//
//    private DrawSegmentNode parseDrawSegment() {
//        List<String> segmentPoints = new ArrayList<>();
//        while (position < tokens.size()) {
//            segmentPoints.add(tokens.get(position++));
//            if (position < tokens.size() && tokens.get(position).equals(",")) {
//                position++; // Skip the comma
//            } else {
//                break;
//            }
//        }
//        return new DrawSegmentNode(segmentPoints);
//    }
//
//    private DrawChordNode parseDrawChord() {
//        List<String> chordPoints = new ArrayList<>();
//        while (position < tokens.size()) {
//            chordPoints.add(tokens.get(position++));
//            if (position < tokens.size() && tokens.get(position).equals(",")) {
//                position++; // Skip the comma
//            } else {
//                break;
//            }
//        }
//        return new DrawChordNode(chordPoints);
//    }
//
//    private void expect(String word) {
//        if (position >= tokens.size() || !tokens.get(position++).equals(word)) {
//            throw new RuntimeException("Expected " + word + " but found " + (position - 1 < tokens.size() ? tokens.get(position - 1) : "end of input"));
//        }
//    }
//
//    private int parseNumber() {
//        char[] xyPair = tokens.get(position++).toCharArray();
//        StringBuilder num = new StringBuilder();
//        for (char c : xyPair) {
//            if (Character.isDigit(c) || c == '-') {
//                num.append(c);
//            }
//        }
//        return Integer.parseInt(num.toString());
//    }

