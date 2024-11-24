import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Для розв'язування задач
public class Solver {
    private RightIsoscelessTriangle triangle;
    private ArrayList<String> solution;
    private String target;

    public Solver(Shape triangle, String target) {
        this.triangle = (RightIsoscelessTriangle) triangle;
        this.target = target;
        solution = new ArrayList<>();
    }

    public ArrayList<String> solve(){
        switch (target){
            case "height" -> findHeight();
            case "projection" -> findProjections();
            case "projections" -> findProjections();
            case "legs" -> findLegs();
            case "leg" -> findLegs();
            case "median" -> findMedian();
            case "hypotenuse" -> findHypotenuse();
            case "square" -> findSquare();
            case "perimeter" -> findPerimeter();
        }

        return solution;
    }

    private void findPerimeter() {

        double result = 0;
        String measure = "";
        String identifierRegex = "([A-Z]\\d*)";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher leg1Matcher = pattern.matcher(triangle.getLeg1().getId());
        Matcher leg2Matcher = pattern.matcher(triangle.getLeg2().getId());
        Matcher heightMatcher = null;
        Matcher projectionMatcher = null;

        if (!triangle.getProjections().isEmpty())
            projectionMatcher = pattern.matcher(triangle.getProjections().getFirst().getId());
        if (!triangle.getHeights().isEmpty())
            heightMatcher = pattern.matcher(triangle.getHeights().getFirst().getId());

        List<String> leg1Ids = new ArrayList<>();
        List<String> leg2Ids = new ArrayList<>();
        List<String> heightIds = new ArrayList<>();
        List<String> projIds = new ArrayList<>();

        while (leg1Matcher.find()) {
            leg1Ids.add(leg1Matcher.group());
        }
        while (leg2Matcher.find()) {
            leg2Ids.add(leg2Matcher.group());
        }
        while (heightMatcher != null && heightMatcher.find()) {
            heightIds.add(heightMatcher.group());
        }
        while (projectionMatcher != null && projectionMatcher.find()) {
            projIds.add(projectionMatcher.group());
        }

        StringBuilder sb = new StringBuilder();

        if (triangle.getLeg1().getLength() != 0 && triangle.getLeg2().getLength() != 0) {

            // Action 1
            measure = triangle.getLeg1().getMeasure();
            sb.append("Знаходимо катети через гіпотенузу ").append(triangle.getHypotenuse().getId());
            sb.append("\n").append(triangle.getLeg1().getId()).append("*√2 = ");
            double hypotenuse = triangle.getLeg1().getLength()*Math.sqrt(2);
            sb.append(triangle.getLeg1().getLength()).append("*√2 = ").append(hypotenuse).append(" ").append(measure);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 2
            sb.append("Знаходимо периметр через сторони ").append(triangle.getHypotenuse().getId()).append(", ").append(triangle.getLeg1().getId()).append(", ").append(triangle.getLeg2().getId());
            sb.append("\n").append(triangle.getLeg1().getId()).append(" + ").append(triangle.getLeg2().getId())
                    .append(" + ").append(triangle.getHypotenuse().getId()).append(" = ");
            result = triangle.getLeg1().getLength() + triangle.getLeg2().getLength() + hypotenuse;
            sb.append(triangle.getLeg1().getLength()).append(" + ").append(triangle.getLeg2().getLength())
                    .append(" + ").append(hypotenuse).append(" = ").append(result).append(" ").append(measure);
            solution.add(sb.toString());
        }

        else if (triangle.getHypotenuse().getLength() != 0) {

            // Action 1
            measure = triangle.getHypotenuse().getMeasure();
            sb.append("Знаходимо катети через гіпотенузу ").append(triangle.getHypotenuse().getId());
            sb.append("\n").append(triangle.getHypotenuse().getId()).append("*√2/2 = ");
            double leg = triangle.getHypotenuse().getLength() * Math.sqrt(2) / 2;
            sb.append(triangle.getHypotenuse().getLength()).append("*√2/2 = ").append(leg).append(" ").append(measure);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 2
            sb.append("Знаходимо периметр:");
            sb.append("\n2 * ").append(triangle.getLeg1().getId()).append(" + ").append(triangle.getHypotenuse().getId()).append(" = ");
            result = 2 * leg + triangle.getHypotenuse().getLength();
            sb.append("2 * ").append(leg).append(" + ").append(triangle.getHypotenuse().getLength())
                    .append(" = ").append(result).append(" ").append(measure);
            solution.add(sb.toString());
        }

        else if (!triangle.getProjections().isEmpty() && triangle.getProjections().getFirst().getLength() != 0) {

            // Action 1
            measure = triangle.getProjections().getFirst().getMeasure();
            sb.append("Знаходимо гіпотенузу через проекції (").append(triangle.getProjections().getFirst().getId()).append(")");
            sb.append("\n").append(triangle.getProjections().getFirst().getId()).append("*2 = ");
            double hypotenuse = triangle.getProjections().getFirst().getLength() * 2;
            sb.append(triangle.getProjections().getFirst().getLength()).append("*2 = ").append(hypotenuse).append(" ").append(measure);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 2
            sb.append("Знаходимо катети через гіпотенузу ").append(triangle.getHypotenuse().getId());
            sb.append("\n").append(triangle.getHypotenuse().getId()).append("*√2/2 = ");
            double leg = hypotenuse * Math.sqrt(2) / 2;
            sb.append(triangle.getHypotenuse().getLength()).append("*√2/2 = ").append(leg).append(" ").append(measure);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 3
            sb.append("Знаходимо периметр:");
            sb.append("\n2 * ").append(triangle.getLeg2().getId()).append(" + ").append(triangle.getHypotenuse().getId()).append(" = ");
            result = 2 * leg + hypotenuse;
            sb.append("2 * ").append(leg).append(" + ").append(hypotenuse)
                    .append(" = ").append(leg).append(" ").append(measure);
            solution.add(sb.toString());
        }

        else if (!triangle.getHeights().isEmpty() && triangle.getHeights().getFirst().getLength() != 0) {

            // Action 1
            measure = triangle.getHeights().getFirst().getMeasure();
            sb.append("Знаходимо катети через висоту ").append(triangle.getHeights().getFirst().getId());
            sb.append("\n").append(triangle.getHeights().getFirst().getId()).append("*√2 = ");
            double leg = triangle.getHeights().getFirst().getLength()*Math.sqrt(2);
            sb.append(triangle.getHeights().getFirst().getLength()).append("*√2 = ").append(leg).append(" ").append(measure);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 2
            sb.append("Знаходимо гіпотенузу через катет ").append(triangle.getLeg1().getId());
            sb.append("\n").append(triangle.getLeg1().getId()).append("*√2 = ");
            double hypotenuse = leg * Math.sqrt(2);
            sb.append(leg).append("*√2 = ").append(hypotenuse).append(" ").append(measure);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 3
            sb.append("Знаходимо периметр");
            sb.append("\n2 * ").append(triangle.getLeg1().getId()).append(" + ").append(triangle.getHypotenuse().getId())
                    .append(" = ");
            result = 2 * leg + hypotenuse;
            sb.append("2 * ").append(leg).append(" + ").append(hypotenuse)
                    .append(" = ").append(result).append(" ").append(measure);
            solution.add(sb.toString());
        }

        else {
            throw new RuntimeException("Неможливо розв'язати задачу!");
        }
    }


