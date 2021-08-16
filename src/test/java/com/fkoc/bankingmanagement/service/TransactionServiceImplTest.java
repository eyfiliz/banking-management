package com.fkoc.bankingmanagement.service;

import static com.fkoc.bankingmanagement.util.TestConstants.ACCOUNT_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.ADJUSTMENT_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.INSUFFICIENT_OLD_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.NON_EXISTING_TRANSACTION_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.OLD_AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.REF_TRANSACTION_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fkoc.bankingmanagement.entity.Transaction;
import com.fkoc.bankingmanagement.enums.MessageType;
import com.fkoc.bankingmanagement.enums.Origin;
import com.fkoc.bankingmanagement.repository.TransactionRepository;
import com.fkoc.bankingmanagement.util.ObjectBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl service;

    @Test
    public void givenTransactionWhenSaveTransactionThenResultIsSavedTransaction() {
        // Arrange
        final Transaction transaction = ObjectBuilder.buildPayment(TRANSACTION_ID);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        final Transaction result = service.saveTransaction(transaction);

        // Assert
        assertThat(result.getTransactionId()).isEqualTo(TRANSACTION_ID);
        assertThat(result.getMessageType()).isEqualTo(MessageType.PAYMENT);
        assertThat(result.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(result.getOrigin()).isEqualTo(Origin.VISA);
        assertThat(result.getAmount()).isEqualTo(AMOUNT);
    }

    @Test
    public void givenIdWhenGetTransactionByIdThenResultIsTransaction() {
        // Arrange
        final Transaction transaction = ObjectBuilder.buildPayment(TRANSACTION_ID);
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transaction));

        // Act
        final Transaction result = service.getTransactionById(TRANSACTION_ID);

        // Assert
        assertThat(result.getTransactionId()).isEqualTo(TRANSACTION_ID);
        assertThat(result.getMessageType()).isEqualTo(MessageType.PAYMENT);
        assertThat(result.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(result.getOrigin()).isEqualTo(Origin.VISA);
        assertThat(result.getAmount()).isEqualTo(AMOUNT);
    }

    @Test
    public void givenNonExistingIdWhenGetTransactionByIdThenThrowException() {
        // Arrange
        when(transactionRepository.findById(NON_EXISTING_TRANSACTION_ID)).thenReturn(Optional.empty());

        // Act
        final Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> service.getTransactionById(NON_EXISTING_TRANSACTION_ID));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(
                String.format("Transaction not found with id: %s", NON_EXISTING_TRANSACTION_ID));
    }

    @Test
    public void givenRefTransactionIdWhenGetTransactionsByRefTransactionIdThenResultIsTransactionList() {
        // Arrange
        final Transaction transaction1 = ObjectBuilder.buildPayment(TRANSACTION_ID);
        final Transaction transaction2 = ObjectBuilder.buildPayment(TRANSACTION_ID_2);
        transaction1.setRefTransactionId(REF_TRANSACTION_ID);
        transaction2.setRefTransactionId(REF_TRANSACTION_ID);
        final List<Transaction> transactionList = List.of(transaction1, transaction2);
        when(transactionRepository.findByRefTransactionId(REF_TRANSACTION_ID)).thenReturn(transactionList);

        // Act
        final List<Transaction> resultList = service.getTransactionsByRefTransactionId(REF_TRANSACTION_ID);

        // Assert
        assertThat(resultList.get(0).getTransactionId()).isEqualTo(TRANSACTION_ID);
        assertThat(resultList.get(1).getTransactionId()).isEqualTo(TRANSACTION_ID_2);
        assertThat(resultList.get(0).getRefTransactionId()).isEqualTo(REF_TRANSACTION_ID);
        assertThat(resultList.get(1).getRefTransactionId()).isEqualTo(REF_TRANSACTION_ID);
    }

    @Test
    public void givenGreaterOldAmountThanAdjustmentAmountWhenCheckAmountThenResultIsOk() {
        // Act
        final BigDecimal response = service.checkAmount(OLD_AMOUNT, ADJUSTMENT_AMOUNT);

        // Assert
        assertThat(response).isEqualTo(ADJUSTMENT_AMOUNT);
    }

    @Test
    public void givenLessOldAmountThanAdjustmentAmountWhenCheckAmountThenThrowException() {
        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.checkAmount(INSUFFICIENT_OLD_AMOUNT, ADJUSTMENT_AMOUNT));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Adjustment amount cannot be greater than old payment amount");
    }

    @Test
    public void givenTransactionListWhenGetAmountSumThenResultIsAmountSum() {
        // Arrange
        final Transaction transaction1 = ObjectBuilder.buildPayment(TRANSACTION_ID);
        final Transaction transaction2 = ObjectBuilder.buildPayment(TRANSACTION_ID_2);
        final List<Transaction> transactionList = List.of(transaction1, transaction2);

        // Act
        final BigDecimal result = service.getAmountSum(transactionList);

        // Assert
        assertThat(result).isEqualTo(AMOUNT.add(AMOUNT));
    }

}
