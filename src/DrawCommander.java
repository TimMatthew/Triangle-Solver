import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Для створення речень з формальної мови для подання в лексичний аналіз
public class DrawCommander {

    /*private static final char[] ENGLISH_ALPHABET = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };*/
    ArrayList<String> graphicsDescription;
    ArrayList<Shape> objects;
    ArrayList<String> identifiers;
    ArrayList<String> conditions;
    private String target;

    public DrawCommander(ArrayList<String> conditions) {
        this.conditions = conditions;
        graphicsDescription=new ArrayList<>();
        identifiers = new ArrayList<>();
        objects=new ArrayList<>();
        target = "";
    }

    public Shape makeCommandsAndObjects(){

        boolean isTriangleBuilt = false;
        RightIsoscelessTriangle mainTriangle = null;
        String triangleid = "";
        for(String condition : conditions){
            String[] conditionSplit = condition.split(" ");

            if(conditionSplit[0].contains("висота") || condition.contains("бісектриса")) makeHeight(condition, isTriangleBuilt);
            else if(conditionSplit[0].contains("медіана")) makeMedian(condition, isTriangleBuilt);
            else if(conditionSplit[0].contains("проекції")) makeProjections(condition, isTriangleBuilt);
            else if(conditionSplit[0].contains("катет")) makeLeg(condition, isTriangleBuilt);
            else if(conditionSplit[0].contains("гіпотенуза")) makeHypotenuse(condition, isTriangleBuilt);
            else if(condition.contains("трикутник") && !conditionSplit[0].contains("знайти")) {
                mainTriangle = makeTriangle(condition);
                isTriangleBuilt = true;
            }

            if(!isTriangleBuilt && condition.contains("трикутник")){
                for(String s : conditionSplit){
                    if(s.matches("(([A-Z]\\d*){3})")){
                        triangleid = s;
                    }
                }
            }
            if(condition.contains("знайти")) {

                if(condition.contains("проекція")){
                    makeProjections(condition, isTriangleBuilt);
                    target = "projection";
                }
                else if(condition.contains("проекції")){
                    makeProjections(condition, isTriangleBuilt);
                    target = "projections";
                }
                else if (condition.contains("катети")){
                    makeLeg(condition, isTriangleBuilt);
                    target = "legs";
                }
                else if(condition.contains("катет")){
                    makeLeg(condition, isTriangleBuilt);
                    target = "leg";
                }
                else if(condition.contains("висота") || condition.contains("бісектрис")){
                    makeHeight(condition, isTriangleBuilt);
                    target = "height";
                }
                else if(condition.contains("медіана")){
                    makeMedian(condition, isTriangleBuilt);
                    target = "median";
                }
                else if(condition.contains("гіпотенуза")){
                    makeHypotenuse(condition, isTriangleBuilt);
                    target = "hypotenuse";
                }
                else if(condition.contains("площа")){
                    target = "square";
                }
                else if(condition.contains("периметр")){
                    target = "perimeter";
                }
            }
        }

        if(!isTriangleBuilt){
            if(triangleid.isEmpty()) triangleid="ABC";
            mainTriangle = new RightIsoscelessTriangle(triangleid);
            String idLeg1 = String.valueOf(mainTriangle.getId().charAt(0))+mainTriangle.getId().charAt(1);
            String idLeg2 = String.valueOf(mainTriangle.getId().charAt(1))+mainTriangle.getId().charAt(2);
            String idHypotenuse = String.valueOf(mainTriangle.getId().charAt(0))+mainTriangle.getId().charAt(2);

            boolean hasDigits = false;

            for(char c: triangleid.toCharArray()){
                if(Character.isDigit(c)) hasDigits = true;
            }

            if(hasDigits){

                String identifierRegex = "([A-Z]\\d*)";
                Pattern pattern = Pattern.compile(identifierRegex);
                Matcher matcher = pattern.matcher(triangleid.toString());
                List<String> ids = new ArrayList<>();

                while (matcher.find()) {
                    ids.add(matcher.group());
                }
                idLeg1 = ids.getFirst() + ids.get(1);
                idLeg2 = ids.get(1)+ids.get(2);
                idHypotenuse = ids.get(0)+ids.get(2);
            }
            mainTriangle.setLeg1(new Segment(idLeg1, "leg1"));
            mainTriangle.setLeg2(new Segment(idLeg2, "leg2"));
            mainTriangle.setHypotenuse(new Segment(idHypotenuse, "hypotenuse"));
            graphicsDescription.add("Побудувати рівнобедрений прямокутний трикутник "+mainTriangle.getId());
        }

        for (Shape s : objects){
            if(s instanceof Segment segment){
               switch (segment.getSegmentType()){
                   case "leg1" -> mainTriangle.setLeg1(segment);
                   case "leg2" -> mainTriangle.setLeg2(segment);
                   case "hypotenuse" -> Objects.requireNonNull(mainTriangle).setHypotenuse(segment);
                   case "height-median-bisector" -> Objects.requireNonNull(mainTriangle).getHeights().add(segment);
                   case "median" -> Objects.requireNonNull(mainTriangle).getMedians().add(segment);
                   case "hypotenuse-projection" -> Objects.requireNonNull(mainTriangle).getProjections().add(segment);
               }
            }
        }

        return mainTriangle;
    }