    private void findSquare() {

        double result = 0;
        String measure = "";
        String identifierRegex = "([A-Z]\\d*)";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher leg1Matcher = pattern.matcher(triangle.getLeg1().getId());
        Matcher leg2Matcher = pattern.matcher(triangle.getLeg2().getId());
        Matcher heightMatcher = null;
        Matcher projectionMatcher=null;
        if(!triangle.getProjections().isEmpty()) projectionMatcher = pattern.matcher(triangle.getProjections().getFirst().getId());
        if(!triangle.getHeights().isEmpty()) heightMatcher = pattern.matcher(triangle.getHeights().getFirst().getId());

        List<String> leg1Ids = new ArrayList<>();
        List<String> leg2Ids = new ArrayList<>();
        List<String> heightIds = new ArrayList<>();
        List<String> projIds = new ArrayList<>();

        while (leg1Matcher.find()) {
            leg1Ids.add(leg1Matcher.group());
        }
        while (leg2Matcher.find()) {
            leg2Ids.add(leg2Matcher.group());
        }
        while (heightMatcher != null && heightMatcher.find()) {
            heightIds.add(heightMatcher.group());
        }
        while (projectionMatcher != null && projectionMatcher.find()) {
            projIds.add(projectionMatcher.group());
        }

        StringBuilder sb = new StringBuilder();

        if(triangle.getHypotenuse().getLength() != 0 && !triangle.getHeights().isEmpty() && triangle.getHeights().getFirst().getLength() != 0){

            measure = triangle.getHypotenuse().getMeasure();
            sb.append("Знаходимо площу через гіпотенузу ").append(triangle.getHypotenuse().getId()).append(" і висоту ").append(triangle.getHeights().getFirst().getId());
            sb.append("\n").append(triangle.getHypotenuse().getId()).append(" * ").append(triangle.getHeights().getFirst().getId()).append(" = ");

            result = triangle.getHypotenuse().getLength() * triangle.getHeights().getFirst().getLength() / 2;
            sb.append(triangle.getHypotenuse().getLength()).append(" * ").append(triangle.getHeights().getFirst().getLength()).append(" = ").append(result).append(measure).append("²");
            solution.add(sb.toString());
        }

        else if(triangle.getHypotenuse().getLength() != 0){

            measure = triangle.getHypotenuse().getMeasure();
            // Action 1
            sb.append("Знаходимо катети через гіпотенузу ").append(triangle.getHypotenuse().getId()).append(": ");

            double legsVal = triangle.getHypotenuse().getLength()*Math.sqrt(2)/2;
            sb.append("\n").append(triangle.getHypotenuse().getId()).append("*√2/2 = ").append(triangle.getHypotenuse().getLength()).append("*√2/2 = ").append(legsVal);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 2
            sb.append("Знаходимо площу через катети ").append(triangle.getLeg1().getId()).append(" та ").append(triangle.getLeg2().getId());
            sb.append("\n").append(triangle.getLeg1().getId()).append("*").append(triangle.getLeg2().getId()).append("/2 = ");
            result = legsVal*legsVal/2;
            sb.append("\n").append(triangle.getLeg1().getLength()).append("*").append(triangle.getLeg2().getLength()).append("/2 = ").append(result).append(measure).append("²");
        }

        else if(!triangle.getProjections().isEmpty() && !triangle.getHeights().isEmpty()
                && triangle.getProjections().getFirst().getLength() != 0 && triangle.getHeights().getFirst().getLength() != 0){

            measure = triangle.getProjections().getFirst().getMeasure();
            // Action 1
            sb.append("Знаходимо гіпотенузу через проекцію (").append(triangle.getProjections().getFirst().getId()).append("):");

            double hypotenuseVal = triangle.getProjections().getFirst().getLength() * 2;
            sb.append("\n").append(triangle.getProjections().getFirst().getId()).append("*2 = ").append(triangle.getProjections().getFirst().getLength()).append("*2 = ").append(result);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 2
            sb.append("Знаходимо площу через гіпотенузу ").append(triangle.getHypotenuse().getId()).append(" та висоту ").append(triangle.getHeights().getFirst().getId());
            sb.append("\n").append(triangle.getHypotenuse().getId()).append("*").append(triangle.getHeights().getFirst().getId()).append("/2 = ");
            result = hypotenuseVal * triangle.getHeights().getFirst().getLength() / 2;
            sb.append("\n").append(triangle.getHypotenuse().getLength()).append("*").append(triangle.getHeights().getFirst().getLength()).append("/2 = ").append(result).append(measure).append("²");

            solution.add(sb.toString());
        }

        else if(!triangle.getProjections().isEmpty() && triangle.getProjections().getFirst().getLength() != 0){

            measure = triangle.getProjections().getFirst().getMeasure();
            // Action 1
            sb.append("Знаходимо гіпотенузу через проекцію (").append(triangle.getProjections().getFirst().getId()).append("):");
            sb.append("\n").append(triangle.getProjections().getFirst().getId()).append("*2 = ").append(triangle.getProjections().getFirst().getLength()).append("*2 = ").append(result);
            double hypotenuseVal = triangle.getProjections().getFirst().getLength() * 2;
            sb.append("\n").append(triangle.getProjections().getFirst().getId()).append("*2 = ").append(triangle.getProjections().getFirst().getLength()).append("*2 = ").append(result).append(measure);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 2
            sb.append("Знаходимо катети через гіпотенузу ").append(triangle.getHypotenuse().getId()).append(": ");
            sb.append("\n").append(triangle.getLeg1().getId()).append("*").append(triangle.getLeg2().getId()).append("/2 = ");
            double legsVal = hypotenuseVal*Math.sqrt(2)/2;
            sb.append("\n").append(triangle.getHypotenuse().getId()).append("*√2/2 = ").append(triangle.getHypotenuse().getLength()).append("*√2/2 = ").append(legsVal).append(measure);
            solution.add(sb.toString());
            sb = new StringBuilder();

            // Action 3
            sb.append("Знаходимо площу через катети ").append(triangle.getLeg1().getId()).append(" та ").append(triangle.getLeg2().getId());
            sb.append("\n").append(triangle.getLeg1().getId()).append("*").append(triangle.getLeg2().getId()).append("/2 = ");
            result = legsVal*legsVal/2;
            sb.append("\n").append(triangle.getLeg1().getLength()).append("*").append(triangle.getLeg2().getLength()).append("/2 = ").append(result).append(measure).append("²");
            solution.add(sb.toString());

        }

        else if(!triangle.getHeights().isEmpty()
                && triangle.getHeights().getFirst().getLength() != 0){

            measure = triangle.getHeights().getFirst().getMeasure();
            sb.append("Знаходимо катети через висоту (").append(triangle.getHeights().getFirst().getId()).append("):");
            double leg = triangle.getHeights().getFirst().getLength()*Math.sqrt(2);

            sb.append("Знаходимо площу через катети (").append(triangle.getHeights().getFirst().getId()).append("):");
            sb.append("\n").append(triangle.getHeights().getFirst().getId()).append("*").append(triangle.getHeights().getFirst().getId()).append("/2 = ");
            result = leg*leg/2;
            sb.append("\n").append(leg).append("*").append(leg).append("/2 = ").append(result).append(measure).append("²");
            solution.add(sb.toString());
        }

        else if(triangle.getLeg1().getLength() != 0){

            sb.append("Знаходимо площу через катети ").append(triangle.getLeg1().getId()).append(" та ").append(triangle.getLeg2().getId());
            sb.append("\n").append(triangle.getLeg1().getId()).append("*").append(triangle.getLeg2().getId()).append("/2 = ");
            result = triangle.getLeg1().getLength()*triangle.getLeg2().getLength()/2;
            sb.append("\n").append(triangle.getLeg1().getLength()).append("*").append(triangle.getLeg2().getLength()).append("/2 = ").append(result).append(measure).append("²");
            solution.add(sb.toString());
        }
        else throw new RuntimeException("Неможливо розв'язати задачу!");

    }

