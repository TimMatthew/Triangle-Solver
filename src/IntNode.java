public class IntNode extends Node{


    int val;
    public IntNode(int childrenAmount, String val) {
        super(childrenAmount);
        this.val= Integer.parseInt(val);
    }


    public int getValue() {
        return val;
    }
}
