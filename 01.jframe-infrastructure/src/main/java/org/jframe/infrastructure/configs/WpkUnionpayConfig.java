package org.jframe.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.jframe.core.unionpay.configs.UnionpayConfig;

/**
 * Created by Leo on 2017/11/6.
 */

@Component
@PropertySource("/WEB-INF/app.properties")
public class WpkUnionpayConfig implements UnionpayConfig {
    @Value("${unionpay.merId}")
    private String merId;

    @Value("${unionpay.frontReturnUrl}")
    private String frontReturnUrl;

    @Value("${unionpay.backNotifyUrl}")
    private String backNotifyUrl;

    @Value("${unionpay.isEncrypted}")
    private boolean isEncrypted;


    @Value("${unionpay.trId}")
    private String trId;


    @Override
    public String getFrontReturnUrl() {
        return this.frontReturnUrl;
    }

    @Override
    public String getBackNotifyUrl() {
        return this.backNotifyUrl;
    }

    @Override
    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    @Override
    public String getTrId() {
        return this.trId;
    }

    @Override
    public String getMerId() {
        return this.merId;
    }
}
