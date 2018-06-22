package org.jframe.infrastructure.alipay;

import org.jframe.core.alipay.AlipayApi;

/**
 * Created by Leo on 2017/11/9.
 */
public class WpkAlipayApi extends AlipayApi {

    private final static WpkAlipayApi instance = new WpkAlipayApi();
    public static WpkAlipayApi getInstance(){
        return instance;
    }

    private WpkAlipayApi(){

    }

}
