import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        StringBuilder testConstructor = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("tests/test1"))) {
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

        for (List<Pair> sentence : text) {
            System.out.println(sentence);
        }
        System.out.println();

        // Синтаксичний та семантичний аналізи
        List<Node> syntaxTree = new ArrayList<>();
        SyntaxAnalyzer sa = new SyntaxAnalyzer(text);
        sa.parse(syntaxTree);

        System.out.println(syntaxTree);

        // Виконання задач
        GraphicWindow window = GraphicWindow.createAndShowGUI();
        window.setSyntaxTree(syntaxTree);

    }
}
