package org.jframe.core.unionpay.configs;

/**
 * Created by Leo on 2017/11/6.
 */
public interface UnionpayConfig {
    String CHARSET = "UTF-8";
    String TIME_PATTERN = "YYYYMMDDhhmmss";

    String getMerId();

    String getFrontReturnUrl();

    String getBackNotifyUrl();

    boolean isEncrypted();

    String getTrId();
}
