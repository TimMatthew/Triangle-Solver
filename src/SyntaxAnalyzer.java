import java.util.List;
class SyntaxAnalyzer {

/*

<TEXT> ::= <SENTENCE_LIST> | #
<SENTENCE_LIST> ::= <SENTENCE> | <SENTENCE> '.' <SENTENCE_LIST>
<SENTENCE> ::= <POINT_ACTION> | <CIRCLE_ACTION> | <LINE_ACTION>




<POINT_ACTION> ::= "Познач(ити|те) " "точк(у|и) "  <POINT_LIST>
<POINT_LIST> ::= <POINT> | <POINT> ',' <POINT_LIST>
<POINT> ::= <ID> | <ID> "(із|з) " "координатами " <COORDINATE>
<COORDINATE> ::= '(' <INT> ';' <INT> ')'

    Ex.:    Познач точку A
            Позначте точку D1 з координатами (-14;9)
            Позначити точки G, F1, C із координатами (78;-23), J




<CIRCLE_ACTION> ::= "Побуд(увати|йте|уй) " "кол(о|а) " "(із|з) " "центр(ом|ами) " <CIRCLE_LIST>
<CIRCLE_LIST> ::= <CIRCLE> | <CIRCLE> ',' <CIRCLE_LIST>
<CIRCLE> ::=  <ID> | <ID> "з " "радіусом " <INT>

    Ex.:    Побудуй коло із центром O,
            Побудувати коло з центром V з радіусом 15
            Побудувати кола з центрами C, D2, S з радіусом 7, Q, P з радіусом 234




<LINE_ACTION> ::= ("Прове(сти|діть|ди) ") ("хорд(а|и) " <RADIUS_LIST>        |
                                           "діаметр(и) " <DIAMETER_LIST>     |
                                           "радіус(и) " <CHORD_LIST>         |
                                           "відріз(ок|ки) " <SEGMENT_LIST>   )


<RADIUS_LIST> ::=  <RADIUS> | (<RADIUS> ',' <RADIUS_LIST>                    |
                              <RADIUS> ',' "діаметр(и) " <DIAMETER_LIST>     |
                              <RADIUS> ',' "хорд(а|и) " <CHORD_LIST>         |
                              <RADIUS> ',' "відріз(ок|ки) " <SEGMENT_LIST>   )

<RADIUS> ::= <ID><ID>


<DIAMETER_LIST> ::= <DIAMETER> | (<DIAMETER> ',' "радіус(и) " <RADIUS_LIST>        |
                                  <DIAMETER> ',' <DIAMETER_LIST>                   |
                                  <DIAMETER> ',' "хорд(а|и) " <CHORD_LIST>         |
                                  <DIAMETER> ',' "відріз(ок|ки) " <SEGMENT_LIST>   )

<DIAMETER> ::= <ID><ID> "на " "колі " <ID>


<CHORD_LIST> ::= <CHORD> | (<CHORD> ',' "радіус(и) " <RADIUS_LIST>        |
                            <CHORD> ',' "діаметр(и) " <DIAMETER_LIST>     |
                            <CHORD> ',' <CHORD_LIST>                      |
                            <CHORD> ',' "відріз(ок|ки) " <SEGMENT_LIST>   )

<CHORD> ::= <ID><ID> "на " "колі " <ID>


<SEGMENT_LIST> ->  <SEGMENT> | (<SEGMENT> ',' "радіус(и) " <RADIUS_LIST>    |
                                <SEGMENT> ',' "діаметр(и) " <DIAMETER_LIST> |
                                <SEGMENT> ',' "хорд(а|и) " <CHORD_LIST>     |
                                <SEGMENT> ',' <SEGMENT_LIST>                )

<SEGMENT> -> <ID><ID>

    Ex.:    Побудуй радіус OK
            Побудуйте діаметр PK на колі O
            Побудувати хорди GH на колі O1, AF на колі O1
            Провести відрізки DF1, AT, радіус OP
            Проведіть діаметри FK на колі O, AL на колі O, хорди GH на колі O1, AF на колі O1, відрізки VM, VM1, радіуси O1W, O1G1
*/

    private final List<Pair> sentences;

