package za.co.no9.app.aggregate.account;

import za.co.no9.app.domain.AccountName;
import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static za.co.no9.app.util.Validation.validate;

public class Account {
    private final AccountRef reference;
    private final Money balance;
    private final AccountName name;
    private final List<Transaction> transactions = new ArrayList<>();

    private Account(AccountRef reference, Money balance, AccountName name) {
        this.reference = validate(reference).notNull().get();
        this.balance = validate(balance).notNull().get();
        this.name = validate(name).notNull().get();
    }

    public static Account from(AccountRef reference, Money balance, AccountName name) {
        return new Account(reference, balance, name);
    }

    public Stream<Transaction> transactions() {
        return transactions.stream();
    }

    public AccountRef reference() {
        return reference;
    }

    public Money balance() {
        return balance;
    }

    public boolean hasSufficientFundsToDebit(Money amount) {
        return balance.greaterThan(amount);
    }
}
