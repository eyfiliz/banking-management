package com.fkoc.bankingmanagement.service;

import static com.fkoc.bankingmanagement.util.TestConstants.ACCOUNT_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTED_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTED_BALANCE;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTMENT_AMOUNT_1;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTMENT_AMOUNT_2;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTMENT_AMOUNT_3;
import static com.fkoc.bankingmanagement.util.TestConstants.AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.CALCULATED_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.OLD_ADJUSTED_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.OLD_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.REF_TRANSACTION_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID_1;
import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID_2;
import static com.fkoc.bankingmanagement.util.TestConstants.UPDATED_BALANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fkoc.bankingmanagement.entity.Account;
import com.fkoc.bankingmanagement.entity.Transaction;
import com.fkoc.bankingmanagement.enums.MessageType;
import com.fkoc.bankingmanagement.enums.Origin;
import com.fkoc.bankingmanagement.util.ObjectBuilder;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionFacadeServiceImplTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionFacadeServiceImpl service;

    @Test
    public void givenTransactionWhenMakePaymentThenResultIsTransaction() {
        // Arrange
        final Transaction transaction = ObjectBuilder.buildPayment(null);
        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        final ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);

        when(accountService.getAccountById(transaction.getAccountId())).thenReturn(account);
        when(accountService.checkBalanceIsSufficient(any(), any())).thenReturn(any());
        when(transactionService.saveTransaction(transaction)).thenReturn(transaction);
        when(accountService.updateAccount(account)).thenReturn(account);

        // Act
        final Transaction result = service.makePayment(transaction);

        // Assert
        assertThat(result.getTransactionId()).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(result.getMessageType()).isEqualTo(MessageType.PAYMENT);
        assertThat(result.getAmount()).isEqualTo(CALCULATED_AMOUNT);
        assertThat(result.getOrigin()).isEqualTo(Origin.VISA);

        verify(accountService, times(1)).updateAccount(accountCaptor.capture());
        assertThat(accountCaptor.getValue().getId()).isEqualTo(ACCOUNT_ID);
        assertThat(accountCaptor.getValue().getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    public void givenNegativeAmountWhenMakePaymentThenThrowException() {
        // Arrange
        final Transaction transaction = ObjectBuilder.buildPayment(TRANSACTION_ID);
        transaction.setAmount(AMOUNT.negate());
        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(account);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transaction));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Payment amount must be positive!");
    }

    @Test
    public void givenZeroAmountWhenMakePaymentThenThrowException() {
        // Arrange
        final Transaction transaction = ObjectBuilder.buildPayment(TRANSACTION_ID);
        transaction.setAmount(BigDecimal.ZERO);
        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(account);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transaction));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Payment amount must be positive!");
    }

    @Test
    public void givenExistingTransactionIdWhenMakePaymentThenThrowException() {
        // Arrange
        final Transaction transaction = ObjectBuilder.buildPayment(TRANSACTION_ID);
        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(account);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transaction));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Transaction id must be null for payment!");
    }

    @Test
    public void givenNonExistingTransactionIdWhenMakeAdjustmentThenThrowException() {
        // Arrange
        final Transaction transaction = ObjectBuilder.buildAdjustment(null, AMOUNT);
        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(account);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transaction));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(
                "Transaction id cannot be null for adjustment!");
    }

    @Test
    public void givenTransactionWithPositiveAmountWhenMakeAdjustmentThenResultIsTransaction() {
        // Arrange
        final Transaction transaction = ObjectBuilder.buildAdjustment(TRANSACTION_ID, AMOUNT);
        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        final ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);

        when(accountService.getAccountById(transaction.getAccountId())).thenReturn(account);
        when(transactionService.getTransactionById(TRANSACTION_ID)).thenReturn(transaction);
        when(accountService.checkBalanceIsSufficient(any(), any())).thenReturn(any());
        when(transactionService.saveTransaction(transaction)).thenReturn(transaction);
        when(accountService.updateAccount(account)).thenReturn(account);

        // Act
        final Transaction result = service.makePayment(transaction);

        // Assert
        assertThat(result.getTransactionId()).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(result.getMessageType()).isEqualTo(MessageType.ADJUSTMENT);
        assertThat(result.getAmount()).isEqualTo(CALCULATED_AMOUNT);
        assertThat(result.getOrigin()).isEqualTo(Origin.VISA);

        verify(accountService, times(1)).updateAccount(accountCaptor.capture());
        assertThat(accountCaptor.getValue().getId()).isEqualTo(ACCOUNT_ID);
        assertThat(accountCaptor.getValue().getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    public void givenTransactionWithNegativeAmountWhenMakeAdjustmentThenResultIsTransaction() {
        // Arrange
        final Transaction oldTransaction = ObjectBuilder.buildPayment(TRANSACTION_ID);
        oldTransaction.setAmount(OLD_AMOUNT);
        final Transaction oldAdjustedTransaction1 = ObjectBuilder.buildAdjustment(
                TRANSACTION_ID_1, ADJUSTMENT_AMOUNT_1);
        final Transaction oldAdjustedTransaction2 = ObjectBuilder.buildAdjustment(
                TRANSACTION_ID_2, ADJUSTMENT_AMOUNT_2);
        final List<Transaction> oldAdjustedTransactions = List.of(oldAdjustedTransaction1,
                oldAdjustedTransaction2);
        final Transaction transaction = ObjectBuilder.
                buildAdjustment(TRANSACTION_ID, ADJUSTMENT_AMOUNT_3);

        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        final ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);

        when(accountService.getAccountById(transaction.getAccountId())).thenReturn(account);
        when(transactionService.getTransactionById(TRANSACTION_ID)).thenReturn(transaction);
        when(transactionService.getTransactionsByRefTransactionId(REF_TRANSACTION_ID))
                .thenReturn(oldAdjustedTransactions);
        when(transactionService.getAmountSum(oldAdjustedTransactions)).thenReturn(
                OLD_ADJUSTED_AMOUNT);
        when(transactionService.checkAmount(any(), any())).thenReturn(any());
        when(transactionService.saveTransaction(transaction)).thenReturn(transaction);
        when(accountService.updateAccount(account)).thenReturn(account);

        // Act
        final Transaction result = service.makePayment(transaction);

        // Assert
        assertThat(result.getTransactionId()).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(result.getMessageType()).isEqualTo(MessageType.ADJUSTMENT);
        assertThat(result.getAmount()).isEqualTo(ADJUSTED_AMOUNT);
        assertThat(result.getOrigin()).isEqualTo(Origin.VISA);

        verify(accountService, times(1)).updateAccount(accountCaptor.capture());
        assertThat(accountCaptor.getValue().getId()).isEqualTo(ACCOUNT_ID);
        assertThat(accountCaptor.getValue().getBalance()).isEqualTo(ADJUSTED_BALANCE);
    }

    @Test
    public void givenZeroAmountWhenMakeAdjustmentThenThrowException() {
        // Arrange
        final Transaction transaction = ObjectBuilder.buildAdjustment(TRANSACTION_ID,
                BigDecimal.ZERO);
        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(account);
        when(transactionService.getTransactionById(TRANSACTION_ID)).thenReturn(transaction);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transaction));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Adjustment amount cannot be zero!");
    }

}