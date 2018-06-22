package org.jframe.core.unionpay.apiResults;

import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.unionpay.configs.UnionpayConfig;
import org.jframe.core.unionpay.core.UnionpayResultBase;
import org.jframe.core.unionpay.sdk.AcpService;

import java.util.Map;

/**
 * Created by Leo on 2017/11/8.
 * 以下字段均来自demo，没有找到API文档
 */
public class FrontReturnResult extends UnionpayResultBase {

    private String orderId;
    private String txnTime;//文档和demo中都没有该字段，猜的
    private String customerInfo;
    private String accNo;
    private String customerRaw;

    public boolean isSuccess(){
        return this.isHttpError() == false && this.isSignError() == false && "00".equals(this.getRespCode());
    }

    @Override
    public FrontReturnResult loadFromResponse(Map<String, String> responseData) {
        super.loadFromResponse(responseData);
        this.orderId = responseData.get("orderId");
        this.txnTime = responseData.get("txnTime");
        this.customerInfo = responseData.get("customerInfo");
        this.accNo = responseData.get("accNo");

        if (!StringHelper.isNullOrWhitespace(this.customerInfo)) {
            Map<String, String> customerMap = AcpService.parseCustomerInfo(customerInfo, UnionpayConfig.CHARSET);
            this.customerRaw = JList.fromMap(customerMap).joinString("&", x -> x.getKey() + "=" + x.getValue());
        }
        if (!StringHelper.isNullOrWhitespace(this.accNo)) {
            this.accNo = AcpService.decryptData(this.accNo, UnionpayConfig.CHARSET);
        }
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public String getAccNo() {
        return accNo;
    }

    public String getCustomerRaw() {
        return customerRaw;
    }

    public String getTxnTime() {
        return txnTime;
    }
}
