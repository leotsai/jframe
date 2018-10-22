package yilianshe.core.expressions;

/**
 * Created by Leo on 2018/9/29.
 */
public class TestNode {

    private final static TestNode instance;
    static {
        instance = new TestNode();
    }

    private TestNode(){

    }

    public static TestNode getInstance(){
        return instance;
    }

    public String say(String name, Integer age){
        return "hello " + name +", your age is: " + age;
    }

}
