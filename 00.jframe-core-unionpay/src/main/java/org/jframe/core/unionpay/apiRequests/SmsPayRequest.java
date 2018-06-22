package org.jframe.core.unionpay.apiRequests;

import org.jframe.core.extensions.JDate;
import org.jframe.core.unionpay.configs.UnionpayConfig;
import org.jframe.core.unionpay.sdk.AcpService;
import org.jframe.core.unionpay.sdk.SDKConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/11/6.
 */
public class SmsPayRequest {

    private String merId;
    private String orderId;
    private BigDecimal amount;
    private String backUrl;
    private String token;
    private String smsCode;
    private String trId;

    private Map<String, String> mapData;


    public Map<String, String> getMapData() {
        return this.mapData;
    }

    public SmsPayRequest buildMapData() {
        int amountFen = this.amount.multiply(new BigDecimal(100)).setScale(0).intValue();
        String txnTime = JDate.now().toString(UnionpayConfig.TIME_PATTERN);


        this.mapData = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        this.mapData.put("version", SDKConfig.getConfig().getVersion());                  //版本号
        this.mapData.put("encoding", UnionpayConfig.CHARSET);                //字符集编码 可以使用UTF-8,GBK两种方式
        this.mapData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        this.mapData.put("txnType", "01");                              //交易类型 01-消费
        this.mapData.put("txnSubType", "01");                           //交易子类型 01-消费
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

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  代收产品接口规范 代收交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        this.mapData.put("backUrl", this.backUrl);

        //消费：token号（从前台开通的后台通知中获取或者后台开通的返回报文中获取），验证码看业务配置(默认要短信验证码)。
        this.mapData.put("tokenPayData", "{token=" + this.token + "&trId=" + this.trId + "}");
        Map<String, String> customerInfoMap = new HashMap<String, String>();
        customerInfoMap.put("smsCode", this.smsCode);                    //短信验证码
        //customerInfoMap不送pin的话 该方法可以不送 卡号
        String customerInfoStr = AcpService.getCustomerInfo(customerInfoMap, null, UnionpayConfig.CHARSET);
        this.mapData.put("customerInfo", customerInfoStr);

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

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public void setTrId(String trId) {
        this.trId = trId;
    }
}
