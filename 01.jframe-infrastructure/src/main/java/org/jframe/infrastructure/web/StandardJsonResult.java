package org.jframe.infrastructure.web;

/**
 * Created by Leo on 2017/1/9.
 */
public class StandardJsonResult<T> {
    private boolean success;
    private String message;
    private int code;
    private T value;

    // custom code ----------------------------------------------

    public void fail(String message, int code){
        this.code = code;
        this.success = false;
        this.message = message;
        this.value = null;
    }

    public void fail(String message){
        this.fail(message, 0);
    }

    public void succeed(T value, String message, int code){
        this.value = value;
        this.message = message;
        this.code = code;
        this.success = true;
    }

    public void succeed(T value, String message){
        this.succeed(value, message, 0);
    }

    public void succeed(T value, int code){
        this.succeed(value, null, code);
    }

    public void succeed(T value){
        this.succeed(value, null, 0);
    }


    //----------------------------------------------

    public boolean getSuccess(){
        return this.success;
    }

    public String getMessage(){
        return this.message;
    }

    public int getCode(){
        return this.code;
    }

    public T getValue(){
        return this.value;
    }

    public void setSuccess(boolean value){
        this.success = value;
    }

    public void setMessage(String value){
        this.message = value;
    }

    public void setCode(int value){
        this.code = value;
    }

    public void setValue(T value){
        this.value = value;
    }

    public StandardJsonResult(){
        this.code = 0;
    }

}
