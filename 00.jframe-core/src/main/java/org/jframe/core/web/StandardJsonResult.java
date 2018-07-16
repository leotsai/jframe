package org.jframe.core.web;

import org.jframe.core.exception.ResultCode;

/**
 * Created by Leo on 2017/1/9.
 */
public class StandardJsonResult<T> {
    private boolean success;
    private String message;
    private String code;
    private T value;

    public void fail(ResultCode code) {
        this.success = false;
        this.code = code.getCode();
        this.message = code.getPrompt();
        this.value = null;
    }

    public void fail(ResultCode code, String message) {
        this.success = false;
        this.code = code.getCode();
        this.message = message;
        this.value = null;
    }

    public void succeed(T value, String message) {
        this.value = value;
        this.message = message;
        this.code = ResultCode.SUCCESS.getCode();
        this.success = true;
    }

    public void succeed(T value) {
        this.succeed(value, null);
    }

    public void succeed() {
        this.succeed(null, null);
    }


    //----------------------------------------------

    public boolean getSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }

    public T getValue() {
        return this.value;
    }

    public void setSuccess(boolean value) {
        this.success = value;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public void setCode(String value) {
        this.code = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public StandardJsonResult() {
        this.code = "0";
    }

}
