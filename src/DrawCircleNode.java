class DrawCircleNode extends ASTNode {
    private final String center;
    private final int radius;

    public DrawCircleNode(String center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public String getCenter() { return center; }
    public int getRadius() { return radius; }

    @Override
    public void accept(TaskVisitor visitor) {
        visitor.visit(this);
    }
}
