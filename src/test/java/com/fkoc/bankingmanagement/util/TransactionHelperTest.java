package com.fkoc.bankingmanagement.util;

import static com.fkoc.bankingmanagement.util.TestConstants.BASE;
import static com.fkoc.bankingmanagement.util.TestConstants.FORMATTED_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.PERCENT;
import static com.fkoc.bankingmanagement.util.TestConstants.PERCENTAGE;
import static com.fkoc.bankingmanagement.util.TestConstants.UNFORMATTED_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionHelperTest {

    @Test
    public void givenNothingWhenGenerateIdThenResultString() {
        // Act
        final String response = TransactionHelper.generateId();
        // Assert
        assertThat(response.isEmpty()).isFalse();
    }

    @Test
    public void givenBaseAndPercentWhenPercentageThenResultPercentage() {
        // Act
        final BigDecimal response = TransactionHelper.percentage(BASE, PERCENT);
        // Assert
        assertThat(response).isEqualTo(PERCENTAGE);
    }

    @Test
    public void givenAmountWhenFormatAmountThenResultFormattedAmount() {
        // Act
        final BigDecimal response = TransactionHelper.formatAmount(UNFORMATTED_AMOUNT);
        // Assert
        assertThat(response).isEqualTo(FORMATTED_AMOUNT);
    }

}
