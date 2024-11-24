import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.net.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Введіть номер задачі: ");
        short taskNumber = Short.parseShort(sc.nextLine());

        StringBuilder testConstructor = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("tests/tasks.txt"))) {
            String line;
            while ((line = br.readLine()) != null)
                testConstructor.append(line).append("\n");

            System.out.println("---------------TEST TASK---------------");

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Отримання задачі
        String[] tasks = testConstructor.toString().split("\n");
        String task;

        if(taskNumber>=0 && taskNumber<tasks.length) task=tasks[taskNumber];
        else task = tasks[0];
        System.out.println(task+"\n");
        ArrayList<String> numericValues;
        task = Preprocessing.preprocessLiterals(task);
        HashMap<String, ArrayList<String>> numericProcess = Preprocessing.preprocessNumericValues(task, new ArrayList<>());
        String[] ConLLUSplitArray=null;

        //if (!numericProcess.isEmpty()) {

            String updatedTask = numericProcess.keySet().iterator().next();
            task = updatedTask;
            numericValues = numericProcess.get(updatedTask);
        //}


        //System.out.println("-------------UDPipe ANALYSIS-------------");
        try {
            String urlString = "https://lindat.mff.cuni.cz/services/udpipe/api/process";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            String model = "ukrainian-iu-ud-2.12-230717";
            String postData = "tokenizer&tagger&parser" +
                    "&model=" + URLEncoder.encode(model, StandardCharsets.UTF_8) +
                    "&data=" + URLEncoder.encode(task, StandardCharsets.UTF_8);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
                ConLLUSplitArray = Preprocessing.getConLLUInfo(connection);

            else System.err.println("Error: HTTP response code " + responseCode);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

//        for(String s: ConLLUSplitArray){
//            System.out.println(s);
//        }


        ConLLUAnalyzer conLLUAnalyzer = new ConLLUAnalyzer(Objects.requireNonNull(ConLLUSplitArray), numericValues);

        ArrayList<String> taskConditions = conLLUAnalyzer.analyze();

        DrawCommander dc = new DrawCommander(taskConditions);
        Shape mainShape = dc.makeCommandsAndObjects();

        ArrayList<String> graphicsDescription = dc.getGraphicsDescription();

        StringBuilder textForLexer = new StringBuilder();
        for(int i=0; i<graphicsDescription.size(); i++){
            if(i==graphicsDescription.size()-1){
                String lastSentence = graphicsDescription.get(i).replace(".", "");
                textForLexer.append(lastSentence).append("#");
            }
            else textForLexer.append(graphicsDescription.get(i)).append("\n");
        }

        //System.out.println(textForLexer);


        // Лексичний аналіз
        LexicalAnalyzer la = new LexicalAnalyzer();
        List<Pair> tokens = la.analyze(textForLexer.toString());

//        for (Pair pair : tokens) {
//            System.out.println(pair);
//        }


        // Синтаксичний та семантичний аналіз
        SyntaxAnalyzer sa = new SyntaxAnalyzer(tokens);
        AST ast = sa.parse();

        //System.out.println(ast);

        // Запис дерева у команди
        ASTRecorder recorder = new ASTRecorder(ast);
        String geomCommands = recorder.recordAST();

        recorder.saveToFile("commands\\commands"+taskNumber+".txt", geomCommands);

        // Виконання задач
        GraphicWindow window = GraphicWindow.createAndShowGUI();
        window.setCommands(geomCommands.split("\n"));
        window.setMainShape(mainShape);

        // Розв'язування
        String target = dc.getTarget();
        Solver solver = new Solver(mainShape, target);
        ArrayList<String> providedSolution = solver.solve();
        int i = 1;
        for(String s : providedSolution){

            System.out.println("------ДІЯ "+i+"------");
            System.out.println(s);
            i++;
        }

    }



}
