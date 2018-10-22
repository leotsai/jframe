package yilianshe.core.expressions;

import yilianshe.core.extensions.JList;
import yilianshe.core.extensions.KnownException;
import yilianshe.core.helpers.StringHelper;

/**
 * Created by Leo on 2018/9/28.
 */
public class ExpressionHelper {

    public static boolean isValue(String input) {
        if (StringHelper.isNullOrWhitespace(input)) {
            return true;
        }
        if (input.startsWith("\"")) {
            return true;
        }
        JList<String> numbers = JList.splitByComma("0,1,2,3,4,5,6,7,8,9");
        if (numbers.any(n -> input.startsWith(n))) {
            return true;
        }
        return false;
    }

    public static Class getValueClass(String value) {
        if (value.startsWith("\"")) {
            return String.class;
        }
        if (value.equals("true") || value.equals("false")) {
            return boolean.class;
        }
        JList<String> numbers = JList.splitByComma("0,1,2,3,4,5,6,7,8,9");
        if (numbers.any(n -> value.startsWith(n))) {
            return int.class;
        }
        if(value.startsWith("(")){
            String type = value.substring(1, value.indexOf(")"));
            switch(type){
                case "long":
                    return long.class;
                case "byte":
                    return byte.class;
                case "short":
                    return short.class;
                case "float":
                    return float.class;
                case "double":
                    return double.class;
                case "char":
                    return char.class;
            }
        }

        String message = "Only String/boolean/int classes supported in parameters. "
                + "\n If the parameter is a class, please use Class.forName."
                + "\n If the parameter is Integer or Boolean, please use java.lang.Integer.valueof or java.lang.Boolean.valueof"
                + "\n If the parameter is long, you can pass (long)123 as the parameter value.";
        throw new RuntimeException(message);
    }

    public static String getFirstBracketValue(String input) {
        int rest = 1;
        int index = 0;
        while(index < input.length()){
            char c = input.charAt(index);
            if (c == '(') {
                rest++;
            } else if (c == ')') {
                rest--;
            }
            if (rest == 0) {
                break;
            }
            index++;
        }
        return input.substring(0, index);
    }

    public static JList<String> getParameters(String input) {
        if (StringHelper.isNullOrWhitespace(input)) {
            return new JList<>();
        }
        JList<String> paras = new JList<>();
        int match = 0;
        StringBuilder sb = new StringBuilder();
        while (true) {
            int indexComma = input.indexOf(",");
            if (indexComma < 0) {
                sb.append(input);
                paras.add(sb.toString());
                break;
            }
            int indexL = input.indexOf("(");
            int indexR = input.indexOf(")");

            indexL = indexL == -1 ? Integer.MAX_VALUE : indexL;
            indexR = indexR == -1 ? Integer.MAX_VALUE : indexR;
            indexComma = indexComma == -1 ? Integer.MAX_VALUE : indexComma;

            if (indexComma < indexL && indexComma < indexR) {
                if (match <= 0) {
                    sb.append(input.substring(0, indexComma));
                    paras.add(sb.toString());
                    sb = new StringBuilder();
                    input = input.substring(indexComma + 1);
                    continue;
                }
            }

            if (indexL < indexR) {
                match++;
            } else {
                match--;
            }

            int index = Math.min(indexL, indexR);
            sb.append(input.substring(0, index + 1));
            input = input.substring(index + 1);
        }
        return paras.select(x -> x.trim());
    }

    public static Class getRealClass(String expressionBody) {
        if (ExpressionHelper.isValue(expressionBody)) {
            return ExpressionHelper.getValueClass(expressionBody);
        }
        Class clazz;
        JList<String> childrenClasses = new JList<>();
        String path = new String(expressionBody);
        while (true) {
            if (StringHelper.isNullOrWhitespace(path)) {
                throw new KnownException("expression path is null or empty");
            }
            try {
                clazz = Class.forName(path);
                break;
            } catch (ClassNotFoundException ex) {
                int index = path.lastIndexOf(".");
                if (index < 0) {
                    throw new KnownException("cannot load class");
                }
                childrenClasses.add(path.substring(index + 1));
                path = path.substring(0, index);
            }
        }
        if (childrenClasses.size() > 0) {
            for (String childName : childrenClasses) {
                clazz = JList.from(clazz.getClasses()).firstOrNull(x -> x.getSimpleName().equals(childName));
            }
        }
        return clazz;
    }

}
