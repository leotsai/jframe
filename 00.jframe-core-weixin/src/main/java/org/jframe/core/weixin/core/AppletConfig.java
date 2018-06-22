package org.jframe.core.weixin.core;

/**
 * Created by Leo on 2017/10/20.
 */
public interface AppletConfig {

    String getAppId();

    String getAppSecret();

    String getMessageToken();

    String getMessageAesKey();
}
