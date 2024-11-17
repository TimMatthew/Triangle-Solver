import java.util.ArrayList;
import java.util.List;

public class AST {
    private String type;
    private List<AST> children = new ArrayList<>();
    private String value;

    public AST(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public AST(String type) {
        this(type, null);
    }

    public void addChild(AST child) {
        children.add(child);
    }


    public String getType() {
        return type;
    }

    public List<AST> getChildren() {
        return children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int indentLevel) {
        StringBuilder builder = new StringBuilder();
        String indent = "  ".repeat(indentLevel);

        builder.append(indent).append(type).append("\n");
        builder.append(indent).append("{\n");

        if (value != null) {
            builder.append(indent).append("  ").append("value: ").append(value).append("\n");
        }

        for (AST child : children) {
            builder.append(child.toString(indentLevel + 1));
        }

        builder.append(indent).append("}\n");
        return builder.toString();
    }
}