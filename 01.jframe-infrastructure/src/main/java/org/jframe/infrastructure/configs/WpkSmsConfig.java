package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.jframe.core.aliyun.sms.SmsConfig;

/**
 * Created by leo on 2017-11-12.
 */
@Component
@PropertySource(value = "/WEB-INF/app.properties", encoding = "UTF-8")
public class WpkSmsConfig implements SmsConfig {

    @Value("${aliyun.sms.key}")
    private String key;

    @Value("${aliyun.sms.secret}")
    private String secret;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Value("${aliyun.sms.templates.findPassword}")
    private String idFindPassword;

    @Value("${aliyun.sms.templates.login}")
    private String idLogin;

    @Value("${aliyun.sms.templates.register}")
    private String idRegister;

    @Value("${aliyun.sms.templates.resetPayPassword}")
    private String idResetPayPassword;

    @Value("${aliyun.sms.templates.generalCaptcha}")
    private String idGeneralCaptcha;

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getSecret() {
        return this.secret;
    }

    public String getIdFindPassword() {
        return idFindPassword;
    }

    public String getIdLogin() {
        return idLogin;
    }

    public String getIdRegister() {
        return idRegister;
    }

    public String getIdResetPayPassword() {
        return idResetPayPassword;
    }

    public String getIdGeneralCaptcha() {
        return idGeneralCaptcha;
    }

    public String getSignName() {
        return signName;
    }
}
