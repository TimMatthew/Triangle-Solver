import javax.swing.*;
import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(GraphicWindow::createAndShowGUI);


        String testTask = readTestFile("tests/test2.txt");


        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        List<List<String>> sentences = lexicalAnalyzer.splitIntoSentences(testTask);

        for (List<String> sentence : sentences){
            System.out.println(sentence);
        }
        System.out.println("\n");
        Parser parser = new Parser();
        TaskExecutor executor = new TaskExecutor();


        for (List<String> sentence : sentences) {
            try {
                ASTNode taskNode = parser.parseSentence(sentence);
                if (taskNode != null) {
                    taskNode.accept(executor);
                } else {
                    System.out.println("Unrecognized sentence structure: " + sentence);
                }
            } catch (Exception e) {
                System.out.println("Error parsing sentence: " + sentence);
                e.printStackTrace();
            }
        }

        System.out.println("Syntax analysis completed successfully.");
    }

     
    private static String readTestFile(String filePath) {
        StringBuilder testConstructor = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                testConstructor.append(line).append("\n");
            }
            System.out.println("---------------TEST TASK---------------\n" + testConstructor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return testConstructor.toString();
    }
}
