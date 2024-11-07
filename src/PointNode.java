import java.util.ArrayList;

public class PointNode extends Node{

    private IDNode id;
    private IntNode x;
    private IntNode y;

    public PointNode(int childrenAmount) {
        super(childrenAmount);
        children = new ArrayList<>();
    }

    void addChild(Node n){

        children.add(n);

        if(n instanceof IDNode) setId((IDNode) n);
        else if(n instanceof IntNode intNode) {

            if(children.indexOf(intNode) == 1) setX(intNode);
            else if(children.indexOf(intNode) == 2) setY(intNode);
        }

    };

    public IDNode getIdNode() {
        return id;
    }

    public IntNode getX() {
        return x;
    }

    public IntNode getY() {
        return y;
    }

    public void setId(IDNode id){
        this.id = id;
    };

    public void setX(IntNode x) {
        this.x = x;
    }

    public void setY(IntNode y) {
        this.y = y;
    }
}
