package com.fkoc.bankingmanagement.util;

import static com.fkoc.bankingmanagement.util.Constants.ONE_HUNDRED;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionHelper {

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public static BigDecimal percentage(BigDecimal base, BigDecimal percent) {
        return base.multiply(percent).divide(ONE_HUNDRED);
    }

    public BigDecimal formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.UP);
    }

}