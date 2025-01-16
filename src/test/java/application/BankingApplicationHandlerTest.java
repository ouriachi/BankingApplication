package application;
import domain.exception.InsufficientBalanceException;
import domain.model.Account;
import domain.model.CurrentAccount;
import domain.model.SavingsAccount;
import domain.service.AccountService;
import domain.service.SavingsAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankingApplicationHandlerTest {
    @Mock
    AccountService accountService;
    @Mock
    SavingsAccountService savingsAccountService;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        savingsAccountService = mock(SavingsAccountService.class);

    }

    @Test
    void testCreateCurrentAccount() {
        // Given
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine())
                .thenReturn("Ahmed CheckingAccount")
                .thenReturn("");

        when(mockScanner.hasNextDouble())
                .thenReturn(false)
                .thenReturn(true);
        when(mockScanner.next())
                .thenReturn("invalid")
                .thenReturn("");

        when(mockScanner.nextDouble())
                .thenReturn(88000.0);

        when(mockScanner.nextInt())
                .thenReturn(1);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        String simulatedInput = """
                                    1
                                    Ahmed CheckingAccount
                                    88000
                                    1
                                    6
                                    """;
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        // When
        handler.createAccount();
        // Then
        ArgumentCaptor<CurrentAccount> captor = ArgumentCaptor.forClass(CurrentAccount.class);
        verify(accountService, times(1)).createAccount(captor.capture());
        CurrentAccount createdAccount = captor.getValue();
        assertNotNull(createdAccount);
        assertEquals(88000, createdAccount.getBalance());
        verify(accountService, times(1)).createAccount(any(CurrentAccount.class));
    }


    @Test
    void testCreateSavingsAccount() {
        // Given
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine())
                .thenReturn("Ahmed OU")
                .thenReturn("");
        when(mockScanner.hasNextDouble())
                .thenReturn(false)
                .thenReturn(true);
        when(mockScanner.nextDouble()).thenReturn(5000.0);
        when(mockScanner.nextInt()).thenReturn(2);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        String simulatedInput = """
                                    1
                                    Ahmed OU
                                    5000
                                    2
                                    6
                                    """;
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        // When
        handler.createAccount();
        // Then
        ArgumentCaptor<SavingsAccount> captor = ArgumentCaptor.forClass(SavingsAccount.class);
        verify(accountService, times(1)).createAccount(captor.capture());
        SavingsAccount createdAccount = captor.getValue();
        assertNotNull(createdAccount);
        assertEquals(5000, createdAccount.getBalance());
        verify(accountService, times(1)).createAccount(any(SavingsAccount.class));
    }


    @Test
    void testDeposit() {
        // Given
        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine())
                .thenReturn("123456");
        when(mockScanner.hasNextDouble())
                .thenReturn(false)
                .thenReturn(true);
        when(mockScanner.nextDouble()).thenReturn(1000.0);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        CurrentAccount mockAccount = new CurrentAccount("123456", "Alice Smith", 5000.0);
        when(accountService.getAccount("123456")).thenReturn(mockAccount);
        // When
        handler.deposit();
        // Then
        verify(accountService, times(1)).deposit(1000.0, mockAccount);
    }

    @Test
    void testIsSavingsAccountwithNull() {
        // Given
        Scanner mockScanner = mock(Scanner.class);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        // When
        boolean result = handler.isSavingsAccount(null);
        // Then
        assertFalse(result, "The account should not be identified as a SavingsAccount if null.");
    }

    @Test
    void testIsCheckingAccount_withNull() {
        // Given

        Scanner mockScanner = mock(Scanner.class);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        // When
        boolean result = handler.isCurrentAccount(null);
        // Then
        assertFalse(result, "The account should not be identified as a CheckingAccount if null.");
    }


    @Test
    void testIsSavingsAccount() {
        // Given
        Account account = new CurrentAccount("123456", "Alice Smith", 5000.0);
        Scanner mockScanner = mock(Scanner.class);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        // When
        boolean isSavingAccount = handler.isSavingsAccount(account);
        // Then
        assertFalse(isSavingAccount);
    }

    @Test
    void testIsCurrentAccount() {
        // Given
        Account account = new SavingsAccount("123487", "Paul Martin ", 1500);
        Scanner mockScanner = mock(Scanner.class);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        // When
        boolean isCheckingAccount = handler.isCurrentAccount(account);

        // Then
        assertFalse(isCheckingAccount);
    }


    @Test
    void testWithdrawForSavingsAccount() {
        // Given
        String accountId = "111111";
        double amount = 100.0;
        SavingsAccount account = new SavingsAccount("111111", "Fabrice Martin ", 1500);
        Scanner mockScanner = mock(Scanner.class);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        when(mockScanner.hasNextDouble())
                .thenReturn(false)
                .thenReturn(true);
        when(mockScanner.nextLine()).thenReturn(accountId);
        when(mockScanner.nextDouble()).thenReturn(amount);
        when(accountService.getAccount(accountId)).thenReturn(account);
        // When
        handler.withdraw();

        // Then
        verify(savingsAccountService).withdraw(account, amount);
        verify(accountService, never()).withdraw(any(), anyDouble());
    }

    @Test
    void testWithdrawForCurrentAccount() {
        // Given
        String accountId = "252525";
        double amount = 1900;
        CurrentAccount account = new CurrentAccount("252525", "Thomas Martin ", 1900);
        Scanner mockScanner = mock(Scanner.class);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        when(mockScanner.hasNextDouble())
                .thenReturn(false)
                .thenReturn(true);
        when(mockScanner.nextLine()).thenReturn(accountId);
        when(mockScanner.nextDouble()).thenReturn(amount);
        when(accountService.getAccount(accountId)).thenReturn(account);

        // When
        handler.withdraw();

        // Then
        verify(accountService).withdraw(account, amount);
        verify(savingsAccountService, never()).withdraw(any(SavingsAccount.class), anyDouble());
    }
    // Test Calculer les intérêts pour un compte épargne.
    @Test
    void testCalculateInterestSavingsAccount() {
        // Given
        String accountId = "12345";
        SavingsAccount mockSavingsAccount = mock(SavingsAccount.class);
        Scanner mockScanner = mock(Scanner.class);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);

        when(mockScanner.nextLine()).thenReturn(accountId);
        when(accountService.getAccount(accountId)).thenReturn(mockSavingsAccount);

        // When
        handler.calculateInterest();

        // Then
        verify(savingsAccountService).calculateInterest(mockSavingsAccount);
    }
    // test Calculer les intérêts pour un compte courrant.
    @Test
    void testCalculateInterestCurrentAccount() {
        // Given
        String accountId = "54321";
        CurrentAccount mockAccount = mock(CurrentAccount.class);
        Scanner mockScanner = mock(Scanner.class);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);

        when(mockScanner.nextLine()).thenReturn(accountId);
        when(accountService.getAccount(accountId)).thenReturn(mockAccount);

        // When
        Exception exception = assertThrows(InsufficientBalanceException.class, handler::calculateInterest);
        // Then
        assertEquals("Interest calculation is only available for savings accounts.", exception.getMessage());
    }

   // Tester l'appel au service de récupération du solde.
    @Test
    void testDisplayBalanceValidAccount() {
        // Given
        String accountId = "353553";
        Account mockAccount = mock(Account.class);
        Scanner mockScanner = mock(Scanner.class);
        BankingApplicationHandler handler = new BankingApplicationHandler(accountService, savingsAccountService);
        handler.setScanner(mockScanner);
        when(mockScanner.nextLine()).thenReturn(accountId);
        when(accountService.getAccount(accountId)).thenReturn(mockAccount);
        when(mockAccount.getBalance()).thenReturn(2500.0);

        // When
        handler.displayBalance();

        // Then
        verify(accountService).getAccount(accountId);
        verify(mockAccount).getBalance();
    }

}
