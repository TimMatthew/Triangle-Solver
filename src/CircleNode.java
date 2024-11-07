public class CircleNode implements CommandNode {

    private String centerId;
    private int radius;
    private PointNode centerNode;


    public int getRadius() {
        return radius;
    }


    public CircleNode(int radius, String center) {
        this.radius = radius;
        this.centerId = center;
    }

    public CircleNode(String centerId, int radius, PointNode centerNode) {
        this.centerId = centerId;
        this.radius = radius;
        this.centerNode = centerNode;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public PointNode getCenterNode() {
        return centerNode;
    }

    @Override
    public String getId() {
        return centerId;
    }

    public void setCenterNode(PointNode centerNode) {
        this.centerNode = centerNode;
    }
}
