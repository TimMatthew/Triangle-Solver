import java.util.ArrayList;

public class RadiusNode extends Node{

    private IDNode idNode;

    private CircleNode adjacentCircle;

    private PointNode end;


    public RadiusNode(int childrenAmount) {
        super(childrenAmount);
        children = new ArrayList<>();
    }

    void addChild(Node n){
        children.add(n);

        if(n instanceof IDNode in) setIdNode(in);
        else if (n instanceof CircleNode cn) setAdjacentCircle(cn);
        else if (n instanceof PointNode pn) setEnd(pn);
    }

    public IDNode getIdNode() {
        return idNode;
    }

    public void setIdNode(IDNode idNode) {
        this.idNode = idNode;
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
}
