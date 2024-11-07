import java.util.ArrayList;

public class CircleNode extends Node{


    private IDNode circleId;

    private IntNode radius;

    private PointNode centerPoint;

    public CircleNode(int childrenAmount) {
        super(childrenAmount);
        children = new ArrayList<>();
    }

    void addChild(Node n){
        children.add(n);

        if(n instanceof IDNode idNode) setCircleId(idNode);

        else if(n instanceof IntNode r) setRadius(r);

        else if(n instanceof PointNode center) setCenter(center);
    }

    public IDNode getCircleId() {
        return circleId;
    }

    public void setCircleId(IDNode circleId) {
        this.circleId = circleId;
    }

    public IntNode getRadius() {
        return radius;
    }

    public void setRadius(IntNode radius) {
        this.radius = radius;
    }

    public PointNode getCenter() {
        return centerPoint;
    }

    public void setCenter(PointNode center) {
        this.centerPoint = center;
    }
}
