import java.util.List;

class SyntaxParser {

/*
    S -> Sentence
    Sentence -> MarkPoint | DrawCircle | DrawRadius | DrawChord | . | #
    MarkPoint -> POINT ID INT INT
    DrawCircle -> CIRCLE INT (POINT ID | MarkPoint)
    DrawRadius -> RADIUS POINT ID POINT ID CIRCLE POINT ID
    DrawDiameter -> DIAMETER POINT ID POINT ID CIRCLE POINT ID
    DrawChord -> CHORD POINT ID POINT ID
    DrawSegment -> SEGMENT POINT ID POINT ID
    ID -> [A-Z]
    INT -> [0-9]+
*/

    private final List<List<Pair>> sentences;

    public SyntaxParser(List<List<Pair>> sentences) {
        this.sentences = sentences;
    }


    public void parse(List<CommandNode> syntaxTree, List<CommandNode> idTable) {
        sortTokenLists(sentences);

        for (List<Pair> sentence : sentences) {
            System.out.println(sentence);
        }



        for (List<Pair> sentence : sentences) {
            Pair curPair = sentence.get(0);
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

                    PointNode pointNode = new PointNode(id, x, y);
                    idTable.add(pointNode);
                    syntaxTree.add(pointNode);
                }

                case CIRCLE -> {
                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.INTEGER, curPair.getToken());
                    int radius = Integer.parseInt(curPair.getValue());

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

                        PointNode centerNode = new PointNode(center, x, y);
                        syntaxTree.add(centerNode);
                        CircleNode circleNode = new CircleNode(radius, center);
                        circleNode.setCenterNode(centerNode);
                        syntaxTree.add(circleNode);
                        idTable.add(circleNode);
                    }
                    else {
                        CircleNode circleNode = new CircleNode(radius, center);
                        syntaxTree.add(circleNode);
                        idTable.add(circleNode);
                    }
                }

                case RADIUS, DIAMETER, CHORD, SEGMENT -> {
                    boolean isRadius = curPair.getToken() == LexicalAnalyzer.Token.RADIUS;
                    boolean isDiameter = curPair.getToken() == LexicalAnalyzer.Token.DIAMETER;
                    boolean isChord = curPair.getToken() == LexicalAnalyzer.Token.CHORD;

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

                    if (isRadius || isDiameter || isChord) {
                        pos++;
                        curPair = sentence.get(pos);
                        expect(LexicalAnalyzer.Token.CIRCLE, curPair.getToken());

                        pos++;
                        curPair = sentence.get(pos);
                        expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                        pos++;
                        curPair = sentence.get(pos);
                        expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                        String circleId = curPair.getValue();
//
//                        if (isRadius) {
//                            DrawRadiusNode radiusNode = new DrawRadiusNode(idStart + idEnd, idStart, idEnd, circleId);
//                            syntaxTree.add(radiusNode);
//                            idTable.add(radiusNode);
//                        } else {
//                            DrawDiameterNode diameterNode = new DrawDiameterNode(idStart + idEnd, idStart, idEnd, circleId);
//                            syntaxTree.add(diameterNode);
//                            idTable.add(diameterNode);
//                        }
                    }
//                    else if (isChord) {
//                        DrawChordNode chordNode = new DrawChordNode(idStart + idEnd, idStart, idEnd);
//                        syntaxTree.add(chordNode);
//                        idTable.add(chordNode);
//                    }
//                    else {
//                        DrawSegmentNode segmentNode = new DrawSegmentNode(idStart + idEnd, idStart, idEnd);
//                        syntaxTree.add(segmentNode);
//                        idTable.add(segmentNode);
//                    }
                }
            }
        }
    }

    public void expect(LexicalAnalyzer.Token expected, LexicalAnalyzer.Token retrieved) {
        if (retrieved != expected) {
            throw new RuntimeException("Unexpected token! Expected: " + expected + " Got: " + retrieved);
        }
    }

    public static void sortTokenLists(List<List<Pair>> tokenLists) {

        tokenLists.sort((list1, list2) -> {
            int priority1 = getPriority(list1);
            int priority2 = getPriority(list2);
            return Integer.compare(priority1, priority2);
        });
    }


    public static int getPriority(List<Pair> tokenList) {

        LexicalAnalyzer.Token firstToken = tokenList.getFirst().getToken();

        if (firstToken == LexicalAnalyzer.Token.POINT) {
            return 1;
        }
        else if (firstToken == LexicalAnalyzer.Token.CIRCLE) {
            return 2;
        }
        else if (firstToken == LexicalAnalyzer.Token.RADIUS ||
                firstToken == LexicalAnalyzer.Token.DIAMETER ||
                firstToken == LexicalAnalyzer.Token.CHORD ||
                firstToken == LexicalAnalyzer.Token.SEGMENT) {
            return 3;
        }
        return -1;
    }

}
