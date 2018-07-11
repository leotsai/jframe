package org.jframe.services.dto;

import org.jframe.core.helpers.StringHelper;
import org.jframe.data.enums.CaptchaUsage;

import java.util.Date;

/**
 * @author qq
 * @date 2018/7/10
 */
public class SmsCaptcha {
    private String phone;
    private String code;
    private CaptchaUsage usage;
    private int tries;

    public SmsCaptcha() {

    }

    public SmsCaptcha(String phone, String code, CaptchaUsage usage) {
        this.phone = phone;
        this.code = code;
        this.usage = usage;
        this.tries = 0;
    }

    public void tried() {
        this.tries = this.tries + 1;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CaptchaUsage getUsage() {
        return usage;
    }

    public void setUsage(CaptchaUsage usage) {
        this.usage = usage;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

}
