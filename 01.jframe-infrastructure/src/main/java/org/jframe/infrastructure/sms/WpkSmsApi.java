package org.jframe.infrastructure.sms;

import org.jframe.infrastructure.sms.templates.*;
import org.jframe.core.aliyun.sms.AliyunSmsApi;
import org.jframe.core.aliyun.sms.SmsTemplate;

/**
 * Created by leo on 2017-11-12.
 */
public class WpkSmsApi extends AliyunSmsApi{

    private static final WpkSmsApi instance = new WpkSmsApi();
    public static final WpkSmsApi getInstance(){
        return instance;
    }

    private WpkSmsApi(){

    }

    public void sendGenralCaptcha(String phone, String code){
        GeneralCaptchaSmsTemplate template = new GeneralCaptchaSmsTemplate(phone, code);
        super.trySend(template);
    }

    public void sendRegisterCaptcha(String phone, String code){
        SmsTemplate template = new RegisterSmsTemplate(phone, code);
        super.trySend(template);
    }

    public void sendFindPasswordCaptcha(String phone, String code){
        SmsTemplate template = new FindPasswordSmsTemplate(phone, code);
        super.trySend(template);
    }

    public void sendLoginCaptcha(String phone, String code){
        SmsTemplate template = new LoginSmsTemplate(phone, code);
        super.trySend(template);
    }

    public void sendResetPayPasswordCaptcha(String phone, String code){
        SmsTemplate template = new ResetPayPasswordSmsTemplate(phone, code);
        super.trySend(template);
    }

}
