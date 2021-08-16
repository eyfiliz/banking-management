package com.fkoc.bankingmanagement.util;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {

    public static final long ACCOUNT_ID = 1L;
    public static final long NON_EXISTING_ACCOUNT_ID = 9999L;
    public static final String TRANSACTION_ID = "57d21686-be1a-4094-92fa-008923917920";
    public static final String TRANSACTION_ID_1 = "57d21686-be1a-4094-92fa-008923917921";
    public static final String TRANSACTION_ID_2 = "57d21686-be1a-4094-92fa-008923917922";
    public static final String NON_EXISTING_TRANSACTION_ID = "abc";
    public static final String REF_TRANSACTION_ID = "57d21686-be1a-4094-92fa-008923917920";
    public static final BigDecimal BALANCE = new BigDecimal("100");
    public static final BigDecimal INSUFFICIENT_BALANCE = new BigDecimal("5");
    public static final BigDecimal UPDATED_BALANCE = new BigDecimal("89.90");
    public static final BigDecimal ADJUSTED_BALANCE = new BigDecimal("103.03");
    public static final BigDecimal ADJUSTED_BALANCE_4 = new BigDecimal("98.99");
    public static final BigDecimal AMOUNT = new BigDecimal("10");
    public static final BigDecimal CALCULATED_AMOUNT = new BigDecimal("10.10");
    public static final BigDecimal ADJUSTMENT_AMOUNT = new BigDecimal("10").negate();
    public static final BigDecimal ADJUSTMENT_AMOUNT_1 = new BigDecimal("1").negate();
    public static final BigDecimal ADJUSTMENT_AMOUNT_2 = new BigDecimal("2").negate();
    public static final BigDecimal ADJUSTMENT_AMOUNT_3 = new BigDecimal("3").negate();
    public static final BigDecimal ADJUSTMENT_AMOUNT_4 = new BigDecimal("1");
    public static final BigDecimal ADJUSTED_AMOUNT = new BigDecimal("3.03").negate();
    public static final BigDecimal ADJUSTED_AMOUNT_4 = new BigDecimal("1.01");
    public static final BigDecimal OLD_ADJUSTED_AMOUNT = new BigDecimal("3.03").negate();
    public static final BigDecimal OLD_AMOUNT = new BigDecimal("20");
    public static final BigDecimal INSUFFICIENT_OLD_AMOUNT = new BigDecimal("5");
    public static final BigDecimal BASE = new BigDecimal("1000");
    public static final BigDecimal PERCENT = new BigDecimal("5");
    public static final BigDecimal PERCENTAGE = new BigDecimal("50");
    public static final BigDecimal UNFORMATTED_AMOUNT = new BigDecimal("455.444445");
    public static final BigDecimal FORMATTED_AMOUNT = new BigDecimal("455.45");
    public static final BigDecimal GREATER_AMOUNT = new BigDecimal("400");
    public static final BigDecimal NEGATIVE_AMOUNT = new BigDecimal("10").negate();
    public static final BigDecimal GREAT_NEGATIVE_AMOUNT = new BigDecimal("8").negate();
}
