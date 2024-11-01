import javax.swing.*;
import java.awt.*;

public class GraphicWindow extends JPanel {

    private static final int STEP_SIZE = 20;  
    private static final int VALUE_STEP = 10;  
    private static final int WINDOW_SIZE = 800;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCartesianGrid(g);
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
            int value = (i / (5 * STEP_SIZE)) * VALUE_STEP / 2;
            g.drawString(String.valueOf(value), width / 2 + i, height / 2 + 15);
            g.drawString(String.valueOf(-value), width / 2 - i, height / 2 + 15);  
        }
         
        for (int i = 100; i < height / 2; i += 5 * STEP_SIZE) {   
            int value = (i / (5 * STEP_SIZE)) * VALUE_STEP / 2;
            g.drawString(String.valueOf(-value), width / 2 + 5, height / 2 + i);   
            g.drawString(String.valueOf(value), width / 2 + 5, height / 2 - i);   
        }
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Cartesian Coordinate System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicWindow panel = new GraphicWindow();
        panel.setPreferredSize(new Dimension(WINDOW_SIZE + 400, WINDOW_SIZE));
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphicWindow::createAndShowGUI);
    }
}
