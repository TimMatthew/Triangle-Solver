import java.util.List;

class SyntaxAnalyzer {

/*

<TEXT> ::= <SENTENCE_LIST> | #
<SENTENCE_LIST> ::= <SENTENCE> | <SENTENCE> '.' <SENTENCE_LIST>
<SENTENCE> ::= <POINT_ACTION> | <TRIANGLE_ACTION> | <LINE_ACTION>

<POINT_ACTION> ::= "Познач(ити|те)" "точк(у|и)"  <POINT_LIST>
<POINT_LIST> ::= <POINT> | <POINT> ',' <POINT_LIST>
<POINT> ::= <ID> | <ID> "(із|з)" "координатами" <COORDINATE>
<COORDINATE> ::= '(' <INT> ';' <INT> ')'

<TRIANGLE_ACTION> ::= "Побуд(увати|йте|уй) " "трикутник(и) " <TRIANGLE_LIST>
<TRIANGLE_LIST> ::= <TRIANGLE> | <TRIANGLE> ',' <TRIANGLE_LIST>
<TRIANGLE> ::= <ID><ID><ID>

<LINE_ACTION> ::= ("Прове(сти|діть|ди) ")  "висот(а|и)" <HEIGHT_LIST>
                                           "медіан(а|и)" <MEDIAN_LIST>
                                           "бісектрис(а|и)" <BISECTOR_LIST>
                                           "відріз(ок|ки)" <SEGMENT_LIST>

<HEIGHT_LIST> ::= <HEIGHT> | <HEIGHT> ',' <HEIGHT_LIST>
<HEIGHT> ::= <ID><ID> "з " "кута " <ANGLE> "до " "сторони " <SEGMENT>

<MEDIAN_LIST> ::= <MEDIAN> | <MEDIAN> ',' <MEDIAN_LIST>
<HEIGHT> ::= <ID><ID> "з " "кута " <ANGLE> "до " "сторони " <SEGMENT>

<ANGLE> ::= <ID> | <ID><ID><ID>

<BISECTOR_LIST> ::= <BISECTOR> | <BISECTOR> ',' <BISECTOR_LIST>
<BISECTOR> ::= <ID><ID>

<SEGMENT_LIST> ::= <SEGMENT> | <SEGMENT> ',' <SEGMENT_LIST>
<SEGMENT> ::= <ID><ID>

*/

    private List<Pair> tokens;
    private int currentIndex = 0;
    private AST sentenceListNode;
    private Pair currentPair;

    public SyntaxAnalyzer(List<Pair> tokens) {
        this.tokens = tokens;
        currentPair = tokens.getFirst();
    }

    private void nextPair() {
        currentIndex++;
        currentPair = tokens.get(currentIndex);
    }

    private boolean expect(LexicalAnalyzer.Token tokenType) {
        Pair token = currentPair;
        if (token != null && token.getToken().equals(tokenType)) {
            return true;
        }
        return false;
    }

    public AST parse() {
        return parseText();
    }

    // <TEXT> ::= <SENTENCE_LIST> | #
    private AST parseText() {

        AST root = new AST("TEXT");
        if (expect(LexicalAnalyzer.Token.EXTRA) && currentPair.getValue().equals("#")) {
            root = new AST("TEXT", "#");
            return root;
        }
        root.addChild(parseSentenceList());
        return root;
    }

    // <SENTENCE_LIST> ::= <SENTENCE> | <SENTENCE> '.' <SENTENCE_LIST>
    private AST parseSentenceList() {
        sentenceListNode = new AST("SENTENCE_LIST");
        sentenceListNode.addChild(parseSentence());
        while (expect(LexicalAnalyzer.Token.DOT)) {
            nextPair();
            sentenceListNode.addChild(parseSentence());
        }

        return sentenceListNode;
    }

    // <SENTENCE> ::= <POINT_ACTION> | <CIRCLE_ACTION> | <LINE_ACTION>
    private AST parseSentence() {
        AST node = null;
        if (expect(LexicalAnalyzer.Token.ACTION_POINT)) {
            node = parsePointAction();
        }
        else if (expect(LexicalAnalyzer.Token.ACTION_CIRCLE)) {
            node = parseCircleAction();
        }
        else if (expect(LexicalAnalyzer.Token.ACTION_LINE)) {
            node = parseLineAction();
        }

        return node;
    }

