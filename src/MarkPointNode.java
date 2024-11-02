class MarkPointNode implements CommandNode{
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


    // Після синтаксичного розбору створення малюнку
    @Override
    public String getId() {
        return pointID;
    }
}
