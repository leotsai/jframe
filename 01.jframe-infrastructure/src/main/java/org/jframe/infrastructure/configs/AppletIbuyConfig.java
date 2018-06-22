package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.jframe.core.weixin.core.AppletConfig;

/**
 * Created by Leo on 2017/10/20.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class AppletIbuyConfig implements AppletConfig {

    @Value("${applet.ibuy.appId}")
    private String appId;

    @Value("${applet.ibuy.appSecret}")
    private String appSecret;

    @Value("${applet.ibuy.messageToken}")
    private String messageToken;

    @Value("${applet.ibuy.messageAesKey}")
    private String messageAesKey;


    //-------------------------------------
    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public String getAppSecret() {
        return appSecret;
    }

    @Override
    public String getMessageToken() {
        return messageToken;
    }

    @Override
    public String getMessageAesKey() {
        return messageAesKey;
    }


}