    public String getTarget() {
        return target;
    }



    private RightIsoscelessTriangle makeTriangle(String cond){

        StringBuilder description = new StringBuilder("Побудувати рівнобедрений прямокутний трикутник ");
        String[] words = cond.split(" ");
        StringBuilder identifier = new StringBuilder();

        for(String w : words){
            if(w.matches("(([A-Z]\\d*){3})")){
                identifier.append(w);
                description.append(w).append(".");
                graphicsDescription.add(description.toString());
                break;
            }
        }
        if(identifier.toString().isEmpty()) identifier.append("ABC");
        RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle(identifier.toString());

        StringBuilder legId1 = new StringBuilder(identifier.toString().charAt(0)), legId2 = new StringBuilder();
        boolean hasDigits = false;
        for(char c: identifier.toString().toCharArray()){
            if(Character.isDigit(c)) hasDigits = true;
        }

        String idLeg1, idLeg2, idHypotenuse;
        if(hasDigits){

            String identifierRegex = "([A-Z]\\d*)";
            Pattern pattern = Pattern.compile(identifierRegex);
            Matcher matcher = pattern.matcher(identifier.toString());
            List<String> ids = new ArrayList<>();

            while (matcher.find()) {
                ids.add(matcher.group());
            }
            idLeg1 = ids.getFirst() + ids.get(1);
            idLeg2 = ids.get(1)+ids.get(2);
            idHypotenuse = ids.get(0)+ids.get(2);
        }
        else{
            idLeg1 = String.valueOf(newTriangle.getId().charAt(0))+newTriangle.getId().charAt(1);
            idLeg2 = String.valueOf(newTriangle.getId().charAt(1))+newTriangle.getId().charAt(2);
            idHypotenuse = String.valueOf(newTriangle.getId().charAt(0))+newTriangle.getId().charAt(2);
        }


        newTriangle.setLeg1(new Segment(idLeg1, "leg1"));
        newTriangle.setLeg2(new Segment(idLeg2, "leg2"));
        newTriangle.setHypotenuse(new Segment(idHypotenuse, "hypotenuse"));
        identifiers.add(identifier.toString());
        objects.add(newTriangle);
        return newTriangle;
    }

