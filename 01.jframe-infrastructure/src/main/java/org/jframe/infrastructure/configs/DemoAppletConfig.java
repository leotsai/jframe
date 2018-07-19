package org.jframe.infrastructure.configs;

import org.jframe.core.weixin.core.AppletConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2017/10/20.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class DemoAppletConfig implements AppletConfig {

    @Value("${applet.demo.appId}")
    private String appId;

    @Value("${applet.demo.appSecret}")
    private String appSecret;

    @Value("${applet.demo.messageToken}")
    private String messageToken;

    @Value("${applet.demo.messageAesKey}")
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
