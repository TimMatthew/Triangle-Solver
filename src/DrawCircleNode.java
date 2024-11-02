public class DrawCircleNode implements CommandNode {

    int radius;
    MarkPointNode centerNode;
    String centerId;
    int centerX;
    int centerY;

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

    public void setCenterNode(MarkPointNode centerNode) {
        this.centerNode = centerNode;
    }

    @Override
    public void execute() {

    }
}
