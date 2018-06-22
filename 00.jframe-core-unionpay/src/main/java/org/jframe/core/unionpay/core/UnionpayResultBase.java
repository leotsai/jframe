package org.jframe.core.unionpay.core;

import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.unionpay.configs.ResponseCode;
import org.jframe.core.unionpay.configs.UnionpayConfig;
import org.jframe.core.unionpay.sdk.AcpService;

import java.util.Map;

/**
 * Created by Leo on 2017/11/8.
 */
public abstract class UnionpayResultBase {

    private boolean isHttpError;
    private boolean isSignError;
    private String respCode;
    private String respMsg = "银联支付出错啦，请重试";
    private String responseRaw;

    public String getMessage() {
        String message = ResponseCode.getMessage(this.respCode);
        return StringHelper.isNullOrWhitespace(message) ? this.respMsg : message;
    }

    public UnionpayResultBase loadFromResponse(Map<String, String> responseData) {
        if (responseData == null || responseData.isEmpty()) {
            this.isHttpError = true;
            return this;
        }
        this.responseRaw = JList.fromMap(responseData).joinString("&", x -> x.getKey() + "=" + x.getValue());
        if (!AcpService.validate(responseData, UnionpayConfig.CHARSET)) {
            this.isSignError = true;
            return this;
        }
        this.respCode = responseData.get("respCode");
        this.respMsg = responseData.get("respMsg");
        return this;
    }

    //---------------------------------------------------------------

    public boolean isHttpError() {
        return isHttpError;
    }

    public void setHttpError(boolean httpError) {
        isHttpError = httpError;
    }

    public boolean isSignError() {
        return isSignError;
    }

    public void setSignError(boolean signError) {
        isSignError = signError;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getResponseRaw() {
        return responseRaw;
    }
}
