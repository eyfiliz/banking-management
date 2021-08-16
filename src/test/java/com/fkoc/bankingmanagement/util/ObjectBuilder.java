package com.fkoc.bankingmanagement.util;

import static com.fkoc.bankingmanagement.util.TestConstants.ACCOUNT_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.BALANCE;
import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID;

import com.fkoc.bankingmanagement.dto.AccountResponse;
import com.fkoc.bankingmanagement.dto.TransactionRequest;
import com.fkoc.bankingmanagement.dto.TransactionResponse;
import com.fkoc.bankingmanagement.entity.Account;
import com.fkoc.bankingmanagement.entity.Transaction;
import com.fkoc.bankingmanagement.enums.MessageType;
import com.fkoc.bankingmanagement.enums.Origin;
import java.math.BigDecimal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectBuilder {

    public static Account buildAccount(Long accountId) {
        return Account.builder()
                .id(accountId)
                .balance(BALANCE)
                .build();
    }

    public static Transaction buildPayment(String transactionId) {
        return Transaction.builder()
                .transactionId(transactionId)
                .messageType(MessageType.PAYMENT)
                .accountId(ACCOUNT_ID)
                .origin(Origin.VISA)
                .amount(AMOUNT)
                .build();
    }

    public static Transaction buildAdjustment(String transactionId, BigDecimal amount) {
        return Transaction.builder()
                .transactionId(transactionId)
                .messageType(MessageType.ADJUSTMENT)
                .accountId(ACCOUNT_ID)
                .origin(Origin.VISA)
                .amount(amount)
                .refTransactionId(TRANSACTION_ID)
                .build();
    }

    public static TransactionRequest buildTransactionRequest() {
        return TransactionRequest.builder()
                .messageType(MessageType.PAYMENT)
                .accountId(ACCOUNT_ID)
                .origin(Origin.VISA)
                .amount(AMOUNT)
                .build();
    }

    public static TransactionResponse buildTransactionResponse() {
        return TransactionResponse.builder()
                .transactionId(TRANSACTION_ID)
                .build();
    }

    public static AccountResponse buildAccountResponse() {
        return AccountResponse.builder()
                .balance(BALANCE)
                .build();
    }
}
