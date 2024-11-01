import java.util.List;

class DrawSegmentNode extends ASTNode {
    private final List<String> twoLetterList;

    public DrawSegmentNode(List<String> twoLetterList) {
        this.twoLetterList = twoLetterList;
    }

    public List<String> getTwoLetterList() { return twoLetterList; }

    @Override
    public void accept(TaskVisitor visitor) {
        visitor.visit(this);
    }
}
