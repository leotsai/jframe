package org.jframe.data.enums;

import org.jframe.core.extensions.JList;

import java.util.Objects;

/**
 * Created by leo on 2017/5/15.
 */
public enum CaptchaUsage {
    UNKNOWN(0, "未知"),
    REGISTER(1, "注册"),
    RESET_PASSWORD(2, "重设密码"),
    RESET_PAY_PASSWORD(3, "重设支付密码"),
    SMS_LOGIN(4, "短信登陆");

    public static final String Doc = "0：未知；1：注册；2：重设密码；3：重设支付密码；4：短信登陆";

    private int value;
    private String text;

    CaptchaUsage(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public static CaptchaUsage from(Integer value) {
        CaptchaUsage usage = JList.from(CaptchaUsage.values()).firstOrNull(x -> Objects.equals(x.getValue(), value));
        return usage == null ? CaptchaUsage.UNKNOWN : usage;
    }

}
