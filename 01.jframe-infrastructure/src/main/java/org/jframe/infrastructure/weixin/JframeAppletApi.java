package org.jframe.infrastructure.weixin;

import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.configs.DemoAppletConfig;
import org.jframe.core.weixin.AppletApi;
import org.jframe.core.weixin.core.AppletConfig;

/**
 * @author xiaojin
 * @desc 微信小程序服务端接口
 * @date 2018-04-20 10:46
 */
public class JframeAppletApi extends AppletApi {

    private static AppletConfig party = AppContext.getAppletConfig(DemoAppletConfig.class);


    public static JframeAppletApi party() {
        return new JframeAppletApi(party);
    }


    private JframeAppletApi(AppletConfig config) {
        super(config);
    }
}
