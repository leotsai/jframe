package org.jframe.core.weixin.pay.requests;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.jframe.core.extensions.JMap;
import org.jframe.core.weixin.core.WeixinPayConfig;
import org.jframe.core.weixin.pay.WxPayHelper;

import java.util.Map;

/**
 * Created by Leo on 2017/11/30.
 */
public class SendRedpackRequest {
    private String mch_billno;//String(28) 商户订单号（每个订单号必须唯一。取值范围：0~9，a~z，A~Z）接口根据商户订单号支持重入，如出现超时可再调用。
    private String send_name;//红包发送者名称
    private String re_openid;//接受红包的用户 用户在wxappid下的openid
    private int amountInFen;//付款金额，单位分
    private String wishing;//红包祝福语

    private String act_name;//活动名称
    private String remark;//备注信息
    private String scene_id;//发放红包使用场景，红包金额大于200时必传
    private String risk_info;//活动信息
    private String consume_mch_id;//资金授权商户号 服务商替特约商户发放时使用


    private final WeixinPayConfig config;

    public SendRedpackRequest(WeixinPayConfig config) {
        this.config = config;
    }

    public Map<String, String> getRequestData() throws Exception {
        Map<String, String> reqData = new JMap<>();

        reqData.put("mch_billno", this.mch_billno);
        reqData.put("mch_id", this.config.getMchID());
        reqData.put("wxappid", this.config.getAppID());
        reqData.put("send_name", this.send_name);
        reqData.put("re_openid", this.re_openid);
        reqData.put("total_amount", this.amountInFen + "");
        reqData.put("total_num", "1");
        reqData.put("wishing", this.wishing);
        reqData.put("client_ip", this.config.getClientIp());
        reqData.put("act_name", this.act_name);
        reqData.put("remark", this.remark);
        reqData.put("scene_id", this.scene_id);
        reqData.put("risk_info", this.risk_info);
        reqData.put("consume_mch_id", this.consume_mch_id);

        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        reqData.put("sign_type", "MD5");
        reqData = WxPayHelper.filterEmptyValues(reqData);

        String sign = WXPayUtil.generateSignature(reqData, this.config.getKey(), WXPayConstants.SignType.MD5);
        reqData.put("sign", sign);
        return reqData;
    }


    public void setMch_billno(String mch_billno) {
        this.mch_billno = mch_billno;
    }

    public void setSend_name(String send_name) {
        this.send_name = send_name;
    }

    public void setRe_openid(String re_openid) {
        this.re_openid = re_openid;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public void setAct_name(String act_name) {
        this.act_name = act_name;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setScene_id(String scene_id) {
        this.scene_id = scene_id;
    }

    public void setRisk_info(String risk_info) {
        this.risk_info = risk_info;
    }

    public void setConsume_mch_id(String consume_mch_id) {
        this.consume_mch_id = consume_mch_id;
    }

    public void setAmountInFen(int amountInFen) {
        this.amountInFen = amountInFen;
    }
}
