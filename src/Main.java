import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        StringBuilder testConstructor = new StringBuilder();
        int i=2;
        try (BufferedReader br = new BufferedReader(new FileReader("tests/test"+i+".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                testConstructor.append(line).append("\n");
            }
            System.out.println("---------------TEST TASK---------------\n" + testConstructor);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        String task = testConstructor.toString();

        // Лексичний аналіз
        LexicalAnalyzer la = new LexicalAnalyzer();
        List<Pair> tokens = la.analyze(task);

        for (Pair pair : tokens) {
            System.out.println(pair);
        }

        // Синтаксичний та семантичний аналіз
        SyntaxAnalyzer sa = new SyntaxAnalyzer(tokens);
        AST ast = sa.parse();

        System.out.println(ast);


        // Запис дерева у команди
        ASTRecorder recorder = new ASTRecorder(ast);
        String geomCommands = recorder.recordAST();

        recorder.saveToFile("commands\\commands"+i+".txt", geomCommands);

        // Виконання задач
        GraphicWindow window = GraphicWindow.createAndShowGUI();
        window.setCommands(geomCommands.split("\n"));


    }


}
