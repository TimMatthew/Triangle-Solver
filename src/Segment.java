public class Segment extends Shape{

    private final Point start;
    private final Point end;
    private double length;
    private String measure;

    public Segment(String id, Point start, Point end) {
        super(id);
        this.start = start;
        this.end = end;
    }

    public Segment(String id, Point start, Point end, double length, String measure) {
        super(id);
        this.start = start;
        this.end = end;
        this.length = length;
        this.measure = measure;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
