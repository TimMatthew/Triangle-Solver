import java.util.List;

class SyntaxAnalyzer {

/*

<TEXT> ::= <SENTENCE_LIST> | #
<SENTENCE_LIST> ::= <SENTENCE> | <SENTENCE> '.' <SENTENCE_LIST>
<SENTENCE> ::= <TRIANGLE_ACTION> | <LINE_ACTION>

<TRIANGLE_ACTION> ::= "Побуд(увати|йте|уй) " "трикутник(и) " <TRIANGLE_LIST>
<TRIANGLE_LIST> ::= <TRIANGLE> | <TRIANGLE> ',' <TRIANGLE_LIST>
<TRIANGLE> ::= <ID><ID><ID>

<LINE_ACTION> ::= ("Провести ")            "висоту(а|и|у)" <HEIGHT_LIST>      |
                                           "медіан(а|и|у)" <MEDIAN_LIST>      |
                                           "бісектрис(а|и|у)" <BISECTOR_LIST> |
                                           "відрізок(а|и|у)" <SEGMENT_LIST>   |

<HEIGHT_LIST> ::= <HEIGHT> | <HEIGHT> ',' <HEIGHT_LIST>
<HEIGHT> ::= <ID><ID> "до " "сторони |гіпотенузи " <SEGMENT>

<MEDIAN_LIST> ::= <MEDIAN> | <MEDIAN> ',' <MEDIAN_LIST>
<HEIGHT> ::= <ID><ID> "до " "сторони |катета " <SEGMENT>

<BISECTOR_LIST> ::= <BISECTOR> | <BISECTOR> ',' <BISECTOR_LIST>
<BISECTOR> ::= <ID><ID> "до " "сторони |катета " <SEGMENT>

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

    // <SENTENCE> ::= <TRIANGLE_ACTION> | <LINE_ACTION>
    private AST parseSentence() {
        AST node = null;

        if (expect(LexicalAnalyzer.Token.ACTION_TRIANGLE)) {
            node = parseTriangleAction();
        }
        else if (expect(LexicalAnalyzer.Token.ACTION_LINE)) {
            node = parseLineAction();
        }

        return node;
    }

//  <TRIANGLE_ACTION> ::= "Побуд(увати|йте|уй) " "рівнобедрений " "прямокутний " "трикутник(и) " <TRIANGLE_LIST>
    private AST parseTriangleAction() {
        AST triangleActionNode = new AST("TRIANGLE_ACTION");
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.TRIANGLE);
        triangleActionNode.addChild(parseTriangleList());
        return triangleActionNode;
    }

    // <TRIANGLE_LIST> ::= <TRIANGLE> | <TRIANGLE> ',' <TRIANGLE_LIST>
    private AST parseTriangleList() {
        AST triangleListNode = new AST("TRIANGLE_LIST");
        triangleListNode.addChild(parseTriangle());
        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT
                && currentPair.getToken() != LexicalAnalyzer.Token.COMMA
                && currentPair.getToken() != LexicalAnalyzer.Token.TEXT_END) nextPair();
        while (expect(LexicalAnalyzer.Token.COMMA))
            triangleListNode.addChild(parseTriangle());

        return triangleListNode;
    }

    // <TRIANGLE> ::= <ID><ID><ID>
    private AST parseTriangle() {
        AST triangleNode = new AST("TRIANGLE");
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        triangleNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        triangleNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        triangleNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        return triangleNode;
    }


/*
    <LINE_ACTION> ::= ("Провести ")     "висоту(а|и|у)" <HEIGHT_LIST>      |
                                        "медіан(а|и|у)" <MEDIAN_LIST>      |
                                        "бісектрис(а|и|у)" <BISECTOR_LIST> |
 */
    private AST parseLineAction() {
        AST lineActionNode = new AST("LINE_ACTION");
        nextPair();
        if (expect(LexicalAnalyzer.Token.SEGMENT)) lineActionNode.addChild(parseSegmentList());
        else if(expect(LexicalAnalyzer.Token.HEIGHT)) lineActionNode.addChild(parseHeightList());
        else if(expect(LexicalAnalyzer.Token.MEDIAN)) lineActionNode.addChild(parseMedianList());
        else if(expect(LexicalAnalyzer.Token.BISECTOR)) lineActionNode.addChild(parseBisectorList());

        return lineActionNode;
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

/*<HEIGHT_LIST> ::= <HEIGHT> | <HEIGHT> ',' <HEIGHT_LIST>*/
    private AST parseHeightList() {
        AST heightListNode = new AST("HEIGHT_LIST");
        heightListNode.addChild(parseHeight());

        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT && currentPair.getToken() != LexicalAnalyzer.Token.COMMA
                && currentPair.getToken() != LexicalAnalyzer.Token.TEXT_END) nextPair();
        while(expect(LexicalAnalyzer.Token.COMMA)){
            heightListNode.addChild(parseHeight());
        }
        return heightListNode;
    }

/*<HEIGHT> ::= <ID><ID> "до " "сторони |гіпотенузи " <SEGMENT>*/
    private AST parseHeight() {
        AST segmentNode = new AST("HEIGHT");
        if(currentPair.getToken() != LexicalAnalyzer.Token.ID)
            nextPair();

        expect(LexicalAnalyzer.Token.ID);
        segmentNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        segmentNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        segmentNode.addChild(parseSegment());

        return segmentNode;
    }

