public class SegmentNode extends Node{

    private PointNode start;

    private PointNode end;


    public SegmentNode(int childrenAmount) {
        super(childrenAmount);
    }

    public PointNode getStart() {
        return start;
    }

    public void setStart(PointNode start) {
        this.start = start;
    }

    public PointNode getEnd() {
        return end;
    }

    public void setEnd(PointNode end) {
        this.end = end;
    }
}
