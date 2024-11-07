public class SegmentNode implements CommandNode{

    String id;
    PointNode start;
    PointNode end;

    public SegmentNode(String id, PointNode start, PointNode end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public PointNode getStart() {
        return start;
    }

    public PointNode getEnd() {
        return end;
    }

    @Override
    public String getId() {
        return null;
    }
}