    public SyntaxAnalyzer(List<Pair> sentences) {
        this.sentences = sentences;
    }


//    public void parse(List<Node> syntaxTree){
//        sortTokenLists(sentences);
//
//        for (List<Pair> sentence : sentences) {
//
//            LexicalAnalyzer.Token firstToken = sentence.getFirst().getToken();
//            int pos=0;
//            Pair curPair;
//
//            switch (firstToken){
//
//                case POINT -> {
//                    PointNode pn = parsePoint(sentence, pos);
//                    syntaxTree.add(pn);
//
//                }
//
//                case CIRCLE -> {
//
//                    CircleNode circleNode = new CircleNode(3);
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.INT, curPair.getToken());
//                    IntNode radius = new IntNode(0, curPair.getValue());
//                    circleNode.addChild(radius);
//
//                    if(sentence.size()==4) {
//
//                        pos++;
//                        curPair = sentence.get(pos);
//                        expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                        pos++;
//                        curPair = sentence.get(pos);
//                        expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                        IDNode id = new IDNode(0, curPair.getValue());
//
//                        for(Node n: syntaxTree){
//                            if (n instanceof PointNode pn && pn.getIdNode().getValue().equals(id.getValue())) {
//                                circleNode.addChild(pn);
//                                circleNode.addChild(id);
//                            }
//                        }
//                    }
//
//                    else if(sentence.size()==6) {
//                        PointNode pn = parsePoint(sentence, pos+1);
//                        circleNode.addChild(pn);
//                        circleNode.addChild(pn.getIdNode());
//                    }
//
//                    syntaxTree.add(circleNode);
//
//                }
//
//                case RADIUS -> {
//
//                    RadiusNode radiusNode = new RadiusNode(3);
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String centerId = curPair.getValue();
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String endId = curPair.getValue();
//
//                    radiusNode.setIdNode(new IDNode(0, centerId+endId));
//
//                    for(Node n : syntaxTree){
//
//                        if(n instanceof CircleNode cn && cn.getCircleId().getValue().equals(centerId))
//                            radiusNode.addChild(cn);
//                        else if(n instanceof PointNode pn && pn.getIdNode().getValue().equals(endId))
//                            radiusNode.addChild(pn);
//                    }
//
//                    syntaxTree.add(radiusNode);
//
//                }
//
//                case DIAMETER -> {
//
//                    DiameterNode diameterNode = new DiameterNode(4);
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String startId = curPair.getValue();
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String endId = curPair.getValue();
//
//                    diameterNode.setIdNode(new IDNode(0, startId+endId));
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.CIRCLE, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String centerId = curPair.getValue();
//
//
//                    for(Node n: syntaxTree){
//
//                        if(n instanceof CircleNode cn && cn.getCircleId().getValue().equals(centerId))
//                            diameterNode.addChild(cn);
//
//                        if(n instanceof PointNode start && start.getIdNode().getValue().equals(startId))
//                        {
//                            diameterNode.addChild(start);
//                        }
//
//                        if(n instanceof PointNode end && end.getIdNode().getValue().equals(endId)) {
//                            diameterNode.addChild(end);
//                        }
//                    }
//
//                    if(diameterNode.getEnd() == null){
//                        PointNode endPoint = new PointNode(3);
//                        endPoint.setId(new IDNode(0, endId));
//                        diameterNode.setEnd(endPoint);
//                    }
//
//                    syntaxTree.add(diameterNode);
//
//                }
//
//                case CHORD -> {
//
//                    ChordNode chordNode = new ChordNode(4);
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String startId = curPair.getValue();
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String endId = curPair.getValue();
//
//                    chordNode.setIdNode(new IDNode(0, startId+endId));
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.CIRCLE, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String centerId = curPair.getValue();
//
//
//                    for(Node n: syntaxTree){
//
//                        if(n instanceof CircleNode cn && cn.getCircleId().getValue().equals(centerId)) {
//                            chordNode.setAdjacentCircle(cn);
//                            chordNode.addChild(cn);
//                        }
//
//                        if(n instanceof PointNode start && start.getIdNode().getValue().equals(startId)) {
//                            chordNode.setStart(start);
//                            chordNode.addChild(start);
//                        }
//
//                        if(n instanceof PointNode end && end.getIdNode().getValue().equals(endId)) {
//                            chordNode.setEnd(end);
//                            chordNode.addChild(end);
//                        }
//                    }
//
//                    syntaxTree.add(chordNode);
//
//                }
//
//                case SEGMENT -> {
//
//                    SegmentNode segmentNode = new SegmentNode(3);
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String startId = curPair.getValue();
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.POINT, curPair.getToken());
//
//                    pos++;
//                    curPair = sentence.get(pos);
//                    expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//                    String endId = curPair.getValue();
//
//                    for(Node n: syntaxTree){
//
//                        if(n instanceof PointNode start && start.getIdNode().getValue().equals(startId))
//                            segmentNode.setStart(start);
//
//                        if(n instanceof PointNode end && end.getIdNode().getValue().equals(endId))
//                            segmentNode.setEnd(end);
//                    }
//
//                    syntaxTree.add(segmentNode);
//
//                }
//            }
//        }
//    }
//
//    private PointNode parsePoint(List<Pair> sentence, int pos) {
//        PointNode pointNode = new PointNode(3);
//
//        pos++;
//        Pair curPair = sentence.get(pos);
//        expect(LexicalAnalyzer.Token.ID, curPair.getToken());
//        IDNode id = new IDNode(0, curPair.getValue());
//
//        pos++;
//        curPair = sentence.get(pos);
//        expect(LexicalAnalyzer.Token.INT, curPair.getToken());
//        IntNode x = new IntNode(0, curPair.getValue());
//
//        pos++;
//        curPair = sentence.get(pos);
//        expect(LexicalAnalyzer.Token.INT, curPair.getToken());
//        IntNode y = new IntNode(0, curPair.getValue());
//
//        pointNode.addChild(id);
//        pointNode.addChild(x);
//        pointNode.addChild(y);
//
//        return pointNode;
//    }
//
//    public void expect(LexicalAnalyzer.Token expected, LexicalAnalyzer.Token retrieved) {
//        if (retrieved != expected) {
//            throw new RuntimeException("Unexpected token! Expected: " + expected + " Got: " + retrieved);
//        }
//    }
//
//    public static void sortTokenLists(List<List<Pair>> tokenLists) {
//
//        tokenLists.sort((list1, list2) -> {
//            int priority1 = getPriority(list1);
//            int priority2 = getPriority(list2);
//            return Integer.compare(priority1, priority2);
//        });
//    }
//
//
//    public static int getPriority(List<Pair> tokenList) {
//
//        LexicalAnalyzer.Token firstToken = tokenList.getFirst().getToken();
//
//        if (firstToken == LexicalAnalyzer.Token.POINT) {
//            return 1;
//        }
//        else if (firstToken == LexicalAnalyzer.Token.CIRCLE ||
//        firstToken == LexicalAnalyzer.Token.SEGMENT) {
//            return 2;
//        }
//        else if (firstToken == LexicalAnalyzer.Token.RADIUS ||
//                firstToken == LexicalAnalyzer.Token.DIAMETER ||
//                firstToken == LexicalAnalyzer.Token.CHORD) {
//            return 3;
//        }
//        return -1;
//    }

}
