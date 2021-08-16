package com.fkoc.bankingmanagement.service;

import com.fkoc.bankingmanagement.entity.Account;
import java.math.BigDecimal;

public interface AccountService {

    Account getAccountById(Long id);

    Account updateAccount(Account account);

    BigDecimal checkBalanceIsSufficient(BigDecimal balance, BigDecimal paymentAmount);

}