package org.jframe.infrastructure.extensions;

/**
 * Created by screw on 2017/5/16.
 */
public enum KnownExceptionCode {
    Session丢失(1001),
    RequestError(1002);

    private final int value;
    private KnownExceptionCode(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
