package com.fkoc.bankingmanagement.service;

import com.fkoc.bankingmanagement.entity.Transaction;

public interface TransactionFacadeService {

    Transaction makePayment(Transaction transactionRequest);

}