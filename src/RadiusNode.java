public class RadiusNode implements CommandNode{

    // Початкову точку нам не треба пам'ятати,
    // бо вона є центром кола,
    // а коло ми й так зберігаємо
    String id;
    CircleNode adjacentCircle;
    PointNode end;

    public RadiusNode(String id, CircleNode adjacentCircle, PointNode end) {
        this.id = id;
        this.adjacentCircle = adjacentCircle;
        this.end = end;
    }


    public void setId(String id) {
        this.id = id;
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

    @Override
    public String getId() {
        return id;
    }
}
