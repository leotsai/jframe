package org.jframe.core.weixin.core;

import org.jframe.core.extensions.JDate;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KeyValuePair;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.security.Crypto;
import org.jframe.core.weixin.WeixinApi;

import java.util.UUID;

/**
 * Created by Leo on 2017/11/1.
 */
public class WeixinJsConfigDto {
    private String appId;
    public String timestamp;
    public String nonceStr;
    public String signature;
    private Long userId;
    private final WeixinApi api;

    public WeixinJsConfigDto(WeixinApi api, Long userId) {
        this.api = api;
        this.appId = api.getAppId();
        this.timestamp = (JDate.now().getVersionMiliSeconds() / 1000) + "";
        this.nonceStr = UUID.randomUUID().toString().replace("-", "");
        this.userId = userId;
    }

    public WeixinJsConfigDto buildSignature(String url) {
        JList<KeyValuePair<String, String>> list = this.getKeyValues(url);
        String query = list.joinString("&", x -> x.getKey() + "=" + x.getValue());
        this.signature = Crypto.sha(query).toUpperCase();
        return this;
    }

    private JList<KeyValuePair<String, String>> getKeyValues(String url) {
        JList<KeyValuePair<String, String>> list = new JList<>();
        list.add(new KeyValuePair<>("noncestr", this.nonceStr));
        list.add(new KeyValuePair<>("jsapi_ticket", WxKeyManager.getInstance().getJsApiTicket(this.api)));
        list.add(new KeyValuePair<>("timestamp", this.timestamp));
        list.add(new KeyValuePair<>("url", url));

        return list.where(x -> !StringHelper.isNullOrWhitespace(x.getValue()))
                .orderByAsc(x -> x.getKey())
                .toList();
    }

    public String getAppId() {
        return appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public Long getUserId() {
        return userId;
    }
}
