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

        return cleanedCommands.toString();
    }

    private String serializeNode(AST node) {
        StringBuilder builder = new StringBuilder();
        switch (node.getType()) {
            case "TRIANGLE_ACTION":
                builder.append(serializeTriangles(node));
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

    private String serializeTriangles(AST node) {
        StringBuilder builder = new StringBuilder();
        for (AST triangle : node.getChildren().getFirst().getChildren()) {
            List<AST> ids = triangle.getChildren();
            String id1 = ids.get(0).getValue();
            String id2 = ids.get(1).getValue();
            String id3 = ids.get(2).getValue();
            builder.append(String.format("drawTriangle(%s, %s, %s)%n", id1, id2, id3));
        }
        return builder.toString();
    }

    private String serializeLines(AST node) {
        StringBuilder builder = new StringBuilder();

        // Identify if it's a HEIGHT or MEDIAN by checking the inner node type
        for (AST lineAction : node.getChildren()) {
            if (lineAction.getType().equals("HEIGHT_LIST") || lineAction.getType().equals("BISECTOR_LIST")) {
                builder.append(serializeHeights(lineAction));
            } else if (lineAction.getType().equals("MEDIAN_LIST")) {
                builder.append(serializeMedians(lineAction));
            }
        }

        return builder.toString();
    }

    private String serializeHeights(AST node) {
        StringBuilder builder = new StringBuilder();
        for (AST height : node.getChildren()) {
            String id1 = height.getChildren().getFirst().getValue() + height.getChildren().get(1).getValue(); // ID node
            String id2 = extractSegment(height.getChildren().get(2)); // SEGMENT node
            builder.append(String.format("drawHeight(%s, %s)%n", id1, id2));
        }
        return builder.toString();
    }

    private String serializeMedians(AST node) {
        StringBuilder builder = new StringBuilder();
        for (AST median : node.getChildren()) {
            String id1 = median.getChildren().getFirst().getValue() + median.getChildren().get(1).getValue(); // ID node
            String id2 = extractSegment(median.getChildren().get(2)); // SEGMENT node
            builder.append(String.format("drawMedian(%s, %s)%n", id1, id2));
        }
        return builder.toString();
    }

    private String extractSegment(AST segmentNode) {
        List<AST> ids = segmentNode.getChildren();
        String id1 = ids.get(0).getValue();
        String id2 = ids.get(1).getValue();
        return String.format("%s%s", id1, id2);
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
