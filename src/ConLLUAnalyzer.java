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
        target = new ArrayList<>();
        this.lexemsInfo = lexemsInfo;
    }

    public ArrayList<String> analyze(){

        ArrayList<String[]> lexemsInfoList = new ArrayList<>();

        for(int i=0; i< lexemsInfo.length; i++){
            String[] lexemInfo = lexemsInfo[i].split(" ");
            lexemsInfoList.add(lexemInfo);
        }

        StringBuilder buffer = new StringBuilder();

        for(String[] lexem : lexemsInfoList){

            String s;
            if(Objects.equals(lexem[2], "ч") || lexem[4].equals("X")) s = lexem[1];
            else s = lexem[2];
            ArrayList<String> lexemList = new ArrayList<>(Arrays.asList(lexem));

            boolean isRightLangPart = !(lexemList.get(3).equals("ADP") || lexemList.get(3).equals("PUNCT") || lexemList.get(3).equals("DET") || lexemList.get(3).equals("SCONJ") || lexemList.get(3).equals("CCONJ") || lexemList.get(2).equals("мати"));
            if(isRightLangPart || lexemList.get(2).equals("ч")){
                if(lexemList.get(1).equals("катети") || lexemList.get(1).equals("Катети")) buffer.append("катети");
                else if(lexemList.get(1).equals("катета") || lexemList.get(1).equals("Катета")) buffer.append("катет");
                else if(lexemList.get(1).equals("проекції") || lexemList.get(1).equals("Проекції")) buffer.append("проекції");
                else if(lexemList.get(3).equals("X")) buffer.append(lexemList.get(1));
                else buffer.append(s);
                buffer.append(" ");
            }

            if(buffer.toString().matches("(рівнобедрений прямокутний|прямокутний рівнобедрений) трикутник( ([A-Z]\\d*){3}) ")){

                triangles.add(buffer.toString());
                buffer = new StringBuilder();
            }
            else if(buffer.toString().matches("гіпотенуза( ([A-Z]\\d*){2})? дорівнювати (\\d(√\\d)?(,\\d)?)+ (см|км|м)? ")){
                hypotenuses.add(buffer.toString());
                buffer = new StringBuilder();
            }
            else if(buffer.toString().matches("(катет( ([A-Z]\\d*){2})?|катети(( ([A-Z]\\d*){2})( ([A-Z]\\d*){2}))?)( (рівнобедрений прямокутний|прямокутний рівнобедрений) трикутник( ([A-Z]\\d*){3}))? дорівнювати (\\d(√\\d)?(,\\d)?)+ (см|км|м) ")){
                legs.add(buffer.toString());
                buffer = new StringBuilder();
            }
            else if(buffer.toString().matches("(проекція( ([A-Z]\\d*){2})?|проекції(( ([A-Z]\\d*){2})( ([A-Z]\\d*){2}))?) гіпотенуза( ([A-Z]\\d*){2})? дорівнювати (\\d(√\\d)?(,\\d)?)+ (см|км|м)? ")){
                legs.add(buffer.toString());
                buffer = new StringBuilder();
            }
            else if(buffer.toString().matches("(висота|медіана|бісектриса) ділити гіпотенуза навпіл ")
                    || buffer.toString().matches("(висота|медіана|бісектриса)( ([A-Z]\\d*){2})? проведений (вершина)? прямий кут( [A-Z](\\d*)| (([A-Z](\\d*)){3}))? дорівнювати (\\d(√\\d)?(,\\d)?)+ (см|км|м) ")
                    || buffer.toString().matches("(висота|медіана|бісектриса)( ([A-Z]\\d*){2})? проведений гіпотенуза( (([A-Z](\\d*)){2}))? дорівнювати (\\d(√\\d)?(,\\d)?)+ (см|км|м) ")
                    || buffer.toString().matches("медіана( ([A-Z]\\d*){2})? проведений гострий кут( [A-Z](\\d*)| (([A-Z](\\d*)){3}))? (катет|сторона)( ([A-Z]\\d*){2}) ")
                    || buffer.toString().matches("медіана( ([A-Z]\\d*){2})? проведений прямий кут( [A-Z](\\d*)| (([A-Z](\\d*)){3}))? (катет|сторона)( ([A-Z]\\d*){2}) ")
                    || buffer.toString().matches("(висота|медіана|бісектриса)( ([A-Z]\\d*){2})? дорівнювати (\\d(√\\d)?(,\\d)?)+ (см|км|м) "))
            {
                heights.add(buffer.toString());
                buffer = new StringBuilder();
            }

            else if(buffer.toString().matches("кут ([A-Z](\\d*)|(([A-Z](\\d*)){3})) дорівнювати \\d+ градус ")){
                angles.add(buffer.toString());
                buffer = new StringBuilder();
            }
            else if (buffer.toString().contains("знайти")) {
                if(lexemsInfoList.indexOf(lexem) == lexemsInfoList.size()-1){
                    target.add(buffer.toString());
                    buffer = new StringBuilder();
                }
            }

        }

        ArrayList<String> conditions = new ArrayList<>();
        conditions.addAll(triangles);
        conditions.addAll(legs);
        conditions.addAll(hypotenuses);
        conditions.addAll(angles);
        conditions.addAll(heights);
        conditions.addAll(target);

        return conditions;
    }
}