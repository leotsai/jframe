package org.jframe.core.exception;


/**
 * Created by Leo on 2018/1/4.
 * 整个项目的错误码编码
 */
public enum ResultCode {
    ATTACK("-1", "恶意攻击", "富强，民主，文明，和谐，自由，平等，公正，法制，爱国，敬业，诚信，友善"),
    SUCCESS("0", "成功", "操作成功"),
    SYSTEM("1000", "系统异常", "网路繁忙..."),
    BUSINESS("2000", "业务异常", "出错了..."),
    WRONG_PASSWORD("2001", "密码错误，用于判断验证码", "密码错误"),
    NOT_AUTHENTICATED("4001", "未登录或session过期，但是访问的url需要登录", "请先登录"),
    INSUFFICIENT_PERMISSION("4002", "已登录，但是权限不足", "您的权限不足");

    private final String code;
    private final String comment;
    private final String prompt;

    ResultCode(String code, String comment, String prompt) {
        this.code = code;
        this.comment = comment;
        this.prompt = prompt;
    }

    public String getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }

    public String getPrompt() {
        return prompt;
    }
}
