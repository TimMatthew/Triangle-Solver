public class DrawCircleNode implements CommandNode {

    int radius;
    MarkPointNode centerNode;
    String centerId;
    int centerX;
    int centerY;

    public int getRadius() {
        return radius;
    }

    public DrawCircleNode(int radius, String center) {
        this.radius = radius;
        this.centerId = center;
    }

    public DrawCircleNode(int radius, String center, int centerX, int centerY) {
        this.radius = radius;
        this.centerId = center;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public MarkPointNode getCenterNode() {
        return centerNode;
    }

    @Override
    public String getId() {
        return centerId;
    }

    public void setCenterNode(MarkPointNode centerNode) {
        this.centerNode = centerNode;
    }
}