    private void makeLeg(String cond, boolean triangleIsCreated){

        String[] conditionWords = cond.split(" ");
        StringBuilder identifier = new StringBuilder();
        Segment leg1 = null;
        Segment leg2 = null;
        boolean isBuilt1 = false;
        boolean isBuilt2 = false;
        String triangleId = "";
        boolean hasDigits=false;

        // Якщо "катет", то призначаємо назву першому катету
        // Тобто, якесь 1 айді катета вже вказане

        boolean many = false;
        for (String w: cond.split(" ")){
            if(w.equals("катети")){
                many= true;
            }
        }

        if(!many){

            // Знаходимо назву першого катета або ж назву трикутника
            for(String w: conditionWords){
                if(w.matches("(([A-Z]\\d*){2})")){
                    identifier.append(w);
                    leg1 = new Segment(identifier.toString(), "leg1");
                    identifiers.add(identifier.toString());
                    objects.add(leg1);
                    isBuilt1 = true;
                }
                else if(w.matches("(([A-Z]\\d*){3})")){
                    triangleId = w;
                    for(char c: w.toCharArray()){
                        if(Character.isDigit(c)) hasDigits = true;
                    }
                }
            }

            // Якщо в умовах катета айді трикутника не вказано, шукаємо в глобальних
            // Якщо нема в глобальних, даємо назву 'ABC' за замовчуванням
            if(triangleId.isEmpty()){

                boolean isTriangle = false;

                for(String condition : conditions) {
                    for (String w : condition.split(" ")) {
                        if (condition.contains("трикутник") && w.matches("(([A-Z]\\d*){3})")) {
                            triangleId = w;
                            isTriangle = true;
                            for(char c: w.toCharArray()){
                                if(Character.isDigit(c)) hasDigits = true;
                            }
                        }
                    }

                    if (!isTriangle) {
                        triangleId = "ABC";
                        if(!identifiers.contains("ABC") && !triangleIsCreated){
                            RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle(triangleId);
                            identifiers.add(triangleId);
                            objects.add(newTriangle);
                        }
                    }
                }
            }
            {

                String idLeg1 = null, idLeg2 = null;
                if(hasDigits){

                    String identifierRegex = "([A-Z]\\d*)";
                    Pattern pattern = Pattern.compile(identifierRegex);
                    Matcher matcher = pattern.matcher(triangleId);

                    List<String> ids = new ArrayList<>();

                    while (matcher.find()) {
                        ids.add(matcher.group());
                    }
                    idLeg1 = ids.getFirst() + ids.get(1);
                    idLeg2 = ids.get(1)+ids.get(2);
                }

                // Якщо 1ий катет все ще не побудовано
                if(!isBuilt1){
                    if(hasDigits) leg1 = new Segment(idLeg1, "leg1");
                    else leg1 = new Segment(triangleId.substring(0,2), "leg1");
                    identifiers.add(leg1.getId());
                    objects.add(leg1);
                }

                // Будуємо 2ий катет
                if(hasDigits) identifier = new StringBuilder(idLeg2);
                else{
                    if(!identifiers.contains(triangleId.substring(0,2))) identifier = new StringBuilder(triangleId.substring(0,2));
                    else if(!identifiers.contains(triangleId.substring(1,3))) identifier = new StringBuilder(triangleId.substring(1,3));
                }

                leg2 = new Segment(identifier.toString(), "leg2");
                identifiers.add(leg2.getId());
                objects.add(leg2);
            }

            for(String w : conditionWords){
                if(w.matches("(\\d(√\\d)?(,\\d)?)+")){
                    double numVal = parseNumeric(w);
                    Objects.requireNonNull(leg1).setLength(numVal);
                    Objects.requireNonNull(leg2).setLength(numVal);
                }
                else if(w.matches("(см|км|м)")){
                    Objects.requireNonNull(leg1).setMeasure(w);
                    Objects.requireNonNull(leg2).setMeasure(w);
                }
            }

            // Le fin
        }
        else if(cond.contains("катети")){

            for(String w: conditionWords){
                if(w.matches("(([A-Z]\\d*){2})")){

                    if(!isBuilt1){
                        identifier.append(w);
                        leg1 = new Segment(identifier.toString(), "leg1");
                        identifiers.add(identifier.toString());
                        objects.add(leg1);
                        isBuilt1 = true;
                    }
                    else if(!isBuilt2){
                        identifier.append(w);
                        leg2 = new Segment(identifier.toString(), "leg2");
                        identifiers.add(identifier.toString());
                        objects.add(leg2);
                        isBuilt2 = true;
                    }
                }
                else if(w.matches("(([A-Z]\\d*){3})")) triangleId = w;
            }

            // Якщо катетів в наших умові не знайдено
            if(!(isBuilt1 && isBuilt2)){

                // Якщо трикутника в наших умовах не знайдено
                // шукаємо в глобальних умовах
                if(triangleId.isEmpty()){

                    boolean isTriangle = false;

                    for(String condition : conditions) {
                        for (String w : condition.split(" ")) {
                            if (condition.contains("трикутник") && w.matches("(([A-Z]\\d*){3})")) {
                                triangleId = w;
                                isTriangle = true;
                            }
                        }

                        // Якщо й там не знайшли, тоді трикутник створюємо за замовчуванням
                        if (!isTriangle) {
                            triangleId = "ABC";
                            if(!identifiers.contains("ABC") && !triangleIsCreated){
                                RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle(triangleId);
                                identifiers.add(triangleId);
                                objects.add(newTriangle);
                            }
                        }
                    }
                }


                boolean hasDigits1=false;
                for(char c: triangleId.toCharArray()){
                    if(Character.isDigit(c)) hasDigits1 = true;
                }

                String idLeg1 = null, idLeg2 = null;
                if(hasDigits1){

                        String identifierRegex = "([A-Z]\\d*)";
                        Pattern pattern = Pattern.compile(identifierRegex);
                        Matcher matcher = pattern.matcher(triangleId);

                        List<String> ids = new ArrayList<>();

                        while (matcher.find()) {
                            ids.add(matcher.group());
                        }
                        idLeg1 = ids.getFirst() + ids.get(1);
                        idLeg2 = ids.get(1)+ids.get(2);

                }

                if(hasDigits1) identifier = new StringBuilder(idLeg1);
                else identifier = new StringBuilder(triangleId.substring(0,2));
                leg1 = new Segment(identifier.toString(), "leg1");
                identifiers.add(identifier.toString());
                objects.add(leg1);

                if(hasDigits1) identifier = new StringBuilder(idLeg2);
                else identifier = new StringBuilder(triangleId.substring(1,3));
                leg2 = new Segment(identifier.toString(), "leg2");
                identifiers.add(identifier.toString());
                objects.add(leg2);

                for(String w : conditionWords){
                    if(w.matches("(\\d(√\\d)?(,\\d)?)+")){
                        double numVal = parseNumeric(w);
                        leg1.setLength(numVal);
                        leg2.setLength(numVal);
                    }
                    else if(w.matches("(см|км|м)")){
                        leg1.setMeasure(w);
                        leg2.setMeasure(w);
                    }
                }
            }
            // La fin
        }
    }
    private void makeHypotenuse(String cond, boolean triangleIsCreated){

        String[] words = cond.split(" ");
        StringBuilder identifier = new StringBuilder();
        Segment newHypotenuse = null;
        boolean isBuilt = false;
        double numVal;
        String measure;
        boolean hasDigits = false;

        //Шукаємо айді гіпотенузи
        for(String w : words){
            if(w.matches("(([A-Z]\\d*){2})")){
                identifier.append(w);
                newHypotenuse = new Segment(identifier.toString(), "hypotenuse");
                identifiers.add(identifier.toString());
                objects.add(newHypotenuse);
                isBuilt = true;
                break;
            }
        }

        // Якщо не знайшли, то шукаємо айді трикутника в наших умовах
        if(!isBuilt){

            String triangleId = "";

            boolean isTriangle = false;

            for(String w : words){
                if(w.matches("(([A-Z]\\d*){3})")){
                    triangleId = w;
                    isTriangle = true;
                    for(char c: w.toCharArray()){
                        if(Character.isDigit(c)) hasDigits = true;
                    }
                    break;
                }
            }

            // Якщо в наших не знайдено, то в глобальних умовах,

            if(!isTriangle){
                boolean found = false;
                for(String condition : conditions){
                    for(String word : condition.split(" ")){
                        if(condition.contains("трикутник") && word.matches("(([A-Z]\\d*){3})")){
                            triangleId = word;
                            isTriangle = true;
                            for(char c: word.toCharArray()){
                                if(Character.isDigit(c)) hasDigits = true;
                            }
                            found=true;
                            break;
                        }
                    }
                    if(found) break;
                }

                // Якщо айді трикутника ніде немає, стоворюємо новий і беремо для гіпотенузи дві крайні букви трикутника
                // Це нібито натякає на те, щоб я тут створював ще й трикутник
                if(!isTriangle){
                    triangleId = "ABC";
                    if(!identifiers.contains("ABC") && !triangleIsCreated){
                        RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle(triangleId);
                        identifiers.add(triangleId);
                        objects.add(newTriangle);
                    }
                }
            }

            String idHypotenuse = null;
            if(hasDigits){

                String identifierRegex = "([A-Z]\\d*)";
                Pattern pattern = Pattern.compile(identifierRegex);
                Matcher matcher = pattern.matcher(triangleId);

                List<String> ids = new ArrayList<>();

                while (matcher.find()) {
                    ids.add(matcher.group());
                }
                idHypotenuse = ids.getFirst() + ids.get(2);
            }

            if(hasDigits) identifier = new StringBuilder(idHypotenuse);
            else identifier = new StringBuilder(String.valueOf(triangleId.charAt(0)) + triangleId.charAt(2));
            newHypotenuse = new Segment(identifier.toString(), "hypotenuse");




        }

        for(String w : words){
            if(w.matches("(\\d(√\\d)?(,\\d)?)+")){
                numVal = parseNumeric(w);
                newHypotenuse.setLength(numVal);
            }
            else if(w.matches("(см|км|м)")){
                newHypotenuse.setMeasure(w);
            }
        }
        identifiers.add(identifier.toString());
        objects.add(newHypotenuse);
    }

