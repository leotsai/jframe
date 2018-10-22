package yilianshe.core.expressions;

import yilianshe.core.extensions.JList;
import yilianshe.core.extensions.KnownException;
import yilianshe.core.helpers.StringHelper;

import java.lang.reflect.Method;

/**
 * Created by Leo on 2018/9/29.
 */
public class Code {
    private String body;
    private String method;
    private JList<Code> parameters;
    private Code next;
    private Code parent;

    public Node compile() throws Exception {
        if(this.parent == null && ExpressionHelper.isValue(this.body)){
            return new ValueNode(this.body);
        }

        Node node = new ClassNode();
        node.setClazz(this.parent !=null ? this.parent.getRealClass() : this.getRealClass());
        if (!StringHelper.isNullOrWhitespace(this.method)) {
            node.setMethod(this.getRealMethod(node.getClazz()));
        }
        if (this.next != null) {
            node.setNext(this.next.compile());
        }
        if (this.parameters != null && this.parameters.size() > 0) {
            node.setParameters(this.parameters.select(x -> {
                try {
                    return x.compile();
                } catch (Exception ex) {
                    return null;
                }
            }));
        }
        return node;
    }

    private Method getRealMethod(Class clazz) throws Exception{
        return clazz.getMethod(this.method, this.getParameterClasses());
    }

    private Class getRealClass(){
        return ExpressionHelper.getRealClass(this.body);
    }

    private Class[] getParameterClasses() {
        if (this.parameters == null || this.parameters.size() == 0) {
            return null;
        }
        Class[] list = new Class[this.parameters.size()];
        for (int i = 0; i < this.parameters.size(); i++) {
            list[i] = this.parameters.get(i).getRealClass();
        }
        return list;
    }

    public static Code parse(String expression) {
        Code code = new Code();
        int bracketIndex = expression.indexOf("(");
        if (bracketIndex == -1) {
            code.body = expression;
        } else {
            String partLeft = expression.substring(0, bracketIndex);
            String paraValue = ExpressionHelper.getFirstBracketValue(expression.substring(bracketIndex + 1));
            JList<String> paras = ExpressionHelper.getParameters(paraValue);
            int dotIndex = partLeft.lastIndexOf(".");
            code.body = partLeft.substring(0, dotIndex);
            code.method = partLeft.substring(dotIndex + 1);
            if (paras.size() > 0) {
                code.parameters = new JList<>();
                paras.forEach(x -> code.parameters.add(parse(x)));
            }
            String next = expression.substring(bracketIndex + paraValue.length() + 2);
            if (!StringHelper.isNullOrWhitespace(next)) {
                code.next = parse(next);
                code.next.parent = code;
            }
        }
        return code;
    }


    public static void main(String[] args) throws Exception {
        String expr = "yilianshe.core.expressions.ExpressionHelper.add200((long)123)";

        String xx = ExpressionHelper.getFirstBracketValue("abc.fuck(123))");
        String x2x = ExpressionHelper.getFirstBracketValue("(long)123)");

        System.out.println(Code.parse(expr).compile().run(null));

        System.out.println("done. " );

    }

}
