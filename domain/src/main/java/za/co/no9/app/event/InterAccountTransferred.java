package za.co.no9.app.event;

import za.co.no9.app.domain.*;
import za.co.no9.app.util.Event;

import java.util.Date;

public class InterAccountTransferred implements Event {
    public final UserName user;
    public final Date when;
    public final AccountRef source;
    public final AccountRef destination;
    public final Money amount;
    public final TransactionRef reference;
    public final TransactionDescription description;

    public InterAccountTransferred(UserName user, Date when, AccountRef source, AccountRef destination, Money amount, TransactionRef reference, TransactionDescription description) {
        this.user = user;
        this.when = when;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.reference = reference;
        this.description = description;
    }
}
