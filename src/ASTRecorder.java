import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ASTRecorder {

    AST tree;

    public ASTRecorder(AST tree) {
        this.tree = tree;
    }

    public String recordAST() {
        StringBuilder builder = new StringBuilder();
        for (AST child : tree.getChildren().getFirst().getChildren()) {
            builder.append(serializeNode(child)).append("\n");
        }
        String commands = builder.toString();
        String[] lines = commands.split("\n");
        StringBuilder cleanedCommands = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (!trimmedLine.isEmpty()) cleanedCommands.append(trimmedLine).append("\n");
        }

        String readyCommands = cleanedCommands.toString();
        readyCommands = readyCommands.replace(";", "");
        readyCommands = clearDuplicates(readyCommands);
        return readyCommands;

    }

    private String clearDuplicates(String commands) {
        Set<String> uniquePoints = new HashSet<>();
        Set<String> uniqueCircles = new HashSet<>();
        Set<String> uniquePairs = new HashSet<>();

        StringBuilder result = new StringBuilder();
        String[] lines = commands.split("\n");

        for (String line : lines) {
            String cleanedLine = line.trim();

            if (cleanedLine.startsWith("drawPoint")) {
                String id = extractIdentifier(cleanedLine, 0);
                if (uniquePoints.add(id)) {
                    result.append(cleanedLine).append(";\n");
                }
            } else if (cleanedLine.startsWith("drawCircle")) {
                String id = extractIdentifier(cleanedLine, 0);
                if (uniqueCircles.add(id)) {
                    result.append(cleanedLine).append(";\n");
                }
            } else if (cleanedLine.startsWith("drawDiameter") || cleanedLine.startsWith("drawChord")
                    || cleanedLine.startsWith("drawRadius") || cleanedLine.startsWith("drawSegment")) {
                String id1 = extractIdentifier(cleanedLine, 0);
                String id2 = extractIdentifier(cleanedLine, 1);


                String pair = id1.compareTo(id2) < 0 ? id1 + "," + id2 : id2 + "," + id1;

                if (uniquePairs.add(pair)) {
                    result.append(cleanedLine).append(";\n");
                }
            }
        }

        return result.toString();
    }

    private String extractIdentifier(String command, int index) {
        int openParenIndex = command.indexOf('(');
        int closeParenIndex = command.indexOf(')');
        String params = command.substring(openParenIndex + 1, closeParenIndex);

        String[] ids = params.split(",");
        if (index < ids.length) {
            return ids[index].trim();
        }
        return "";
    }

    private String serializeNode(AST node) {
        StringBuilder builder = new StringBuilder();
        switch (node.getType()) {
            case "POINT_ACTION":
                builder.append(serializePoints(node));
                break;
            case "CIRCLE_ACTION":
                builder.append(serializeCircles(node));
                break;
            case "LINE_ACTION":
                builder.append(serializeLines(node));
                break;
            default:
                for (AST child : node.getChildren()) {
                    builder.append(serializeNode(child));
                }
                break;
        }
        return builder.toString();
    }

    private String serializePoints(AST node) {
        StringBuilder builder = new StringBuilder();
        for (AST pointNode : node.getChildren()) {

            for (AST child : pointNode.getChildren()) {
                String id;
                String x = null;
                String y = null;
                AST idElem = child.getChildren().getFirst();
                id = idElem.getValue();

                AST coord;
                if(child.getChildren().size()>1){
                    coord = child.getChildren().get(1);
                    if ("COORDINATE".equals(coord.getType())) {
                        List<AST> coordValues = coord.getChildren();
                        x = coordValues.getFirst().getValue();
                        y = coordValues.get(1).getValue();
                    }
                }

                if (x == null && y == null){
                    Random random = new Random();
                    int generatedX = random.nextInt(-GraphicWindow.MAX_ABSCISSA,GraphicWindow.MAX_ABSCISSA+1);
                    int generatedY = random.nextInt(-GraphicWindow.MAX_ORDINATE,GraphicWindow.MAX_ORDINATE+1);

                    builder.append(String.format("drawPoint(%s, %s, %s)%n", id, generatedX, generatedY));
                }
                else builder.append(String.format("drawPoint(%s, %s, %s)%n", id, x, y));

            }

        }
        return builder.toString();
    }

    private String serializeCircles(AST node) {

        StringBuilder builder = new StringBuilder();

        List<AST> circles = node.getChildren().getFirst().getChildren();

        String id;
        String radius;
        for (AST child : circles) {

            AST idElem = child.getChildren().getFirst();

            id = idElem.getValue();

            if (child.getChildren().size() == 1) {
                Random random = new Random();
                int rad = random.nextInt(1,GraphicWindow.MAX_ORDINATE-14);
                builder.append(String.format("drawCircle(%s, %s);%n", id, rad));
            }
            else{
                AST radiusElem = child.getChildren().get(1);
                radius = radiusElem.getValue();
                builder.append(String.format("drawCircle(%s, %s);%n", id, radius));
            }

        }
        return builder.toString();
    }

    private String serializeLines(AST node) {
        StringBuilder builder = new StringBuilder();
        for (AST lineNode : node.getChildren()) {

            if(lineNode.getType().equals("DIAMETER_LIST")){

                for(AST diameter : lineNode.getChildren()){

                    String startId;
                    String endId;
                    String circleId;
                    startId = diameter.getChildren().getFirst().getValue();
                    endId = diameter.getChildren().get(1).getValue();
                    circleId = diameter.getChildren().get(2).getValue();

                    builder.append(String.format("drawDiameter(%s, %s, %s);%n", startId, endId, circleId));
                }
            }
            else if(lineNode.getType().equals("CHORD_LIST")){

                for(AST chord : lineNode.getChildren()){

                    String startId = chord.getChildren().getFirst().getValue();
                    String endId = chord.getChildren().get(1).getValue();
                    String circleId = chord.getChildren().get(2).getValue();

                    builder.append(String.format("drawChord(%s, %s, %s);%n", startId, endId, circleId));
                }
            }
            else if(lineNode.getType().equals("RADIUS_LIST")){

                for(AST radius : lineNode.getChildren()){

                    String centerId = radius.getChildren().getFirst().getValue();
                    String endId = radius.getChildren().get(1).getValue();

                    builder.append(String.format("drawRadius(%s, %s, %s);%n", centerId, endId, centerId));
                }
            }
            else if(lineNode.getType().equals("SEGMENT_LIST")){

                for(AST radius : lineNode.getChildren()){

                    String startId = radius.getChildren().getFirst().getValue();
                    String endId = radius.getChildren().get(1).getValue();

                    builder.append(String.format("drawSegment(%s, %s);%n", startId, endId));
                }
            }
        }
        return builder.toString();
    }

    public void saveToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + filePath);
            e.printStackTrace();
        }
    }
}