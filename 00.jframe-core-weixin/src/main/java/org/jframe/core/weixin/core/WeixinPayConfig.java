package org.jframe.core.weixin.core;

import com.github.wxpay.sdk.WXPayConfig;

/**
 * Created by Leo on 2017/11/9.
 */
public interface WeixinPayConfig extends WXPayConfig {

    String getPayNotifyUrl();

    String getClientIp();
}