    private double parseNumeric(String w) {
        Pattern pattern = Pattern.compile("(\\d+)(√(\\d+))?(,(\\d))?");
        Matcher matcher = pattern.matcher(w);

        int squareRootIndex = w.indexOf("√");
        int commaIndex = w.indexOf(",");
        double integer, rootPart = 0, decimalPart = 0;

        if(squareRootIndex != -1){
            if(commaIndex != -1){

                integer = Double.parseDouble(w.substring(0, squareRootIndex));
                rootPart = Math.sqrt(Double.parseDouble(w.substring(squareRootIndex+1, commaIndex)));
                decimalPart = Double.parseDouble(w.substring(commaIndex+1));

                return integer*rootPart+decimalPart;
            }
            else{

                integer = Double.parseDouble(w.substring(0, squareRootIndex));
                rootPart = Math.sqrt(Double.parseDouble(w.substring(squareRootIndex+1)));
                return integer*rootPart;
            }
        }
        else{
            if(commaIndex != -1){

                integer = Double.parseDouble(w.substring(0, commaIndex));
                decimalPart = Double.parseDouble("0."+w.substring(commaIndex+1));
                return integer+decimalPart;
            }
            else{
                integer = Double.parseDouble(w);
                return integer;
            }
        }
    }

    private void makeHeight(String cond, boolean triangleIsCreated){
        StringBuilder identifier = new StringBuilder();
        Segment newHeight = null;
        boolean isBuilt = false;
        boolean hasDigits = false;
        String newMedianId = null;
        boolean hasDigits1 = false;


        // Дивимось, чи є айді висоти
        for(String w: cond.split(" ")){
            if(w.matches("(([A-Z]\\d*){2})")){
                identifier.append(w);
                newMedianId = w;
                newHeight = new Segment(identifier.toString(), "height-median-bisector");
                isBuilt = true;
                for(char c: w.toCharArray()){
                    if(Character.isDigit(c)) hasDigits = true;
                }
                identifiers.add(identifier.toString());
                objects.add(newHeight);

                break;
            }
        }
        String triangleId = "";
        String idHeight = null;
        if(!isBuilt){

            // Якщо кута нема, беремо з трикутника (з глобальних умов)
            boolean isTriangle = false;
                boolean found = false;
                for(String condition : conditions){
                    for(String w: condition.split(" ")){
                        if(condition.contains("трикутник") && w.matches("(([A-Z]\\d*){3})")){
                            identifier = new StringBuilder();
                            triangleId = w;
                            identifier.append(w.charAt(1)).append("H");
                            for(char c: w.toCharArray()){
                                if(Character.isDigit(c)) hasDigits = true;
                            }
                            isTriangle = true;
                            found=true;
                            break;
                        }
                    }
                    if(found) break;
                }


            // Якщо трикутника нема, створюємо та беремо за замовчуванням
            if(!isTriangle){
                triangleId = "ABC";
                identifier = new StringBuilder();
                identifier.append(triangleId.charAt(1)).append("H");
                if(!identifiers.contains("ABC") && !triangleIsCreated){
                    RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle(triangleId);
                    identifiers.add(triangleId);
                    objects.add(newTriangle);
                }
            }

            if(hasDigits){

                String identifierRegex = "([A-Z]\\d*)";
                Pattern pattern = Pattern.compile(identifierRegex);
                Matcher matcher = pattern.matcher(triangleId);

                List<String> ids = new ArrayList<>();

                while (matcher.find()) {
                    ids.add(matcher.group());
                }
                idHeight = ids.get(1) + "H";
            }

            if(hasDigits) newHeight = new Segment(idHeight, "height-median-bisector");
            else newHeight = new Segment(identifier.toString(), "height-median-bisector");
            identifiers.add(identifier.toString());
            objects.add(newHeight);

            for(String w : cond.split(" ")){
                if(w.matches("(\\d(√\\d)?(,\\d)?)+")){
                    double numVal = parseNumeric(w);
                    newHeight.setLength(numVal);
                }
                else if(w.matches("(см|км|м)")){
                    newHeight.setMeasure(w);
                }
            }
        }

        String hypotenuse = "";
        boolean found = false;
        for(String c: conditions){
            for(String w : c.split(" ")){
                if(c.contains("трикутник") && w.matches("(([A-Z]\\d*){3})")){
                    hypotenuse = String.valueOf(w.charAt(0)) + w.charAt(2);
                    found = true;
                    break;
                }
            }
            if(found) break;
        }

        if(!found){
            for(Shape s: objects){
                if(s instanceof RightIsoscelessTriangle rit) hypotenuse = String.valueOf(rit.getId().charAt(0)) + rit.getId().charAt(2);
            }
        }

        if(!found) {
            RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle("ABC");
            identifiers.add("ABC");
            objects.add(newTriangle);
            hypotenuse = String.valueOf(newTriangle.getId().charAt(0)) + newTriangle.getId().charAt(2);
        }

        String triangleName = "";
        String idHypotenuse = null;
        String idHeightToDescribe = null;
        if(hasDigits){

            boolean found1 = false;
            for(String condition : conditions){
                for(String w: condition.split(" ")){
                    if(condition.contains("трикутник") && w.matches("(([A-Z]\\d*){3})")){
                        triangleName = w;
                        for(char c: w.toCharArray()){
                            if(Character.isDigit(c)) hasDigits = true;
                        }
                        found1=true;
                        break;
                    }
                }
                if(found) break;
            }

            String identifierRegex = "([A-Z]\\d*)";
            Pattern pattern = Pattern.compile(identifierRegex);
            Matcher matcher = pattern.matcher(triangleName);
            List<String> ids = new ArrayList<>();

            while (matcher.find()) {
                ids.add(matcher.group());
            }
            idHeightToDescribe = ids.get(1) + "H";
            idHypotenuse = ids.get(0)+ids.get(2);
        }

        for(String w : cond.split(" ")){
            if(w.matches("(\\d(√\\d)?(,\\d)?)+")){
                double numVal = parseNumeric(w);
                newHeight.setLength(numVal);
            }
            else if(w.matches("(см|км|м)")){
                newHeight.setMeasure(w);
            }
        }

        if(idHeight==null && hasDigits) graphicsDescription.add("Провести висоту " + newMedianId + " до гіпотенузи " + idHypotenuse + ".");
        else if(hasDigits) graphicsDescription.add("Провести висоту " + idHeight + " до гіпотенузи " + idHypotenuse + ".");
        else graphicsDescription.add("Провести висоту " + identifier + " до гіпотенузи " + hypotenuse + ".");
    }

