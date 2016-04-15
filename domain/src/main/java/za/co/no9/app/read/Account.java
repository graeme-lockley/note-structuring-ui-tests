package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.TransactionRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class Account {
    private final AccountRef reference;
    private Money balance;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(AccountRef reference, Money openingBalance) {
        this.reference = reference;
        this.balance = openingBalance;
    }

    public void debit(Date when, TransactionRef reference, TransactionDescription description, Money amount) {
        balance = balance.subtract(amount);
        transactions.add(new Transaction(when, reference, description, amount, true));
    }

    public void credit(Date when, TransactionRef reference, TransactionDescription description, Money amount) {
        balance = balance.add(amount);
        transactions.add(new Transaction(when, reference, description, amount, false));
    }

    public AccountRef reference() {
        return reference;
    }

    public Money balance() {
        return balance;
    }

    public Stream<Transaction> transactions() {
        return transactions.stream();
    }
}
