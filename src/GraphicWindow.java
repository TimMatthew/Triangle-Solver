import javax.swing.*;
import java.awt.*;
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

    private void drawCircle(Graphics g, int x, int y, int radius, PointNode node){

        int coordX = getWidth() / 2 + x * STEP_SIZE;
        int coordY = getHeight() / 2 - y * STEP_SIZE;
        int coordRadius = radius * STEP_SIZE * 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.0f));
        g.drawOval(coordX-coordRadius/2, coordY-coordRadius/2, coordRadius, coordRadius);
    }

    private void drawChord(Graphics g, int startX, int startY, int endX, int endY){

        int coordStartX = getWidth() / 2 + startX * STEP_SIZE;
        int coordStartY = getHeight() / 2 - startY * STEP_SIZE;
        int coordEndX = getWidth() / 2 + endX * STEP_SIZE;
        int coordEndY =  getHeight() / 2 - endY * STEP_SIZE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.0f));
        g.setColor(Color.BLUE);
        g.drawLine(coordStartX, coordStartY, coordEndX, coordEndY);
    }

    private void drawSegment(Graphics g, int startX, int startY, int endX, int endY) {
        int coordStartX = getWidth() / 2 + startX * STEP_SIZE;
        int coordStartY = getHeight() / 2 - startY * STEP_SIZE;
        int coordEndX = getWidth() / 2 + endX * STEP_SIZE;
        int coordEndY =  getHeight() / 2 - endY * STEP_SIZE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.0f));
        g.setColor(Color.BLUE);
        g.drawLine(coordStartX, coordStartY, coordEndX, coordEndY);
    }


    private void drawRadius(Graphics g, int centerX, int centerY, int endX, int endY){

        int coordStartX = getWidth() / 2 + centerX * STEP_SIZE;
        int coordStartY = getHeight() / 2 - centerY * STEP_SIZE;
        int coordEndX = getWidth() / 2 + endX * STEP_SIZE;
        int coordEndY =  getHeight() / 2 - endY * STEP_SIZE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.0f));
        g.setColor(Color.BLUE);
        g.drawLine(coordStartX, coordStartY, coordEndX, coordEndY);
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
                IntNode  x = (IntNode) pn.getChildren().get(1);
                IntNode y = (IntNode) pn.getChildren().get(2);

                drawPoint(g, x.getValue(), y.getValue(), id.getValue());
            }

//            else (node instanceof CircleNode cn){
//
//            }
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