    // <POINT_ACTION> ::= "Познач(ити|те)" "точк(у|и)" <POINT_LIST>
    private AST parsePointAction() {
        AST pointActionNode = new AST("POINT_ACTION");
        nextPair();
        expect(LexicalAnalyzer.Token.POINT);
        pointActionNode.addChild(parsePointList());
        return pointActionNode;
    }

    // <POINT_LIST> ::= <POINT> | <POINT> ',' <POINT_LIST>
    private AST parsePointList() {
        AST pointListNode = new AST("POINT_LIST");
        pointListNode.addChild(parsePoint());
        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT
                && currentPair.getToken() != LexicalAnalyzer.Token.COMMA
                && currentPair.getToken() != LexicalAnalyzer.Token.TEXT_END) nextPair();

        while (expect(LexicalAnalyzer.Token.COMMA)) {
            pointListNode.addChild(parsePoint());
        }
        return pointListNode;
    }

    // <POINT> ::= <ID> | <ID> "(із|з)" "координатами" <COORDINATE>
    private AST parsePoint() {
        AST pointNode = new AST("POINT");
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        pointNode.addChild(new AST("ID", currentPair.getValue()));  // ID (e.g., "O")
        nextPair();
        if(expect(LexicalAnalyzer.Token.EXTRA)){
                nextPair();
                nextPair();
                pointNode.addChild(parseCoordinate());
        }
        return pointNode;
    }

    // <COORDINATE> ::= '(' <INT> ';' <INT> ')'
    private AST parseCoordinate() {
        AST coordinateNode = new AST("COORDINATE");
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.INT);
        coordinateNode.addChild(new AST("INT", currentPair.getValue()));  // first INT
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.INT);
        coordinateNode.addChild(new AST("INT", currentPair.getValue()));  // second INT
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        return coordinateNode;
    }

    // <CIRCLE_ACTION> ::= "Побуд(увати|йте|уй)" "кол(о|а)" "(із|з)" "центр(ом|ами)" <CIRCLE_LIST>
    private AST parseCircleAction() {
        AST circleActionNode = new AST("CIRCLE_ACTION");
        nextPair();
        expect(LexicalAnalyzer.Token.CIRCLE);
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.POINT);
        circleActionNode.addChild(parseCircleList());
        return circleActionNode;
    }

    // <CIRCLE_LIST> ::= <CIRCLE> | <CIRCLE> ',' <CIRCLE_LIST>
    private AST parseCircleList() {
        AST circleListNode = new AST("CIRCLE_LIST");
        circleListNode.addChild(parseCircle());
        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT
                && currentPair.getToken() != LexicalAnalyzer.Token.COMMA
                && currentPair.getToken() != LexicalAnalyzer.Token.TEXT_END) nextPair();
        while (expect(LexicalAnalyzer.Token.COMMA))
            circleListNode.addChild(parseCircle());

        return circleListNode;
    }

    // <CIRCLE> ::= <ID> | <ID> "з" "радіусом" <INT>
    private AST parseCircle() {
        AST circleNode = new AST("CIRCLE");
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        circleNode.addChild(new AST("ID", currentPair.getValue()));  // ID (e.g., "O")
        nextPair();
        if (expect(LexicalAnalyzer.Token.EXTRA)) {
            nextPair();
            expect(LexicalAnalyzer.Token.RADIUS);
            nextPair();
            expect(LexicalAnalyzer.Token.INT);
            if(Integer.parseInt(currentPair.getValue()) < 0)
                currentPair.setValue(currentPair.getValue().replace("-", ""));
            circleNode.addChild(new AST("INT", currentPair.getValue()));  // INT (e.g., radius)
            nextPair();
        }
        return circleNode;
    }


