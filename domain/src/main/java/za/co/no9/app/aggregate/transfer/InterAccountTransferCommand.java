package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.*;
import za.co.no9.app.event.InterAccountTransferred;

import java.util.Date;

public class InterAccountTransferCommand {
    public final UserID userID;
    public final AccountRef source;
    public final AccountRef destination;
    public final Money paymentAmount;
    public final TransactionDescription description;

    public InterAccountTransferCommand(UserID userID, AccountRef source, AccountRef destination, Money paymentAmount, TransactionDescription description) {
        this.userID = userID;
        this.source = source;
        this.destination = destination;
        this.paymentAmount = paymentAmount;
        this.description = description;
    }

    public InterAccountTransferred makeEvent(Date when, TransactionRef reference) {
        return new InterAccountTransferred(userID, when, source, destination, paymentAmount, reference, description);
    }
}
