class DrawLineNode extends ASTNode {
    private final String twoLetterID;
    private final String pointID;

    public DrawLineNode(String twoLetterID, String pointID) {
        this.twoLetterID = twoLetterID;
        this.pointID = pointID;
    }

    public String getTwoLetterID() { return twoLetterID; }
    public String getPointID() { return pointID; }

    @Override
    public void accept(TaskVisitor visitor) {
        visitor.visit(this);
    }
}
