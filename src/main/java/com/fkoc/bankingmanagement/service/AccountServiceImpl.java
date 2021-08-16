package com.fkoc.bankingmanagement.service;

import static com.fkoc.bankingmanagement.util.Constants.ZERO;

import com.fkoc.bankingmanagement.entity.Account;
import com.fkoc.bankingmanagement.repository.AccountRepository;
import java.math.BigDecimal;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Account not found with id: %s", id)));
    }

    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public BigDecimal checkBalanceIsSufficient(BigDecimal balance, BigDecimal paymentAmount) {
        if (balance.compareTo(paymentAmount) < ZERO) {
            throw new RuntimeException("Insufficient balance to make payment");
        }
        return balance;
    }

}