    /*<MEDIAN_LIST> ::= <MEDIAN> | <MEDIAN> ',' <MEDIAN_LIST>*/
    private AST parseMedianList() {
        AST medianListNode = new AST("MEDIAN_LIST");
        medianListNode.addChild(parseMedian());

        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT && currentPair.getToken() != LexicalAnalyzer.Token.COMMA
                && currentPair.getToken() != LexicalAnalyzer.Token.TEXT_END) nextPair();
        while(expect(LexicalAnalyzer.Token.COMMA)){
            medianListNode.addChild(parseMedian());
        }
        return medianListNode;
    }

    /*<MEDIAN> ::= <ID><ID> "до " "сторони |гіпотенузи " <SEGMENT>*/
    private AST parseMedian() {
        AST medianNode = new AST("MEDIAN");
        if(currentPair.getToken() != LexicalAnalyzer.Token.ID)
            nextPair();

        expect(LexicalAnalyzer.Token.ID);
        medianNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        medianNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.SEGMENT);
        medianNode.addChild(parseSegment());

        return medianNode;
    }

    /*<BISECTOR_LIST> ::= <BISECTOR> | <BISECTOR> ',' <BISECTOR_LIST>*/
    private AST parseBisectorList() {
        AST bisectorListNode = new AST("BISECTOR_LIST");
        bisectorListNode.addChild(parseBisector());

        if(currentPair.getToken() != LexicalAnalyzer.Token.DOT && currentPair.getToken() != LexicalAnalyzer.Token.COMMA
                && currentPair.getToken() != LexicalAnalyzer.Token.TEXT_END) nextPair();
        while(expect(LexicalAnalyzer.Token.COMMA)){
            bisectorListNode.addChild(parseBisector());
        }
        return bisectorListNode;
    }

    /*<BISECTOR> ::= <ID><ID> "до " "сторони |катета " <SEGMENT>*/
    private AST parseBisector() {
        AST bisectorNode = new AST("BISECTOR");
        if(currentPair.getToken() != LexicalAnalyzer.Token.ID)
            nextPair();

        expect(LexicalAnalyzer.Token.ID);
        bisectorNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.ID);
        bisectorNode.addChild(new AST("ID", currentPair.getValue()));
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.EXTRA);
        nextPair();
        expect(LexicalAnalyzer.Token.SEGMENT);
        bisectorNode.addChild(parseSegment());
        nextPair();

        return bisectorNode;
    }
}
