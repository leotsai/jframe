package org.jframe.core.pay;

import java.math.BigDecimal;

/**
 * Created by Leo on 2017/11/9.
 */
public interface QueryPaymentResult {
    String getRequestUuid();
    String getTradeNumber();
    BigDecimal getPayAmount();
    PaymentStatus getStatus();
}
