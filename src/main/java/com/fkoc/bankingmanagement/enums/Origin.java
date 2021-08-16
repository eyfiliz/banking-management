package com.fkoc.bankingmanagement.enums;

import static com.fkoc.bankingmanagement.util.Constants.ONE;
import static com.fkoc.bankingmanagement.util.Constants.TWO;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Origin {
    VISA(ONE),
    MASTER(TWO);

    public final BigDecimal commissionRate;

}
