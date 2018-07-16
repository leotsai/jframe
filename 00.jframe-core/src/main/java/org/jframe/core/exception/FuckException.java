package org.jframe.core.exception;

/**
 * Created by screw on 2017/5/23.
 */
public class FuckException extends KnownException {
    public FuckException() {
        super(ResultCode.ATTACK);
    }

    public FuckException(String message) {
        super(ResultCode.ATTACK, message);
    }


}
