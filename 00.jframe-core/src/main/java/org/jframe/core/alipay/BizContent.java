package org.jframe.core.alipay;

/**
 * Created by Leo on 2017/11/9.
 */
public class BizContent {
    private String out_trade_no;
    private String trade_no;
    private String out_request_no;//退款请求号

    public BizContent(){

    }

    public BizContent(String out_trade_no, String trade_no){
        this.out_trade_no = out_trade_no;
        this.trade_no = trade_no;
    }

    public BizContent(String out_trade_no, String trade_no,String out_request_no){
        this.out_trade_no = out_trade_no;
        this.trade_no = trade_no;
        this.out_request_no = out_request_no;
    }

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

    public String getOut_request_no() {
        return out_request_no;
    }

    public void setOut_request_no(String out_request_no) {
        this.out_request_no = out_request_no;
    }
}
