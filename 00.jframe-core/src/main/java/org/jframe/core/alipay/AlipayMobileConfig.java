package org.jframe.core.alipay;

/**
 * Created by Leo on 2017/11/9.
 */
public interface AlipayMobileConfig {
    String getNotifyUrl();

    String getReturnUrl();

    String getUrl();

    String getAppId();

    String getCharset();

    String getFormat();

    String getSignType();

    String getRsaPrivateKey();

    String getAlipayPublicKey();
}
