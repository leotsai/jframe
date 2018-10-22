package yilianshe.core.expressions;

import yilianshe.core.extensions.JList;

import java.lang.reflect.Method;

/**
 * Created by Leo on 2018/9/28.
 */
public class ClassNode extends Node {
    private Class clazz;
    private Method method;
    private JList<Node> parameters;

}
