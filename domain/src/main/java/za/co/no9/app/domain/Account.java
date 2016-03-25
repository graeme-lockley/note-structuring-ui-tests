package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public class Account {
    private final AccountRef reference;
    private final Money balance;
    private final AccountName name;

    private Account(AccountRef reference, Money balance, AccountName name) {
        this.reference = validate(reference).notNull().get();
        this.balance = validate(balance).notNull().get();
        this.name = validate(name).notNull().get();
    }

    public static Account from(AccountRef reference, Money balance, AccountName name) {
        return new Account(reference, balance, name);
    }
}
