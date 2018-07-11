package org.jframe.core.unionpay.apiRequests;

import org.jframe.core.extensions.JDate;
import org.jframe.core.unionpay.configs.UnionpayConfig;
import org.jframe.core.unionpay.sdk.AcpService;
import org.jframe.core.unionpay.sdk.CertUtil;
import org.jframe.core.unionpay.sdk.SDKConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/11/6.
 */
public class SendPaySmsRequest {
    private String merId;
    private String orderId;
    private BigDecimal amount;
    private String phoneNo;
    private String token;
    private String trId;

    private Map<String, String> mapData;

    public Map<String, String> getMapData() {
        return this.mapData;
    }

    public SendPaySmsRequest buildMapData() {
        int amountFen = this.amount.multiply(new BigDecimal(100)).setScale(0).intValue();
        String txnTime = JDate.now().toString(UnionpayConfig.TIME_PATTERN);

        this.mapData = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        this.mapData.put("version", SDKConfig.getConfig().getVersion());                   //版本号
        this.mapData.put("encoding", UnionpayConfig.CHARSET);            //字符集编码 可以使用UTF-8,GBK两种方式
        this.mapData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        this.mapData.put("txnType", "77");                              //交易类型 11-代收
        this.mapData.put("txnSubType", "02");                           //交易子类型 02-消费短信
        this.mapData.put("bizType", "000902");                          //业务类型 认证支付2.0
        this.mapData.put("channelType", "07");                          //渠道类型07-PC

        /***商户接入参数***/
        this.mapData.put("merId", this.merId);                               //商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
        this.mapData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
        this.mapData.put("orderId", this.orderId);                           //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        this.mapData.put("txnTime", txnTime);                           //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        this.mapData.put("currencyCode", "156");                           //交易币种（境内商户一般是156 人民币）
        this.mapData.put("txnAmt", amountFen + "");                               //交易金额，单位分，不要带小数点
        this.mapData.put("accType", "01");                              //账号类型

        //送手机号码
        Map<String, String> customerInfoMap = new HashMap<>();
        customerInfoMap.put("phoneNo", this.phoneNo);                    //手机号
        String customerInfoStr = AcpService.getCustomerInfoWithEncrypt(customerInfoMap, null, UnionpayConfig.CHARSET);

        this.mapData.put("customerInfo", customerInfoStr);
        this.mapData.put("encryptCertId", CertUtil.getEncryptCertId());       //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
        //消费短信：token号（从前台开通的后台通知中获取或者后台开通的返回报文中获取）
        this.mapData.put("tokenPayData", "{token=" + this.token + "&trId=" + this.trId + "}");
        return this;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTrId(String trId) {
        this.trId = trId;
    }
}
