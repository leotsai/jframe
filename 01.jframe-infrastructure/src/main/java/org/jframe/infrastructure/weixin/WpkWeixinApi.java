package org.jframe.infrastructure.weixin;

import org.jframe.infrastructure.AppContext;
import org.jframe.core.weixin.WeixinApi;

/**
 * Created by leo on 2017-10-22.
 */
public class WpkWeixinApi extends WeixinApi {

    private static final WpkWeixinApi instance = new WpkWeixinApi();
    public static WpkWeixinApi getInstance(){
        return instance;
    }

    private WpkWeixinApi() {
        super(AppContext.getWeixinWpkConfig());
    }


}
