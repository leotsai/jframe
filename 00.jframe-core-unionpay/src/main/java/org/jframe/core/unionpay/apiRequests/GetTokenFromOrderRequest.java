package org.jframe.core.unionpay.apiRequests;

import org.jframe.core.unionpay.configs.UnionpayConfig;
import org.jframe.core.unionpay.sdk.SDKConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/11/3.
 */
public class GetTokenFromOrderRequest {

    private String orderId;
    private String txnTime;
    private String merId;
    private String tokenPayData;


    private Map<String, String> mapData;


    public Map<String, String> getMapData() {
        return this.mapData;
    }

    public GetTokenFromOrderRequest buildMapData() {
        this.mapData = new HashMap<>();
        this.mapData.put("version", SDKConfig.getConfig().getVersion());                  //版本号
        this.mapData.put("encoding", UnionpayConfig.CHARSET);                //字符集编码 可以使用UTF-8,GBK两种方式
        this.mapData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        this.mapData.put("txnType", "00");                              //交易类型 01-消费
        this.mapData.put("txnSubType", "00");                           //交易子类型 01-消费
        this.mapData.put("bizType", "000201");                          //业务类型 认证支付2.0
//        this.mapData.put("channelType", "08");                          //渠道类型07-PC

        this.mapData.put("merId", this.merId);                                         //商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
        this.mapData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
        this.mapData.put("orderId", this.orderId);                                //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        this.mapData.put("txnTime", this.txnTime);         //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        this.mapData.put("tokenPayData", this.tokenPayData); //测试环境固定trId=62000000001&tokenType=01，生产环境由业务分配。测试环境因为所有商户都使用同一个trId，所以同一个卡获取的token号都相同，任一人发起更新token或者解除token请求都会导致原token号失效，所以之前成功、突然出现3900002报错时请先尝试重新开通一下。

        return this;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public void setTokenPayData(String tokenPayData) {
        this.tokenPayData = tokenPayData;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }
}
