class TaskExecutor implements TaskVisitor {

    @Override
    public void visit(MarkPointNode node) {
        System.out.println("Marking point " + node.getPointID() + " at (" + node.getX() + "; " + node.getY() + ")");
    }

    @Override
    public void visit(DrawCircleNode node) {
        System.out.println("Drawing circle with center " + node.getCenter() + " and radius " + node.getRadius());
    }

    @Override
    public void visit(DrawDiameterNode node) {
        System.out.println("Drawing diameter " + node.getTwoLetterID());
    }

    @Override
    public void visit(DrawSegmentNode node) {
        System.out.println("Drawing segment with points " + String.join(", ", node.getTwoLetterList()));
    }

    @Override
    public void visit(DrawRadiusNode node) {
        System.out.println("Drawing radius " + node.getTwoLetterID());
    }

    @Override
    public void visit(DrawChordNode node) {
        System.out.println("Drawing chords " + String.join(", ", node.getTwoLetterList()));
    }

    @Override
    public void visit(DrawLineNode node) {
        System.out.println("Drawing line " + node.getTwoLetterID() + " tangent to point " + node.getPointID());
    }
}
