package org.jframe.infrastructure.weixin;

import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.configs.AppletIbuyConfig;
import org.jframe.infrastructure.configs.AppletPartyConfig;
import org.jframe.core.weixin.AppletApi;
import org.jframe.core.weixin.core.AppletConfig;

/**
 * @author xiaojin
 * @desc 微信小程序服务端接口
 * @date 2018-04-20 10:46
 */
public class WpkAppletApi extends AppletApi {

    private static AppletConfig party = AppContext.getAppletConfig(AppletPartyConfig.class);

    private static AppletConfig ibuy = AppContext.getAppletConfig(AppletIbuyConfig.class);

    public static WpkAppletApi party() {
        return new WpkAppletApi(party);
    }

    public static AppletApi ibuy() {
        return new WpkAppletApi(ibuy);
    }

    private WpkAppletApi(AppletConfig config) {
        super(config);
    }
}
