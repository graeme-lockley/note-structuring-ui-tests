package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.TransactionDescription;
import za.co.no9.app.domain.UserName;

public class InterAccountTransferCommand {
    public final UserName user;
    public final AccountRef source;
    public final AccountRef destination;
    public final Money paymentAmount;
    public final TransactionDescription description;

    public InterAccountTransferCommand(UserName user, AccountRef source, AccountRef destination, Money paymentAmount, TransactionDescription description) {
        this.user = user;
        this.source = source;
        this.destination = destination;
        this.paymentAmount = paymentAmount;
        this.description = description;
    }
}
