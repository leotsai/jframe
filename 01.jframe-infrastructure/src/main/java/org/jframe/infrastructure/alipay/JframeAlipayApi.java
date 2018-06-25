package org.jframe.infrastructure.alipay;

import org.jframe.core.alipay.AlipayApi;
import org.jframe.core.app.AppInitializer;
import org.jframe.infrastructure.AppContext;

/**
 * Created by Leo on 2017/11/9.
 */
public class JframeAlipayApi extends AlipayApi implements AppInitializer {

    private final static JframeAlipayApi instance = new JframeAlipayApi();

    public static JframeAlipayApi getInstance() {
        return instance;
    }

    private JframeAlipayApi() {

    }

    @Override
    public void initialize() {
        super.initialize(AppContext.getAlipayMobileConfig());
    }

    @Override
    public void close() {

    }
}
