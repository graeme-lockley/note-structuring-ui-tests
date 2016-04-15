package za.co.no9.app.read;

import org.apache.commons.lang3.builder.ToStringBuilder;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.TransactionRef;
import za.co.no9.app.util.Validation;

import java.util.Date;

public class Transaction {
    public final Date when;
    public final TransactionRef reference;
    public final TransactionDescription description;
    public final Money amount;
    public final boolean isDebit;

    public Transaction(Date when, TransactionRef reference, TransactionDescription description, Money amount, boolean isDebit) {
        this.when = Validation.value(when).notNull().get();
        this.reference = Validation.value(reference).notNull().get();
        this.description = Validation.value(description).notNull().get();
        this.amount = Validation.value(amount).notNull().get();
        this.isDebit = isDebit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("when", when)
                .append("reference", reference)
                .append("description", description)
                .append("amount", amount)
                .append("isDebit", isDebit)
                .build();
    }
}