//     <LINE_ACTION> ::= ("Прове(сти|діть|ди) ") ("хорд(а|и)" <CHORD_LIST> |
//                                                       "діаметр(и)" <DIAMETER_LIST> |
//                                                       "радіус(и)" <RADIUS_LIST> |
//                                                       "відріз(ок|ки)" <SEGMENT_LIST>)
    private AST parseLineAction() {
        AST lineActionNode = new AST("LINE_ACTION");
        nextPair();

        if (expect(LexicalAnalyzer.Token.RADIUS)) lineActionNode.addChild(parseRadiusList());
        else if (expect(LexicalAnalyzer.Token.DIAMETER)) lineActionNode.addChild(parseDiameterList());
        else if (expect(LexicalAnalyzer.Token.CHORD)) lineActionNode.addChild(parseChordList());
        else if (expect(LexicalAnalyzer.Token.SEGMENT)) lineActionNode.addChild(parseSegmentList());


        return lineActionNode;
    }


    //  <RADIUS_LIST> ::=  <RADIUS> | <RADIUS> ',' <RADIUS_LIST>

    private AST parseRadiusList() {
        AST radiusListNode = new AST("RADIUS_LIST");
        radiusListNode.addChild(parseRadius());

        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT && currentPair.getToken() != LexicalAnalyzer.Token.COMMA) nextPair();
        while(expect(LexicalAnalyzer.Token.COMMA)){
           radiusListNode.addChild(parseRadius());
        }
        return radiusListNode;
    }

    //  <RADIUS> ::= <ID><ID>
    private AST parseRadius() {
        AST radiusNode = new AST("RADIUS");
        if(currentPair.getToken() != LexicalAnalyzer.Token.ID)
            nextPair();
        expect(LexicalAnalyzer.Token.ID);
        radiusNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        radiusNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        return radiusNode;
    }


/*<SEGMENT_LIST> ->  <SEGMENT> | (<SEGMENT> ',' "відріз(ок|ки)" <SEGMENT_LIST>)*/
    private AST parseSegmentList() {
        AST segmentListNode = new AST("SEGMENT_LIST");
        segmentListNode.addChild(parseSegment());

        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT && currentPair.getToken() != LexicalAnalyzer.Token.COMMA
                && currentPair.getToken() != LexicalAnalyzer.Token.TEXT_END) nextPair();
        while(expect(LexicalAnalyzer.Token.COMMA)){
            segmentListNode.addChild(parseSegment());
        }
        return segmentListNode;
    }

// <SEGMENT> -> <ID><ID>
    private AST parseSegment() {
        AST segmentNode = new AST("SEGMENT");
        if(currentPair.getToken() != LexicalAnalyzer.Token.ID)
            nextPair();
        expect(LexicalAnalyzer.Token.ID);
        segmentNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        segmentNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        return segmentNode;
    }


/*<CHORD_LIST> ::= <CHORD> | <CHORD> ',' "хорд(а|и)" <CHORD_LIST>*/
    private AST parseChordList() {

        AST chordListNode = new AST("CHORD_LIST");
        chordListNode.addChild(parseChord());

        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT && currentPair.getToken() != LexicalAnalyzer.Token.COMMA
                && currentPair.getToken() != LexicalAnalyzer.Token.TEXT_END) nextPair();
        while (expect(LexicalAnalyzer.Token.COMMA)) {
            chordListNode.addChild(parseChord());
        }

        return chordListNode;
    }

//  <CHORD> ::= <ID><ID> "на" "колі" <ID>
    private AST parseChord() {

        AST chordNode = new AST("CHORD");
        if(currentPair.getToken() != LexicalAnalyzer.Token.ID)
            nextPair();
        expect(LexicalAnalyzer.Token.ID);
        chordNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        chordNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.CIRCLE);
        chordNode.addChild(new AST("CIRCLE_ID", currentPair.getValue()));
        nextPair();
        return chordNode;
    }


/*
<DIAMETER_LIST> ::= <DIAMETER> | (<DIAMETER> ',' "діаметр(и)" <DIAMETER_LIST>)
*/
    private AST parseDiameterList() {
        AST diameterListNode = new AST("DIAMETER_LIST");
        diameterListNode.addChild(parseDiameter());

        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT && currentPair.getToken() != LexicalAnalyzer.Token.COMMA
                && currentPair.getToken() != LexicalAnalyzer.Token.TEXT_END) nextPair();
        while (expect(LexicalAnalyzer.Token.COMMA)) {
            diameterListNode.addChild(parseDiameter());
        }

        return diameterListNode;
    }

//    <DIAMETER> ::= <ID><ID> "на" "колі" <ID>
    private AST parseDiameter() {

        AST diameterNode = new AST("DIAMETER");
        if(currentPair.getToken() != LexicalAnalyzer.Token.ID)
            nextPair();
        expect(LexicalAnalyzer.Token.ID);
        diameterNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        diameterNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.CIRCLE);
        nextPair();
        diameterNode.addChild(new AST("CIRCLE_ID", currentPair.getValue()));
        nextPair();
        return diameterNode;
    }

}
