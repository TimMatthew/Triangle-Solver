import java.util.ArrayList;

public class ChordNode extends Node{

    private IDNode idNode;

    private CircleNode adjacentCircle;

    private PointNode start;

    private PointNode end;


    public ChordNode(int childrenAmount) {
        super(childrenAmount);
        children = new ArrayList<>();
    }

    void addChild(Node n){
        children.add(n);

        if(n instanceof IDNode in) setIdNode(in);
        else if (n instanceof CircleNode cn) setAdjacentCircle(cn);
        else if (n instanceof PointNode pn) {

            String pointID = pn.getIdNode().getValue();
            if(pointID.equals(idNode.getValue().substring(0,0))){
                setStart(pn);
            }
            else if(pointID.equals(idNode.getValue().substring(1,1))){
                setEnd(pn);
            }
        }
    }

    public IDNode getIdNode() {
        return idNode;
    }

    public void setIdNode(IDNode idNode) {
        this.idNode = idNode;
    }

    public PointNode getEnd() {
        return end;
    }

    public void setEnd(PointNode end) {
        this.end = end;
    }

    public CircleNode getAdjacentCircle() {
        return adjacentCircle;
    }

    public void setAdjacentCircle(CircleNode adjacentCircle) {
        this.adjacentCircle = adjacentCircle;
    }

    public PointNode getStart() {
        return start;
    }

    public void setStart(PointNode start) {
        this.start = start;
    }
}
