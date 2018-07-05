package org.jframe.core.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.jframe.core.alipay.apiResults.QueryOrderStatusResult;
import org.jframe.core.alipay.apiResults.QueryRefundStatusResult;
import org.jframe.core.alipay.dtos.RefundRequestDto;
import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.logging.LogHelper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/11/9.
 */
public class AlipayApi {

    private AlipayMobileConfig config;

    public void initialize(AlipayMobileConfig config) {
        this.config = config;
    }

    public QueryOrderStatusResult queryOrderStatus(String outTradeNo, String tradeNo) {
        AlipayClient alipayClient = this.createClient();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        BizContent bizContent = new BizContent(outTradeNo, tradeNo);
        request.setBizContent(JsonHelper.serialize(bizContent));
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            return new QueryOrderStatusResult().loadFromResponse(response);
        } catch (Exception ex) {
            LogHelper.log("alipayapi.query", ex);
        }
        return new QueryOrderStatusResult().setHttpError();
    }

    public QueryRefundStatusResult queryRefundStatus(String tradeNo, String out_request_no) {
        AlipayClient alipayClient = this.createClient();
        AlipayTradeFastpayRefundQueryRequest refundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();
        BizContent bizContent = new BizContent("", tradeNo, out_request_no);
        refundQueryRequest.setBizContent(JsonHelper.serialize(bizContent));
        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(refundQueryRequest);
            return new QueryRefundStatusResult().loadFromAlipayResponse(response, tradeNo);
        } catch (Exception ex) {
            LogHelper.log("alipayapi.query.refund", ex);
            ex.printStackTrace();
            return new QueryRefundStatusResult().setHttpError();
        }
    }


    public AlipayResultDto parsePayResult(HttpServletRequest request) {
        AlipayResultDto dto = new AlipayResultDto();
        Map<String, String> params = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            String[] values = request.getParameterMap().get(key);
            String valueStr = String.join(",", values);
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(key, valueStr);
        }

        try {
            dto.loadValuesFromRequest(request);
            dto.setVeryfied(AlipaySignature.rsaCheckV1(params, this.config.getAlipayPublicKey(), this.config.getCharset(), this.config.getSignType()));
        } catch (Exception ex) {
            dto.setVeryfied(false);
        }
        return dto;
    }

    public void refund(String refundRequestUuid, String tradeNumber, BigDecimal refundAmount) throws AlipayApiException {
        AlipayClient alipayClient = this.createClient();
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

        RefundRequestDto requestDto = new RefundRequestDto();
        requestDto.setTrade_no(tradeNumber);
        requestDto.setRefund_amount(refundAmount.toString());
        requestDto.setOut_request_no(refundRequestUuid);


        request.setBizContent(JsonHelper.serialize(requestDto));
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                LogHelper.log("_支付宝退款失败", JsonHelper.serialize(response));
                throw new KnownException("退款失败。" + response.getMsg());
            }
        } catch (AlipayApiException ex) {
            LogHelper.log("_支付宝退款失败", ex);
            throw ex;
        }
    }

    private AlipayClient createClient() {
        return new DefaultAlipayClient(this.config.getUrl(), this.config.getAppId(), this.config.getRsaPrivateKey(),
                this.config.getFormat(), this.config.getCharset(), this.config.getAlipayPublicKey(), this.config.getSignType());
    }

}
