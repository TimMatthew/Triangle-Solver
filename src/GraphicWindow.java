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

                    boolean matched=false;

                    if(center!=null && end!=null && adjacentCircle!=null){
                        newRadius = new Radius(center.getId()+end.getId(), center, end, adjacentCircle);
                        if(drawRadius(g, newRadius))
                            idTable.add(newRadius);
                    }

                    else if(center==null && end==null && adjacentCircle==null){

                        // Створюємо центр, кінець
                        // Вираховуємо радіус
                        // Створюємо коло
                        // Створюємо радіус

                        if(centerParams!=null && endParams!=null && circleParams!=null){
                            // Беремо параметри з команд

                                center = operatePoint(g, centerParams[0], Integer.parseInt(centerParams[1]), Integer.parseInt(centerParams[2]));
                                end = operatePoint(g, endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));
                                adjacentCircle = new Circle(circleParams[0], center, Integer.parseInt(circleParams[1]));
                                idTable.add(adjacentCircle);


                                drawPoint(g, center.getX(), center.getY(), center.getId());
                                drawPoint(g, end.getX(), end.getY(), end.getId());
                                drawCircle(g, adjacentCircle);

                                newRadius = new Radius(center.getId()+end.getId(), center, end, adjacentCircle);
                                if(drawRadius(g, newRadius))
                                    idTable.add(newRadius);
                        }

                    }

                    // Якщо коло намальоване
                    else if(adjacentCircle!=null){

                        if(end == null){
                            if (endParams!=null){
                                // Установити параметри
                                // Перевірити кінець на належність кола

                                end = operatePoint(g, endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));

                                newRadius = new Radius(adjacentCircle.getCenter().getId()+end.getId(), adjacentCircle.getCenter(), end, adjacentCircle);
                                if(drawRadius(g, newRadius))
                                    idTable.add(newRadius);
                            }
                            else{
                                // Треба генерити
                            }
                        }
                        else{
                            newRadius = new Radius(adjacentCircle.getId()+end.getId(), adjacentCircle.getCenter(), end, adjacentCircle);
                            if(drawRadius(g, newRadius))
                                idTable.add(newRadius);

                        }
                    }

                    // Якщо кола нема
                    // Центральна точка є, кінця нема
                    else if(center!=null && end==null){


                        if(endParams!=null){
                            // Встановити параметри
                            end = operatePoint(g, endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));
                            if(circleParams!=null) adjacentCircle = new Circle(circleParams[0], center, Integer.parseInt(circleParams[1]));
                            else{
                                int radius = (int) Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-center.getX(),2)+(Math.pow(end.getY()-center.getY(),2)))));
                                adjacentCircle = new Circle(center.getId(), center, radius);
                            }
                            drawCircle(g, adjacentCircle);
                            idTable.add(adjacentCircle);

                            newRadius = new Radius(center.getId()+end.getId(), center, end, adjacentCircle);
                            if(drawRadius(g, newRadius))
                                idTable.add(newRadius);

                        }
                        else{
                            // Треба генерити

                        }
                    }
                    // Кінець є, центру нема
                    else if(end!=null && center==null){

                        if(centerParams!=null){

                            // Встановити параметри
                            center = operatePoint(g, centerParams[0], Integer.parseInt(centerParams[1]), Integer.parseInt(centerParams[2]));

                            if(circleParams!=null) adjacentCircle = new Circle(circleParams[0], center, Integer.parseInt(circleParams[1]));
                            else{
                                int radius = (int) Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-center.getX(),2)+(Math.pow(end.getY()-center.getY(),2)))));
                                adjacentCircle = new Circle(center.getId(), center, radius);
                            }
                            drawCircle(g, adjacentCircle);
                            idTable.add(adjacentCircle);

                            newRadius = new Radius(center.getId()+end.getId(), center, end, adjacentCircle);
                            if(drawRadius(g, newRadius))
                                idTable.add(newRadius);
                        }
                        else{

                        }
                    }
                    else if(center != null && end != null){

                        if(circleParams!=null) adjacentCircle = new Circle(circleParams[0], center, Integer.parseInt(circleParams[1]));
                        else{
                            int radius = (int) Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-center.getX(),2)+(Math.pow(end.getY()-center.getY(),2)))));
                            adjacentCircle = new Circle(center.getId(), center, radius);
                        }
                        drawCircle(g, adjacentCircle);
                        idTable.add(adjacentCircle);

                        newRadius = new Radius(center.getId()+end.getId(), center, end, adjacentCircle);
                        if(drawRadius(g, newRadius))
                            idTable.add(newRadius);
                    }
                }
            }
            else if(command.contains("Diameter")){

                String[] diameterParams = extractParameters(command);
                Diameter newDiameter;
                boolean exists=false;

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

                    String[] startParams = null, endParams = null, circleParams = null;

                    for(String c : commands){
                        if (c.contains("Point") && c.contains(diameterParams[0])) {
                            startParams = extractParameters(c);
                        }
                        else if(c.contains("Point") && c.contains(diameterParams[1])){
                            endParams = extractParameters(c);
                        }
                        else if(c.contains("Point") && c.contains(diameterParams[2])){
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

                    if(start!=null && end!=null && adjacentCircle!=null){
                        newDiameter = new Diameter(start.getId()+end.getId(), start, end, adjacentCircle);
                        if(drawDiameter(g, newDiameter))
                            idTable.add(newDiameter);
                    }

                    else if(start==null && end==null && adjacentCircle==null){

                        // Створюємо центр, кінець
                        // Вираховуємо радіус
                        // Створюємо коло
                        // Створюємо радіус

                        if(startParams!=null && endParams!=null && circleParams!=null){
                            // Беремо параметри з команд

                            start = operatePoint(g, startParams[0], Integer.parseInt(startParams[1]), Integer.parseInt(startParams[2]));
                            end = operatePoint(g, endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));

                            int centerX = (start.getX()+end.getX())/2;
                            int centerY = (start.getY()+end.getY())/2;

                            circleCenter = operatePoint(g, diameterParams[2], centerX, centerY);
                            int radius = (int) Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-start.getX(),2)+(Math.pow(end.getY()-start.getY(),2)))))/2;
                            adjacentCircle = new Circle(circleCenter.getId(), circleCenter, radius);
                            idTable.add(adjacentCircle);


                            drawPoint(g, start.getX(), start.getY(), start.getId());
                            drawPoint(g, end.getX(), end.getY(), end.getId());
                            drawCircle(g, adjacentCircle);

                            newDiameter = new Diameter(start.getId()+end.getId(), start, end, adjacentCircle);
                            if(drawDiameter(g, newDiameter))
                                idTable.add(newDiameter);
                        }
                        else if(startParams==null) System.out.println("Точка "+diameterParams[0]+" не має координат");
                        else if(endParams==null) System.out.println("Точка "+diameterParams[1]+" не має координат");
                        else if(circleParams==null){
                            // Побудувати коло за центром вектора початку й кінця
                            // Радіус буде довжина між початком/кінцем та центром
                            // Тоді діаметр буде можливий
                        }
                    }

                    // Якщо коло намальоване
                    else if(adjacentCircle!=null){

                        if(start!=null){

                            start = operatePoint(g, startParams[0], Integer.parseInt(startParams[1]), Integer.parseInt(startParams[2]));

                            int endX, endY;
                            endX = 2 * adjacentCircle.getCenter().getX() - start.getX();
                            endY = 2 * adjacentCircle.getCenter().getY()- start.getY();
                            end = operatePoint(g, diameterParams[0], endX, endY);

                            newDiameter = new Diameter(start.getId()+end.getId(), start, end, adjacentCircle);
                            if(drawDiameter(g, newDiameter))
                                idTable.add(newDiameter);

                        }
                        else if(end != null){

                            end = operatePoint(g, endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));

                            int startX = 2 * adjacentCircle.getCenter().getX()- end.getX();
                            int startY = 2 * adjacentCircle.getCenter().getY()- end.getY();
                            start = operatePoint(g, diameterParams[0], startX, startY);

                            newDiameter = new Diameter(start.getId()+end.getId(), start, end, adjacentCircle);
                            if(drawDiameter(g, newDiameter))
                                idTable.add(newDiameter);
                        }
                        else {
                            System.out.println("Точка "+diameterParams[0]+" та "+ diameterParams[1]+" не мають координат");
                        }
                    }

                // Якщо кола нема
                    // Початок є, кінця нема
                    else if(start!=null && end==null){

                        if(endParams!=null){
                            // Встановити параметри
                            end = operatePoint(g, endParams[0], Integer.parseInt(endParams[1]), Integer.parseInt(endParams[2]));

                            int centerX = (start.getX()+end.getX())/2;
                            int centerY = (start.getY()+end.getY())/2;

                            circleCenter = operatePoint(g, diameterParams[2], centerX, centerY);
                            int radius = (int) Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-start.getX(),2)+(Math.pow(end.getY()-start.getY(),2)))))/2;
                            adjacentCircle = new Circle(circleCenter.getId(), circleCenter, radius);
                            drawCircle(g, adjacentCircle);
                            idTable.add(adjacentCircle);

                            newDiameter = new Diameter(start.getId()+end.getId(), start, end, adjacentCircle);
                            if(drawDiameter(g, newDiameter))
                                idTable.add(newDiameter);

                        }
                        else{
                            System.out.println("Точка "+diameterParams[1]+" не має координат");
                        }
                    }
                    // Кінець є, початку нема
                    else if(start == null){

                        if(startParams!=null){

                            // Встановити параметри
                            start = operatePoint(g, startParams[0], Integer.parseInt(startParams[1]), Integer.parseInt(startParams[2]));

                            int centerX = (start.getX()+end.getX())/2;
                            int centerY = (start.getY()+end.getY())/2;

                            circleCenter = operatePoint(g, diameterParams[2], centerX, centerY);
                            int radius = (int) Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-start.getX(),2)+(Math.pow(end.getY()-start.getY(),2)))))/2;
                            adjacentCircle = new Circle(start.getId(), circleCenter, radius);
                            drawCircle(g, adjacentCircle);
                            idTable.add(adjacentCircle);

                            newDiameter = new Diameter(start.getId()+end.getId(), start, end, adjacentCircle);
                            if(drawDiameter(g, newDiameter))
                                idTable.add(newDiameter);
                        }
                        else System.out.println("Точка "+diameterParams[0]+" не має координат");

                    }
                    else {

                        if(centerExists){

                            int radius = (int) Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-start.getX(),2)+(Math.pow(end.getY()-start.getY(),2)))))/2;
                            adjacentCircle = new Circle(diameterParams[2], circleCenter, radius);
                            drawCircle(g, adjacentCircle);
                            idTable.add(adjacentCircle);

                            newDiameter = new Diameter(start.getId()+end.getId(), start, end, adjacentCircle);
                            if(drawDiameter(g, newDiameter))
                                idTable.add(newDiameter);
                        }
                        else{

                            int radius = (int) Math.round(Math.sqrt(Math.abs(Math.pow(end.getX()-start.getX(),2)+(Math.pow(end.getY()-start.getY(),2)))))/2;
                            int centerX = (start.getX()+end.getX())/2;
                            int centerY = (start.getY()+end.getY())/2;

                            circleCenter = operatePoint(g, diameterParams[2], centerX, centerY);
                            adjacentCircle = new Circle(diameterParams[2], circleCenter, radius);
                            drawCircle(g, adjacentCircle);
                            idTable.add(adjacentCircle);

                            newDiameter = new Diameter(start.getId()+end.getId(), start, end, adjacentCircle);
                            if(drawDiameter(g, newDiameter))
                                idTable.add(newDiameter);
                        }
                    }
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
                    boolean startExists = false, endExists = false, circleExists = false;

                    String[] startParams = null, endParams = null, circleParams = null, centerParams;

                    for (String c : commands) {
                        if (c.contains("Point") && c.contains(chordParams[0])) {
                            startParams = extractParameters(c);
                            startExists=true;
                        }
                        else if (c.contains("Point") && c.contains(chordParams[1])) {
                            endParams = extractParameters(c);
                            endExists = true;
                        }
                        else if(c.contains("Point") && c.contains(chordParams[2])){
                            centerParams = extractParameters(c);
                        }
                        else if (c.contains("Circle") && c.contains(chordParams[2])) {
                            circleParams = extractParameters(c);
                            circleExists = true;
                        }
                    }

                    for (Shape s : idTable) {

                        if (s instanceof Point)
                        {
                            if (s.getId().equals(chordParams[0])) {
                                start = (Point) s;
                            }
                            else if (s.getId().equals(chordParams[1])) {
                                end = (Point) s;
                            }
                            else if (s.getId().equals(chordParams[2])) {
                                circleCenter = (Point) s;
                            }
                        }
                        else if (s instanceof Circle && s.getId().equals(chordParams[0])) {
                            adjacentCircle = (Circle) s;
                        }
                    }

                    if(startExists && endExists && circleExists){

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
