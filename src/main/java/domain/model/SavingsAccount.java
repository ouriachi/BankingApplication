package domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SavingsAccount extends Account {

    private double interest ;
    private LocalDate lastInterestDate;
    private double monthlyDrawal;
    private LocalDate lastDrawalDate;

    public SavingsAccount(String id, String name, double initialBalance) {
        super(id, name, initialBalance);
        this.monthlyDrawal = 0.0;
        this.lastDrawalDate = LocalDate.now();
    }

    @Override
    public void setBalance(double amount) {
        super.balance = amount;
        this.lastInterestDate = LocalDate.now();
    }
}

