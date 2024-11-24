import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphicWindow extends JPanel {
    private static final int WINDOW_HEIGHT = 800;
    private static final int WINDOW_WIDTH = 1200;

    String[] commands;
    ArrayList<Shape> idTable;
    RightIsoscelessTriangle mainShape;

    public void setMainShape(Shape mainShape) {
        this.mainShape = (RightIsoscelessTriangle) mainShape;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        idTable = new ArrayList<>();
        executeCommands(g);
    }

    public void executeCommands(Graphics g) {

        boolean triangleIsBuilt = false;
        String triangleCommand = "";
        for(String command : commands){
            if(command.contains("Triangle")) triangleCommand = command;
        }

        for(String command : commands){
            if(command.contains("Triangle")){
                drawRightIsoscelesTriangle(g, extractParameters(command));
                triangleIsBuilt = true;
            }
            else if(command.contains("Height")){
                if(!triangleIsBuilt){
                    drawRightIsoscelesTriangle(g, extractParameters(triangleCommand));
                    triangleIsBuilt = true;
                }
                drawHeight(g, extractParameters(command));
            }
            else if(command.contains("Median")){
                if(!triangleIsBuilt){
                    drawRightIsoscelesTriangle(g, extractParameters(triangleCommand));
                    triangleIsBuilt = true;
                }
                drawMedian(g, extractParameters(command));
            }
        }

    }

    private void drawHeight(Graphics g, String[] ids) {
        String heightParams = ids[0];
        String oppositeSideParams = ids[1];

        Point pointFrom = null;
        Point opposite1 = null, opposite2 = null;

        String heightPoint1ID, heightPoint2ID;
        String identifierRegex = "([A-Z]\\d*)";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher heightMatcher = pattern.matcher(heightParams);
        Matcher oppositeMatcher = pattern.matcher(oppositeSideParams);
        List<String> groupedIds = new ArrayList<>();

        while (heightMatcher.find()) {
            groupedIds.add(heightMatcher.group());
        }
        while (oppositeMatcher.find()) {
            groupedIds.add(oppositeMatcher.group());
        }
        heightPoint1ID = groupedIds.getFirst();
        heightPoint2ID = groupedIds.get(1);



        for (Shape s : idTable) {
            if (s instanceof Point p && p.getId().equals(String.valueOf(heightPoint1ID))) {
                pointFrom = p;
            } else if (s instanceof Segment sg && sg.getId().equals(oppositeSideParams)) {
                opposite1 = sg.getStart();
                opposite2 = sg.getEnd();
            }
        }

        int x3 = pointFrom.getX(), y3 = pointFrom.getY();
        int x1 = opposite1.getX(), y1 = opposite1.getY();
        int x2 = opposite2.getX(), y2 = opposite2.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;
        double dSquared = dx * dx + dy * dy;
        double t = ((x3 - x1) * dx + (y3 - y1) * dy) / dSquared;

        int perpX = (int) Math.round(x1 + t * dx);
        int perpY = (int) Math.round(y1 + t * dy);

        Point pointTo = new Point(heightPoint2ID, perpX, perpY);
        Segment newHeight = new Segment(heightPoint1ID+heightPoint2ID, pointFrom, pointTo);

        double lengthHeight = 0;
        String measureHeight = null;

        for(Segment s : mainShape.getHeights()){
            if(s.getId().equals(heightParams) && s.getSegmentType().equals("height-median-bisector")){
                lengthHeight = s.getLength();
                measureHeight = s.getMeasure();
            }
        }

        drawSegment(g, newHeight.getStart(), newHeight.getEnd(), lengthHeight, measureHeight, 50, 0);
        drawPoint(g, perpX, perpY, perpX+10,perpY, pointTo.getId());
        g.drawLine(pointTo.getX()-14, pointTo.getY()-14, pointTo.getX()-28, pointTo.getY());
        g.drawLine(pointTo.getX()-28, pointTo.getY(), pointTo.getX()-14, pointTo.getY()+14);
        idTable.add(newHeight);
    }


    private String[] extractParameters(String command) {
        int startIndex = command.indexOf('(') + 1;
        int endIndex = command.indexOf(')');

        String paramString = command.substring(startIndex, endIndex).replace(" ", "");

        return paramString.split(",");
    }

    private void drawRightIsoscelesTriangle(Graphics g, String[] pointIds){

        Point point1 = new Point(pointIds[0], 400, 180);
        Point point2 = new Point(pointIds[1], 400, 500);
        Point point3 = new Point(pointIds[2], 700, 500);

        idTable.add(point1);
        idTable.add(point2);
        idTable.add(point3);

        drawPoint(g, point1.getX(), point1.getY(), point1.getX()+5, point1.getY()-5, point1.getId());
        drawPoint(g, point2.getX(), point2.getY(), point2.getX() - 16, point2.getY() + 28, point2.getId());
        drawPoint(g, point3.getX(), point3.getY(), point3.getX() - 16, point3.getY() + 28, point3.getId());

        Segment leg1 = new Segment(point1.getId()+point2.getId(), point1, point2);
        Segment leg2 = new Segment(point2.getId()+point3.getId(), point2, point3);
        Segment hypotenuse = new Segment(point1.getId()+point3.getId(), point1, point3);

        idTable.add(leg1);
        idTable.add(leg2);
        idTable.add(hypotenuse);

        double lengthLeg1 = mainShape.getLeg1().getLength();
        String measureLeg1 = mainShape.getLeg1().getMeasure();

        double lengthLeg2 = mainShape.getLeg2().getLength();
        String measureLeg2 = mainShape.getLeg2().getMeasure();

        double lengthHypotenuse = mainShape.getHypotenuse().getLength();
        String measureHypotenuse = mainShape.getHypotenuse().getMeasure();

        drawSegment(g, point1, point2, lengthLeg1, measureLeg1, 130, 10);
        drawSegment(g, point2, point3, lengthLeg2, measureLeg2, 20, -30);
        drawSegment(g, point1, point3, lengthHypotenuse, measureHypotenuse, -20, 10);

        drawSegment(g, new Point("", point2.getX(), point2.getY()-20), new Point("", point2.getX()+20, point2.getY()-20), 0, "", 0,0);
        drawSegment(g, new Point("", point2.getX()+20, point2.getY()-20), new Point("", point2.getX()+20, point2.getY()), 0, "",0,0);

        if(!mainShape.getProjections().isEmpty()){
            double lengthProjection1 = mainShape.getProjections().getFirst().getLength();
            String measureProjection1 = mainShape.getProjections().getFirst().getMeasure();

            double lengthProjection2 = mainShape.getProjections().get(1).getLength();
            String measureProjection2 = mainShape.getProjections().get(1).getMeasure();

            drawSegment(g, new Point("", point1.getX(),point1.getY()), new Point("", point1.getX(),point1.getY()), lengthProjection1, measureProjection1, -70, -70);
            drawSegment(g, new Point("", point3.getX(),point3.getY()), new Point("", point3.getX(),point3.getY()), lengthProjection2, measureProjection2, 60, 70);
        }
    }

    private void drawPoint(Graphics g, int x, int y, int offsetX, int offsetY, String id) {
        g.setColor(Color.BLACK);
        g.fillOval(x - 8, y - 8, 16, 16);
        g.setFont(new Font("Arial Black", Font.PLAIN, 24));
        g.drawString(id, offsetX, offsetY);
    }

    private void drawSegment(Graphics g, Point start, Point end, double length, String measure, int lengthOffsetX, int lengthOffsetY, int measureOffsetX, int measureOffsetY) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3.4f));
        g.setColor(Color.BLACK);
        g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());

        int midX = (start.getX() + end.getX()) / 2;
        int midY = (start.getY() + end.getY()) / 2;

        if (length != 0) {
            g.drawString(String.format("%.2f", length), midX-measureOffsetX, midY - measureOffsetY);

            if (measure != null && !measure.isEmpty()) {
                g.drawString(measure, midX-measureOffsetX+80, midY - measureOffsetY);
            }
        }
    }


    private void drawMedian(Graphics g, String[] ids) {
        String medianParams = ids[0];
        String oppositeParams = ids[1];

        Point vertex = null;
        Point opposite1 = null, opposite2 = null;

        String vertexID, midpointID;
        String identifierRegex = "([A-Z]\\d*)";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher medianMatcher = pattern.matcher(medianParams);
        Matcher oppositeMatcher = pattern.matcher(oppositeParams);
        List<String> groupedIds = new ArrayList<>();

        while (medianMatcher.find()) {
            groupedIds.add(medianMatcher.group());
        }
        while (oppositeMatcher.find()) {
            groupedIds.add(oppositeMatcher.group());
        }
        vertexID = groupedIds.get(0);
        midpointID = groupedIds.get(1);

        for (Shape s : idTable) {
            if (s instanceof Point p && p.getId().equals(vertexID)) {
                vertex = p;
            } else if (s instanceof Segment sg && sg.getId().equals(oppositeParams)) {
                opposite1 = sg.getStart();
                opposite2 = sg.getEnd();
            }
        }

        if (vertex == null || opposite1 == null || opposite2 == null) {
            System.out.println("Error: Missing points or segment for median calculation.");
            return;
        }

        int x1 = opposite1.getX(), y1 = opposite1.getY();
        int x2 = opposite2.getX(), y2 = opposite2.getY();
        int midpointX = (x1 + x2) / 2;
        int midpointY = (y1 + y2) / 2;

        Point midpoint = new Point(midpointID, midpointX, midpointY);

        Segment median = new Segment(vertexID + midpointID, vertex, midpoint);

        double lengthMedian = 0;
        String measureMedian = null;

        for(Segment s : mainShape.getHeights()){
            if(s.getId().equals(medianParams) && s.getSegmentType().equals("median")){
                lengthMedian = s.getLength();
                measureMedian = s.getMeasure();
            }
        }

        drawSegment(g, median.getStart(), median.getEnd(), lengthMedian, measureMedian, 20, 20);
        drawPoint(g, midpointX, midpointY, midpointX + 10, midpointY, midpoint.getId());
        idTable.add(median);
    }

    private Point getPointById(String id) {
        for (Shape shape : idTable) {
            if (shape instanceof Point && ((Point) shape).getId().equals(id)) {
                return (Point) shape;
            }
        }
        return null;
    }

    public static GraphicWindow createAndShowGUI() {
        JFrame frame = new JFrame("Planimetry executor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicWindow panel = new GraphicWindow();
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphicWindow::createAndShowGUI);
    }
}
