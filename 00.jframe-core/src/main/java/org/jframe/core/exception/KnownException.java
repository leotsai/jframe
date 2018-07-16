package org.jframe.core.exception;

/**
 * Created by leo on 2017-10-09.
 */
public class KnownException extends RuntimeException {
    private ResultCode resultCode;

    public KnownException(ResultCode resultCode) {
        super(resultCode.getPrompt());
        this.resultCode = resultCode;
    }

    public KnownException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public KnownException(String message) {
        super(message);
        this.resultCode = ResultCode.BUSINESS;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
