package za.co.no9.app.read;

import za.co.no9.app.domain.AccountNumber;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.TransactionRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

class Account {
    private final AccountNumber reference;
    private Money balance;
    private final List<Transaction> transactions = new ArrayList<>();

    Account(AccountNumber reference, Money openingBalance) {
        this.reference = reference;
        this.balance = openingBalance;
    }

    void debit(Date when, TransactionRef reference, TransactionDescription description, Money amount) {
        balance = balance.subtract(amount);
        transactions.add(new Transaction(when, reference, description, amount, true));
    }

    void credit(Date when, TransactionRef reference, TransactionDescription description, Money amount) {
        balance = balance.add(amount);
        transactions.add(new Transaction(when, reference, description, amount, false));
    }

    AccountNumber reference() {
        return reference;
    }

    Money balance() {
        return balance;
    }

    Stream<Transaction> transactions() {
        return transactions.stream();
    }
}
