class DrawRadiusNode extends ASTNode {
    private final String twoLetterID;

    public DrawRadiusNode(String twoLetterID) {
        this.twoLetterID = twoLetterID;
    }

    public String getTwoLetterID() { return twoLetterID; }

    @Override
    public void accept(TaskVisitor visitor) {
        visitor.visit(this);
    }
}
