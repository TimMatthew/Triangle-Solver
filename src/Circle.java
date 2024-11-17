public class Circle extends Shape{



    private Point center;

    private int radius;

    public Circle(String id, Point center) {
        super(id);
        this.center = center;
    }

    public Circle(String id, Point center, int radius) {
        super(id);
        this.center = center;
        this.radius = radius;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