    private double findHypotenuse() {
        double result = 0;
        String measure = "";
        String identifierRegex = "([A-Z]\\d*)";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher leg1Matcher = pattern.matcher(triangle.getLeg1().getId());
        Matcher leg2Matcher = pattern.matcher(triangle.getLeg2().getId());
        Matcher heightMatcher = null;
        Matcher projectionMatcher=null;
        if(!triangle.getProjections().isEmpty()) projectionMatcher = pattern.matcher(triangle.getProjections().getFirst().getId());
        if(!triangle.getHeights().isEmpty()) heightMatcher = pattern.matcher(triangle.getHeights().getFirst().getId());

        List<String> leg1Ids = new ArrayList<>();
        List<String> leg2Ids = new ArrayList<>();
        List<String> heightIds = new ArrayList<>();
        List<String> projIds = new ArrayList<>();

        while (leg1Matcher.find()) {
            leg1Ids.add(leg1Matcher.group());
        }
        while (leg2Matcher.find()) {
            leg2Ids.add(leg2Matcher.group());
        }
        while (heightMatcher != null && heightMatcher.find()) {
            heightIds.add(heightMatcher.group());
        }
        while (projectionMatcher != null && projectionMatcher.find()) {
            projIds.add(projectionMatcher.group());
        }

        StringBuilder sb = new StringBuilder();

        if (triangle.getLeg1().getLength() != 0 && triangle.getLeg2().getLength() != 0) {
            measure = triangle.getLeg1().getMeasure();
            sb.append("! Знаходимо гіпотенузу ").append(leg1Ids.getFirst()).append(leg2Ids.getFirst()).append(" через катети (").append(triangle.getLeg1().getId()).append(", ").append(triangle.getLeg2().getId()).append("): ");

            result = triangle.getLeg1().getLength() * Math.sqrt(2);
            sb.append(triangle.getLeg1().getId()).append("*√2 = ").append(triangle.getLeg1().getLength()).append("*√2 = ").append(result);

        }
        else if (!triangle.getHeights().isEmpty() && triangle.getHeights().getFirst().getLength() != 0) {
            measure = triangle.getHeights().getFirst().getMeasure();
            sb.append("! Знаходимо гіпотенузу ").append(triangle.getHypotenuse().getId()).append("через висоту ").append(heightIds.getFirst()).append(", проведену до неї: ");

            result = 2 * triangle.getHeights().getFirst().getLength();
            sb.append("\n").append(triangle.getHeights().getFirst().getId()).append("*2 = ").append(triangle.getHeights().getFirst().getLength()).append("*2 = ").append(result);
        }
        else if(!triangle.getProjections().isEmpty() && triangle.getProjections().getFirst().getLength() != 0){

            measure = triangle.getHeights().getFirst().getMeasure();
            sb.append("! Знаходимо гіпотенузу ").append(triangle.getHypotenuse().getId()).append("через її проекцію ").append(triangle.getProjections().getFirst().getId()).append(": ");

            result = 2 * triangle.getProjections().getFirst().getLength();
            sb.append("\n").append(triangle.getHeights().getFirst().getId()).append("*2 = ").append(triangle.getHeights().getFirst().getLength()).append("*2 = ").append(result);
        }

        else System.out.println("Неможливо розв'язати задачу!");

        solution.add(sb.toString());
        sb = new StringBuilder();
        sb.append("Отже, гіпотенуза ").append(triangle.getHypotenuse().getId()).append(" = ").append(result).append(" ").append(measure);
        solution.add(sb.toString());
        return result;
    }


