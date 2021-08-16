package com.fkoc.bankingmanagement.service;

import static com.fkoc.bankingmanagement.util.TestConstants.ACCOUNT_ID;
import static com.fkoc.bankingmanagement.util.TestConstants.AMOUNT;
import static com.fkoc.bankingmanagement.util.TestConstants.BALANCE;
import static com.fkoc.bankingmanagement.util.TestConstants.INSUFFICIENT_BALANCE;
import static com.fkoc.bankingmanagement.util.TestConstants.NON_EXISTING_ACCOUNT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fkoc.bankingmanagement.entity.Account;
import com.fkoc.bankingmanagement.repository.AccountRepository;
import com.fkoc.bankingmanagement.util.ObjectBuilder;
import java.math.BigDecimal;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl service;

    @Test
    public void givenIdWhenGetAccountByIdThenResultIsAccount() {
        // Arrange
        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // Act
        final Account result = service.getAccountById(ACCOUNT_ID);

        // Assert
        assertThat(result.getId()).isEqualTo(ACCOUNT_ID);
        assertThat(result.getBalance()).isEqualTo(BALANCE);
    }

    @Test
    public void givenNonExistingIdWhenGetAccountByIdThenThrowException() {
        // Arrange
        when(accountRepository.findById(NON_EXISTING_ACCOUNT_ID)).thenReturn(Optional.empty());

        // Act
        final Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> service.getAccountById(NON_EXISTING_ACCOUNT_ID));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(
                String.format("Account not found with id: %s", NON_EXISTING_ACCOUNT_ID));
    }

    @Test
    public void givenAccountWhenUpdateAccountThenResultIsAccount() {
        // Arrange
        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        when(accountRepository.save(account)).thenReturn(account);

        // Act
        final Account result = service.updateAccount(account);

        // Assert
        assertThat(result.getId()).isEqualTo(ACCOUNT_ID);
        assertThat(result.getBalance()).isEqualTo(BALANCE);
    }

    @Test
    public void givenSufficientBalanceWhenCheckBalanceIsSufficientThenResultIsOk() {
        // Act
        final BigDecimal response = service.checkBalanceIsSufficient(BALANCE, AMOUNT);

        // Assert
        assertThat(response).isEqualTo(BALANCE);
    }

    @Test
    public void givenInsufficientBalanceWhenCheckBalanceIsSufficientThenThrowException() {
        // Act
        final Throwable exception = assertThrows(RuntimeException.class,
                () -> service.checkBalanceIsSufficient(INSUFFICIENT_BALANCE, AMOUNT));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Insufficient balance to make payment");
    }
}
