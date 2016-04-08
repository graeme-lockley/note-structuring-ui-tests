package za.co.no9.app.aggregate.user;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.TransactionRef;

import java.util.Date;

import static za.co.no9.app.util.Validation.validate;

public class AuditItem {
    private final Date when;
    private final TransactionRef reference;
    private final TransactionDescription description;
    private final Money amount;
    private final AccountRef from;
    private final AccountRef to;

    private AuditItem(Date when, TransactionRef reference, TransactionDescription description, Money amount, AccountRef from, AccountRef to) {
        this.when = validate(when).notNull().get();
        this.reference = validate(reference).notNull().get();
        this.description = validate(description).notNull().get();
        this.amount = validate(amount).notNull().get();
        this.from = validate(from).notNull().get();
        this.to = validate(to).notNull().get();
    }

    public static AuditItem from(Date when, TransactionRef reference, TransactionDescription description, Money amount, AccountRef from, AccountRef to) {
        return new AuditItem(when, reference, description, amount, from, to);
    }
}
