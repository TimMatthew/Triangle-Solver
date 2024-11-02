public class DrawChordNode implements CommandNode{

    String id;
    String start;
    String end;
    MarkPointNode startNode;
    MarkPointNode endNode;

    public DrawChordNode(String id, String p1, String p2) {
        this.id = id;
        this.start = p1;
        this.end = p2;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public MarkPointNode getStartNode() {
        return startNode;
    }

    public MarkPointNode getEndNode() {
        return endNode;
    }

    public void setStartNode(MarkPointNode startNode) {
        this.startNode = startNode;
    }



    public void setEndNode(MarkPointNode endNode) {
        this.endNode = endNode;
    }


    @Override
    public String getId() {
        return id;
    }
}
