package org.jframe.infrastructure.unionpay;

import org.jframe.core.unionpay.UnionpayApi;

/**
 * Created by Leo on 2017/11/6.
 */
public class WpkUnionpayApi extends UnionpayApi {

    private final static WpkUnionpayApi instance = new WpkUnionpayApi();

    public static WpkUnionpayApi getInstance(){
        return instance;
    }

    private WpkUnionpayApi(){

    }

}
