package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.TransactionRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Account {
    private final AccountRef reference;
    private final Money openingBalance;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(AccountRef reference, Money openingBalance) {
        this.reference = reference;
        this.openingBalance = openingBalance;
    }

    public void debit(Date when, TransactionRef reference, TransactionDescription description, Money amount) {
        transactions.add(new Transaction(when, reference, description, amount, true));
    }

    public void credit(Date when, TransactionRef reference, TransactionDescription description, Money amount) {
        transactions.add(new Transaction(when, reference, description, amount, false));
    }
}
