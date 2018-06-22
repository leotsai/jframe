package org.jframe.core.weixin.pay;

import com.github.wxpay.sdk.WXPay;
import org.jframe.core.alipay.apiResults.QueryRefundStatusResult;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.weixin.core.WeixinPayConfig;
import org.jframe.core.weixin.pay.requests.CreateOrderRequest;
import org.jframe.core.weixin.pay.requests.RefundRequest;
import org.jframe.core.weixin.pay.requests.SendRedpackRequest;
import org.jframe.core.weixin.pay.results.CreateOrderResult;
import org.jframe.core.weixin.pay.results.PayResult;
import org.jframe.core.weixin.pay.results.QueryOrderResult;
import org.jframe.core.weixin.pay.results.RefundResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/11/9.
 */
public class WeixinPayApi {

    private WXPay wxpay;
    private WeixinPayConfig config;

    public WeixinPayApi(WeixinPayConfig config) {
        this.config = config;
        this.wxpay = new WXPay(config);
    }

    public CreateOrderResult createOrder(CreateOrderRequest request) {
        try {
            Map<String, String> response = wxpay.unifiedOrder(request.getDataMap(), this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
            return new CreateOrderResult().loadFromResponse(response);
        } catch (Exception ex) {
            LogHelper.log("weixinpay.createorder", ex);
            return null;
        }
    }

    public PayResult parsePayResult(String xmlResponse) {
        try {
            Map<String, String> data = this.wxpay.processResponseXml(xmlResponse);
            return new PayResult().loadFromResponse(data);
        } catch (Exception ex) {
            LogHelper.log("weixinpay.parseresult", ex);
            return null;
        }
    }

    public QueryOrderResult queryOrderStatus(String outTradeNo, String transactionId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("out_trade_no", outTradeNo);
        data.put("transaction_id", transactionId);
        try {
            Map<String, String> responseData = wxpay.orderQuery(data);
            return new QueryOrderResult().loadFromResponse(responseData);
        } catch (Exception e) {
            LogHelper.log("weixinpay.queryorder", e);
            return null;
        }
    }

    public void sendRedpack(String openId, int amountInFen, String orderId) {
        if (orderId == null || orderId.length() > 28) {
            throw new KnownException("orderId参数不能为空，且长度不能超过28字节");
        }

        String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
        SendRedpackRequest request = new SendRedpackRequest(this.config);
        request.setAct_name("帮砍微信红包");
        request.setAmountInFen(amountInFen);
        request.setSend_name("五品库");
        request.setRe_openid(openId);
        request.setMch_billno(orderId);
        request.setWishing("感谢您的帮助");
        request.setRemark("这里是备注信息");

        try {
            String respXml = this.wxpay.requestWithCert(url, request.getRequestData(), 30000, 30000);
            Map<String, String> responseData = this.wxpay.processResponseXml(respXml);
            LogHelper.log("微信红包", JsonHelper.serialize(responseData));
        } catch (Exception ex) {
            LogHelper.log("微信红包", ex);
        }
    }

    public void doOrderClose() {
//        System.out.println("关闭订单");
//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("out_trade_no", out_trade_no);
//        try {
//            Map<String, String> r = wxpay.closeOrder(data);
//            System.out.println(r);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    public void doOrderReverse() {
//        System.out.println("撤销");
//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("out_trade_no", out_trade_no);
////        data.put("transaction_id", "4008852001201608221962061594");
//        try {
//            Map<String, String> r = wxpay.reverse(data);
//            System.out.println(r);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 长链接转短链接
     * 测试成功
     */
    public void doShortUrl() {
//        String long_url = "weixin://wxpay/bizpayurl?pr=etxB4DY";
//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("long_url", long_url);
//        try {
//            Map<String, String> r = wxpay.shortUrl(data);
//            System.out.println(r);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public RefundResult refund(String refundRequestUuid, String tradeNumber, BigDecimal totalPayAmount, BigDecimal refundAmount) {
        RefundRequest request = new RefundRequest(this.config);
        request.setTransaction_id(tradeNumber);
        request.setOut_refund_no(refundRequestUuid);
        request.setTotal_fee(totalPayAmount);
        request.setRefund_fee(refundAmount);

        Map<String, String> data = request.getDataMap();
        try {
            Map<String, String> response = wxpay.refund(data);
            RefundResult result = new RefundResult().loadFromResponse(response);
            if (result == null || !result.isRefundSuccess()) {
                LogHelper.log("weixinpay.refund", (result == null ? "null" : "response") + JsonHelper.serialize(response));
            }
            return result;
        } catch (Exception e) {
            LogHelper.log("weixinpay.refund", e);
            return null;
        }
    }

    /**
     * 查询退款
     * 已经测试
     */
    public QueryRefundStatusResult refundQuery(String tradeNumber, String refundRequestUuid) {
        HashMap<String, String> data = new HashMap();
        data.put("out_refund_no", refundRequestUuid);
        data.put("transaction_id", tradeNumber);
        try {
            Map<String, String> response = wxpay.refundQuery(data);
            LogHelper.log("alipayapi.query.refund.response", JsonHelper.serialize(response));
            return new QueryRefundStatusResult().loadFromWeixinpayResponse(response);
        } catch (Exception e) {
            LogHelper.log("alipayapi.query.refund", e);
            e.printStackTrace();
            return new QueryRefundStatusResult().setHttpError();
        }
    }

    /**
     * 对账单下载
     * 已测试
     */
    public void doDownloadBill() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("bill_date", "20161102");
        data.put("bill_type", "ALL");
        try {
            Map<String, String> r = wxpay.downloadBill(data);
            System.out.println(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doGetSandboxSignKey() throws Exception {
//        WXPayConfigImpl config = WXPayConfigImpl.getInstance();
//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("mch_id", config.getMchID());
//        data.put("nonce_str", WXPayUtil.generateNonceStr());
//        String sign = WXPayUtil.generateSignature(data, config.getKey());
//        data.put("sign", sign);
//        WXPay wxPay = new WXPay(config);
//        String result = wxPay.requestWithoutCert("https://api.mch.weixin.qq.com/sandbox/pay/getsignkey", data, 10000, 10000);
//        System.out.println(result);
    }


}
