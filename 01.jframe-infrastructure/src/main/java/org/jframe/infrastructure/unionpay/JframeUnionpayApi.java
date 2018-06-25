package org.jframe.infrastructure.unionpay;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.unionpay.UnionpayApi;
import org.jframe.infrastructure.AppContext;

/**
 * Created by Leo on 2017/11/6.
 */
public class JframeUnionpayApi extends UnionpayApi implements AppInitializer {

    private final static JframeUnionpayApi instance = new JframeUnionpayApi();

    public static JframeUnionpayApi getInstance() {
        return instance;
    }

    private JframeUnionpayApi() {

    }

    @Override
    public void initialize() {
        super.initialize(AppContext.getUnionpayConfig());
    }

    @Override
    public void close() {

    }
}
