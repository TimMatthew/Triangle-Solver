public class Segment extends Shape{

    private Point start;
    private Point end;
    private double length;
    private String measure;
    private String segmentType;

    public Segment(String id, String type) {
        super(id);
        segmentType = type;
    }

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

    public String getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
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
