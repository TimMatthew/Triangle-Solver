import java.util.ArrayList;

public class RightIsoscelessTriangle extends Shape{

    private Segment leg1;
    private Segment leg2;
    private Segment hypotenuse;
    private double square;
    private double perimeter;
    ArrayList<Segment> heights;
    public RightIsoscelessTriangle(String id, Segment leg1, Segment leg2, Segment hypotenuse) {
        super(id);
        this.leg1 = leg1;
        this.leg2 = leg2;
        this.hypotenuse = hypotenuse;
        this.heights = new ArrayList<>();
    }

    public RightIsoscelessTriangle(String id) {
        super(id);
    }

    public Segment getLeg1() {
        return leg1;
    }

    public void setLeg1(Segment leg1) {
        this.leg1 = leg1;
    }

    public Segment getLeg2() {
        return leg2;
    }

    public void setLeg2(Segment leg2) {
        this.leg2 = leg2;
    }

    public Segment getHypotenuse() {
        return hypotenuse;
    }

    public void setHypotenuse(Segment hypotenuse) {
        this.hypotenuse = hypotenuse;
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }
}
