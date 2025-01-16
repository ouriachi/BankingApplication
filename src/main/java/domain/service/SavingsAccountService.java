package domain.service;

import domain.exception.AccountNotFoundException;
import domain.exception.InsufficientBalanceException;
import domain.exception.InvalidOperationException;
import domain.model.SavingsAccount;
import utils.BankingConstants;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SavingsAccountService {



    public void withdraw(SavingsAccount account, double amount) {

        if (account == null) {
            throw new AccountNotFoundException(BankingConstants.ACCOUNT_NOT_FOUND_MESSAGE);
        }
        // Vérifiez si un nouveau mois a commencé
        LocalDate currentDate = LocalDate.now();
        // Calculer l'écart entre les deux dates en mois.
        long monthsSinceLastDrawal = ChronoUnit.MONTHS.between(account.getLastDrawalDate(), currentDate);

        if (account.getMonthlyDrawal() + amount > BankingConstants.WITHDRAWAL_MAX) {
            throw new InvalidOperationException(BankingConstants.WITHDRAWAL_MAX_MESSAGE);
        }
        if (amount > account.getBalance()) {
            throw new InsufficientBalanceException(BankingConstants.INSUFFICIENT_BALANCE_MESSAGE);
        }

        // Dans un contexte de production, configurer un batch qui réinitialise la somme
        // setMonthlyDrawal à zéro sans attendre une opération.
        if (monthsSinceLastDrawal >= 1) {
            account.setMonthlyDrawal(0.0);
            account.setLastDrawalDate(currentDate);
        }
        account.setBalance(account.getBalance() - amount);
    }

    public void calculateInterest(SavingsAccount account) {

        if (account == null) {
            throw new AccountNotFoundException(BankingConstants.ACCOUNT_NOT_FOUND_MESSAGE);
        }

        // Vérifiez si un mois s'est écoulé depuis le dernier calcul des intérêts.
        // Selon le besoin, on peut mettre le calcul des intérêts à la fin de chaque mois.
        // Ceci est une autre possibilité.
        LocalDate lastInterestDate = account.getLastInterestDate();
        LocalDate currentDate = LocalDate.now();

        long monthsSinceLastInterest = lastInterestDate != null ? ChronoUnit.MONTHS.between(lastInterestDate, currentDate): 0;

        if (monthsSinceLastInterest < 1 && lastInterestDate != null ) {
            throw new InvalidOperationException(BankingConstants.Interest_MESSAGE_ERROR
                    .concat(lastInterestDate.plusMonths(1).toString()));
        }

        // Calcul des intérêts.
        double interest = account.getBalance() * BankingConstants.INTEREST_RATE;
        account.setBalance(account.getBalance() + interest);

        // Mise à jour de la dernière date de clacul d'intérêts.
        account.setLastInterestDate(currentDate);
        System.out.println("Interest for this month is ".concat(String.format("%.2f", interest)).concat("euros"));
        System.out.println("New balance: " + String.format("%.2f", account.getBalance()) + " euros");
    }
}
