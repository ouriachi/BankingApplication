package domain.service;

import domain.exception.AccountNotFoundException;
import domain.exception.InsufficientBalanceException;
import domain.exception.InvalidOperationException;
import domain.model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.BankingConstants;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


public class SavingsAccountServiceTest {

    private SavingsAccountService savingsAccountService;
    double expectedInterest = 50;

    @BeforeEach
    void setUp() {
        savingsAccountService = new SavingsAccountService();
    }

    @Test
    void withdrawShouldThrowExceptionWhenAccountIsNull() {
        // When
        Exception exception = assertThrows(AccountNotFoundException.class, () ->
                savingsAccountService.withdraw(null, 100.0)
        );
        // Then
        assertEquals(BankingConstants.ACCOUNT_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void withdrawShouldResetMonthlyDrawalWhenNewMonthHasStarted() {
        // Given
        SavingsAccount account = new SavingsAccount("123", "Ahmed ", 1000.0);
        account.setMonthlyDrawal(500.0);
        account.setLastDrawalDate(LocalDate.now().minusMonths(2));
        // When
        savingsAccountService.withdraw(account, 100.0);
        // Then
        assertEquals(900.0, account.getBalance());
        assertEquals(LocalDate.now(), account.getLastDrawalDate());
    }

    @Test
    void withdrawShouldThrowExceptionWhenMonthlyLimitExceeded() {
        // Given
        SavingsAccount account = new SavingsAccount("123", "Ahmed OU", 2000.0);
        account.setMonthlyDrawal(900.0);
        account.setLastDrawalDate(LocalDate.now());
        // when
        Exception exception = assertThrows(InvalidOperationException.class, () ->
                savingsAccountService.withdraw(account, 200.0)
        );
        // Then
        assertEquals("The withdrawal exceeds the maximum allowed amount of 1000 euros per month!", exception.getMessage());
    }

    @Test
    void withdrawShouldThrowExceptionWhenBalanceIsInsufficient() {
        // Given
        SavingsAccount account = new SavingsAccount("888899", "Ahmed OU", 100.0);
        account.setMonthlyDrawal(0.0);
        account.setLastDrawalDate(LocalDate.now());
        // When
        Exception exception = assertThrows(InsufficientBalanceException.class, () ->
                savingsAccountService.withdraw(account, 200.0)
        );
        // Then
        assertEquals("Insufficient balance.", exception.getMessage());
    }



    @Test
    void calculateInterestShouldThrowExceptionWhenAccountIsNull() {
        // When
        Exception exception = assertThrows(AccountNotFoundException.class, () ->
                savingsAccountService.calculateInterest(null)
        );
        // Then
        assertEquals(BankingConstants.ACCOUNT_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void calculateInterestShouldThrowExceptionWhenLessThanOneMonthSinceLastInterest() {
        // Given
        SavingsAccount account = new SavingsAccount("555", "Aylan OU", 1000.0);
        account.setLastInterestDate(LocalDate.now().minusDays(15));
        // When
        Exception exception = assertThrows(InvalidOperationException.class, () ->
                savingsAccountService.calculateInterest(account)
        );
        // Then
        assertTrue(exception.getMessage().contains("Interest can only be added once a month. Next calculation date: "));
    }

    @Test
    void calculateInterestShouldCalculateInterestWhenMoreThanOneMonthSinceLastInterest() {
        // Given
        SavingsAccount account = new SavingsAccount("222", "Ahmed OU", 1000.0);
        account.setLastInterestDate(LocalDate.now().minusMonths(2));

        // When
        savingsAccountService.calculateInterest(account);

        // Then
        assertEquals(1000.0 + expectedInterest, account.getBalance());
        assertEquals(LocalDate.now(), account.getLastInterestDate());
    }

    @Test
    void calculateInterestShouldCalculateInterestWhenNoPreviousInterestDate() {
        // given
        SavingsAccount account = new SavingsAccount("333", "Ahmed ou", 1000.0);
        account.setLastInterestDate(null);
        // When
        savingsAccountService.calculateInterest(account);

        // Then
        assertEquals(1000.0 + expectedInterest, account.getBalance(), 0.01);
        assertEquals(LocalDate.now(), account.getLastInterestDate());
    }



}
