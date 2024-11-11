import javax.swing.*;
import java.awt.*;
import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

public class GraphicWindow extends JPanel {

    private static final int STEP_SIZE = 20;
    private static final int VALUE_STEP = 5;
    private static final int WINDOW_SIZE = 800;

    private List<Node> syntaxTree;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCartesianGrid(g);
        if (syntaxTree != null) {
            executeCommands(g);
        }
    }

    public void setSyntaxTree(List<Node> syntaxTree) {
        this.syntaxTree = syntaxTree;
    }


    private void drawPoint(Graphics g, int x, int y, String id) {
        int coordX = getWidth() / 2 + x * STEP_SIZE;
        int coordY = getHeight() / 2 - y * STEP_SIZE;
        g.setColor(Color.BLACK);
        g.fillOval(coordX - 5, coordY - 5, 10, 10);
        g.setFont(new Font("Arial Black", Font.PLAIN, 14));
        g.drawString(id, coordX + 5, coordY - 5);
    }

    private void drawCircle(Graphics g, int x, int y, int radius){

        int coordX = getWidth() / 2 + x * STEP_SIZE;
        int coordY = getHeight() / 2 - y * STEP_SIZE;
        int coordRadius = radius * STEP_SIZE * 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.0f));
        g.drawOval(coordX-coordRadius/2, coordY-coordRadius/2, coordRadius, coordRadius);
    }

    private void drawSegment(Graphics g, SegmentNode segmentNode) {
        int coordStartX = getWidth() / 2 + segmentNode.getStart().getX().getValue() * STEP_SIZE;
        int coordStartY = getHeight() / 2 - segmentNode.getStart().getY().getValue() * STEP_SIZE;
        int coordEndX = getWidth() / 2 + segmentNode.getEnd().getX().getValue() * STEP_SIZE;
        int coordEndY =  getHeight() / 2 - segmentNode.getEnd().getY().getValue() * STEP_SIZE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.0f));
        g.setColor(Color.BLUE);
        g.drawLine(coordStartX, coordStartY, coordEndX, coordEndY);
    }


    private void drawRadius(Graphics g, int centerX, int centerY, int endX, int endY, String endId, int circleRadius){

        double length = Math.round(Math.sqrt(Math.abs(Math.pow(endX-centerX,2)+(Math.pow(endY-centerY,2)))));
        double subtract = circleRadius-length;
        if(Math.abs(subtract)<0.5){

            int coordStartX = getWidth() / 2 + centerX * STEP_SIZE;
            int coordStartY = getHeight() / 2 - centerY * STEP_SIZE;
            int coordEndX = getWidth() / 2 + endX * STEP_SIZE;
            int coordEndY =  getHeight() / 2 - endY * STEP_SIZE;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2.0f));
            g.setColor(Color.BLUE);
            g.drawLine(coordStartX, coordStartY, coordEndX, coordEndY);
        }
        else throw new RuntimeException("Точка не належить колу!");
    }

    private void drawDiameter(Graphics g, int startX, int startY, CircleNode adjacentCircle, String endId) {

        IntNode radius = (IntNode) adjacentCircle.getChildren().getFirst();
        PointNode center = (PointNode) adjacentCircle.getChildren().get(1);

        IntNode centerX = center.getX();
        IntNode centerY = center.getY();


        int length = (int) Math.round(Math.sqrt((Math.pow(startX-centerX.getValue(),2))+(Math.pow(startY-centerY.getValue(),2))));

        if(Math.abs(radius.getValue()-length)<0.5){

            int endX = 2 * centerX.getValue() - startX;
            int endY = 2 * centerY.getValue() - startY;

            double coordStartX = (double) getWidth() / 2 + startX * STEP_SIZE;
            double coordStartY = (double) getHeight() / 2 - startY * STEP_SIZE;
            double coordEndX = (double) getWidth() / 2 + endX * STEP_SIZE;
            double coordEndY =  (double) getHeight() / 2 - endY * STEP_SIZE;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2.0f));
            g.setColor(Color.BLUE);
            g.drawLine((int) coordStartX, (int) coordStartY, (int) coordEndX, (int) coordEndY);
        }
    }

    private void drawChord(Graphics g, ChordNode chordNode) {

        PointNode start = chordNode.getStart();
        PointNode end = chordNode.getEnd();
        CircleNode adjacentCircle = chordNode.getAdjacentCircle();
        PointNode center = chordNode.getAdjacentCircle().getCenter();


        double lengthStart = Math.round(Math.sqrt(Math.abs(Math.pow(start.getX().getValue()-center.getX().getValue(),2) + (Math.pow(start.getY().getValue()-center.getY().getValue(),2)))));

        double lengthEnd = Math.round(Math.sqrt(Math.abs(Math.pow(end.getX().getValue()-center.getX().getValue(),2) + (Math.pow(end.getY().getValue()-center.getY().getValue(),2)))));

        boolean laysOnCircleX = Math.abs(adjacentCircle.getRadius().getValue()-lengthStart)<0.5;
        boolean laysOnCircleY = Math.abs(adjacentCircle.getRadius().getValue()-lengthEnd)<0.5;

        if(laysOnCircleX && laysOnCircleY){

            int endX = chordNode.getEnd().getX().getValue();
            int endY = chordNode.getEnd().getY().getValue();

            int coordStartX = getWidth() / 2 + start.getX().getValue() * STEP_SIZE;
            int coordStartY = getHeight() / 2 - start.getY().getValue() * STEP_SIZE;
            int coordEndX = getWidth() / 2 + endX * STEP_SIZE;
            int coordEndY =  getHeight() / 2 - endY * STEP_SIZE;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2.0f));
            g.setColor(Color.BLUE);
            g.drawLine(coordStartX, coordStartY, coordEndX, coordEndY);
        }
        else System.out.println("Одна з точка не належить колу");
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

    public void executeCommands(Graphics g) {

        for (Node node : syntaxTree) {

            if(node instanceof PointNode pn){

                IDNode id = (IDNode) pn.getChildren().get(0);
                IntNode x = (IntNode) pn.getChildren().get(1);
                IntNode y = (IntNode) pn.getChildren().get(2);

                drawPoint(g, x.getValue(), y.getValue(), id.getValue());
            }

            else if(node instanceof CircleNode cn){

                IntNode radius = (IntNode) cn.getChildren().get(0);
                PointNode centerPoint = (PointNode) cn.getChildren().get(1);
                IntNode x = (IntNode) centerPoint.getChildren().get(1);
                IntNode y = (IntNode) centerPoint.getChildren().get(2);

                drawCircle(g, x.getValue(), y.getValue(), radius.getValue());
            }

            else if(node instanceof RadiusNode rn){

                PointNode endPoint = (PointNode) rn.getChildren().get(0);
                CircleNode adjacentCircle = (CircleNode) rn.getChildren().get(1);
                IntNode radius = (IntNode) adjacentCircle.getChildren().getFirst();
                PointNode centerPoint = adjacentCircle.getCenter();

                IntNode endX = (IntNode) endPoint.getChildren().get(1);
                IntNode endY = (IntNode) endPoint.getChildren().get(2);
                IDNode endId = (IDNode) endPoint.getChildren().get(0);

                IntNode centerX = (IntNode) centerPoint.getChildren().get(1);
                IntNode centerY = (IntNode) centerPoint.getChildren().get(2);

                drawRadius(g, centerX.getValue(), centerY.getValue(), endX.getValue(), endY.getValue(), endId.getValue(), radius.getValue());
            }

            else if(node instanceof DiameterNode dn){

                CircleNode adjacentCircle = dn.getAdjacentCircle();
                PointNode startPoint = dn.getStart();
                PointNode endPoint = dn.getEnd();

                IDNode endID = endPoint.getIdNode();

                IntNode startX = startPoint.getX();
                IntNode startY = startPoint.getY();

                drawDiameter(g, startX.getValue(), startY.getValue(), adjacentCircle, endID.getValue());
            }
            else if(node instanceof ChordNode cn) drawChord(g, cn);
            else if(node instanceof SegmentNode sn) drawSegment(g, sn);
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
