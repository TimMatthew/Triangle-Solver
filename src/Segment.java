public class Segment extends Shape{

    private Point start;
    private Point end;

    public Segment(String id, Point start, Point end) {
        super(id);
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
