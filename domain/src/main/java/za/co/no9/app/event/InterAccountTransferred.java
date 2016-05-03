package za.co.no9.app.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import za.co.no9.app.domain.*;
import za.co.no9.app.util.Event;

import java.util.Date;

public class InterAccountTransferred implements Event {
    public final UserName userName;
    public final Date when;
    public final AccountNumber source;
    public final AccountNumber destination;
    public final Money amount;
    public final TransactionRef reference;
    public final TransactionDescription description;

    public InterAccountTransferred(UserName userName, Date when, AccountNumber source, AccountNumber destination, Money amount, TransactionRef reference, TransactionDescription description) {
        this.userName = userName;
        this.when = when;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.reference = reference;
        this.description = description;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userName", userName)
                .append("date", when)
                .append("source", source)
                .append("destination", destination)
                .append("amount", amount)
                .append("reference", reference)
                .append("description", description)
                .build();
    }
}
