package org.jframe.infrastructure.sms.templates;

import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.sms.CaptchaUsage;

/**
 * Created by Leo on 2017/11/13.
 模版类型:验证码
 模版名称:找回登录密码验证码
 模版CODE:SMS_110840257
 模版内容:您的验证码为：${code}。您正在通过短信找回登录密码，请勿将此验证码泄露于他人。
 申请说明:找回登录密码验证码
 */
public class FindPasswordSmsCaptchaTemplate extends GeneralCaptchaSmsCaptchaTemplate {

    public FindPasswordSmsCaptchaTemplate(String phone, String code){
        super(phone, code);
    }

    @Override
    public String getTemplateCode() {
        return AppContext.getSmsConfig().getIdFindPassword();
    }

    @Override
    public CaptchaUsage getUsage() {
        return CaptchaUsage.RESET_PASSWORD;
    }
}
