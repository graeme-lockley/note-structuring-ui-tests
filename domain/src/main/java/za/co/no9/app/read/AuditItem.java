package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.TransactionRef;
import za.co.no9.app.util.Validation;

import java.util.Date;

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
        this.when = Validation.value(when).notNull().get();
        this.from = Validation.value(from).notNull().get();
        this.to = Validation.value(to).notNull().get();
        this.amount = Validation.value(amount).notNull().get();
        this.reference = Validation.value(reference).notNull().get();
        this.description = Validation.value(description).notNull().get();
    }
}
