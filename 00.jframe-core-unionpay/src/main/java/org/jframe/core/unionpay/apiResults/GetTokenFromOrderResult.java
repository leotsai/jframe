package org.jframe.core.unionpay.apiResults;

import org.jframe.core.helpers.StringHelper;
import org.jframe.core.unionpay.configs.UnionpayConfig;
import org.jframe.core.unionpay.core.UnionpayResultBase;
import org.jframe.core.unionpay.sdk.AcpService;
import org.jframe.core.unionpay.sdk.SDKUtil;

import java.util.Map;

/**
 * Created by Leo on 2017/11/3.
 * https://open.unionpay.com/ajweb/help/api
 * 左侧导航 - 无跳转支付产品 - 交易状态查询交易
 */
public class GetTokenFromOrderResult extends UnionpayResultBase{

    private String orderId;
    private String txnTime;
    private String accNo;
    private String payCardType;
    private String customerInfo;
    private String tokenPayData;//文档中没有该字段，但是demo中有，无语。。。


    @Override
    public GetTokenFromOrderResult loadFromResponse(Map<String, String> responseData) {
        super.loadFromResponse(responseData);
        this.orderId = responseData.get("orderId");
        this.txnTime = responseData.get("txnTime");
        this.accNo = responseData.get("accNo");
        this.payCardType = responseData.get("payCardType");
        this.customerInfo = responseData.get("customerInfo");
        this.tokenPayData = responseData.get("tokenPayData");
        return this;
    }

    public boolean isSuccess() {
        return this.isHttpError() == false && this.isSignError() == false && "00".equals(this.getRespCode());
    }

    public Map<String, String> parseCustomerInfo() {
        if (StringHelper.isNullOrWhitespace(this.customerInfo)) {
            return null;
        }
        return AcpService.parseCustomerInfo(this.customerInfo, UnionpayConfig.CHARSET);
    }

    public String getDecryptedAccNo() {
        if (StringHelper.isNullOrWhitespace(this.accNo)) {
            return null;
        }
        return AcpService.decryptData(this.accNo, UnionpayConfig.CHARSET);
    }

    public Map<String, String> getTokenPayData() {
        if (StringHelper.isNullOrWhitespace(this.tokenPayData)) {
            return null;
        }
        return SDKUtil.parseQString(this.tokenPayData.substring(1, this.tokenPayData.length() - 1));
    }

    public String getToken() {
        Map<String, String> data = this.getTokenPayData();
        if (data == null) {
            return null;
        }
        return data.get("token");
    }


}
