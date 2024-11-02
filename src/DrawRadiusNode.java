public class DrawRadiusNode implements CommandNode{

    private String id;
    private String center;
    private String end;
    private MarkPointNode centerNode;
    private MarkPointNode endNode;

    public DrawRadiusNode(String id, String center, String end) {
        this.id = id;
        this.center = center;
        this.end = end;
    }

    public void setCenterNode(MarkPointNode centerNode) {
        this.centerNode = centerNode;
    }

    public void setEndNode(MarkPointNode endNode) {
        this.endNode = endNode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public MarkPointNode getCenterNode() {
        return centerNode;
    }

    public MarkPointNode getEndNode() {
        return endNode;
    }

    @Override
    public String getId() {
        return id;
    }
}