    private double findMedian() {

        String measure = "";
        double result = 0;
        String identifierRegex = "([A-Z]\\d*)";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher hypotenuseMatcher = pattern.matcher(triangle.getHypotenuse().getId());
        Matcher leg1Matcher = pattern.matcher(triangle.getLeg1().getId());
        Matcher leg2Matcher = pattern.matcher(triangle.getLeg2().getId());
        Matcher medianMetcher = pattern.matcher(triangle.getMedians().getFirst().getId());

        List<String> hypoIds = new ArrayList<>();
        List<String> leg1Ids = new ArrayList<>();
        List<String> leg2Ids = new ArrayList<>();
        List<String> medianIds = new ArrayList<>();

        while (hypotenuseMatcher.find()) {
            hypoIds.add(hypotenuseMatcher.group());
        }
        while (leg1Matcher.find()) {
            leg1Ids.add(leg1Matcher.group());
        }
        while (leg2Matcher.find()) {
            leg2Ids.add(leg2Matcher.group());
        }
        while (medianMetcher.find()) {
            medianIds.add(medianMetcher.group());
        }

        StringBuilder sb = new StringBuilder();

        double hypotenuseVal = 0, legVal = 0;
        if(triangle.getHypotenuse().getLength() == 0){
            hypotenuseVal = findHypotenuse();
        }
        else hypotenuseVal = triangle.getHypotenuse().getLength();

        if(triangle.getLeg1().getLength() == 0){
            legVal = findLegs();
        }
        else legVal = triangle.getLeg1().getLength();

        double semiResult=0;

        sb.append("Знаходимо медіану ").append(triangle.getMedians().getFirst().getId())
                .append(" через гіпотенузу ").append(triangle.getHypotenuse().getId())
                .append(" та 2 катети ").append(triangle.getLeg1().getId()).append(", ").append(triangle.getLeg2().getId()).append(": ");

        if(Objects.equals(medianIds.getFirst(), leg1Ids.getFirst())) {
            sb.append("\n").append("√((2*").append(triangle.getLeg1().getId()).append(")²+(2*").append(triangle.getHypotenuse().getId()).append(")²-").append(triangle.getLeg2().getId()).append("²)/4");
            sb.append("=").append("√((2*").append(triangle.getLeg1().getLength()).append(")²+(2*").append(triangle.getHypotenuse().getLength()).append(")²-").append(triangle.getLeg2().getLength()).append("²)/4");
            semiResult = Math.pow(2*legVal,2)+Math.pow(2*hypotenuseVal,2)-Math.pow(legVal,2);
        }
        else if(Objects.equals(medianIds.getFirst(), leg2Ids.getFirst())) {
            sb.append("\n").append("√(2*").append(triangle.getLeg2().getId()).append(")²+(2*").append(triangle.getHypotenuse().getId()).append(")²-").append(triangle.getLeg1().getId()).append(")²/4");
            sb.append(" = ").append("√(2*").append(triangle.getLeg2().getLength()).append(")²+(2*").append(triangle.getHypotenuse().getLength()).append(")²-").append(triangle.getLeg1().getLength()).append(")²/4");
            semiResult = Math.pow(2*legVal,2)+Math.pow(2*hypotenuseVal,2)-Math.pow(legVal,2);
        }
        else {
            sb.append("\n").append("√(2*").append(triangle.getLeg2().getId()).append(")²+(2*").append(triangle.getLeg1().getId()).append(")²-(").append(triangle.getHypotenuse().getId()).append("²)/4");
            sb.append("=").append("√(2*").append(triangle.getLeg2().getLength()).append(")²+(2*").append(triangle.getLeg1().getLength()).append(")²-(").append(triangle.getHypotenuse().getLength()).append("²)/4");
            semiResult = Math.pow(2*legVal,2)+Math.pow(2*legVal,2)-Math.pow(hypotenuseVal,2);
        }

        result = Math.sqrt(semiResult)/4;
        sb.append("\n Отже, медіана ").append(medianIds.getFirst()).append(medianIds.get(1)).append(" = ").append(result);
        solution.add(sb.toString());

        return result;
    }

