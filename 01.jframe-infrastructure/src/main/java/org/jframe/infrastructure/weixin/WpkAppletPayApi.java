package org.jframe.infrastructure.weixin;

import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.configs.WpkAppletIbuyPayConfig;
import org.jframe.core.weixin.core.WeixinPayConfig;
import org.jframe.core.weixin.pay.WeixinPayApi;

/**
 * DESC:小程序支付接口
 *
 * @author xiaojin
 * @date 2018-05-22 17:27
 */
public class WpkAppletPayApi extends WeixinPayApi {

    private static WeixinPayConfig ibuy = AppContext.getWeixinPayConfig(WpkAppletIbuyPayConfig.class);

    public static WpkAppletPayApi ibuy() {
        return new WpkAppletPayApi(ibuy);
    }


    public WpkAppletPayApi(WeixinPayConfig config) {
        super(config);
    }
}
