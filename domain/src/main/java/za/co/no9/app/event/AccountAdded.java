package za.co.no9.app.event;

import za.co.no9.app.domain.AccountNumber;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.util.Event;

public class AccountAdded implements Event {
    public final UserName userName;
    public final AccountNumber reference;
    public final Money openingBalance;

    public AccountAdded(UserName userName, AccountNumber reference, Money openingBalance) {
        this.userName = userName;
        this.reference = reference;
        this.openingBalance = openingBalance;
    }
}
