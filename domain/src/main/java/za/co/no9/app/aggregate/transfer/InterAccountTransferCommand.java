package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.*;
import za.co.no9.app.event.InterAccountTransferred;

import java.util.Date;

public class InterAccountTransferCommand {
    final UserName userName;
    final AccountNumber source;
    final AccountNumber destination;
    final Money paymentAmount;
    private final TransactionDescription description;

    public InterAccountTransferCommand(UserName userName, AccountNumber source, AccountNumber destination, Money paymentAmount, TransactionDescription description) {
        this.userName = userName;
        this.source = source;
        this.destination = destination;
        this.paymentAmount = paymentAmount;
        this.description = description;
    }

    InterAccountTransferred makeEvent(Date when, TransactionRef reference) {
        return new InterAccountTransferred(userName, when, source, destination, paymentAmount, reference, description);
    }
}
