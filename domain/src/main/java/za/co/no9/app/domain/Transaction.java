package za.co.no9.app.domain;

import java.util.Date;

import static za.co.no9.app.util.Validation.validate;

public class Transaction {
    private final Date when;
    private final TransactionRef reference;
    private final TransactionDescription description;
    private final Money amount;

    private Transaction(Date when, TransactionRef reference, TransactionDescription description, Money amount) {
        this.when = validate(when).notNull().get();
        this.reference = validate(reference).notNull().get();
        this.description = validate(description).notNull().get();
        this.amount = validate(amount).notNull().get();
    }

    public static Transaction from(Date when, TransactionRef reference, TransactionDescription description, Money amount) {
        return new Transaction(when, reference, description, amount);
    }
}
