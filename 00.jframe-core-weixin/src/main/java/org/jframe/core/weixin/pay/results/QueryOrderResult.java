package org.jframe.core.weixin.pay.results;

import org.jframe.core.extensions.JMap;
import org.jframe.core.pay.PaymentStatus;
import org.jframe.core.pay.QueryPaymentResult;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by leo on 2017-11-09.
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2
 */
public class QueryOrderResult extends _ApiResultBase implements QueryPaymentResult {

    private String appid;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String device_info;
    private String openid;
    private String is_subscribe;
    private String trade_type;
    private String trade_state;
    private String bank_type;
    private int total_fee;
    private int settlement_total_fee;
    private String fee_type;
    private int cash_fee;
    private String cash_fee_type;
    private int coupon_fee;
    private int coupon_count;
    private String transaction_id;
    private String out_trade_no;
    private String attach;
    private String time_end;
    private String trade_state_desc;



    @Override
    public QueryOrderResult loadFromResponse(Map<String, String> response){
        super.loadFromResponse(response);
        if(super.isHttpSuccess() == false){
            return this;
        }
        JMap<String, String> jmap = new JMap<>(response);
        this.appid = jmap.get("appid");
        this.mch_id = jmap.get("mch_id");
        this.nonce_str = jmap.get("nonce_str");
        this.sign = jmap.get("sign");
        this.result_code = jmap.get("result_code");
        this.err_code = jmap.get("err_code");
        this.err_code_des = jmap.get("err_code_des");
        this.device_info = jmap.get("device_info");
        this.openid = jmap.get("openid");
        this.is_subscribe = jmap.get("is_subscribe");
        this.trade_type = jmap.get("trade_type");
        this.trade_state = jmap.get("trade_state");
        this.bank_type = jmap.get("bank_type");
        this.total_fee = jmap.getInteger("total_fee", 0);
        this.settlement_total_fee = jmap.getInteger("settlement_total_fee", 0);
        this.fee_type = jmap.get("fee_type");
        this.cash_fee = jmap.getInteger("cash_fee", 0);
        this.cash_fee_type = jmap.get("cash_fee_type");
        this.coupon_fee = jmap.getInteger("coupon_fee", 0);
        this.coupon_count = jmap.getInteger("coupon_count", 0);
        this.transaction_id = jmap.get("transaction_id");
        this.out_trade_no = jmap.get("out_trade_no");
        this.attach = jmap.get("attach");
        this.time_end = jmap.get("time_end");
        this.trade_state_desc = jmap.get("trade_state_desc");
        return this;
    }


    @Override
    public String getRequestUuid() {
        return this.out_trade_no;
    }

    @Override
    public String getTradeNumber() {
        return this.transaction_id;
    }

    @Override
    public BigDecimal getPayAmount() {
        return BigDecimal.valueOf(this.total_fee).divide(BigDecimal.valueOf(100), 10, BigDecimal.ROUND_HALF_UP)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public PaymentStatus getStatus() {
        if("SUCCESS".equals(this.trade_state)&&"SUCCESS".equals(this.result_code)&&this.isHttpSuccess()){
            return PaymentStatus.SUCCESS;
        }
        if("USERPAYING".equals(this.trade_state)){
            return PaymentStatus.AWAITING;
        }
        return this.isHttpSuccess() ? PaymentStatus.FAILED : PaymentStatus.HTTP_ERROR;
    }

    public String getAppid() {
        return appid;
    }

    public String getMch_id() {
        return mch_id;
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

    public String getDevice_info() {
        return device_info;
    }

    public String getOpenid() {
        return openid;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public String getBank_type() {
        return bank_type;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public int getSettlement_total_fee() {
        return settlement_total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public String getCash_fee_type() {
        return cash_fee_type;
    }

    public int getCoupon_fee() {
        return coupon_fee;
    }

    public int getCoupon_count() {
        return coupon_count;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public String getAttach() {
        return attach;
    }

    public String getTime_end() {
        return time_end;
    }

    public String getTrade_state_desc() {
        return trade_state_desc;
    }
}