    private double findLegs() {

        String measure = "";
        double result = 0;
        String identifierRegex = "([A-Z]\\d*)";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher hypotenuseMatcher = pattern.matcher(triangle.getHypotenuse().getId());
        Matcher leg1Matcher = pattern.matcher(triangle.getLeg1().getId());
        Matcher leg2Matcher = pattern.matcher(triangle.getLeg2().getId());
        Matcher heightMatcher = pattern.matcher(triangle.getHeights().getFirst().getId());
        Matcher projectionMatcher=null;
        if(!triangle.getProjections().isEmpty()) projectionMatcher = pattern.matcher(triangle.getProjections().getFirst().getId());

        List<String> hypoIds = new ArrayList<>();
        List<String> leg1Ids = new ArrayList<>();
        List<String> leg2Ids = new ArrayList<>();
        List<String> projIds = new ArrayList<>();
        List<String> heightsIds = new ArrayList<>();

        while (hypotenuseMatcher.find()) {
            hypoIds.add(hypotenuseMatcher.group());
        }
        while (leg1Matcher.find()) {
            leg1Ids.add(leg1Matcher.group());
        }
        while (leg2Matcher.find()) {
            leg2Ids.add(leg2Matcher.group());
        }
        while (projectionMatcher != null && projectionMatcher.find()) {
            projIds.add(projectionMatcher.group());
        }
        while (projectionMatcher != null && heightMatcher.find()) {
            heightsIds.add(heightMatcher.group());
        }

        StringBuilder sb = new StringBuilder();
        if(!triangle.getHeights().isEmpty() && triangle.getHeights().getFirst().getLength() != 0){

            measure = triangle.getHeights().getFirst().getMeasure();
            sb.append("Знаходимо катети (").append(triangle.getLeg1().getId()).append(", ").append(triangle.getLeg2().getId()).append(") через висоту ").append(triangle.getHeights().getFirst().getId());
            result = triangle.getHeights().getFirst().getLength()*Math.sqrt(2);
            sb.append("\n").append(triangle.getHeights().getFirst().getId()).append("*√2 = ").append(triangle.getHeights().getFirst().getLength()).append("*√2/2 = ").append(result);
        }
        else if(triangle.getHypotenuse().getLength() != 0){

            measure = triangle.getHypotenuse().getMeasure();
            sb.append("Знаходимо катети (").append(triangle.getLeg1().getId()).append(", ").append(triangle.getLeg2().getId()).append(") через гіпотенузу ").append(triangle.getHypotenuse().getId());
            result = triangle.getHypotenuse().getLength()*Math.sqrt(2)/2;
            sb.append("\n").append(triangle.getHypotenuse().getId()).append("*√2 = ").append(triangle.getHypotenuse().getLength()).append("*√2/2 = ").append(result);
        }
        else System.out.println("Неможливо розв'язати задачу!");

        solution.add(sb.toString());
        sb = new StringBuilder();
        sb.append("Отже, катети (").append(triangle.getLeg1().getId()).append(", ").append(triangle.getLeg2().getId()).append(") дорівнюють ").append(result).append(" ").append(measure);
        solution.add(sb.toString());
        return result;
    }

