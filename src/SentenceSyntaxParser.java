import java.util.List;




/*
 Текст розподілений на речення.
 Слова в реченнях були замінені на відповідні токени.
 Тож граматика побудована для речення

    S -> Sentence
    Sentence -> MarkPoint | DrawCircle | DrawDiameter | DrawRadius | DrawChord | '.' | #
    MarkPoint -> 'POINT' ID Num Num
    DrawCircle -> 'CIRCLE' Num 'POINT' ID (Num Num)
    DrawDiameter -> 'DIAMETER' 'POINT' ID 'POINT' ID
    DrawRadius -> 'RADIUS' 'POINT' ID 'POINT' ID
    DrawChord -> 'CHORD' 'POINT' ID 'POINT' ID
    ID -> [A-Z]
    Num -> [0-9]+
*/


class SyntaxParser {
    private List<String> sentenceTokens;
    private int pos = 0;

//    public CommandNode parse(List<String> sentence){
//
//        String first = sentence.getFirst();
//
//        switch (first){
//
//            case 'POINT':
//                expect()
//        }
//    }

//    public void expect(String word)




























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

