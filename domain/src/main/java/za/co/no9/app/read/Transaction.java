package za.co.no9.app.read;

import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.TransactionRef;
import za.co.no9.app.util.Validation;

import java.util.Date;

public class Transaction {
    private final Date when;
    private final TransactionRef reference;
    private final TransactionDescription description;
    private final Money amount;
    private final boolean isDebit;

    public Transaction(Date when, TransactionRef reference, TransactionDescription description, Money amount, boolean isDebit) {
        this.when = Validation.value(when).notNull().get();
        this.reference = Validation.value(reference).notNull().get();
        this.description = Validation.value(description).notNull().get();
        this.amount = Validation.value(amount).notNull().get();
        this.isDebit = isDebit;
    }
}
