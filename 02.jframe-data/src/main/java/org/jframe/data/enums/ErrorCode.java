package org.jframe.data.enums;


/**
 * Created by Leo on 2018/1/4.
 * 整个项目的错误码编码
 */
public enum ErrorCode {
    NOT_AUTHENTICATED("4001", "未登录或session过期，但是访问的url需要登录"),
    INSUFFICIENT_PERMISSION("4002", "已登录，但是权限不足");

    private final String code;
    private final String text;

    ErrorCode(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
