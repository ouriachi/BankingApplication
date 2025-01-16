package domain.service;

import domain.exception.AccountNotFoundException;
import domain.exception.InsufficientBalanceException;
import domain.model.Account;
import domain.model.SavingsAccount;
import infrastructure.persistence.InMemoryAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.BankingConstants;

@Service
public class AccountService {


    private final InMemoryAccountRepository inMemoryAccountRepository;

    // Injecter le repo via le constructeur
    @Autowired
    public AccountService(InMemoryAccountRepository inMemoryAccountRepository) {
        this.inMemoryAccountRepository = inMemoryAccountRepository;
    }


    public void createAccount(Account account) {
        inMemoryAccountRepository.saveAccount(account);
    }

    public Account getAccount(String id) {

        if (id == null) {
            throw new AccountNotFoundException("Account not found.");
        }
        return inMemoryAccountRepository.getAccountById(id);
    }
   // Dépot
    public void deposit(double amount, Account account) {
        if (account == null) {
            throw new AccountNotFoundException(BankingConstants.ACCOUNT_NOT_FOUND_MESSAGE);
        }
        account.setBalance(account.getBalance() + amount);
    }

    // Retrait pour un compte épargne,  sinon sortir de la méthode.
    public void withdraw(Account account, double amount) {
        if (account == null) {
            throw new AccountNotFoundException(BankingConstants.ACCOUNT_NOT_FOUND_MESSAGE);
        }

        if (account instanceof SavingsAccount) {
            return;
        }

        if (amount > account.getBalance()) {
            throw new InsufficientBalanceException(BankingConstants.INSUFFICIENT_BALANCE_MESSAGE);
        }

        account.setBalance(account.getBalance() - amount);
    }



}