    private double findProjections() {

        String measure = "";
        String identifierRegex = "([A-Z]\\d*)";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher hypotenuseMatcher = pattern.matcher(triangle.getHypotenuse().getId());
        Matcher legMatcher = pattern.matcher(triangle.getLeg1().getId());
        Matcher heightMatcher = pattern.matcher(triangle.getHeights().getFirst().getId());
        Matcher projectionMatcher=null;
        if(!triangle.getProjections().isEmpty()) projectionMatcher = pattern.matcher(triangle.getProjections().getFirst().getId());

        List<String> hypoIds = new ArrayList<>();
        List<String> leg1Ids = new ArrayList<>();
        List<String> projIds = new ArrayList<>();
        List<String> heightsIds = new ArrayList<>();

        while (hypotenuseMatcher.find()) {
            hypoIds.add(hypotenuseMatcher.group());
        }
        while (legMatcher.find()) {
            leg1Ids.add(legMatcher.group());
        }
        while (projectionMatcher != null && projectionMatcher.find()) {
            projIds.add(projectionMatcher.group());
        }
        while (heightMatcher.find()) {
            heightsIds.add(heightMatcher.group());
        }

        double result = 0;
        StringBuilder sb = new StringBuilder();

        if(triangle.getHypotenuse().getLength() != 0){

            measure = triangle.getHypotenuse().getMeasure();
            if(!projIds.isEmpty()) sb.append("Знаходимо з-ння проекцій (").append(triangle.getProjections().getFirst().getId()).append(", ").append(triangle.getProjections().get(1).getId()).append(") через гіпотенузу ").append(triangle.getHypotenuse().getId());
            else if(!triangle.getHeights().isEmpty()) sb.append("Знаходимо з-ння проекцій (").append(hypoIds.getFirst()).append(heightsIds.get(1)).append(") через гіпотенузу ").append(triangle.getHypotenuse().getId());

            result = triangle.getHypotenuse().getLength()/2;
            sb.append("\n").append(triangle.getHypotenuse().getId()).append("/2 = ").append(triangle.getHypotenuse().getLength()).append("/2 = ").append(result);
        }
        else if(triangle.getLeg1().getLength() != 0){


            measure = triangle.getLeg1().getMeasure();
            if(triangle.getProjections().isEmpty()) sb.append("! Знаходимо з-ння проекцій (").append(leg1Ids.getFirst()).append(heightsIds.get(1)).append(") через катети ").append(triangle.getLeg1().getId()).append(": ");
            else sb.append("! Знаходимо з-ння проекцій (").append(projIds.getFirst()).append(projIds.get(1)).append(") через катети ").append(triangle.getLeg1().getId()).append(": ");

            result = triangle.getLeg1().getLength()*Math.sqrt(2)/2;
            sb.append("\n").append(triangle.getLeg1()).append("*√2/2 = ").append(triangle.getLeg1().getLength()).append("*√2/2 = ").append(result);
        }
        else System.out.println("Неможливо розв'язати задачу!");

        solution.add(sb.toString());
        sb = new StringBuilder();
        sb.append("Отже, ").append(triangle.getProjections().getFirst().getId()).append(", ").append(triangle.getProjections().get(1).getId()).append(" = ").append(result).append(" ").append(measure);
        solution.add(sb.toString());
        return result;
    }

