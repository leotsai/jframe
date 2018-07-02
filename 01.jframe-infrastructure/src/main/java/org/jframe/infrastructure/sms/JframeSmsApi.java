package org.jframe.infrastructure.sms;

import org.jframe.core.aliyun.sms.AliyunSmsApi;
import org.jframe.core.aliyun.sms.SmsTemplate;
import org.jframe.core.app.AppInitializer;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.sms.templates.*;

/**
 * Created by leo on 2017-11-12.
 */
public class JframeSmsApi extends AliyunSmsApi implements AppInitializer {

    private static final JframeSmsApi instance = new JframeSmsApi();

    public static final JframeSmsApi getInstance() {
        return instance;
    }

    private JframeSmsApi() {

    }

    @Override
    public void initialize() {
        super.initialize(AppContext.getSmsConfig());
    }

    @Override
    public void close() {

    }

    @Override
    public void trySend(SmsTemplate template) {
        if (AppContext.getAppConfig().isDeveloperMode()) {
            System.out.println(template.toJson());
            return;
        }
        super.trySend(template);
    }

    public void sendGenralCaptcha(String phone, String code) {
        GeneralCaptchaSmsTemplate template = new GeneralCaptchaSmsTemplate(phone, code);
        this.trySend(template);
    }

    public void sendRegisterCaptcha(String phone, String code) {
        SmsTemplate template = new RegisterSmsTemplate(phone, code);
        this.trySend(template);
    }

    public void sendFindPasswordCaptcha(String phone, String code) {
        SmsTemplate template = new FindPasswordSmsTemplate(phone, code);
        this.trySend(template);
    }

    public void sendLoginCaptcha(String phone, String code) {
        SmsTemplate template = new LoginSmsTemplate(phone, code);
        this.trySend(template);
    }

    public void sendResetPayPasswordCaptcha(String phone, String code) {
        SmsTemplate template = new ResetPayPasswordSmsTemplate(phone, code);
        this.trySend(template);
    }

}
