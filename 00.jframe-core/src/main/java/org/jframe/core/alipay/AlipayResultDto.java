package org.jframe.core.alipay;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * Created by Leo on 2017/10/20.
 */
public class AlipayResultDto {

    private String out_trade_no;
    private String trade_no;
    private String trade_status;
    private BigDecimal total_amount;
    private boolean isVeryfied;

    public boolean isTradeFinished() {
        return "TRADE_FINISHED".equals(this.trade_status);
    }

    public boolean isTradeSuccess() {
        return "TRADE_SUCCESS".equals(this.trade_status);
    }


    private String convertToUtf8(String input) throws UnsupportedEncodingException {
        return new String(input.getBytes("ISO-8859-1"), "UTF-8");
    }

    public void loadValuesFromRequest(HttpServletRequest request) throws UnsupportedEncodingException {
        this.setOut_trade_no(convertToUtf8(request.getParameter("out_trade_no")));
        this.setTrade_no(convertToUtf8(request.getParameter("trade_no")));
        this.setTrade_status(convertToUtf8(request.getParameter("trade_status")));
        this.setTotal_amount(BigDecimal.valueOf(Double.valueOf(request.getParameter("total_amount"))).setScale(2, BigDecimal.ROUND_HALF_UP));
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

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public boolean isVeryfied() {
        return isVeryfied;
    }

    public void setVeryfied(boolean veryfied) {
        isVeryfied = veryfied;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("out_trade_no", out_trade_no)
                .append("trade_no", trade_no)
                .append("trade_status", trade_status)
                .append("total_amount", total_amount)
                .append("isVeryfied", isVeryfied)
                .toString();
    }
}