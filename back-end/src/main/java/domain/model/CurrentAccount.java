package domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentAccount extends Account {

    public CurrentAccount(String id, String name, double initialBalance) {
        super(id, name, initialBalance);
    }

    @Override
    public void setBalance(double amount) {
        super.balance = amount;
    }

}
