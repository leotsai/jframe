package org.jframe.core.alipay.apiResults;

import com.alipay.api.response.AlipayTradeQueryResponse;
import org.jframe.core.pay.PaymentStatus;
import org.jframe.core.pay.QueryPaymentResult;

import java.math.BigDecimal;

/**
 * Created by Leo on 2017/11/9.
 */
public class QueryOrderStatusResult implements QueryPaymentResult {

    private String outTradeNo;
    private String tradeNo;
    private PaymentStatus status;
    private BigDecimal totalAmount;


    public QueryOrderStatusResult loadFromResponse(AlipayTradeQueryResponse response) {
        this.totalAmount = BigDecimal.valueOf(Double.valueOf(response.getTotalAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.outTradeNo = response.getOutTradeNo();
        this.tradeNo = response.getTradeNo();
        this.status = this.getPaymentStatus(response.getTradeStatus());
        return this;
    }

    private PaymentStatus getPaymentStatus(String value) {
        switch (value) {
            case "WAIT_BUYER_PAY":
                return PaymentStatus.AWAITING;
            case "TRADE_CLOSED":
                return PaymentStatus.FAILED;
            case "TRADE_SUCCESS":
                return PaymentStatus.SUCCESS;
            case "TRADE_FINISHED":
                return PaymentStatus.FAILED;
        }
        return PaymentStatus.HTTP_ERROR;
    }

    public QueryOrderStatusResult setHttpError() {
        this.status = PaymentStatus.HTTP_ERROR;
        return this;
    }

    @Override
    public String getRequestUuid() {
        return this.outTradeNo;
    }

    @Override
    public String getTradeNumber() {
        return this.tradeNo;
    }

    @Override
    public BigDecimal getPayAmount() {
        return this.totalAmount;
    }

    @Override
    public PaymentStatus getStatus() {
        return this.status;
    }
}
