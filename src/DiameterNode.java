public class DiameterNode {

    String id;
    PointNode start;
    CircleNode adjacentCircle;
    PointNode end;

    public DiameterNode(String id, PointNode start, CircleNode adjacentCircle) {
        this.id = id;
        this.start = start;
        this.adjacentCircle = adjacentCircle;
    }

    public DiameterNode(String id, PointNode start, CircleNode adjacentCircle, PointNode end) {
        this.id = id;
        this.start = start;
        this.adjacentCircle = adjacentCircle;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PointNode getStart() {
        return start;
    }

    public void setStart(PointNode start) {
        this.start = start;
    }

    public CircleNode getAdjacentCircle() {
        return adjacentCircle;
    }

    public void setAdjacentCircle(CircleNode adjacentCircle) {
        this.adjacentCircle = adjacentCircle;
    }

    public PointNode getEnd() {
        return end;
    }

    public void setEnd(PointNode end) {
        this.end = end;
    }
}
