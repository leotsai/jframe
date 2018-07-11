package org.jframe.infrastructure.sms.templates;

import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.sms.CaptchaUsage;

/**
 * Created by Leo on 2017/11/13.
 * 模版类型:验证码
 * 模版类型:验证码
 * 模版名称:重置支付密码验证码
 * 模版CODE:SMS_110840256
 * 模版内容:您的验证码为：${code}。您正在设置支付密码，请勿将此验证码泄露于他人。
 * 申请说明:重置支付密码验证码
 */
public class ResetPayPasswordSmsCaptchaTemplate extends GeneralCaptchaSmsCaptchaTemplate {

    public ResetPayPasswordSmsCaptchaTemplate(String phone, String code) {
        super(phone, code);
    }

    @Override
    public String getTemplateCode() {
        return AppContext.getSmsConfig().getIdResetPayPassword();
    }

    @Override
    public CaptchaUsage getUsage() {
        return CaptchaUsage.RESET_PAY_PASSWORD;
    }
}
