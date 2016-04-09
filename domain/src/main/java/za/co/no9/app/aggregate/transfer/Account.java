package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;

import static za.co.no9.app.util.Validation.validate;

public class Account {
    private final AccountRef reference;
    private Money balance;

    private Account(AccountRef reference, Money balance) {
        this.reference = validate(reference).notNull().get();
        this.balance = validate(balance).notNull().get();
    }

    public static Account from(AccountRef reference, Money balance) {
        return new Account(reference, balance);
    }

    public AccountRef reference() {
        return reference;
    }

    public boolean hasSufficientFundsToDebit(Money amount) {
        return balance.greaterThan(amount);
    }

    public void debit(Money amount) {
        balance = balance.subtract(amount);
    }

    public void credit(Money amount) {
        balance = balance.add(amount);
    }

    public Money balance() {
        return balance;
    }
}
