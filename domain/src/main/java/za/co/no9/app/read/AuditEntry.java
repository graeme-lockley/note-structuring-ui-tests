package za.co.no9.app.read;

import za.co.no9.app.domain.AccountNumber;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.TransactionRef;
import za.co.no9.app.util.Validation;

import java.util.Date;

public class AuditEntry {
    public final Date when;
    public final AccountNumber sourceAccount;
    public final AccountNumber destinationAccount;
    public final Money amount;
    public final TransactionRef reference;
    public final TransactionDescription description;

    AuditEntry(
            Date when,
            AccountNumber sourceAccount,
            AccountNumber destinationAccount,
            Money amount,
            TransactionRef reference,
            TransactionDescription description) {
        this.when = Validation.value(when).notNull().get();
        this.sourceAccount = Validation.value(sourceAccount).notNull().get();
        this.destinationAccount = Validation.value(destinationAccount).notNull().get();
        this.amount = Validation.value(amount).notNull().get();
        this.reference = Validation.value(reference).notNull().get();
        this.description = Validation.value(description).notNull().get();
    }
}
