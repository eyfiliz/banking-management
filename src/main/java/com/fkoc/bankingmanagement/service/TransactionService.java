package com.fkoc.bankingmanagement.service;

import com.fkoc.bankingmanagement.entity.Transaction;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    Transaction saveTransaction (Transaction transaction);

    Transaction getTransactionById(String id);

    List<Transaction> getTransactionsByRefTransactionId(String refTransactionId);

    BigDecimal checkAmount(BigDecimal oldAmount, BigDecimal adjustmentAmount);

    BigDecimal getAmountSum(List<Transaction> transactions);

}