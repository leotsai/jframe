package org.jframe.core.weixin.pay.requests;

import org.jframe.core.weixin.core.WeixinPayConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/10/17.
 */
public class CreateOrderRequest {

    private String body;
    private String out_trade_no;
    private String device_info;
    private String fee_type;
    private BigDecimal amount;
    private String ip;
    private String notify_url;
    private String trade_type;
    private String openid;

    public CreateOrderRequest(WeixinPayConfig config) {
        this.device_info = "MP";
        this.fee_type = "CNY";
        this.trade_type = "JSAPI";
        this.notify_url = config.getPayNotifyUrl();
    }

    public CreateOrderRequest(String notifyUrl) {
        this.device_info = "MP";
        this.fee_type = "CNY";
        this.trade_type = "JSAPI";
        this.notify_url = notifyUrl;
    }

    public Map<String, String> getDataMap() {
        Map<String, String> map = new HashMap<>();
        map.put("body", this.body);
        map.put("out_trade_no", out_trade_no);
        map.put("device_info", this.device_info);
        map.put("fee_type", this.fee_type);
        map.put("total_fee", this.amount.multiply(BigDecimal.valueOf(100)).intValue() + "");
        map.put("spbill_create_ip", this.ip);
        map.put("notify_url", this.notify_url);
        map.put("trade_type", this.trade_type);
        map.put("openid", this.openid);
        return map;
    }


    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
