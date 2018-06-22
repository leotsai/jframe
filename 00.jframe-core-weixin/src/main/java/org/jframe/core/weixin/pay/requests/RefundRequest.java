package org.jframe.core.weixin.pay.requests;

import org.jframe.core.weixin.core.WeixinConfig;
import org.jframe.core.weixin.core.WeixinPayConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/11/30.
 */
public class RefundRequest {
    private String transaction_id;//微信生成的订单号，在支付通知中有返回
    private String out_refund_no;//商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
    private BigDecimal total_fee;//订单总金额，单位为分，只能为整数，详见支付金额
    private BigDecimal refund_fee;//	退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
    private String refund_fee_type = "CNY";
    private String op_user_id;

    public RefundRequest(WeixinPayConfig config) {
        this.op_user_id = config.getMchID();
    }

    public Map<String, String> getDataMap() {
        String totalFeeInFen = this.total_fee.multiply(BigDecimal.valueOf(100)).intValue() + "";
        String refundFeeInFen = this.refund_fee.multiply(BigDecimal.valueOf(100)).intValue() + "";

        Map<String, String> map = new HashMap<>();
        map.put("transaction_id", this.transaction_id);
        map.put("out_refund_no", this.out_refund_no);
        map.put("total_fee", totalFeeInFen);
        map.put("refund_fee", refundFeeInFen);
        map.put("refund_fee_type", this.refund_fee_type);
        map.put("op_user_id", this.op_user_id);
        return map;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public void setTotal_fee(BigDecimal total_fee) {
        this.total_fee = total_fee;
    }

    public void setRefund_fee(BigDecimal refund_fee) {
        this.refund_fee = refund_fee;
    }

}
