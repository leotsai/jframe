package org.jframe.infrastructure.unionpay;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.unionpay.UnionpayApi;
import org.jframe.core.unionpay.sdk.SDKConfig;
import org.jframe.infrastructure.AppContext;

import java.util.Properties;

/**
 * Created by Leo on 2017/11/6.
 */
public class JframeUnionpayApi extends UnionpayApi implements AppInitializer {

    private final static JframeUnionpayApi instance = new JframeUnionpayApi();

    public static JframeUnionpayApi getInstance() {
        return instance;
    }

    private Properties properties;

    private JframeUnionpayApi() {

    }

    public JframeUnionpayApi setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public String init() {
        if (this.properties == null) {
            throw new KnownException(this.getClass().getName() + ":please call setProperties method before initializing");
        }
        SDKConfig.getConfig().loadProperties(this.properties);
        super.initialize(AppContext.getUnionpayConfig());
        return this.getClass().getName() + " initialize success!";
    }

    @Override
    public void close() {

    }
}
