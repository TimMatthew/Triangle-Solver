public class Radius extends Shape {

    private Point center;
    private Point end;
    private Circle adjacentCircle;

    public Radius(String id, Point center, Point end, Circle adjacentCircle) {
        super(id);
        this.adjacentCircle = adjacentCircle;
        this.center = center;
        this.end = end;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Point getEnd() {
        return end;
    }

    public Circle getAdjacentCircle() {
        return adjacentCircle;
    }

    public void setAdjacentCircle(Circle adjacentCircle) {
        this.adjacentCircle = adjacentCircle;
    }

    public void setEnd(Point end) {
        this.end = end;
    }
}
