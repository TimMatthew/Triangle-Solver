abstract class ASTNode {
    public abstract void accept(TaskVisitor visitor);
}

interface TaskVisitor {
    void visit(MarkPointNode node);
    void visit(DrawCircleNode node);
    void visit(DrawDiameterNode node);
    void visit(DrawSegmentNode node);
    void visit(DrawRadiusNode node);
    void visit(DrawChordNode node);
    void visit(DrawLineNode node);
}

