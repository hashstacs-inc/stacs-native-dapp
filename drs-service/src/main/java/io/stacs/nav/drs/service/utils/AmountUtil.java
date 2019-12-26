package io.stacs.nav.drs.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * 检查 amount
 *
 * @author lingchao
 * @create 2017年12月19日11:01
 */
@Slf4j
public class AmountUtil {
    private static BigDecimal MAX_AMOUNT = new BigDecimal("999999999999999999.99999999");
    private static BigDecimal MIN_AMOUNT = new BigDecimal("0.00000001");
    private static final String RS_AMOUNT_PATTERN = "(\\d{1,18})(\\.\\d{0,8})?";
    public static final int DEFAULT_DECIMALS = 8;
    /**
     * 校验 amount 是否在规定单位内
     *
     * @param amount
     * @return
     */
    public static boolean isLegal(String amount) {
        if(!Pattern.matches(RS_AMOUNT_PATTERN, amount)){
            log.warn("Illegal number, format (18,8), amount:{}",amount);
            return false;
        }
        try {
            BigDecimal validAmount = new BigDecimal(amount).setScale(8, BigDecimal.ROUND_DOWN);
            if (validAmount.compareTo(new BigDecimal(amount)) != 0) {
                return false;
            }
            int max = validAmount.compareTo(MAX_AMOUNT);
            int min = validAmount.compareTo(MIN_AMOUNT);
            //amount 在 最大和最小之间为合法
            if (max == 1 || min == -1) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isLegalWithZero(String amount) {
        if (!Pattern.matches(RS_AMOUNT_PATTERN, amount)) {
            log.warn("Illegal number, format (18,8), amount:{}", amount);
            return false;
        }
        return true;
    }
    /**
     * transfer ContractAmount to Rs Amount, because RS amount * 10^8  = contract amount
     *
     * @param amountStr
     * @param decimals
     * @return
     */
    public static BigDecimal transContractAmount2RSAmount(String amountStr, int decimals) {
        BigDecimal contractAmount = StringUtils.isBlank(amountStr) ? BigDecimal.ZERO : new BigDecimal(amountStr);
        BigDecimal amount = contractAmount.divide(BigDecimal.TEN.pow(decimals));
        return amount.setScale(decimals, BigDecimal.ROUND_DOWN);
    }

    /**
     * transfer RS Amount to ContractAmount, because RS amount * 10^8  = contract amount
     *
     * @param amount
     * @param decimals
     * @return
     */
    public static BigInteger transRSAmount2ContractAmount(BigDecimal amount, int decimals) {
        if (null == amount) {
            return BigInteger.ZERO;
        }
        return amount.scaleByPowerOfTen(decimals).toBigInteger();
    }

    /**
     * String to BigDecimal
     */
    public static BigDecimal toBigDecimal(BigDecimal amount) {
        return amount.setScale(8,BigDecimal.ROUND_DOWN);
    }
    /**
     * String to BigDecimal
     */
    public static BigDecimal toBigDecimal(String amountStr) {
        return toBigDecimal(amountStr,8);
    }

    /**
     * String to BigDecimal
     */
    public static BigDecimal toBigDecimal(long amountStr) {
        return toBigDecimal(""+amountStr,8);
    }

    /**
     * String to BigDecimal
     */
    public static BigDecimal toBigDecimal(String amountStr, int decimals) {
        BigDecimal amount = StringUtils.isBlank(amountStr) ? BigDecimal.ZERO : new BigDecimal(amountStr);
        return amount.setScale(decimals, BigDecimal.ROUND_DOWN);
    }

    /**
     * @param bigDecimal
     * @return BigDecimal
     * @Title: clearNoUseZeroForBigDecimal
     * @Description: 去掉BigDecimal尾部多余的0，通过stripTrailingZeros().toPlainString()实现
     */
    public static BigDecimal clearNoUseZeroForBigDecimal(BigDecimal bigDecimal) {
        if (null == bigDecimal) {
            throw new NullPointerException("clearNoUseZeroForBigDecimal  bigDecimal is null error!");
        }
        if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        String bigDecimalStr = bigDecimal.stripTrailingZeros().toPlainString();
        return new BigDecimal(bigDecimalStr);
    }

    /**
     * 判断是否是一个符合不超过18正数，小数不超过8位，且大于0
     * @param amount
     * @return
     */
    public static boolean isMatchesRsAmount(String amount){

        if(!Pattern.matches(RS_AMOUNT_PATTERN, amount)){
            throw new RuntimeException("Illegal number, format (18,8)");
        }
        boolean result = new BigDecimal(amount).compareTo(BigDecimal.ZERO) == 0 ? false : true;
        return result;
    }
}
