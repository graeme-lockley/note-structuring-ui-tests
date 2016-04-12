package za.co.no9.app.domain;

import java.util.Date;

import static za.co.no9.app.util.Validation.validate;

public class Transaction {
    private final Date when;
    private final TransactionRef reference;
    private final TransactionDescription description;
    private final Money amount;
    private final boolean isDebit;

    private Transaction(Date when, TransactionRef reference, TransactionDescription description, Money amount, boolean isDebit) {
        this.when = validate(when).notNull().get();
        this.reference = validate(reference).notNull().get();
        this.description = validate(description).notNull().get();
        this.amount = validate(amount).notNull().get();
        this.isDebit = isDebit;
    }

    public static Transaction from(Date when, TransactionRef reference, TransactionDescription description, Money amount, boolean isDebit) {
        return new Transaction(when, reference, description, amount, isDebit);
    }
}
