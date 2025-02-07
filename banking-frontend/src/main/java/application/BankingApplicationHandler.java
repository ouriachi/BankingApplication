package application;

import domain.exception.BankingErrorHandler;
import domain.exception.InsufficientBalanceException;
import domain.model.Account;
import domain.model.AccountType;
import domain.model.CurrentAccount;
import domain.model.SavingsAccount;
import domain.service.AccountService;
import domain.service.SavingsAccountService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import utils.BankingConstants;

import java.util.Scanner;

@Getter
@Setter
@Service
public class BankingApplicationHandler {
    private final AccountService accountService;
    private final SavingsAccountService savingsAccountService;
    private  Scanner scanner;

    public BankingApplicationHandler(AccountService accountService, SavingsAccountService savingsAccountService) {
        this.accountService = accountService;
        this.savingsAccountService = savingsAccountService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean quit = false;

        while (!quit) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1 -> createAccount();
                    case 2 -> deposit();
                    case 3 -> withdraw();
                    case 4 -> displayBalance();
                    case 5 -> calculateInterest();
                    case 6 -> {
                              System.out.println("Goodbye!");
                              quit = true;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                BankingErrorHandler.handle(e);
            }
        }
    }

    private void printMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Display balance");
        System.out.println("5. Calculate interest for a savings account");
        System.out.println("6. Quit");
        System.out.print("Enter your choice: ");
    }

    public void createAccount() {

        System.out.print("Enter your name: ");
        String ownerName = scanner.nextLine();
        System.out.print("Enter the initial balance: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid numeric value for the balance.");
            scanner.next();
        }

        double initialBalance = scanner.nextDouble();

        String idAccount = String.valueOf(System.currentTimeMillis() % 1000000);
        System.out.print("Choose the account type (1 for checking, 2 for savings): ");
        int accountType = scanner.nextInt();
        scanner.nextLine();

        if (AccountType.CURRENT_ACCOUNT.getCode() == accountType ) {
            accountService.createAccount(new CurrentAccount(idAccount, ownerName, initialBalance));
            System.out.println("Checking account created successfully. Account identifier: ".concat(idAccount));

        } else if (AccountType.SAVINGS_ACCOUNT.getCode() == accountType) {
            accountService.createAccount(new SavingsAccount(idAccount, ownerName, initialBalance));
            System.out.println("Savings account created successfully. Account identifier: ".concat(idAccount));
        }

    }

    public void deposit() {
        System.out.print(BankingConstants.ACCOUNT_IDENTIFIER_MESSAGE);
        String id = scanner.nextLine();
        System.out.print("Enter the amount to deposit: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid numeric value for deposit.");
            scanner.next();
        }
        double amount = scanner.nextDouble();
        Account account = accountService.getAccount(id);
        accountService.deposit(amount, account);
        System.out.println(String.valueOf(amount).concat(" euros have been deposited into your account."));
    }
    // Effectuer un retrait si le montant du retrait est supérieur au solde.
    public void withdraw() {
        System.out.print(BankingConstants.ACCOUNT_IDENTIFIER_MESSAGE);
        String id = scanner.nextLine();
        System.out.print("Enter amount to withdraw: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid numeric value for withdraw.");
            scanner.next();
        }
        double amount = scanner.nextDouble();
        Account account = accountService.getAccount(id);

        if (isSavingsAccount(account)) {
            savingsAccountService.withdraw((SavingsAccount) account, amount);
        } else if (isCurrentAccount(account)){
            accountService.withdraw(account, amount);
        }
        System.out.println(String.valueOf(amount).concat(" euros have been withdrawn from your account."));
    }
    // Consulter et afficher le solde.
    public void displayBalance() {
        System.out.print(BankingConstants.ACCOUNT_IDENTIFIER_MESSAGE);
        String id = scanner.nextLine();

        Account account = accountService.getAccount(id);
        System.out.println("Your balance is ".concat(String.valueOf(account.getBalance())).concat(" euros"));
    }

   //Calculer les intérêts uniquement pour les comptes courants. Sinon, retourner un message d'erreur.
    public void calculateInterest() {
        System.out.print(BankingConstants.ACCOUNT_IDENTIFIER_MESSAGE);
        String id = scanner.nextLine();
        Account account = accountService.getAccount(id);
        if (isSavingsAccount(account)) {
            savingsAccountService.calculateInterest((SavingsAccount) account);
        } else {
            throw new InsufficientBalanceException(BankingConstants.INTEREST_CALCULATION_ERROR_MESSAGE);
        }
    }

    public boolean isCurrentAccount(Account account){
        if (account == null ) {
            return false;
        }
        return account instanceof CurrentAccount savingsAccount;
    }

    public boolean isSavingsAccount(Account account){
        if (account == null ) {
            return false;
        }
        return  account instanceof SavingsAccount savingsAccount;
    }

}
