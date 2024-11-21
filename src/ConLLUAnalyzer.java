import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConLLUAnalyzer {

   private String[] lexemsInfo;
   private ArrayList<String> numericValues;
   private ArrayList<String> triangles;
   private ArrayList<String> legs;
   private ArrayList<String> hypotenuses;
   private ArrayList<String> angles;

   // Насправді, і для медіан, і для бісектрис
   private ArrayList<String> heights;
   private ArrayList<String> target;

    public ConLLUAnalyzer(String[] lexemsInfo, ArrayList<String> numericValues) {

        short numIdx = 0;
        this.numericValues = numericValues;

        for (int i=1; i< lexemsInfo.length; i++){
            if(lexemsInfo[i].contains("Ч")){
                lexemsInfo[i] = lexemsInfo[i].replace("Ч", numericValues.get(numIdx));
                numIdx++;
            }
        }
        triangles = new ArrayList<>();
        legs = new ArrayList<>();
        hypotenuses = new ArrayList<>();
        angles = new ArrayList<>();
        heights = new ArrayList<>();
        this.lexemsInfo = lexemsInfo;
    }

    public void analyze(){

        ArrayList<String[]> lexemsInfoList = new ArrayList<>();

        for(int i=0; i< lexemsInfo.length; i++){
            String[] lexemInfo = lexemsInfo[i].split(" ");
//            String personalLevel = lexemInfo[1];
//            String lexemForm = lexemInfo[2];
//            String lematizedForm = lexemInfo[3];
//            String languagePart = lexemInfo[4];
//            String description = lexemInfo[5];
//            String rootLevel = lexemInfo[6];
//
//            String[] necessaryLexemInfo = new String[6];
//            necessaryLexemInfo[0] = personalLevel;
//            necessaryLexemInfo[1] = lexemForm;
//            necessaryLexemInfo[2] = lematizedForm;
//            necessaryLexemInfo[3] = languagePart;
//            necessaryLexemInfo[4] = description;
//            necessaryLexemInfo[5] = rootLevel;

            lexemsInfoList.add(lexemInfo);
        }


        for(String[] arr : lexemsInfoList){
            String buffer;
            for(int i=0; i< arr.length; i++){
                String s = arr[i];

                if(s.matches("рівнобедрений|рівнобедреному")){

                }
            }
        }

        //findGivenData(lexemsInfoList, "0", new StringBuilder(), new ArrayList<>());
    }

    private void findGivenData(ArrayList<String[]> lexemsInfoList, String number, StringBuilder condition, ArrayList<String[]> taskPart) {



//        String[] curElem = new String[0];
//        for (String[] arr: lexemsInfoList){
//           for(String s: arr){
//               if (s.equals(number)) {
//                   curElem = arr;
//                   break;
//               }
//           }
//        }
//
//        String lemma = curElem[2];
//        String partOfLang = curElem[3];
//        String nextLevel = curElem[0];
//        String partOfSent = curElem[7];
//
//

//        if(partOfLang.equals("NOUN")){
//
//            if(lemma.contains("трикутник")){
//                taskPart.add(curElem);
//                findGivenData(lexemsInfoList, nextLevel, condition, taskPart);
//            }
//            else if(lemma.contains("катет")){
//                taskPart.add(curElem);
//                findGivenData(lexemsInfoList, nextLevel, condition, taskPart);
//            }
//            else if(lemma.contains("гіпотенуза")){
//                taskPart.add(curElem);
//                findGivenData(lexemsInfoList, nextLevel, condition, taskPart);
//            }
//            else if (lemma.contains("висот")) {
//                taskPart.add(curElem);
//                findGivenData(lexemsInfoList, nextLevel, condition, taskPart);
//            }
//            else if (lemma.contains("медіан")) {
//                taskPart.add(curElem);
//                findGivenData(lexemsInfoList, nextLevel, condition, taskPart);
//            }
//
//        }
//        else if(partOfLang.equals("VERB")){
//            if(lemma.equals("дорівнювати")){
//                taskPart.add(curElem);
//                findGivenData(lexemsInfoList, nextLevel, condition, taskPart);
//            }
//            else if(lemma.equals("знайти")){
//
//            }
//            else{
//                findGivenData(lexemsInfoList, nextLevel, condition, taskPart);
//            }
//        }
//        else if(partOfLang.equals("ADJ")){
//            for(String[] arr : taskPart){
//                for(String s : arr){
//
//                }
//            }
//        }
//        else{
//
//        }
    }

    private boolean checkID(String s) {
        return s.matches("[A-Z][A-Z\\d]*");
    }


    public String[] getLexemsInfo() {
        return lexemsInfo;
    }


}








//curElem=infoList;



//                if(curElem[3].equals("NOUN")){
//
//                    if(!objectData.isEmpty()){
//                        geomObjectTable.add(String.valueOf(objectData));
//                        objectData.delete(0, objectData.length());
//                        objectData.append(curElem[3]);
//                    }
//                    else{
//                        objectData.append(curElem[3]);
//                    }
//                }
//                else if(curElem[3].equals("ADJ")){
//                    if(!objectData.isEmpty() && (objectData.toString().contains("трикутник")
//                                                || objectData.toString().contains("висота")
//                                                || objectData.toString().contains("медіана")
//                                                || objectData.toString().contains("бісектриса"))){
//
//                    }
//                }
//                else if(curElem[3].equals("VERB")){
//
//                }

               /* if(infoList[3].matches("NOUN|ADJ")){

                    objectData.append(infoList[2]).append(" ");
                }

                else if(infoList[3].equals("X") && objectData.toString().equals("трикутник pівнобедренний прямокутний ")){

                    if(checkID(infoList[3])){
                        String triangle = infoList[2];
                        geomObjectTable.add(objectData+triangle);
                        objectData.delete(0, objectData.length());
                    }
                }

                findGivenData(lexemsInfoList, curElem[0], objectData);*/