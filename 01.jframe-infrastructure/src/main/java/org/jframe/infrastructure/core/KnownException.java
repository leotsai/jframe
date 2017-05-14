package org.jframe.infrastructure.core;

/**
 * Created by Leo on 2017/1/5.
 */
public class KnownException extends Exception {
    private int code;
    public int getCode(){
        return this.code;
    }

    public KnownException(String message){
        super(message);
    }

    public KnownException(String message, int code){
        super(message);
        this.code = code;
    }
}
