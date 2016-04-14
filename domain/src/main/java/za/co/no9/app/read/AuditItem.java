package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.TransactionRef;

import java.util.Date;

import static za.co.no9.app.util.Validation.validate;

public class AuditItem {
    private final Date when;
    private final AccountRef from;
    private final AccountRef to;
    private final Money amount;
    private final TransactionRef reference;
    private final TransactionDescription description;

    public AuditItem(
            Date when,
            AccountRef from,
            AccountRef to,
            Money amount,
            TransactionRef reference,
            TransactionDescription description) {
        this.when = validate(when).notNull().get();
        this.from = validate(from).notNull().get();
        this.to = validate(to).notNull().get();
        this.amount = validate(amount).notNull().get();
        this.reference = validate(reference).notNull().get();
        this.description = validate(description).notNull().get();
    }
}
