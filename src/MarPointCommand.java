class MarPointCommand extends CommandNode{
    private final String pointID;
    private final int x;
    private final int y;

    public MarPointCommand(String pointID, int x, int y) {
        this.pointID = pointID;
        this.x = x;
        this.y = y;
    }

    public String getPointID() { return pointID; }
    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public void execute() {
        System.out.println("Point");
    }
}
