package org.jframe.core.weixin.pay.results;

import java.util.Map;

/**
 * Created by Leo on 2017/11/9.
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 */
public class CreateOrderResult extends _ApiResultBase {

    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String trade_type;
    private String prepay_id;
    private String code_url;

    @Override
    public CreateOrderResult loadFromResponse(Map<String, String> response){
        super.loadFromResponse(response);
        if(super.isHttpSuccess() == false){
            return this;
        }
        this.appid = response.get("appid");
        this.prepay_id = response.get("prepay_id");
        this.mch_id = response.get("mch_id");
        this.device_info = response.get("device_info");
        this.nonce_str = response.get("nonce_str");
        this.sign = response.get("sign");
        this.result_code = response.get("result_code");
        this.err_code = response.get("err_code");
        this.err_code_des = response.get("err_code_des");
        this.trade_type = response.get("trade_type");
        this.code_url = response.get("code_url");
        return this;
    }

    public String getAppid() {
        return appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public String getCode_url() {
        return code_url;
    }

    public String getPrepay_id() {
        return prepay_id;
    }


}
