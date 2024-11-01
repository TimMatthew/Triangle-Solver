class DrawDiameterNode extends ASTNode {
    private final String twoLetterID;


    public DrawDiameterNode(String twoLetterID) {
        this.twoLetterID = twoLetterID;
    }

    public String getTwoLetterID() { return twoLetterID; }

    @Override
    public void accept(TaskVisitor visitor) {
        visitor.visit(this);
    }
}
