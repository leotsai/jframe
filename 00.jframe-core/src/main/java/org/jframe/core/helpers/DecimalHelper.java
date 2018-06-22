package org.jframe.core.helpers;

import org.jframe.core.extensions.Action2;
import org.jframe.core.extensions.JList;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.function.Function;

/**
 * Created by Leo on 2017/11/21.
 */
public class DecimalHelper {

    public static BigDecimal min(BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) > 0 ? value2 : value1;
    }

    public static <T> void fixDecimal(JList<T> list, BigDecimal originalTotal, Function<T, BigDecimal> get, Action2<T, BigDecimal> set) {
        BigDecimal extraAdded = list.sumDecimal(get).subtract(originalTotal);
        if (extraAdded.compareTo(BigDecimal.ZERO) != 0) {
            T lastItem = list.lastOrNull();
            BigDecimal fixedAmount = get.apply(lastItem).subtract(extraAdded);
            set.apply(lastItem, fixedAmount);
        }
    }

    public static String convertToString(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return amount.toString();
    }

    public static boolean isZero(BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) == 0) {
            return true;
        }
        return false;
    }

    public static BigDecimal calculateDivision(BigDecimal dividend, BigDecimal divisor, int scale) {
        if (dividend == null || divisor == null) {
            return BigDecimal.ZERO;
        }
        if (isZero(divisor)) {
            return BigDecimal.ZERO;
        }
        return dividend.divide(divisor, scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal calculateComparePercent(BigDecimal dividend, BigDecimal divisor, int scale) {
        if (isZero(dividend) && isZero(divisor)) {
            return BigDecimal.ZERO;
        }
        if (isZero(dividend)) {
            return new BigDecimal("-1");
        }
        if (isZero(divisor)) {
            return BigDecimal.ONE;
        }

        BigDecimal divideResult = dividend.divide(divisor, scale, BigDecimal.ROUND_HALF_UP);
        return divideResult.subtract(BigDecimal.ONE).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

	public static String convertToMoneyString(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return DecimalHelper.moneyFormat(amount.doubleValue());
    }

    public static String convertToPercentString(BigDecimal amount) {
        if (amount == null) {
            return "0.00%";
        }
        return DecimalHelper.format("0.00%",amount.doubleValue());
    }

    public static String format(String patten, double num) {
        return new DecimalFormat(patten).format(num);
    }

    public static String moneyFormat(double money) {
        return DecimalHelper.format(",##0.00", money);
    }
}
