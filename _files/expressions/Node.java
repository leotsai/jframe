package yilianshe.core.expressions;

import yilianshe.core.extensions.JList;
import yilianshe.core.extensions.KnownException;

import java.lang.reflect.Method;

/**
 * Created by Leo on 2018/9/28.
 */
public abstract class Node {
    private Class clazz;
    private Method method;
    private Node next;
    private JList<Node> parameters;

    public Object run(Object subject) throws Exception {
        if (this instanceof ValueNode) {
            return ((ValueNode)this).getValue();
        }

        subject = this.method.invoke(subject, this.getParameterValues());
        Node next = this.next;
        while (next != null) {
            subject = next.run(subject);
            next = next.next;
        }
        return subject;
    }

    private Object[] getParameterValues() throws Exception {
        if (this.parameters == null || this.parameters.size() == 0) {
            return null;
        }
        Object[] values = new Object[this.parameters.size()];
        for (int i = 0; i < this.parameters.size(); i++) {
            values[i] = this.parameters.get(i).run(null);
        }
        return values;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public JList<Node> getParameters() {
        return parameters;
    }

    public void setParameters(JList<Node> parameters) {
        this.parameters = parameters;
    }
}
