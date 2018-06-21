package org.jframe.infrastructure.extensions;

/**
 * Created by screw on 2017/5/23.
 */
public class FuckException extends KnownException {

    public FuckException(String message) {
        super(message);
    }

    public FuckException() {
        super("fuck");
    }
}
