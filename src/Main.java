import javax.swing.*;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        //SwingUtilities.invokeLater(GraphicWindow::createAndShowGUI);

        StringBuilder testConstructor = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("tests/test1.txt"))) {
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
        List<List<Pair>> text = la.analyze(task);
        System.out.println("dskjfsdof");

        for (List<Pair> sentence : text) {
            System.out.println(sentence);
        }

        // Синтаксичний аналіз
        SyntaxParser sa = new SyntaxParser();

        List<CommandNode> tree = new ArrayList<>();

        for (List<Pair> tokens : text){

        }

        System.out.println("Syntax analysis completed successfully.");
    }
}
