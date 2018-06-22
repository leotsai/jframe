package org.jframe.core.alipay.apiResults;

import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import org.jframe.core.pay.RefundStatus;

import java.util.Map;

/**
 * created by yezi on 2018/4/17
 */
public class QueryRefundStatusResult {

    private String code;
    private RefundStatus status;
    private String msg;

    public QueryRefundStatusResult loadFromAlipayResponse(AlipayTradeFastpayRefundQueryResponse response, String tradeNo) {
        this.code = response.getCode();
        this.msg = response.getMsg();
        this.status = this.getRefundStatus(response.getTradeNo(), tradeNo);
        return this;
    }

    public QueryRefundStatusResult loadFromWeixinpayResponse(Map<String, String> response) {
        this.code = response.get("return_code");
        this.msg = response.get("return_msg");
        if (this.code.equals("SUCCESS")) {
            this.status = response.get("result_code").equals("SUCCESS") ? RefundStatus.REFUND_SUCCESS : RefundStatus.REFUND_FAIL;
        }
        return this;
    }

    private RefundStatus getRefundStatus(String value, String tradeNo) {
        if (value.equals(tradeNo)) {
            return RefundStatus.REFUND_SUCCESS;
        }
        return RefundStatus.REFUND_FAIL;
    }

    public QueryRefundStatusResult setHttpError() {
        this.status = RefundStatus.HTTP_ERROR;
        return this;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
