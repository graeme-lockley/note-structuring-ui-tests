package za.co.no9.app.aggregate.account;

import za.co.no9.app.domain.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Account {
    private final List<Transaction> transactions = new ArrayList<>();

    public Account() {
    }

    public Stream<Transaction> transactions() {
        return transactions.stream();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
