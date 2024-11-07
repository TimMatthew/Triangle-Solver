public class IDNode extends Node{

    String value;
    public IDNode(int childrenAmount, String value) {
        super(childrenAmount);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
