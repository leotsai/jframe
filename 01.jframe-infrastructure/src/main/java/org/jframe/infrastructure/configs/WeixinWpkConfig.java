package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.jframe.core.weixin.core.WeixinConfig;

/**
 * Created by Leo on 2017/10/20.
 */
@Component
@PropertySource("/WEB-INF/app.properties")
public class WeixinWpkConfig implements WeixinConfig {

    @Value("${weixin.wpk.appId}")
    private String appId;

    @Value("${weixin.wpk.appSecret}")
    private String appSecret;

    @Value("${weixin.wpk.messageToken}")
    private String messageToken;

    @Value("${weixin.wpk.messageAesKey}")
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
