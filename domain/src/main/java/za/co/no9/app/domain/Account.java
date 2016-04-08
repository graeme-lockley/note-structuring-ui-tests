package za.co.no9.app.domain;

import za.co.no9.app.service.Repository;
import za.co.no9.app.util.DI;

import java.util.stream.Stream;

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

    public Stream<Transaction> transactions() {
        return DI.get(Repository.class).transactions(reference);
    }

    public AccountRef reference() {
        return reference;
    }

    public Money balance() {
        return balance;
    }
}
