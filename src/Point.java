public class Point extends Shape{

    private int x;
    private int y;

    public Point(String id) {
        super(id);
    }

    public Point(String id, int x, int y) {
        super(id);
        this.x = x;
        this.y = y;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
