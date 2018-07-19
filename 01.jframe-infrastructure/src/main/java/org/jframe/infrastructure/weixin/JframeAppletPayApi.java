package org.jframe.infrastructure.weixin;

import org.jframe.core.weixin.core.WeixinPayConfig;
import org.jframe.core.weixin.pay.WeixinPayApi;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.configs.DemoAppletPayConfig;

/**
 * DESC:小程序支付接口
 *
 * @author xiaojin
 * @date 2018-05-22 17:27
 */
public class JframeAppletPayApi extends WeixinPayApi {

    private static WeixinPayConfig ibuy = AppContext.getWeixinPayConfig(DemoAppletPayConfig.class);

    public static JframeAppletPayApi ibuy() {
        return new JframeAppletPayApi(ibuy);
    }


    public JframeAppletPayApi(WeixinPayConfig config) {
        super(config);
    }
}
