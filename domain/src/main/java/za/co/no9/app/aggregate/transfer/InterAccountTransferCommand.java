package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.*;
import za.co.no9.app.event.InterAccountTransferred;

import java.util.Date;

public class InterAccountTransferCommand {
    public final UserName userName;
    public final AccountRef source;
    public final AccountRef destination;
    public final Money paymentAmount;
    public final TransactionDescription description;

    public InterAccountTransferCommand(UserName userName, AccountRef source, AccountRef destination, Money paymentAmount, TransactionDescription description) {
        this.userName = userName;
        this.source = source;
        this.destination = destination;
        this.paymentAmount = paymentAmount;
        this.description = description;
    }

    public InterAccountTransferred makeEvent(Date when, TransactionRef reference) {
        return new InterAccountTransferred(userName, when, source, destination, paymentAmount, reference, description);
    }
}
