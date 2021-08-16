package com.fkoc.bankingmanagement.service;

import static com.fkoc.bankingmanagement.util.TestConstants.ACCOUNT_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTED_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTED_AMOUNT_4;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTED_BALANCE;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTED_BALANCE_4;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTMENT_AMOUNT_1;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTMENT_AMOUNT_3;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTMENT_AMOUNT_4;
import static com.fkoc.bankingmanagement.util.TestConstants.AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.BALANCE;
import static com.fkoc.bankingmanagement.util.TestConstants.CALCULATED_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.GREATER_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.GREAT_NEGATIVE_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.NEGATIVE_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.NON_EXISTING_TRANSACTION_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.REF_TRANSACTION_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID_1;
import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID_2;
import static com.fkoc.bankingmanagement.util.TestConstants.UPDATED_BALANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fkoc.bankingmanagement.entity.Account;
import com.fkoc.bankingmanagement.entity.Transaction;
import com.fkoc.bankingmanagement.enums.MessageType;
import com.fkoc.bankingmanagement.enums.Origin;
import com.fkoc.bankingmanagement.repository.AccountRepository;
import com.fkoc.bankingmanagement.repository.TransactionRepository;
import com.fkoc.bankingmanagement.util.ObjectBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class TransactionFacadeServiceImplIntegrationTest {

    @Autowired
    private TransactionFacadeService service;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    private Account createAccount(Long accountId) {
        Account account = Account.builder()
                .id(accountId)
                .balance(BALANCE)
                .build();
        accountRepository.saveAndFlush(account);
        return account;
    }

    private Transaction createTransaction(String transactionId) {
        Transaction transaction = Transaction.builder()
                .transactionId(transactionId)
                .messageType(MessageType.PAYMENT)
                .accountId(ACCOUNT_ID)
                .origin(Origin.VISA)
                .amount(AMOUNT)
                .build();
        transactionRepository.saveAndFlush(transaction);
        return transaction;
    }

    private Transaction createAdjustment(String transactionId, BigDecimal amount) {
        Transaction transaction = Transaction.builder()
                .transactionId(transactionId)
                .messageType(MessageType.ADJUSTMENT)
                .accountId(ACCOUNT_ID)
                .origin(Origin.VISA)
                .amount(amount)
                .refTransactionId(REF_TRANSACTION_ID)
                .build();
        transactionRepository.saveAndFlush(transaction);
        return transaction;
    }

    @Test
    public void givenTransactionRequestWhenMakePaymentThenReturnTransaction() {

        // Arrange
        createAccount(ACCOUNT_ID);
        final Transaction transactionRequest = ObjectBuilder.buildPayment(null);

        // Act
        service.makePayment(transactionRequest);

        // Assert
        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());
        Transaction transaction = transactions.get(0);
        assertNotNull(transaction.getTransactionId());
        assertEquals(ACCOUNT_ID, transaction.getAccountId());
        assertEquals(MessageType.PAYMENT, transaction.getMessageType());
        assertEquals(Origin.VISA, transaction.getOrigin());
        assertEquals(CALCULATED_AMOUNT, transaction.getAmount());

        Optional<Account> account = accountRepository.findById(ACCOUNT_ID);
        assertEquals(ACCOUNT_ID, account.get().getId());
        assertEquals(UPDATED_BALANCE, account.get().getBalance());
    }

    @Test
    public void givenTransactionRequestWithPositiveAdjustmentWhenMakeAdjustmentThenReturnTransaction() {

        // Arrange
        createAccount(ACCOUNT_ID);
        createTransaction(TRANSACTION_ID);
        final Transaction transactionRequest = ObjectBuilder.
                buildAdjustment(TRANSACTION_ID, ADJUSTMENT_AMOUNT_4);

        // Act
        service.makePayment(transactionRequest);

        // Assert
        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(2, transactions.size());
        Transaction transaction = transactions.get(1);
        assertNotNull(transaction.getTransactionId());
        assertEquals(ACCOUNT_ID, transaction.getAccountId());
        assertEquals(MessageType.ADJUSTMENT, transaction.getMessageType());
        assertEquals(Origin.VISA, transaction.getOrigin());
        assertEquals(ADJUSTED_AMOUNT_4, transaction.getAmount());

        Optional<Account> account = accountRepository.findById(ACCOUNT_ID);
        assertEquals(ACCOUNT_ID, account.get().getId());
        assertEquals(ADJUSTED_BALANCE_4, account.get().getBalance());
    }

    @Test
    public void givenTransactionRequestWithNegativeAdjustmentWhenMakeAdjustmentThenReturnTransaction() {

        // Arrange
        createAccount(ACCOUNT_ID);
        createTransaction(TRANSACTION_ID);
        createAdjustment(TRANSACTION_ID_1, BigDecimal.ONE.negate());
        createAdjustment(TRANSACTION_ID_2, BigDecimal.valueOf(2).negate());
        final Transaction transactionRequest = ObjectBuilder.
                buildAdjustment(TRANSACTION_ID, ADJUSTMENT_AMOUNT_3);

        // Act
        service.makePayment(transactionRequest);

        // Assert
        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(4, transactions.size());
        Transaction transaction = transactions.get(3);
        assertNotNull(transaction.getTransactionId());
        assertEquals(ACCOUNT_ID, transaction.getAccountId());
        assertEquals(MessageType.ADJUSTMENT, transaction.getMessageType());
        assertEquals(Origin.VISA, transaction.getOrigin());
        assertEquals(ADJUSTED_AMOUNT, transaction.getAmount());

        Optional<Account> account = accountRepository.findById(ACCOUNT_ID);
        assertEquals(ACCOUNT_ID, account.get().getId());
        assertEquals(ADJUSTED_BALANCE, account.get().getBalance());
    }

    @Test
    public void givenNonExistingAccountIdWhenMakePaymentThenThrowException() {
        // Arrange
        final Transaction transactionRequest = ObjectBuilder.buildPayment(null);

        // Act
        final Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> service.makePayment(transactionRequest));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(
                String.format("Account not found with id: %s", ACCOUNT_ID));
    }

    @Test
    public void givenNegativeAmountWhenMakePaymentThenThrowException() {
        // Arrange
        createAccount(ACCOUNT_ID);
        final Transaction transactionRequest = ObjectBuilder.buildPayment(null);
        transactionRequest.setAmount(NEGATIVE_AMOUNT);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transactionRequest));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Payment amount must be positive!");
    }

    @Test
    public void givenZeroAmountWhenMakePaymentThenThrowException() {
        // Arrange
        createAccount(ACCOUNT_ID);
        final Transaction transactionRequest = ObjectBuilder.buildPayment(null);
        transactionRequest.setAmount(BigDecimal.ZERO);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transactionRequest));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Payment amount must be positive!");
    }

    @Test
    public void givenExistingTransactionIdWhenMakePaymentThenThrowException() {
        // Arrange
        createAccount(ACCOUNT_ID);
        final Transaction transactionRequest = ObjectBuilder.buildPayment(TRANSACTION_ID);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transactionRequest));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Transaction id must be null for payment!");
    }

    @Test
    public void givenNullTransactionIdWhenMakeAdjustmentThenThrowException() {
        // Arrange
        createAccount(ACCOUNT_ID);
        final Transaction transactionRequest = ObjectBuilder.buildAdjustment(null, AMOUNT);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transactionRequest));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(
                "Transaction id cannot be null for adjustment!");
    }

    @Test
    public void givenInsufficientBalanceWhenMakePaymentThenThrowException() {
        // Arrange
        createAccount(ACCOUNT_ID);
        final Transaction transactionRequest = ObjectBuilder.buildPayment(null);
        transactionRequest.setAmount(GREATER_AMOUNT);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transactionRequest));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Insufficient balance to make payment");
    }

    @Test
    public void givenNonExistingTransactionIdWhenMakeAdjustmentThenThrowException() {
        // Arrange
        createAccount(ACCOUNT_ID);
        final Transaction transactionRequest = ObjectBuilder.buildAdjustment(
                NON_EXISTING_TRANSACTION_ID, AMOUNT);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transactionRequest));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(
                String.format("Transaction not found with id: %s", NON_EXISTING_TRANSACTION_ID));
    }

    @Test
    public void givenInsufficientOldAmountWhenMakeAdjustmentThenThrowException() {
        // Arrange
        createAccount(ACCOUNT_ID);
        createTransaction(TRANSACTION_ID);
        createAdjustment(TRANSACTION_ID_1, ADJUSTMENT_AMOUNT_1);
        createAdjustment(TRANSACTION_ID_2, GREAT_NEGATIVE_AMOUNT);
        final Transaction transactionRequest = ObjectBuilder.
                buildAdjustment(TRANSACTION_ID, ADJUSTMENT_AMOUNT_3);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transactionRequest));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(
                "Adjustment amount cannot be greater than old payment amount");
    }

    @Test
    public void givenZeroAmountWhenMakeAdjustmentThenThrowException() {
        // Arrange
        createAccount(ACCOUNT_ID);
        createTransaction(TRANSACTION_ID);
        final Transaction transactionRequest = ObjectBuilder.buildAdjustment(
                TRANSACTION_ID, BigDecimal.ZERO);

        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.makePayment(transactionRequest));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Adjustment amount cannot be zero!");
    }

}
