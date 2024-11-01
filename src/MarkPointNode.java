class MarkPointNode extends ASTNode {
    private final String pointID;
    private final int x;
    private final int y;

    public MarkPointNode(String pointID, int x, int y) {
        this.pointID = pointID;
        this.x = x;
        this.y = y;
    }

    public String getPointID() { return pointID; }
    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public void accept(TaskVisitor visitor) {
        visitor.visit(this);
    }
}
