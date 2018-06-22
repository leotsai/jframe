package org.jframe.core.alipay.dtos;


/**
 * Created by Leo on 2017/11/30.
 * https://docs.open.alipay.com/api_1/alipay.trade.refund
 */
public class RefundRequestDto {

    private String out_trade_no;//订单支付时传入的商户订单号,不能和 trade_no同时为空。
    private String trade_no;//支付宝交易号，和商户订单号不能同时为空
    private String refund_amount;//需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
    private String refund_reason = "订单退款";//可选, 退款的原因说明
    private String out_request_no;//可选, 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
    private String operator_id;//可选, 商户的操作员编号
    private String store_id;//	可选,商户的门店编号
    private String terminal_id;//可选,商户的终端编号

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public String getOut_request_no() {
        return out_request_no;
    }

    public void setOut_request_no(String out_request_no) {
        this.out_request_no = out_request_no;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }
}
