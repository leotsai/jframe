package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.jframe.infrastructure.AppContext;
import org.jframe.core.alipay.AlipayMobileConfig;

/**
 * Created by Leo on 2017/10/20.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class JframeAlipayMobileConfig implements AlipayMobileConfig {

    @Value("${alipay.mobile.url}")
    private String url;

    @Value("${alipay.mobile.appid}")
    private String appId;

    @Value("${alipay.mobile.charset}")
    private String charset;

    @Value("${alipay.mobile.format}")
    private String format;

    @Value("${alipay.mobile.signtype}")
    private String signType;

    @Value("${alipay.mobile.RSA_PRIVATE_KEY}")
    private String rsaPrivateKey;

    @Value("${alipay.mobile.ALIPAY_PUBLIC_KEY}")
    private String alipayPublicKey;

    //-------------------------------------------


    @Override
    public String getNotifyUrl(){
        return AppContext.getAppConfig().getHost()+"/app/alipay/back-notify";
    }

    @Override
    public String getReturnUrl(){
        return AppContext.getAppConfig().getHost()+"/app/alipay/front-return";
    }

    //-------------------------------------------

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public String getCharset() {
        return charset;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public String getSignType() {
        return signType;
    }

    @Override
    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    @Override
    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

}
