public class Chord extends Shape{
    private Circle adjacentCircle;
    private Point start;
    private Point end;

    public Chord(String id, Point start, Point end, Circle adjacentCircle) {
        super(id);
        this.start = start;
        this.end = end;
        this.adjacentCircle = adjacentCircle;
    }

    public Circle getAdjacentCircle() {
        return adjacentCircle;
    }

    public void setAdjacentCircle(Circle adjacentCircle) {
        this.adjacentCircle = adjacentCircle;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }
}