    private void makeMedian(String cond, boolean triangleIsCreated){

        String[] words = cond.split(" ");
        StringBuilder identifier = new StringBuilder();
        Segment newMedian = null;
        boolean isBuilt = false;
        boolean hasDigits = false;
        String idMedian = null;

        //Якщо айді є в нашій умові, то берем звідти
        for(String w : words){
            if(w.matches("(([A-Z]\\d*){2})")){
                identifier.append(w);
                newMedian = new Segment(identifier.toString(), "median");
                isBuilt = true;
                break;
            }
        }

        // Якщо немає, то аналізуємо, чи медіана з прямого чи з гострого кута
        if(!isBuilt){
            boolean isTriangle = false;

            // Якщо з прямого, беремо з трикутника (глобальні умови) середню букву + M
            if(cond.contains("прямий")){
                for(String condition : conditions){
                    for(String w: condition.split(" ")){
                        if(condition.contains("трикутник") && w.matches("(([A-Z]\\d*){3})")){
                            identifier = new StringBuilder();
                            for(char c: w.toCharArray()){
                                if(Character.isDigit(c)) hasDigits = true;
                            }
                            identifier.append(w.charAt(1)).append("M");
                            isTriangle = true;
                        }
                    }
                }

                if(!isTriangle){
                    String triangleId = "ABC";
                    identifier = new StringBuilder();
                    identifier.append(triangleId.charAt(1)).append("H");
                    if(!identifiers.contains(triangleId) && !triangleIsCreated){
                        RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle(triangleId);
                        identifiers.add(triangleId);
                        objects.add(newTriangle);
                    }
                }
            }

            // Якщо з гострого, дивимось, чи в нашій уомві вказаний айді кута, з якого вона проведена
            else if(cond.contains("гострий")){

                boolean isAngle = false;

                for(int i=0; i< words.length; i++){
                    if(words[i].equals("кут")){
                       if(words[i+1].matches("([A-Z](\\d*))")){
                           identifier = new StringBuilder(words[i+1]);
                           for(char c: words[i+1].toCharArray()){
                               if(Character.isDigit(c)) hasDigits = true;
                           }
                           identifier.append("M");
                           isAngle=true;
                       }
                       else if(words[i+1].matches("(([A-Z](\\d*)){3})")){
                           identifier = new StringBuilder(String.valueOf(words[i+1].charAt(1)));
                           identifier.append("M");
                           isAngle=true;
                       }
                    }
                }

                // Якщо нема, беремо з трикутника 1 з крайніх букв + M
                if(!isAngle){

                    for(String condition : conditions){
                        for(String w: condition.split(" ")){
                            if(condition.contains("трикутник") && w.matches("(([A-Z]\\d*){3})")){
                                identifier = new StringBuilder();
                                identifier.append(w.charAt(0)).append("M");
                                for(char c: w.toCharArray()){
                                    if(Character.isDigit(c)) hasDigits = true;
                                }
                                isTriangle = true;
                            }
                        }
                    }

                    if(!isTriangle){
                        String triangleId = "ABC";
                        identifier = new StringBuilder();
                        identifier.append(triangleId.charAt(0)).append("M");
                        if(!identifiers.contains(triangleId) && !triangleIsCreated){
                            RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle(triangleId);
                            identifiers.add(triangleId);
                            objects.add(newTriangle);
                        }
                    }
                }
            }


            if(hasDigits){

                String identifierRegex = "([A-Z]\\d*)";
                Pattern pattern = Pattern.compile(identifierRegex);
                Matcher matcher = pattern.matcher(identifier.toString());

                List<String> ids = new ArrayList<>();

                while (matcher.find()) {
                    ids.add(matcher.group());
                }
                idMedian = ids.getFirst() + "M";
            }

            if(hasDigits) newMedian = new Segment(idMedian, "median");
            else newMedian = new Segment(identifier.toString(), "median");

            for(String w : cond.split(" ")){
                if(w.matches("(\\d(√\\d)?(,\\d)?)+")){
                    double numVal = parseNumeric(w);
                    newMedian.setLength(numVal);
                }
                else if(w.matches("(см|км|м)")){
                    newMedian.setMeasure(w);
                }
            }


        }

        if(!identifiers.contains(identifier.toString())){
            identifiers.add(identifier.toString());
            objects.add(newMedian);
        }

        String legId = "", hypotenuseId = "", triangleId = "";
        boolean found = false;
        for(String c: conditions){
            for(String w : c.split(" ")){
                if(c.contains("трикутник") && w.matches("(([A-Z]\\d*){3})")){
                    triangleId = w;
                    for(char c1: w.toCharArray()){
                        if(Character.isDigit(c1)) hasDigits = true;
                    }
                    if(!identifier.isEmpty()){
                        if(identifier.charAt(0) == w.charAt(0)) legId = String.valueOf(w.charAt(1))+w.charAt(2);
                        else if(identifier.charAt(0) == w.charAt(2)) legId = String.valueOf(w.charAt(0))+w.charAt(1);
                        else if(identifier.charAt(0) == w.charAt(1)) hypotenuseId = String.valueOf(w.charAt(0))+w.charAt(2);
                    }
                    found = true;
                    break;
                }
            }
            if(found) break;
        }

        if(!found){
            for(Shape s: objects){
                if(s instanceof RightIsoscelessTriangle rit) {
                    if(identifier.charAt(0) == rit.getId().charAt(0)) legId = String.valueOf(rit.getId().charAt(1))+rit.getId().charAt(2);
                    else if(identifier.charAt(0) == rit.getId().charAt(2)) legId = String.valueOf(rit.getId().charAt(0))+rit.getId().charAt(1);
                    else if(identifier.charAt(0) == rit.getId().charAt(1)) hypotenuseId = String.valueOf(rit.getId().charAt(0))+rit.getId().charAt(2);
                }
            }
        }

        if(!found) {
            RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle("ABC");
            identifiers.add("ABC");
            objects.add(newTriangle);
            if(identifier.charAt(0) == newTriangle.getId().charAt(0)) legId = String.valueOf(newTriangle.getId().charAt(1))+newTriangle.getId().charAt(2);
            else if(identifier.charAt(0) == newTriangle.getId().charAt(2)) legId = String.valueOf(newTriangle.getId().charAt(0))+newTriangle.getId().charAt(1);
            else if(identifier.charAt(0) == newTriangle.getId().charAt(1)) hypotenuseId = String.valueOf(newTriangle.getId().charAt(0))+newTriangle.getId().charAt(2);
        }

        String idLeg1 = null, idLeg2 = null, idHypotenuse = null;
        if(hasDigits){

            String identifierRegex = "([A-Z]\\d*)";
            Pattern pattern = Pattern.compile(identifierRegex);
            Matcher matcher = pattern.matcher(triangleId);
            List<String> ids = new ArrayList<>();

            while (matcher.find()) {
                ids.add(matcher.group());
            }
            idLeg1 = ids.getFirst() + ids.get(1);
            idLeg2 = ids.get(1)+ids.get(2);
            idHypotenuse = ids.get(0)+ids.get(2);
            idMedian = newMedian.getId();
        }

        if(hasDigits){
            if(cond.contains("гострий")){
                if(!idLeg2.contains(String.valueOf(newMedian.getId().charAt(0)))){
                    graphicsDescription.add("Провести медіану " + newMedian.getId() + " до катета " + idLeg2 + ".");
                }
                else if(!idLeg1.contains(String.valueOf(newMedian.getId().charAt(0)))){
                    graphicsDescription.add("Провести медіану " + newMedian.getId() + " до катета " + idLeg1 + ".");
                }
            }
            else if(cond.contains("прямий")){
                graphicsDescription.add("Провести медіану " + idMedian + " до катета " + idHypotenuse + ".");
            }
        }

        else if(cond.contains("гострий")){
                graphicsDescription.add("Провести медіану " + identifier + " до катета " + legId + ".");
        }
        else if(cond.contains("прямий")){
            graphicsDescription.add("Провести медіану " + identifier + " до катета " + hypotenuseId + ".");
        }
    }

