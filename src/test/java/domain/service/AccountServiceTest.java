package domain.service;

import domain.exception.AccountNotFoundException;
import domain.exception.InsufficientBalanceException;
import domain.model.Account;
import domain.model.CurrentAccount;
import domain.model.SavingsAccount;
import infrastructure.persistence.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.BankingConstants;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;
    private InMemoryAccountRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = mock(InMemoryAccountRepository.class);
        accountService = new AccountService(mockRepository);
    }

    @Test
    void depositShouldThrowExceptionWhenAccountIsNull() {
        // Given
        Exception exception = assertThrows(AccountNotFoundException.class, () ->
                accountService.deposit(500.0, null)
        );
        // When
        assertEquals(BankingConstants.ACCOUNT_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void depositShouldIncreaseBalance() {
        // Given
        Account account = new SavingsAccount("555", "Aylan OU", 555.0);

        // When
        accountService.deposit(500.0, account);

        // Then
        assertEquals(1055.0, account.getBalance());
    }

    @Test
    void depositShouldIncreaseBalanceCurrentAccount() {
        // Given
        Account account = new CurrentAccount("555", "Aylan OU", 555.0);

        // When
        accountService.deposit(500.0, account);

        // Then
        assertEquals(1055.0, account.getBalance());
    }

    @Test
    void createAccountShouldCallSaveAccountWhenAccountIsValid() {
        // Given
        Account account = new SavingsAccount("123", "Ahmed", 124.0);

        // When
        accountService.createAccount(account);

        // Then
        verify(mockRepository, times(1)).saveAccount(account);
    }

    // Compte existe pas
    @Test
    void getAccountShouldThrowExceptionWhenIdIsNull() {
        // When
        Exception exception = assertThrows(AccountNotFoundException.class, () ->
                accountService.getAccount(null)
        );

        // Then
        assertEquals("Account not found.", exception.getMessage());
    }
    // Compte valide trouvé.
    @Test
    void getAccount_ShouldReturnAccount_WhenIdIsValid() {
        // Given
        String accountId = "123";
        Account account = new SavingsAccount("100", "Ahmed OU", 287.0);
        when(mockRepository.getAccountById(accountId)).thenReturn(account);

        // When
        Account result = accountService.getAccount(accountId);

        // Then
        assertNotNull(result);
        assertEquals(account, result);
        verify(mockRepository, times(1)).getAccountById(accountId);
    }

    // Cas compte null
    @Test
    void withdrawShouldThrowExceptionWhenAccountIsNull() {
        // When
        Exception exception = assertThrows(AccountNotFoundException.class, () ->
                accountService.withdraw(null, 100.0)
        );

        // Then
        assertEquals("Account not found.", exception.getMessage());
    }

    // Vérifie que la méthode ne modifie pas le solde pour un compte épargne
    @Test
    void withdrawShouldNotModifyBalanceWhenAccountIsSavingsAccount() {
        // Given
        SavingsAccount savingsAccount = new SavingsAccount("123", "Ahmed Ahmed", 200.0);

        // When
        accountService.withdraw(savingsAccount, 500.0);

        // Then
        assertEquals(200.0, savingsAccount.getBalance());
    }

    // Cas montant retrait supérieur au solde dispo.
    @Test
    void withdraw_ShouldThrowException_WhenAmountExceedsBalance() {
        // Given
        Account account = new CurrentAccount("198", "Marc Marc", 2000.0);

        // When
        Exception exception = assertThrows(InsufficientBalanceException.class, () ->
                accountService.withdraw(account, 2500.0)
        );

        // Then
        assertEquals("Insufficient balance.", exception.getMessage());
    }

}
