package org.jframe.infrastructure.weixin;

import org.jframe.core.app.AppInitializer;
import org.jframe.infrastructure.AppContext;
import org.jframe.infrastructure.configs.JframeWeixinPayConfig;
import org.jframe.core.weixin.pay.WeixinPayApi;

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
    public void initialize() {

    }

    @Override
    public void close() {

    }
}
