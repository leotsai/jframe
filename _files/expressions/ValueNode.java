package yilianshe.core.expressions;

/**
 * Created by Leo on 2018/9/28.
 */
public class ValueNode extends Node {

    private final String value;

    public ValueNode(String value) {
        this.value = value;
    }

    public Object getValue() {
        if (this.value == null || this.value.equals("")) {
            return null;
        }
        if (this.value.startsWith("\"")) {
            return this.value.substring(1, this.value.length() - 1);
        }
        if (this.value.equals("true") || this.value.equals("false")) {
            return this.value.equals("true");
        }
        if (this.value.contains(".")) {
            return Float.parseFloat(this.value);
        }
        return Integer.parseInt(this.value);
    }

}
