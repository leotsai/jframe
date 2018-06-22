package org.jframe.core.unionpay.apiResults;

import org.jframe.core.unionpay.core.UnionpayResultBase;

import java.util.Map;

/**
 * Created by Leo on 2017/11/6.
 * https://open.unionpay.com/ajweb/help/api
 * 左侧导航 - 无跳转支付产品 - 发送短信验证码交易
 */
public class SendPaySmsResult extends UnionpayResultBase {

    private String orderId;
    private String queryId;
    private String currencyCode;
    private String txnAmt;
    private String txnTime;

    public boolean isSuccess() {
        return this.isHttpError() == false && this.isSignError() == false && "00".equals(this.getRespCode());
    }

    @Override
    public SendPaySmsResult loadFromResponse(Map<String, String> responseData) {
        super.loadFromResponse(responseData);
        this.orderId = responseData.get("orderId");
        this.queryId = responseData.get("queryId");
        this.currencyCode = responseData.get("currencyCode");
        this.txnAmt = responseData.get("txnAmt");
        this.txnTime = responseData.get("txnTime");
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getQueryId() {
        return queryId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public String getTxnTime() {
        return txnTime;
    }
}
