package org.jframe.infrastructure.weixin;

import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.configs.WpkWeixinPayConfig;
import org.jframe.core.weixin.pay.WeixinPayApi;

/**
 * Created by Leo on 2017/11/9.
 */
public class WpkWeixinPayApi extends WeixinPayApi {

    private final static WpkWeixinPayApi instance = new WpkWeixinPayApi();

    public static WpkWeixinPayApi getInstance() {
        return instance;
    }


    private WpkWeixinPayApi() {
        super(AppContext.getWeixinPayConfig(WpkWeixinPayConfig.class));
    }
}
