package org.jframe.infrastructure.sms.templates;

import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.sms.CaptchaUsage;

/**
 * Created by Leo on 2017/11/13.
 * 模版类型:验证码
 模版名称:用户注册验证码
 模版CODE:SMS_109725654
 模版内容:您的验证码为：${code}。您正在使用短信验证码登录，请勿将此验证码泄露于他人。
 申请说明:
 */
public class LoginSmsCaptchaTemplate extends GeneralCaptchaSmsCaptchaTemplate {

    public LoginSmsCaptchaTemplate(String phone, String code){
        super(phone, code);
    }

    @Override
    public String getTemplateCode() {
        return AppContext.getSmsConfig().getIdLogin();
    }

    @Override
    public CaptchaUsage getUsage() {
        return CaptchaUsage.SMS_LOGIN;
    }
}
