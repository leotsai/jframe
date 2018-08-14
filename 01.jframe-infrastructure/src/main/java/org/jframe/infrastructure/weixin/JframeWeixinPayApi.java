package org.jframe.infrastructure.weixin;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.weixin.pay.WeixinPayApi;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.configs.JframeWeixinPayConfig;

/**
 * Created by Leo on 2017/11/9.
 */
public class JframeWeixinPayApi extends WeixinPayApi implements AppInitializer {

    private final static JframeWeixinPayApi instance = new JframeWeixinPayApi();

    public static JframeWeixinPayApi getInstance() {
        return instance;
    }

    private JframeWeixinPayApi() {
        super(AppContext.getWeixinPayConfig(JframeWeixinPayConfig.class));
    }

    @Override
    public String init() {
        return "WeixinPay[" + super.config.getAppID() + "/" + super.config.getMchID() + "] initialized.";
    }

    @Override
    public void close() {

    }
}
