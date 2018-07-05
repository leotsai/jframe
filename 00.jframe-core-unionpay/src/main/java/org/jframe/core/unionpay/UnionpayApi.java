package org.jframe.core.unionpay;

import org.jframe.core.unionpay.apiRequests.*;
import org.jframe.core.unionpay.apiResults.GetTokenFromOrderResult;
import org.jframe.core.unionpay.apiResults.QueryOrderStatusResult;
import org.jframe.core.unionpay.apiResults.SendPaySmsResult;
import org.jframe.core.unionpay.apiResults.SmsPayResult;
import org.jframe.core.unionpay.configs.UnionpayConfig;
import org.jframe.core.unionpay.sdk.AcpService;
import org.jframe.core.unionpay.sdk.SDKConfig;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Leo on 2017/11/3.
 */
public class UnionpayApi {

    private UnionpayConfig config;

    public void initialize(UnionpayConfig config) {
        this.config = config;
    }

    public String buildPayAndOpenCardForm(BigDecimal amount, String requestUuid) {
        PayAndOpenCardRequest request = new PayAndOpenCardRequest();
        request.setAmount(amount);
        request.setFrontUrl(this.config.getFrontReturnUrl());
        request.setBackUrl(this.config.getBackNotifyUrl());
        request.setMerId(this.config.getMerId());
        request.setOrderId(requestUuid);
        request.buildMapData();

        Map<String, String> signedRequestData = AcpService.sign(request.getMapData(), UnionpayConfig.CHARSET);
        String html = AcpService.createAutoFormHtml(SDKConfig.getConfig().getFrontRequestUrl(), signedRequestData, UnionpayConfig.CHARSET);
        return html;
    }

    public GetTokenFromOrderResult getTokenFromOrder(String orderId, String txnTime) {
        GetTokenFromOrderRequest request = new GetTokenFromOrderRequest();
        request.setMerId(this.config.getMerId());
        request.setOrderId(orderId);
        request.setTxnTime(txnTime);
        request.setTokenPayData("{trId=" + config.getTrId() + "&tokenType=01}");
        request.buildMapData();

        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> signedRequestData = AcpService.sign(request.getMapData(), UnionpayConfig.CHARSET);

        //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        String requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();

        //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData
        // 中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> responseData = AcpService.post(signedRequestData, requestBackUrl, UnionpayConfig.CHARSET);

        return new GetTokenFromOrderResult().loadFromResponse(responseData);
    }

    public QueryOrderStatusResult queryOrderStatus(String orderId, String txnTime) {
        QueryOrderStatusRequest request = new QueryOrderStatusRequest();
        request.setMerId(this.config.getMerId());
        request.setOrderId(orderId);
        request.setTxnTime(txnTime);
        request.buildMapData();


        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> signedData = AcpService.sign(request.getMapData(), UnionpayConfig.CHARSET);


        String url = SDKConfig.getConfig().getSingleQueryUrl();
        //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl

        //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，
        // 调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> responseData = AcpService.post(signedData, url, UnionpayConfig.CHARSET);
        return new QueryOrderStatusResult().loadFromResponse(responseData);
    }

    public SendPaySmsResult sendPaySms(String orderId, String phoneNumber, BigDecimal amount, String token) {
        /**对请求参数进行签名并发送http post请求，接收同步应答报文**/
        SendPaySmsRequest request = new SendPaySmsRequest();
        request.setMerId(this.config.getMerId());
        request.setOrderId(orderId);
        request.setPhoneNo(phoneNumber);
        request.setAmount(amount);
        request.setToken(token);
        request.setTrId(config.getTrId());
        request.buildMapData();

        Map<String, String> reqData = AcpService.sign(request.getMapData(), UnionpayConfig.CHARSET);             //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();                                 //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> responseData = AcpService.post(reqData, requestBackUrl, UnionpayConfig.CHARSET); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        return new SendPaySmsResult().loadFromResponse(responseData);
    }

    public SmsPayResult smsPay(String orderId, String smsCode, BigDecimal amount, String token) {
        SmsPayRequest request = new SmsPayRequest();
        request.setMerId(this.config.getMerId());
        request.setOrderId(orderId);
        request.setAmount(amount);
        request.setSmsCode(smsCode);
        request.setToken(token);
        request.setBackUrl(this.config.getBackNotifyUrl());
        request.setTrId(this.config.getTrId());
        request.buildMapData();

        Map<String, String> reqData = AcpService.sign(request.getMapData(), UnionpayConfig.CHARSET);                //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();                            //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> responseData = AcpService.post(reqData, requestBackUrl, UnionpayConfig.CHARSET);    //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        return new SmsPayResult().loadFromResponse(responseData);
    }

}
