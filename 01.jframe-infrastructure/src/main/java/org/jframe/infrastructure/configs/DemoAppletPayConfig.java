package org.jframe.infrastructure.configs;

import org.jframe.core.logging.LogHelper;
import org.jframe.core.weixin.core.WeixinPayConfig;
import org.jframe.infrastructure.AppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Leo on 2017/11/9.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class DemoAppletPayConfig implements WeixinPayConfig {

    @Value("${applet.pay.demo.appId}")
    private String appId;

    @Value("${applet.pay.demo.mchId}")
    private String mchId;

    @Value("${applet.pay.demo.key}")
    private String key;

    @Value("${applet.pay.demo.certFilePath}")
    private String certFilePath;

    @Value("${applet.pay.demo.clientIp}")
    private String clientIp;

    @Value("${applet.pay.demo.httpConnectTimeoutMs}")
    private int httpConnectTimeoutMs;

    @Value("${applet.pay.demo.httpReadTimeoutMs}")
    private int httpReadTimeoutMs;

    @Override
    public String getPayNotifyUrl() {
        return AppContext.getAppConfig().getHost() + "/app/weixinPay/ibuy-back-notify";
    }

    @Override
    public String getClientIp() {
        return clientIp;
    }

    @Override
    public String getAppID() {
        return this.appId;
    }

    @Override
    public String getMchID() {
        return this.mchId;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public InputStream getCertStream() {
        try {
            return new FileInputStream(this.certFilePath);
        } catch (IOException ex) {
            LogHelper.error().log("极其严重.weixinpay", "证书不存在：" + this.certFilePath);
            return null;
        }
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return this.httpConnectTimeoutMs;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return this.httpReadTimeoutMs;
    }
}
