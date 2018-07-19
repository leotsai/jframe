package org.jframe.infrastructure.weixin;

import org.jframe.core.weixin.WeixinApi;
import org.jframe.infrastructure.AppContext;

/**
 * Created by leo on 2017-10-22.
 */
public class JframeWeixinApi extends WeixinApi {

    private static final JframeWeixinApi instance = new JframeWeixinApi();
    public static JframeWeixinApi getInstance(){
        return instance;
    }

    private JframeWeixinApi() {
        super(AppContext.getJframeWeixinConfig());
    }


}
