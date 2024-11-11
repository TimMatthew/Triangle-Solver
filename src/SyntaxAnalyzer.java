import java.util.List;
class SyntaxAnalyzer {

/*
   S -> Sentence
    Sentence -> MarkPoint | DrawCircle | DrawRadius | DrawChord | . | #
    MarkPoint -> POINT ID INT INT
    DrawCircle -> CIRCLE INT (POINT ID | MarkPoint)
    DrawRadius -> RADIUS POINT ID POINT ID
    DrawDiameter -> DIAMETER POINT ID POINT ID CIRCLE POINT ID
    DrawChord -> CHORD POINT ID POINT ID CIRCLE POINT ID
    DrawSegment -> SEGMENT POINT ID POINT ID
    ID -> [A-Z]
    INT -> [0-9]+
*/

    private final List<List<Pair>> sentences;

    public SyntaxAnalyzer(List<List<Pair>> sentences) {
        this.sentences = sentences;
    }


    public void parse(List<Node> syntaxTree){
        sortTokenLists(sentences);

        for (List<Pair> sentence : sentences) {

            LexicalAnalyzer.Token firstToken = sentence.getFirst().getToken();
            int pos=0;
            Pair curPair;

            switch (firstToken){

                case POINT -> {
                    PointNode pn = parsePoint(sentence, pos);
                    syntaxTree.add(pn);
                    
                }

                case CIRCLE -> {

                    CircleNode circleNode = new CircleNode(3);

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.INT, curPair.getToken());
                    IntNode radius = new IntNode(0, curPair.getValue());
                    circleNode.addChild(radius);

                    if(sentence.size()==4) {

                        pos++;
                        curPair = sentence.get(pos);
                        expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                        pos++;
                        curPair = sentence.get(pos);
                        expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                        IDNode id = new IDNode(0, curPair.getValue());

                        for(Node n: syntaxTree){
                            if (n instanceof PointNode pn && pn.getIdNode().getValue().equals(id.getValue())) {
                                circleNode.addChild(pn);
                                circleNode.addChild(id);
                            }
                        }
                    }

                    else if(sentence.size()==6) {
                        PointNode pn = parsePoint(sentence, pos+1);
                        circleNode.addChild(pn);
                        circleNode.addChild(pn.getIdNode());
                    }

                    syntaxTree.add(circleNode);
                    
                }

                case RADIUS -> {

                    RadiusNode radiusNode = new RadiusNode(3);

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String centerId = curPair.getValue();

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String endId = curPair.getValue();

                    radiusNode.setIdNode(new IDNode(0, centerId+endId));

                    for(Node n : syntaxTree){

                        if(n instanceof CircleNode cn && cn.getCircleId().getValue().equals(centerId))
                            radiusNode.addChild(cn);
                        else if(n instanceof PointNode pn && pn.getIdNode().getValue().equals(endId))
                            radiusNode.addChild(pn);
                    }

                    syntaxTree.add(radiusNode);
                    
                }

                case DIAMETER -> {

                    DiameterNode diameterNode = new DiameterNode(4);

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String startId = curPair.getValue();

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String endId = curPair.getValue();

                    diameterNode.setIdNode(new IDNode(0, startId+endId));

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.CIRCLE, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String centerId = curPair.getValue();


                    for(Node n: syntaxTree){

                        if(n instanceof CircleNode cn && cn.getCircleId().getValue().equals(centerId))
                            diameterNode.addChild(cn);

                        if(n instanceof PointNode start && start.getIdNode().getValue().equals(startId))
                        {
                            diameterNode.addChild(start);
                        }

                        if(n instanceof PointNode end && end.getIdNode().getValue().equals(endId)) {
                            diameterNode.addChild(end);
                        }
                    }

                    if(diameterNode.getEnd() == null){
                        PointNode endPoint = new PointNode(3);
                        endPoint.setId(new IDNode(0, endId));
                        diameterNode.setEnd(endPoint);
                    }

                    syntaxTree.add(diameterNode);
                    
                }

                case CHORD -> {

                    ChordNode chordNode = new ChordNode(4);

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String startId = curPair.getValue();

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String endId = curPair.getValue();

                    chordNode.setIdNode(new IDNode(0, startId+endId));

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.CIRCLE, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String centerId = curPair.getValue();


                    for(Node n: syntaxTree){

                        if(n instanceof CircleNode cn && cn.getCircleId().getValue().equals(centerId)) {
                            chordNode.setAdjacentCircle(cn);
                            chordNode.addChild(cn);
                        }

                        if(n instanceof PointNode start && start.getIdNode().getValue().equals(startId)) {
                            chordNode.setStart(start);
                            chordNode.addChild(start);
                        }

                        if(n instanceof PointNode end && end.getIdNode().getValue().equals(endId)) {
                            chordNode.setEnd(end);
                            chordNode.addChild(end);
                        }
                    }

                    syntaxTree.add(chordNode);
                    
                }

                case SEGMENT -> {

                    SegmentNode segmentNode = new SegmentNode(3);

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String startId = curPair.getValue();

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());

                    pos++;
                    curPair = sentence.get(pos);
                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
                    String endId = curPair.getValue();

                    for(Node n: syntaxTree){

                        if(n instanceof PointNode start && start.getIdNode().getValue().equals(startId))
                            segmentNode.setStart(start);

                        if(n instanceof PointNode end && end.getIdNode().getValue().equals(endId))
                            segmentNode.setEnd(end);
                    }

                    syntaxTree.add(segmentNode);
                    
                }
            }
        }
    }

    private PointNode parsePoint(List<Pair> sentence, int pos) {
        PointNode pointNode = new PointNode(3);

        pos++;
        Pair curPair = sentence.get(pos);
        expect(LexicalAnalyzer.Token.ID, curPair.getToken());
        IDNode id = new IDNode(0, curPair.getValue());

        pos++;
        curPair = sentence.get(pos);
        expect(LexicalAnalyzer.Token.INT, curPair.getToken());
        IntNode x = new IntNode(0, curPair.getValue());

        pos++;
        curPair = sentence.get(pos);
        expect(LexicalAnalyzer.Token.INT, curPair.getToken());
        IntNode y = new IntNode(0, curPair.getValue());

        pointNode.addChild(id);
        pointNode.addChild(x);
        pointNode.addChild(y);

        return pointNode;
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
        else if (firstToken == LexicalAnalyzer.Token.CIRCLE ||
        firstToken == LexicalAnalyzer.Token.SEGMENT) {
            return 2;
        }
        else if (firstToken == LexicalAnalyzer.Token.RADIUS ||
                firstToken == LexicalAnalyzer.Token.DIAMETER ||
                firstToken == LexicalAnalyzer.Token.CHORD) {
            return 3;
        }
        return -1;
    }

}
