package org.jframe.core.unionpay.apiRequests;

import org.jframe.core.unionpay.configs.UnionpayConfig;
import org.jframe.core.unionpay.sdk.SDKConfig;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/11/3.
 * 支付并开通无跳转支付
 */
public class MobileWapPayRequest {

    private String merId;
    private String orderId;
    private BigDecimal amount;
    private String frontUrl;
    private String backUrl;

    private Map<String, String> mapData;

    public Map<String, String> getMapData() {
        return this.mapData;
    }

    public MobileWapPayRequest buildMapData() {
        int amountFen = this.amount.multiply(new BigDecimal(100)).setScale(0).intValue();

        this.mapData = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        mapData.put("version", SDKConfig.getConfig().getVersion());                  //版本号
        mapData.put("encoding", UnionpayConfig.CHARSET);                //字符集编码 可以使用UTF-8,GBK两种方式
        mapData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        mapData.put("txnType", "01");                              //交易类型 01-消费
        mapData.put("txnSubType", "01");                           //交易子类型 01-消费
        mapData.put("bizType", "000201");                          //业务类型 认证支付2.0
        mapData.put("channelType", "08");                          //渠道类型07-PC

        /***商户接入参数***/
        mapData.put("merId", merId);                               //商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
        mapData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
        mapData.put("orderId", orderId);                           //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        mapData.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));                           //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        mapData.put("currencyCode", "156");                           //交易币种（境内商户一般是156 人民币）
        mapData.put("txnAmt", amountFen + "");                               //交易金额，单位分，不要带小数点
        mapData.put("accType", "01");                              //账号类型
        //mapData.put("reqReserved", "透传字段");        					//请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节。出现&={}[]符号时可能导致查询接口应答报文解析失败，建议尽量只传字母数字并使用|分割，或者可以最外层做一次base64编码(base64编码之后出现的等号不会导致解析失败可以不用管)。

        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
        //异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注：如果开通失败的“返回商户”按钮也是触发frontUrl地址，点击时是按照get方法返回的，没有通知数据返回商户
        mapData.put("frontUrl", this.frontUrl);

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        mapData.put("backUrl", this.backUrl);

        // 订单超时时间。
        // 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
        // 此时间建议取支付时的北京时间加15分钟。
        // 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
        mapData.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis() + 15 * 60 * 1000));

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

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }
}
