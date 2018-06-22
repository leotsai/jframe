package org.jframe.core.unionpay.apiResults;

import org.jframe.core.unionpay.core.UnionpayResultBase;
import org.jframe.core.unionpay.enums.SmsPayStatus;

import java.util.Map;

/**
 * Created by Leo on 2017/11/6.
 * https://open.unionpay.com/ajweb/help/api
 * 左侧导航 - 无跳转支付产品 - 消费类交易
 */
public class SmsPayResult extends UnionpayResultBase {

    private SmsPayStatus status;
    private String queryId;
    private String orderId;
    private String currencyCode;
    private String txnAmt;
    private String txnTime;
    private String payType;
    private String accNo;
    private String payCardType;

    public boolean isSuccess() {
        return this.isHttpError() == false
                && this.isSignError() == false
                && this.status == SmsPayStatus.REQUEST_SUCCESS;
    }

    @Override
    public SmsPayResult loadFromResponse(Map<String, String> responseData) {
        super.loadFromResponse(responseData);
        this.queryId = responseData.get("queryId");
        this.orderId = responseData.get("orderId");
        this.currencyCode = responseData.get("currencyCode");
        this.txnAmt = responseData.get("txnAmt");
        this.txnTime = responseData.get("txnTime");
        this.payType = responseData.get("payType");
        this.accNo = responseData.get("accNo");
        this.payCardType = responseData.get("payCardType");

        switch (this.getRespCode()) {
            case "00":
                this.status = SmsPayStatus.REQUEST_SUCCESS;
                break;
            case "03":
            case "04":
            case "05":
                this.status = SmsPayStatus.REQUEST_STATUS_UNKNOWN;
                break;
            default:
                this.status = SmsPayStatus.REQUEST_ERROR;
                break;
        }
        return this;
    }


    public String getQueryId() {
        return queryId;
    }

    public String getOrderId() {
        return orderId;
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

    public String getPayType() {
        return payType;
    }

    public String getAccNo() {
        return accNo;
    }

    public String getPayCardType() {
        return payCardType;
    }
}
