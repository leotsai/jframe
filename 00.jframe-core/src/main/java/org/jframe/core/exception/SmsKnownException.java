package org.jframe.core.exception;

/**
 * @author qq
 * @date 2018/7/10
 */
public class SmsKnownException extends KnownException {
    public SmsKnownException(String message) {
        super(message);
    }

    public SmsKnownException(String message, String code) {
        super(message, code);
    }
}
