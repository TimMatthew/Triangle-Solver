public class DrawRadiusNode implements CommandNode{

    String id;
    String start;
    String end;
    MarkPointNode startNode;
    MarkPointNode endNode;

    public DrawRadiusNode(String id, String start, String end) {
        this.id = id;
        this.start = start;
        this.end = end;
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
