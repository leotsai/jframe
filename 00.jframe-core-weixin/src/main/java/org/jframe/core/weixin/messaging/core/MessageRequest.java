package org.jframe.core.weixin.messaging.core;

import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.security.Crypto;
import org.jframe.core.weixin.core.AppletConfig;
import org.jframe.core.weixin.core.WeixinConfig;

import java.util.Objects;

/**
 * Created by Leo on 2017/10/30.
 */
public class MessageRequest {
    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;

    public boolean isValid(WeixinConfig config) {
        String[] paras = new String[]{this.timestamp, this.nonce, config.getMessageToken()};
        String joined = JList.from(paras).orderByAsc(x -> x).toList().joinString("", x -> x);
        return Objects.equals(Crypto.sha(joined), this.signature);
    }

    public boolean isValid(AppletConfig config) {
        String[] paras = new String[]{this.timestamp, this.nonce, config.getMessageToken()};
        String joined = JList.from(paras).orderByAsc(x -> x).toList().joinString("", x -> x);
        return Objects.equals(Crypto.sha(joined), this.signature);
    }

    public boolean isEcho() {
        return !StringHelper.isNullOrEmpty(this.echostr);
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEchostr() {
        return echostr;
    }

    public void setEchostr(String echostr) {
        this.echostr = echostr;
    }
}
