import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Preprocessing {

    public static String preprocessLiterals(String text){
        return text.replace(". ", "; ")
                .replaceAll("∠", "кут ")
                .replaceAll("°", " градусів")
                .replaceAll("=", " дорівнює ")
                .replaceAll("-", " дорівнює ")
                .replaceAll("\\bА\\b", "A")
                .replaceAll("\\bВ\\b", "B")
                .replaceAll("\\bС\\b", "C")
                .replaceAll("\\bХ\\b", "X")
                .replaceAll("\\bІ\\b", "I")
                .replaceAll("\\bМ\\b", "M")
                .replaceAll("\\bЕ\\b", "E")
                .replaceAll("\\bО\\b", "O")
                .replaceAll("\\bР\\b", "P")
                .replaceAll("\\bК\\b", "K")
                .replaceAll("\\bТ\\b", "T")
                .replace("Скільки дорівнює", "Знайдіть")
                .replace("Чому дорівнює", "Знайдіть")
                .replace("Обчисліть", "Знайдіть");
    }

    public static HashMap<String, ArrayList<String>> preprocessNumericValues(String text, ArrayList<String> numValues){

        String[] words = text.split(" ");
        StringBuilder newText = new StringBuilder();

        for (String word : words) {
            if (word.matches("(\\d)+|(\\d)+,(\\d)+|(\\d)+√(\\d)+")) {
                numValues.add(word);
                newText.append("Ч ");
            } else {
                newText.append(word);
                newText.append(" ");
            }
        }

        HashMap<String, ArrayList<String>> processedMap = new HashMap<>();
        processedMap.put(newText.toString(), numValues);

        return processedMap;
    }

    public static String[] getConLLUInfo(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String jsonResponse = response.toString();
        String[] resultSplit = jsonResponse.split("(?=#)");
        String ConLLUSplit = resultSplit[resultSplit.length-1];

        ConLLUSplit = ConLLUSplit.replace("\\t", " ")
                .replace("\\n", " ")
                .replace("_", "")
                .replace("}", "");

        List<String> ConLLUList = Arrays.asList(ConLLUSplit.split("(?= (\\d)+ [а-яА-ЯЄЇІЮЙЬєїіюйьA-Z;,])"));
        ConLLUList = new ArrayList<>(ConLLUList);
        System.out.println(ConLLUList.getFirst());
        ConLLUList.removeFirst();

        String[] ConLLUArray = new String[ConLLUList.size()];

        for(int i=0; i<ConLLUList.size(); i++){
            ConLLUArray[i] = ConLLUList.get(i).substring(1, ConLLUList.get(i).length()-2);
        }

        return ConLLUArray;
    }

}
