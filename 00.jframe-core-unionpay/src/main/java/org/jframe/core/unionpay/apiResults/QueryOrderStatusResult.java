package org.jframe.core.unionpay.apiResults;

import org.jframe.core.pay.PaymentStatus;
import org.jframe.core.pay.QueryPaymentResult;
import org.jframe.core.unionpay.core.UnionpayResultBase;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Leo on 2017/11/6.
 * https://open.unionpay.com/ajweb/help/api
 * 左侧导航 - 无跳转支付产品 - 交易状态查询交易
 */
public class QueryOrderStatusResult extends UnionpayResultBase implements QueryPaymentResult {

    private String issuerIdentifyMode;//发卡机构识别模式
    private String queryId;//交易查询流水号
    private String traceNo;//系统跟踪号
    private String traceTime;//交易传输时间, MMDDHHmmss
    private String settleDate;//清算日期, MMDD
    private String settleCurrencyCode;//清算币种
    private String settleAmt;//清算金额, 取值同交易金额
    private String origRespCode;//原交易应答码,在交易状态查询时返回此域,查询交易成功时返回
    private String origRespMsg;//原交易应答信息

    private String orderId;//商户订单号
    private String currencyCode;//交易币种
    private String txnAmt;//交易金额
    private String txnTime;//订单发送时间, YYYYMMDDHHmmss
    private String payType;//支付方式
    private String accNo;//账号
    private String payCardType;//支付卡类型

    @Override
    public PaymentStatus getStatus() {
        if (this.isHttpError()) {
            return PaymentStatus.HTTP_ERROR;
        }
        if (this.isSignError()) {
            return PaymentStatus.HTTP_ERROR;
        }
        if ("00".equals(this.getRespCode())) {
            switch (this.origRespCode) {
                case "00":
                    return PaymentStatus.SUCCESS;
                case "03":
                case "04":
                case "05":
                    return PaymentStatus.AWAITING;
                default:
                    return PaymentStatus.FAILED;
            }
        }
        if ("34".equals(this.getRespCode())) {
            return PaymentStatus.HTTP_ERROR;
        }
        return PaymentStatus.HTTP_ERROR;
    }

    @Override
    public String getRequestUuid() {
        return this.orderId;
    }

    @Override
    public String getTradeNumber() {
        return this.queryId;
    }

    @Override
    public BigDecimal getPayAmount() {
        int value = Integer.valueOf(this.txnAmt);
        return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(100), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public QueryOrderStatusResult loadFromResponse(Map<String, String> responseData) {
        super.loadFromResponse(responseData);
        this.issuerIdentifyMode = responseData.get("issuerIdentifyMode");
        this.queryId = responseData.get("queryId");
        this.traceNo = responseData.get("traceNo");
        this.traceTime = responseData.get("traceTime");
        this.settleDate = responseData.get("settleDate");
        this.settleCurrencyCode = responseData.get("settleCurrencyCode");
        this.settleAmt = responseData.get("settleAmt");
        this.origRespCode = responseData.get("origRespCode");
        this.origRespMsg = responseData.get("origRespMsg");

        this.orderId = responseData.get("orderId");
        this.currencyCode = responseData.get("currencyCode");
        this.txnAmt = responseData.get("txnAmt");
        this.txnTime = responseData.get("txnTime");
        this.payType = responseData.get("payType");
        this.accNo = responseData.get("accNo");
        this.payCardType = responseData.get("payCardType");

        return this;
    }

    //-------------------------------------------------------------------------------------

    public String getQueryId() {
        return queryId;
    }


    public String getPayCardType() {
        return payCardType;
    }

    public String getPayType() {
        return payType;
    }

    public String getIssuerIdentifyMode() {
        return issuerIdentifyMode;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public String getTraceTime() {
        return traceTime;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public String getSettleCurrencyCode() {
        return settleCurrencyCode;
    }

    public String getSettleAmt() {
        return settleAmt;
    }

    public String getOrigRespCode() {
        return origRespCode;
    }

    public String getOrigRespMsg() {
        return origRespMsg;
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

    public String getAccNo() {
        return accNo;
    }
}
