public class ChordNode implements CommandNode{

    String id;

    PointNode start;
    PointNode end;
    CircleNode adjacentCircle;

    public ChordNode(String id, PointNode start, PointNode end, CircleNode adjacentCircle) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.adjacentCircle = adjacentCircle;
    }

    public PointNode getStart() {
        return start;
    }

    public PointNode getEnd() {
        return end;
    }

    public CircleNode getAdjacentCircle() {
        return adjacentCircle;
    }

    @Override
    public String getId() {
        return id;
    }
}
