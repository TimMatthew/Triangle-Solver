import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GraphicWindow extends JPanel {

    public static final int MAX_ABSCISSA = 30;
    public static final int MAX_ORDINATE = 25;
    private static final int STEP_SIZE = 20;
    private static final int VALUE_STEP = 5;
    private static final int WINDOW_SIZE = 800;

    String[] commands;
    ArrayList<Shape> idTable;

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    public ArrayList<Shape> getIdTable() {
        return idTable;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCartesianGrid(g);
        executeCommands(g, commands);
       // System.out.println(idTable);
    }

    public void executeCommands(Graphics g, String[] commands) {
        idTable = new ArrayList<>();
        for(String command : commands){

            if(command.contains("Point")){

                String[] pointParams = extractParameters(command);
                boolean exists = false;

                for(Shape s : idTable){
                    if(s.getId().equals(pointParams[0])){
                        exists=true;
                        break;
                    }
                }
                if(!exists) operatePoint(g, pointParams[0], Integer.parseInt(pointParams[1]), Integer.parseInt(pointParams[2]));
            }

            else if (command.contains("Circle")) {

                String[] circleParams = extractParameters(command);
                Circle newCircle;
                boolean exists = false;

                for (Shape s : idTable) {
                    if (s.getId().equals(circleParams[0] + circleParams[1])) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {

                    Point potentialCenter = null;
                    boolean circleIsDrawn = false;
                    String pointCommand = null;

                    for (String c : commands) {
                        if (c.contains("Point") && c.contains(circleParams[0])) {
                            pointCommand = c;
                            break;
                        }
                    }

                    for (Shape s : idTable) {
                        if (s instanceof Circle && s.getId().equals(circleParams[0])) circleIsDrawn = true;
                        else if (s instanceof Point && s.getId().equals(circleParams[0])) potentialCenter = (Point) s;
                    }

                    if (potentialCenter == null && !circleIsDrawn) {

                        if (pointCommand != null) {
                            String[] pointParams = extractParameters(pointCommand);
                            potentialCenter = operatePoint(g, pointParams[0], Integer.parseInt(pointParams[1]), Integer.parseInt(pointParams[2]));
                            newCircle = new Circle(circleParams[0], potentialCenter, Integer.parseInt(circleParams[1]));
                            drawCircle(g, newCircle);
                            idTable.add(newCircle);
                        } else {
                            Random random = new Random();
                            int generatedX = random.nextInt(-MAX_ABSCISSA, MAX_ABSCISSA + 1);
                            int generatedY = random.nextInt(-MAX_ORDINATE, MAX_ORDINATE + 1);

                            potentialCenter = operatePoint(g, circleParams[0], generatedX, generatedY);
                            newCircle = new Circle(circleParams[0], potentialCenter, Integer.parseInt(circleParams[1]));
                            drawCircle(g, newCircle);
                            idTable.add(newCircle);
                        }
                    } else if (potentialCenter != null && !circleIsDrawn) {
                        // Scale the radius and center to fit the frame
                        int scaledRadius = scaleRadius(Integer.parseInt(circleParams[1]));
                        newCircle = new Circle(circleParams[0], potentialCenter, scaledRadius);
                        drawCircle(g, newCircle);
                        idTable.add(newCircle);
                    }
                }
            }
            else if(command.contains("Radius")){

                String[] radiusParams = extractParameters(command);
                Radius newRadius;
                boolean exists=false;

                for(Shape s : idTable){
                    if(s.getId().equals(radiusParams[0]+radiusParams[1])){
                        exists=true;
                        break;
                    }
                }

                if(!exists){
                    Point center = null, end = null;
                    Circle adjacentCircle = null;
                    boolean centerExists=false;
                    boolean endExists=false;
                    Random random;

                    String[] centerParams = null, endParams = null, circleParams = null;

                    for(String c : commands){
                        if (c.contains("Point") && c.contains(radiusParams[0])) {
                            centerParams = extractParameters(c);
                        }
                        else if(c.contains("Point") && c.contains(radiusParams[1])){
                            endParams = extractParameters(c);
                        }
                        else if(c.contains("Circle") && c.contains(radiusParams[2])){
                            circleParams = extractParameters(c);
                        }
                    }

                    for(Shape s : idTable){

                        if(s instanceof Point) {
                            if(s.getId().equals(radiusParams[0])){
                                centerExists=true;
                                center = (Point) s;
                            }
                            else if(s.getId().equals(radiusParams[1])){
                                endExists = true;
                                end = (Point) s;
                            }
                        }
                        else if (s instanceof Circle && s.getId().equals(radiusParams[0])) {
                            adjacentCircle = (Circle) s;
                        }
                    }

                    if(!centerExists){

                        if(centerParams!=null){
                            center = new Point(centerParams[0], Integer.parseInt(centerParams[1]), Integer.parseInt(centerParams[2]));
                        }
                        else{
                            random = new Random();
                            int centerX = random.nextInt(-MAX_ABSCISSA, MAX_ABSCISSA+1);
                            int centerY = random.nextInt(-MAX_ABSCISSA, MAX_ABSCISSA+1);
                            center = new Point(radiusParams[0], centerX, centerY);
                        }
                    }
                    if(!endExists){

                        if(endParams!=null) {
                            end = new Point(endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));
                        }
                        else{
                            random = new Random();
                            int endX = random.nextInt(-MAX_ABSCISSA, MAX_ABSCISSA+1);
                            int endY = random.nextInt(-MAX_ABSCISSA, MAX_ABSCISSA+1);
                            end = new Point(radiusParams[0], endX, endY);
                        }
                    }

                    if(adjacentCircle==null){

                        if(circleParams!=null){
                            adjacentCircle = new Circle(circleParams[0], center, Integer.parseInt(circleParams[1]));
                        }
                        else{
                            // Визначаю центром
                            int radius = (int) Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-center.getX(),2)+(Math.pow(end.getY()-center.getY(),2)))));
                            adjacentCircle = new Circle(center.getId(), center, radius);
                        }
                    }

                    newRadius = new Radius(radiusParams[0]+radiusParams[1], center, end, adjacentCircle);
                    if(drawRadius(g, newRadius))
                        idTable.add(newRadius);
                }
            }
            else if(command.contains("Diameter")){

                String[] diameterParams = extractParameters(command);
                Diameter newDiameter;
                boolean exists=false;
                Random random;

                for(Shape s : idTable){
                    if(s.getId().equals(diameterParams[0]+diameterParams[1])){
                        exists=true;
                        break;
                    }
                }

                if(!exists){
                    Point start = null, end = null, circleCenter=null;
                    Circle adjacentCircle = null;
                    boolean centerExists=false;

                    String[] startParams = null, endParams = null, circleParams = null, centerParams = null;

                    for(String c : commands){
                        if (c.contains("Point") && c.contains(diameterParams[0])) {
                            startParams = extractParameters(c);
                        }
                        else if(c.contains("Point") && c.contains(diameterParams[1])){
                            endParams = extractParameters(c);
                        }
                        else if(c.contains("Point") && c.contains(diameterParams[2])){
                            centerParams = extractParameters(c);
                        }
                        else if(c.contains("Circle") && c.contains(diameterParams[2])){
                            circleParams = extractParameters(c);
                        }
                    }

                    for(Shape s : idTable){

                        if(s instanceof Point) {
                            if(s.getId().equals(diameterParams[0])){
                                start = (Point) s;
                            }
                            else if(s.getId().equals(diameterParams[1])){
                                end = (Point) s;
                            }
                            else if(s.getId().equals(circleParams[0])){
                                centerExists=true;
                                circleCenter = (Point) s;
                            }
                        }
                        else if (s instanceof Circle && s.getId().equals(diameterParams[0])) {
                            adjacentCircle = (Circle) s;
                        }
                    }

                    if(start==null){

                        if(startParams!=null){
                            start = new Point(startParams[0], Integer.parseInt(startParams[1]), Integer.parseInt(startParams[2]));
                        }
                        else{
                            random = new Random();

                            int startX = random.nextInt(-MAX_ABSCISSA, MAX_ABSCISSA+1);
                            int startY = random.nextInt(-MAX_ABSCISSA, MAX_ABSCISSA+1);
                            start = new Point(diameterParams[0], startX, startY);
                        }
                        drawPoint(g, start.getX(), start.getY(), start.getId());
                        idTable.add(start);
                    }

                    if(end==null){

                        if(endParams!=null){
                            end = new Point(endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));
                        }
                        else{
                            random = new Random();
                            int endX = random.nextInt(-MAX_ABSCISSA, MAX_ABSCISSA+1);
                            int endY = random.nextInt(-MAX_ABSCISSA, MAX_ABSCISSA+1);
                            end = new Point(diameterParams[1], endX, endY);
                        }
                        drawPoint(g, end.getX(), end.getY(), end.getId());
                        idTable.add(end);
                    }

                    if (adjacentCircle==null){

                        if(circleParams!=null){

                            if(centerExists){
                                adjacentCircle = new Circle(circleParams[0], circleCenter, Integer.parseInt(circleParams[1]));
                            }
                            else{

                                if(centerParams!=null){
                                    circleCenter = new Point(centerParams[0], Integer.parseInt(centerParams[1]), Integer.parseInt(centerParams[2]));
                                    adjacentCircle = new Circle(circleParams[0], circleCenter, Integer.parseInt(circleParams[1]));
                                }
                                else{
                                    int centerX = (start.getX()+end.getX()) / 2;
                                    int centerY = (start.getY()+end.getY()) / 2;
                                    circleCenter = new Point(circleParams[0], centerX, centerY);
                                    adjacentCircle = new Circle(circleParams[0], circleCenter, Integer.parseInt(circleParams[1]));
                                }
                                drawPoint(g, circleCenter.getX(), circleCenter.getY(), circleCenter.getId());
                                idTable.add(adjacentCircle);
                            }
                        }
                        else{

                            int centerX = (start.getX()+end.getX()) / 2;
                            int centerY = (start.getY()+end.getY()) / 2;
                            int radius = (int) (Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-start.getX(),2)+(Math.pow(end.getY()-start.getY(),2)))))/2);

                            circleCenter = new Point(diameterParams[2], centerX, centerY);
                            adjacentCircle = new Circle(diameterParams[2], circleCenter, radius);
                        }
                        drawCircle(g, adjacentCircle);
                        idTable.add(adjacentCircle);
                    }

                    newDiameter = new Diameter(diameterParams[0]+diameterParams[1], start, end, adjacentCircle);
                    if(drawDiameter(g, newDiameter))
                        idTable.add(newDiameter);
                }
            }
            else if(command.contains("Chord")) {

                String[] chordParams = extractParameters(command);
                Point circleCenter = null;
                Chord newChord;
                boolean exists = false;

                for (Shape s : idTable) {
                    if (s.getId().equals(chordParams[0] + chordParams[1])) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    Point start, end;
                    Circle adjacentCircle;
                    boolean startExists = false, endExists = false, circleExists = false, centerExists=false;

                    String[] startParams = null, endParams = null, circleParams = null, centerParams;

                    for (String c : commands) {
                        if (c.contains("Point") && c.contains(chordParams[0])) {
                            startParams = extractParameters(c);

                        }
                        else if (c.contains("Point") && c.contains(chordParams[1])) {
                            endParams = extractParameters(c);

                        }
                        else if(c.contains("Point") && c.contains(chordParams[2])){
                            centerParams = extractParameters(c);

                        }
                        else if (c.contains("Circle") && c.contains(chordParams[2])) {
                            circleParams = extractParameters(c);

                        }
                    }

                    for (Shape s : idTable) {

                        if (s instanceof Point)
                        {
                            if (s.getId().equals(chordParams[0])) {
                                start = (Point) s;
                                startExists=true;
                            }
                            else if (s.getId().equals(chordParams[1])) {
                                end = (Point) s;
                                endExists = true;
                            }
                            else if (s.getId().equals(chordParams[2])) {
                                circleCenter = (Point) s;
                                centerExists=true;
                            }
                        }
                        else if (s instanceof Circle && s.getId().equals(chordParams[0])) {
                            adjacentCircle = (Circle) s;
                            circleExists = true;
                        }
                    }

                    if(startExists && endExists && circleExists && centerExists){

                        start = new Point(startParams[0], Integer.parseInt(startParams[1]), Integer.parseInt(startParams[2]));
                        end = new Point(endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));
                        adjacentCircle = new Circle(circleParams[0], circleCenter, Integer.parseInt(circleParams[1]));

                        newChord = new Chord(chordParams[0]+chordParams[1], start, end, adjacentCircle);
                        if(drawChord(g, newChord))
                            idTable.add(newChord);
                    }
                }
            }
            else if(command.contains("Segment")){
                String[] segmentParams = extractParameters(command);
                Segment newSegment;
                boolean exists = false;

                for (Shape s : idTable) {
                    if (s.getId().equals(segmentParams[0] + segmentParams[1])) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    Point start, end;
                    boolean startExists = false, endExists = false;

                    String[] startParams = null, endParams = null;

                    for (String c : commands) {
                        if (c.contains("Point")) {
                            String[] params = extractParameters(c);
                            if (Arrays.asList(params).contains(segmentParams[0])){
                                startParams = extractParameters(c);
                                startExists=true;
                            }
                            if (Arrays.asList(params).contains(segmentParams[1])){
                                endParams = extractParameters(c);
                                endExists=true;
                            }
                        }
                    }

                    for (Shape s : idTable) {

                        if (s instanceof Point)
                        {
                            if (s.getId().equals(segmentParams[0])) {
                                start = (Point) s;
                            }
                            else if (s.getId().equals(segmentParams[1])) {
                                end = (Point) s;
                            }
                        }
                    }

                    if(startExists && endExists){

                        start = new Point(startParams[0], Integer.parseInt(startParams[1]), Integer.parseInt(startParams[2]));
                        end = new Point(endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));

                        newSegment = new Segment(segmentParams[0]+segmentParams[1], start, end);
                        drawSegment(g, newSegment);
                        idTable.add(newSegment);
                    }
                }
            }
        }
    }

    private int scaleRadius(int radius) {
        int maxRadius = Math.min(WINDOW_SIZE / 2, MAX_ABSCISSA);
        return Math.min(radius, maxRadius);
    }
    private int scaleCoordinate(int coordinate, int maxCoordinate, int windowSize) {
        return (int) ((double) coordinate / maxCoordinate * windowSize);
    }

    private Point operatePoint(Graphics g, String id, int x, int y) {
        Point test = new Point(id, x, y);
        drawPoint(g, test.getX(), test.getY(), test.getId());
        idTable.add(test);
        return test;
    }

    private String[] extractParameters(String command) {
        int startIndex = command.indexOf('(') + 1;
        int endIndex = command.indexOf(')');

        String paramString = command.substring(startIndex, endIndex).replace(" ", "");

        return paramString.split(",");
    }

    private void drawPoint(Graphics g, int x, int y, String id) {
        int coordX = getWidth() / 2 + x * STEP_SIZE;
        int coordY = getHeight() / 2 - y * STEP_SIZE;
        g.setColor(Color.BLACK);
        g.fillOval(coordX - 5, coordY - 5, 10, 10);
        g.setFont(new Font("Arial Black", Font.PLAIN, 14));
        g.drawString(id, coordX + 5, coordY - 5);
    }

    private void drawCircle(Graphics g, Circle circle){

        int x = circle.getCenter().getX();
        int y = circle.getCenter().getY();
        int radius = circle.getRadius();

        int coordX = getWidth() / 2 + x * STEP_SIZE;
        int coordY = getHeight() / 2 - y * STEP_SIZE;
        int coordRadius = radius * STEP_SIZE * 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.0f));
        g.drawOval(coordX-coordRadius/2, coordY-coordRadius/2, coordRadius, coordRadius);
    }

    private void drawSegment(Graphics g, Segment segment) {


        int coordStartX = getWidth() / 2 + segment.getStart().getX() * STEP_SIZE;
        int coordStartY = getHeight() / 2 - segment.getStart().getY() * STEP_SIZE;
        int coordEndX = getWidth() / 2 + segment.getEnd().getX() * STEP_SIZE;
        int coordEndY =  getHeight() / 2 - segment.getEnd().getY() * STEP_SIZE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.0f));
        g.setColor(Color.BLUE);
        g.drawLine(coordStartX, coordStartY, coordEndX, coordEndY);
    }


    private boolean drawRadius(Graphics g, Radius r){

        int centerX = r.getCenter().getX();
        int centerY = r.getCenter().getY();
        int endX = r.getEnd().getX();
        int endY = r.getEnd().getY();
        int circleRadius = r.getAdjacentCircle().getRadius();

        double length = Math.sqrt(Math.abs(Math.pow(endX-centerX,2)+(Math.pow(endY-centerY,2))));
        double subtract = circleRadius-length;
        if(Math.abs(subtract)<0.2){

            int coordStartX = getWidth() / 2 + centerX * STEP_SIZE;
            int coordStartY = getHeight() / 2 - centerY * STEP_SIZE;
            int coordEndX = getWidth() / 2 + endX * STEP_SIZE;
            int coordEndY =  getHeight() / 2 - endY * STEP_SIZE;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2.0f));
            g.setColor(Color.BLUE);
            g.drawLine(coordStartX, coordStartY, coordEndX, coordEndY);
            return true;
        }
        else System.out.println("Точка "+r.getCenter().getId()+" або "+r.getEnd().getId()+" не належать колу");
        return false;
    }

    private boolean drawDiameter(Graphics g, Diameter diameter) {

        int startX = diameter.getStart().getX();
        int startY = diameter.getStart().getY();

        int endX = diameter.getEnd().getX();
        int endY = diameter.getEnd().getY();

        int centerX = diameter.getAdjacentCircle().getCenter().getX();
        int centerY = diameter.getAdjacentCircle().getCenter().getY();

        int radius = diameter.getAdjacentCircle().getRadius();

        int lengthStart = (int) Math.round(Math.sqrt((Math.pow(startX-centerX,2))+(Math.pow(startY-centerY,2))));
        int lengthEnd = (int) Math.round(Math.sqrt((Math.pow(endX-centerX,2))+(Math.pow(endY-centerY,2))));

        if(Math.abs(radius-lengthStart)==0 && Math.abs(radius-lengthEnd)==0){

            double coordStartX = (double) getWidth() / 2 + startX * STEP_SIZE;
            double coordStartY = (double) getHeight() / 2 - startY * STEP_SIZE;
            double coordEndX = (double) getWidth() / 2 + endX * STEP_SIZE;
            double coordEndY =  (double) getHeight() / 2 - endY * STEP_SIZE;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2.0f));
            g.setColor(Color.BLUE);
            g.drawLine((int) coordStartX, (int) coordStartY, (int) coordEndX, (int) coordEndY);
            return true;
        }
        else System.out.println("Точка "+diameter.getStart().getId()+" або "+diameter.getEnd().getId()+" не належать колу");
        return false;
    }

    private boolean drawChord(Graphics g, Chord chord) {

       Point start = chord.getStart();
       Point end = chord.getEnd();
       Circle  adjacentCircle = chord.getAdjacentCircle();
       Point center = chord.getAdjacentCircle().getCenter();

        double lengthStart = Math.sqrt(Math.abs(Math.pow(start.getX()-center.getX(),2) + (Math.pow(start.getY()-center.getY(),2))));

        double lengthEnd = Math.sqrt(Math.abs(Math.pow(end.getX()-center.getX(),2) + (Math.pow(end.getY()-center.getY(),2))));

        boolean laysOnCircleX = Math.abs(adjacentCircle.getRadius()-lengthStart)<0.2;
        boolean laysOnCircleY = Math.abs(adjacentCircle.getRadius()-lengthEnd)<0.2;

        if(laysOnCircleX && laysOnCircleY){

            int endX = chord.getEnd().getX();
            int endY = chord.getEnd().getY();

            int coordStartX = getWidth() / 2 + start.getX() * STEP_SIZE;
            int coordStartY = getHeight() / 2 - start.getY() * STEP_SIZE;
            int coordEndX = getWidth() / 2 + endX * STEP_SIZE;
            int coordEndY =  getHeight() / 2 - endY * STEP_SIZE;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2.0f));
            g.setColor(Color.BLUE);
            g.drawLine(coordStartX, coordStartY, coordEndX, coordEndY);
            return true;
        }
        else System.out.println("Одна з точка не належить колу");
        return false;
    }

    private void drawCartesianGrid(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        g.setColor(Color.LIGHT_GRAY);
        for (int i = STEP_SIZE; i < width; i += STEP_SIZE) {
            g.drawLine(i, 0, i, height);
        }
        for (int i = STEP_SIZE; i < height; i += STEP_SIZE) {
            g.drawLine(0, i, width, i);
        }

        g.setColor(Color.BLACK);
        g.drawLine(0, height / 2, width, height / 2);
        g.drawLine(width / 2, 0, width / 2, height);

        g.setColor(Color.RED);
        for (int i = 0; i < width / 2; i += 5 * STEP_SIZE) {
            int value = (i / (5 * STEP_SIZE)) * VALUE_STEP;
            g.drawString(String.valueOf(value), width / 2 + i, height / 2 + 15);
            g.drawString(String.valueOf(-value), width / 2 - i, height / 2 + 15);
        }

        for (int i = 100; i < height / 2; i += 5 * STEP_SIZE) {
            int value = (i / (5 * STEP_SIZE)) * VALUE_STEP;
            g.drawString(String.valueOf(-value), width / 2 + 5, height / 2 + i);
            g.drawString(String.valueOf(value), width / 2 + 5, height / 2 - i);
        }
    }

    public static GraphicWindow createAndShowGUI() {
        JFrame frame = new JFrame("Planimetry executor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicWindow panel = new GraphicWindow();
        panel.setPreferredSize(new Dimension(WINDOW_SIZE + 400, WINDOW_SIZE));
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
