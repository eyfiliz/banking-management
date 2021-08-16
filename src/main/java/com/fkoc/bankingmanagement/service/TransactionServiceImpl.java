package com.fkoc.bankingmanagement.service;

import static com.fkoc.bankingmanagement.util.Constants.ZERO;

import com.fkoc.bankingmanagement.entity.Transaction;
import com.fkoc.bankingmanagement.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction saveTransaction (Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Transaction not found with id: %s", id)));
    }

    @Override
    public List<Transaction> getTransactionsByRefTransactionId(String refTransactionId) {
        return transactionRepository.findByRefTransactionId(refTransactionId);
    }

    @Override
    public BigDecimal checkAmount(BigDecimal oldAmount, BigDecimal adjustmentAmount) {
        if (oldAmount.compareTo(adjustmentAmount.negate()) < ZERO) {
            throw new RuntimeException("Adjustment amount cannot be greater than old payment amount");
        }
        return adjustmentAmount;
    }

    @Override
    public BigDecimal getAmountSum(List<Transaction> transactions) {
        return transactions.stream()
                .map(transaction -> transaction.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}