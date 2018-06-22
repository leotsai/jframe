package org.jframe.core.unionpay.apiRequests;

import org.jframe.core.unionpay.configs.UnionpayConfig;
import org.jframe.core.unionpay.sdk.SDKConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/11/6.
 */
public class QueryOrderStatusRequest {


    private String merId;
    private String orderId;
    private String txnTime;

    private Map<String, String> mapData;


    public Map<String, String> getMapData(){
        return this.mapData;
    }

    public QueryOrderStatusRequest buildMapData(){
        this.mapData = new HashMap<>();

        this.mapData.put("version", SDKConfig.getConfig().getVersion());  //版本号
        this.mapData.put("encoding", UnionpayConfig.CHARSET);          //字符集编码
        this.mapData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        this.mapData.put("txnType", "00");                             //交易类型
        this.mapData.put("txnSubType", "00");                          //交易子类型
        this.mapData.put("bizType", "000000");                         //业务类型

        /***商户接入参数***/
        this.mapData.put("merId", this.merId);                                          //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        this.mapData.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改

        /***要调通交易以下字段必须修改***/
        this.mapData.put("orderId", this.orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
        this.mapData.put("txnTime", this.txnTime);                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

        return this;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }
}
