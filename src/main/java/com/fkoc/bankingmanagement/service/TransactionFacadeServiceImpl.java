package com.fkoc.bankingmanagement.service;

import static com.fkoc.bankingmanagement.util.Constants.ZERO;

import com.fkoc.bankingmanagement.entity.Account;
import com.fkoc.bankingmanagement.entity.Transaction;
import com.fkoc.bankingmanagement.enums.MessageType;
import com.fkoc.bankingmanagement.util.TransactionHelper;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class TransactionFacadeServiceImpl implements TransactionFacadeService {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @Override
    @Synchronized
    public Transaction makePayment(Transaction transaction) {

        final Account account = accountService.getAccountById(transaction.getAccountId());

        if (transaction.getMessageType().equals(MessageType.PAYMENT)) {
            if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= ZERO) {
                throw new RuntimeException("Payment amount must be positive!");
            }
            if (!ObjectUtils.isEmpty(transaction.getTransactionId())) {
                throw new RuntimeException("Transaction id must be null for payment!");
            }
            return payment(transaction, account);

        } else if (transaction.getMessageType().equals(MessageType.ADJUSTMENT)) {
            if (ObjectUtils.isEmpty(transaction.getTransactionId())) {
                throw new RuntimeException("Transaction id cannot be null for adjustment!");
            }
            return adjustment(transaction.getTransactionId(), account, transaction.getAmount());
        } else {
            throw new RuntimeException("Invalid message type!");
        }

    }

    private Transaction payment(Transaction transaction, Account account) {

        final BigDecimal amount = transaction.getAmount();
        final BigDecimal balance = account.getBalance();
        final BigDecimal commission = TransactionHelper.percentage(amount,
                transaction.getOrigin().getCommissionRate());
        final BigDecimal calculatedAmount = amount.add(commission);
        accountService.checkBalanceIsSufficient(balance, calculatedAmount);
        final BigDecimal formattedAmount = TransactionHelper.formatAmount(calculatedAmount);

        final String transactionId = TransactionHelper.generateId();
        transaction.setTransactionId(transactionId);
        transaction.setAmount(formattedAmount);
        transactionService.saveTransaction(transaction);

        if (!ObjectUtils.isEmpty(transaction.getTransactionId())) {
            BigDecimal updatedBalance = balance.subtract(formattedAmount);
            account.setBalance(updatedBalance);
            accountService.updateAccount(account);
        }

        return transaction;
    }

    private Transaction adjustment(String id, Account account, BigDecimal adjustedAmount) {

        final Transaction existingTransaction = transactionService.getTransactionById(id);
        final BigDecimal balance = account.getBalance();
        final BigDecimal commission = TransactionHelper.percentage(adjustedAmount,
                existingTransaction.getOrigin().getCommissionRate());
        final BigDecimal calculatedAmount = adjustedAmount.add(commission);

        if (adjustedAmount.compareTo(BigDecimal.ZERO) > ZERO) {
            accountService.checkBalanceIsSufficient(balance, calculatedAmount);

        } else if (adjustedAmount.compareTo(BigDecimal.ZERO) < ZERO) {
            final BigDecimal existingAmount = existingTransaction.getAmount();
            final List<Transaction> adjustedTransactions =
                    transactionService.getTransactionsByRefTransactionId(id);
            final BigDecimal adjustedAmountSum = transactionService.getAmountSum(
                    adjustedTransactions);
            final BigDecimal oldAmount = existingAmount.add(adjustedAmountSum);
            transactionService.checkAmount(oldAmount, calculatedAmount);

        } else {
            throw new RuntimeException("Adjustment amount cannot be zero!");
        }

        final BigDecimal formattedAmount = TransactionHelper.formatAmount(calculatedAmount);
        final String transactionId = TransactionHelper.generateId();
        Transaction transaction = Transaction.builder()
                .transactionId(transactionId)
                .accountId(existingTransaction.getAccountId())
                .origin(existingTransaction.getOrigin())
                .messageType(MessageType.ADJUSTMENT)
                .amount(formattedAmount)
                .refTransactionId(existingTransaction.getTransactionId())
                .build();
        transactionService.saveTransaction(transaction);

        if (!ObjectUtils.isEmpty(transaction.getTransactionId())) {
            BigDecimal updatedBalance = balance.subtract(formattedAmount);
            account.setBalance(updatedBalance);
            accountService.updateAccount(account);
        }

        return transaction;
    }

}

