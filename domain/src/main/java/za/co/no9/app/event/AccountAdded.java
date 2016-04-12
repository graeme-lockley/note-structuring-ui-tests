package za.co.no9.app.event;

import za.co.no9.app.domain.AccountName;
import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Money;
import za.co.no9.app.util.Event;

public class AccountAdded implements Event {
    public final ClientID clientID;
    public final AccountRef reference;
    public final Money openingBalance;
    public final AccountName name;

    public AccountAdded(ClientID clientID, AccountRef reference, Money openingBalance, AccountName name) {
        this.clientID = clientID;
        this.reference = reference;
        this.openingBalance = openingBalance;
        this.name = name;
    }
}