    private double findHeight() {

        double result = 0;
        String measure = "";
        String identifierRegex = "([A-Z]\\d*)";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher hypotenuseMatcher = pattern.matcher(triangle.getHypotenuse().getId());
        Matcher legMatcher = pattern.matcher(triangle.getLeg1().getId());
        Matcher heightMatcher = pattern.matcher(triangle.getHeights().getFirst().getId());
        Matcher projectionMatcher=null;
        if(!triangle.getProjections().isEmpty()) projectionMatcher = pattern.matcher(triangle.getProjections().getFirst().getId());

        List<String> hypoIds = new ArrayList<>();
        List<String> legIds = new ArrayList<>();
        List<String> projIds = new ArrayList<>();
        List<String> heightsIds = new ArrayList<>();

        while (hypotenuseMatcher.find()) {
            hypoIds.add(hypotenuseMatcher.group());
        }
        while (legMatcher.find()) {
            legIds.add(legMatcher.group());
        }
        while (projectionMatcher != null && projectionMatcher.find()) {
            projIds.add(projectionMatcher.group());
        }
        while (heightMatcher.find()) {
            heightsIds.add(heightMatcher.group());
        }
        StringBuilder sb = new StringBuilder();
        if(triangle.getHypotenuse().getLength() != 0){

            measure = triangle.getHypotenuse().getMeasure();
            if(projIds.isEmpty()) sb.append("! Знаходимо з-ння проекцій (").append(hypoIds.getFirst()).append(heightsIds.get(1)).append(")").append(" через гіпотенузу ").append(triangle.getHypotenuse().getId()).append(": ");
            else sb.append("! Знаходимо з-ння проекцій (").append(projIds.getFirst()).append(projIds.get(1)).append(") через гіпотенузу ").append(triangle.getHypotenuse().getId()).append(": ");

            result = triangle.getHypotenuse().getLength()/2;
            sb.append("\n").append(triangle.getHypotenuse().getId()).append("/2 = ").append(triangle.getHypotenuse().getLength()).append("/2 = ").append(result);

        }
        else if(triangle.getLeg1().getLength() != 0){

            if(triangle.getProjections().isEmpty()) sb.append("! Знаходимо з-ння проекцій (").append(legIds.getFirst()).append("H) через катети ").append(triangle.getLeg1().getId()).append(": ");
            else sb.append("! Знаходимо з-ння проекцій (").append(projIds.getFirst()).append(projIds.get(1)).append(") через катети ").append(triangle.getLeg1().getId()).append(": ");

            measure = triangle.getLeg1().getMeasure();
            result = triangle.getLeg1().getLength()*Math.sqrt(2)/2;
            sb.append("\n").append(triangle.getLeg1().getId()).append("*√2/2 = ").append(triangle.getLeg1().getLength()).append("*√2/2 = ").append(result);
        }
        else System.out.println("Неможливо розв'язати задачу!");

        solution.add(sb.toString());
        sb = new StringBuilder();
        sb.append("Оскільки в прям. рівнобедр. трикутнику висота, проведена до гіпотенузи, є й медіаною й бісектрисою: ");
        sb.append("\nВисота ").append(triangle.getHeights().getFirst().getId()).append(" = ").append(result).append(" ").append(measure);
        solution.add(sb.toString());
        return result;
    }
}
