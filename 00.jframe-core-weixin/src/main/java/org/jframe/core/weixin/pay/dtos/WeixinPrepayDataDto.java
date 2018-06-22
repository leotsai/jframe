package org.jframe.core.weixin.pay.dtos;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.KeyValuePair;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.security.Crypto;
import org.jframe.core.weixin.core.WeixinConfig;

import java.util.UUID;

/**
 * Created by Leo on 2017/10/17.
 */
public class WeixinPrepayDataDto {
    private String appId;
    private String prePayId;
    private String paySign;
    private String signType;
    private String timeStamp;
    private String nonceStr;
    private String payRequestUuid;


    public WeixinPrepayDataDto() {

    }

//    public WeixinPrepayDataDto(WeixinConfig config, String prepayId, String payRequestUuid) {
//        this.signType = "MD5";
//        this.appId = config.getAppId();
//        this.nonceStr = UUID.randomUUID().toString().replace("-", "");
//        this.timeStamp = String.valueOf(System.currentTimeMillis());
//        this.prePayId = prepayId;
//        this.payRequestUuid = payRequestUuid;
//    }

    public WeixinPrepayDataDto(String appId, String prepayId, String payRequestUuid) {
        this.signType = "MD5";
        this.appId = appId;
        this.nonceStr = UUID.randomUUID().toString().replace("-", "");
        this.timeStamp = String.valueOf(System.currentTimeMillis());
        this.prePayId = prepayId;
        this.payRequestUuid = payRequestUuid;
    }

    public void buildSign(String weixinAppKey) {
        JList<KeyValuePair<String, String>> list = this.getKeyValues();
        list.add(new KeyValuePair<>("key", weixinAppKey));
        String query = String.join("&", list.select(x -> x.getKey() + "=" + x.getValue()));
        this.paySign = Crypto.md5(query).toUpperCase();
    }

    private JList<KeyValuePair<String, String>> getKeyValues() {
        JList<KeyValuePair<String, String>> list = new JList<>();
        list.add(new KeyValuePair<>("appId", this.appId));
        list.add(new KeyValuePair<>("timeStamp", this.timeStamp));
        list.add(new KeyValuePair<>("nonceStr", this.nonceStr));
        list.add(new KeyValuePair<>("package", "prepay_id=" + this.prePayId));
        list.add(new KeyValuePair<>("signType", this.signType));

        return list.where(x -> !StringHelper.isNullOrWhitespace(x.getValue()))
                .orderByAsc(x -> x.getKey())
                .toList();
    }


    //--------------------------------------------------------

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }


    public String getPayRequestUuid() {
        return payRequestUuid;
    }

    public void setPayRequestUuid(String payRequestUuid) {
        this.payRequestUuid = payRequestUuid;
    }
}
