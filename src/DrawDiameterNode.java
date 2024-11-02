public class DrawDiameterNode implements CommandNode{

    String id;

    String start;
    String end;
    MarkPointNode startNode;
    MarkPointNode endNode;




    public DrawDiameterNode(String id, String p1, String p2) {
        this.id = id;
        this.start = p1;
        this.end = p2;
    }

    public void setStartNode(MarkPointNode startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(MarkPointNode endNode) {
        this.endNode = endNode;
    }

    @Override
    public void execute() {

    }
}
