import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(GraphicWindow::createAndShowGUI);

        StringBuilder testConstructor = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("tests/test3.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                testConstructor.append(line).append("\n");
            }
            System.out.println("---------------TEST TASK---------------\n" + testConstructor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String task = testConstructor.toString();


        // Лексичний аналіз
        LexicalAnalyzer la = new LexicalAnalyzer();
        List<List<String>> text = la.analyze(task);

        for (List<String> sentence : text) {
            System.out.println(sentence);
        }

        // Синтаксичний аналіз


        System.out.println("Syntax analysis completed successfully.");
    }
}
