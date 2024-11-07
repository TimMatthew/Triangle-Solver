import java.util.List;

public abstract class Node {
    List<Node> children;
    int childrenAmount;

    public Node(int childrenAmount) {
        this.childrenAmount = childrenAmount;
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getChildrenAmount() {
        return childrenAmount;
    }

}
