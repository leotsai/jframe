package org.jframe.infrastructure.sms.templates;

import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.sms.CaptchaUsage;

/**
 * Created by Leo on 2017/11/13.
 * 模版类型:验证码
 模版名称:用户注册验证码
 模版CODE:SMS_109725654
 模版内容:验证码${code}，您正在注册成为新用户，感谢您的支持！
 申请说明:
 */
public class RegisterSmsCaptchaTemplate extends GeneralCaptchaSmsCaptchaTemplate {

    public RegisterSmsCaptchaTemplate(String phone, String code){
        super(phone, code);
    }

    @Override
    public String getTemplateCode() {
        return AppContext.getSmsConfig().getIdRegister();
    }

    @Override
    public CaptchaUsage getUsage() {
        return CaptchaUsage.REGISTER;
    }
}
