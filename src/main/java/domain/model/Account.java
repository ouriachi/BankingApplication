package domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Account {

    protected String id;
    protected String name;
    public double balance;

    public Account(String id, String name, double initialBalance) {
        this.id = id;
        this.name = name;
        this.balance = initialBalance;
    }
}