    private void makeProjections(String condition, boolean triangleIsCreated) {
        String[] words = condition.split(" ");
        StringBuilder identifier = new StringBuilder();
        Segment proj1;
        Segment proj2;
        boolean isBuilt1 = false;
        boolean isBuilt2 = false;
        String triangleId = "";
        boolean hasDigits = false;

        boolean many = false;
        for (String w: words){
            if(w.equals("проекції")){
                many= true;
            }
        }

        if(!many){

            //Якщо айді є в нашій умові, то берем звідти
            for(String w: words){
                if(w.matches("(([A-Z]\\d*){2})")){
                    identifier.append(w);
                    proj1 = new Segment(identifier.toString(), "hypotenuse-projection");
                    identifiers.add(identifier.toString());
                    objects.add(proj1);
                    isBuilt1 = true;
                    break;
                }
                else if(w.matches("(([A-Z]\\d*){3})")){
                    triangleId = w;
                    for(char c: w.toCharArray()){
                        if(Character.isDigit(c)) hasDigits = true;
                    }
                }
            }

            // Якщо немає, то беремо з гіпотенузи з нашої умови
            if(!isBuilt1){

                boolean isHypotenuse = false;

                for(int i=0; i< words.length; i++){
                    if(words[i].equals("гіпотенуза")){
                        if(words[i+1].matches("(([A-Z](\\d*)){2})")){
                            identifier = new StringBuilder(String.valueOf(words[i+1].charAt(0)));
                            identifier.append("H");
                            isHypotenuse=true;
                            break;
                        }
                    }
                }

                // Якщо нема гіпотенузи, то шукаємо айді нашого трикутника в глобальних умовах
                // витягуємо айді проекції звідти

                if(!isHypotenuse && triangleId.isEmpty()){

                    for(String cond : conditions){
                        for(String w : cond.split(" ")){
                            if(cond.contains("трикутник") && w.matches("(([A-Z](\\d*)){3})")){
                                triangleId = w;
                                identifier = new StringBuilder(String.valueOf(triangleId.charAt(0)));
                                identifier.append("H");
                            }
                        }
                    }

                    // Якщо трикутника немає, створюємо айді ABC. Тоді гіпотенуза буде AC.
                    // Тоді перша проекція буде AH. Якщо трикутник вже створений, то не створюємо об'єкт трикутника
                    if(triangleId.isEmpty()){
                        triangleId = "ABC";
                        identifier = new StringBuilder(String.valueOf(triangleId.charAt(0)));
                        identifier.append("H");
                        if(!identifiers.contains(triangleId) && !triangleIsCreated){
                            RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle(triangleId);
                            identifiers.add(triangleId);
                            objects.add(newTriangle);
                        }
                    }
                }

                String idLeg1 = null, idLeg2 = null;
                if(hasDigits){

                    String identifierRegex = "([A-Z]\\d*)";
                    Pattern pattern = Pattern.compile(identifierRegex);
                    Matcher matcher = pattern.matcher(triangleId);

                    List<String> ids = new ArrayList<>();

                    while (matcher.find()) {
                        ids.add(matcher.group());
                    }
                    idLeg1 = ids.getFirst() + "H";
                    idLeg2 = ids.get(2)+ "H";
                }

                // Будуємо 1шу проекцію
                if(hasDigits) proj1 = new Segment(idLeg1, "hypotenuse-projection");
                else proj1 = new Segment(identifier.toString(), "hypotenuse-projection");
                identifiers.add(proj1.getId());
                objects.add(proj1);

                // Будуємо 2гу відмінну від 1шої
                if(!identifiers.contains(triangleId.charAt(0) +"H")) identifier = new StringBuilder(triangleId.charAt(0) + "H");
                else if(!identifiers.contains(triangleId.charAt(1) +"H")) identifier = new StringBuilder(triangleId.charAt(1) + "H");

                if(hasDigits) proj2 = new Segment(idLeg2, "hypotenuse-projection");
                else proj2 = new Segment(identifier.toString(), "hypotenuse-projection");
                identifiers.add(proj2.getId());
                objects.add(proj2);
            }
        }
        else if(condition.contains("проекції")){

            for(String w: words){
                if(w.matches("(([A-Z]\\d*){2})")){

                    if(!isBuilt1){
                        identifier.append(w);
                        proj1 = new Segment(identifier.toString(), "hypotenuse-projection");
                        identifiers.add(identifier.toString());
                        objects.add(proj1);
                        isBuilt1 = true;
                    }
                    else if(!isBuilt2){
                        identifier.append(w);
                        proj2 = new Segment(identifier.toString(), "hypotenuse-projection");
                        identifiers.add(identifier.toString());
                        objects.add(proj2);
                        isBuilt2 = true;
                    }
                }
                else if(w.matches("(([A-Z]\\d*){3})")){
                    triangleId = w;
                    for(char c: w.toCharArray()){
                        if(Character.isDigit(c)) hasDigits = true;
                    }
                }
            }

            // Якщо проекцій в наших умовах не знайдено
            if(!(isBuilt1 && isBuilt2)){

                // Якщо трикутника в наших умовах не знайдено
                // шукаємо в глобальних умовах
                if(triangleId.isEmpty()){
                    boolean isTriangle = false;
                    for(String c : conditions) {
                        for (String w : c.split(" ")) {
                            if (c.contains("трикутник") && w.matches("(([A-Z]\\d*){3})")) {
                                triangleId = w;
                                isTriangle = true;
                                for(char c1: w.toCharArray()){
                                    if(Character.isDigit(c1)) hasDigits = true;
                                }
                                break;
                            }
                        }

                        // Якщо й там не знайшли, тоді трикутник створюємо за замовчуванням
                        if (!isTriangle) {
                            triangleId = "ABC";
                            if(!identifiers.contains("ABC") && !triangleIsCreated){
                                RightIsoscelessTriangle newTriangle = new RightIsoscelessTriangle(triangleId);
                                identifiers.add(triangleId);
                                objects.add(newTriangle);
                            }
                        }
                    }
                }

                String idLeg1 = null, idLeg2 = null;
                if(hasDigits){

                    String identifierRegex = "([A-Z]\\d*)";
                    Pattern pattern = Pattern.compile(identifierRegex);
                    Matcher matcher = pattern.matcher(triangleId);

                    List<String> ids = new ArrayList<>();

                    while (matcher.find()) {
                        ids.add(matcher.group());
                    }
                    idLeg1 = ids.getFirst() + "H";
                    idLeg2 = ids.get(2)+ "H";
                }

                if(hasDigits) identifier = new StringBuilder(idLeg1);
                else identifier = new StringBuilder(triangleId.charAt(0) +"H");
                proj1 = new Segment(identifier.toString(), "hypotenuse-projection");
                identifiers.add(identifier.toString());
                objects.add(proj1);

                if(hasDigits) identifier = new StringBuilder(idLeg2);
                else identifier = new StringBuilder(triangleId.charAt(2) +"H");
                proj2 = new Segment(identifier.toString(), "hypotenuse-projection");
                identifiers.add(identifier.toString());
                objects.add(proj2);

                for(String w : words){
                    if(w.matches("(\\d(√\\d)?(,\\d)?)+")){
                        double numVal = parseNumeric(w);
                        proj1.setLength(numVal);
                        proj2.setLength(numVal);
                    }
                    else if(w.matches("(см|км|м)")){
                        proj1.setMeasure(w);
                        proj2.setMeasure(w);
                    }
                }
            }
        }
    }

    public ArrayList<String> getGraphicsDescription() {
        return graphicsDescription;
    }
}
