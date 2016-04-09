package za.co.no9.app.event;

import za.co.no9.app.domain.AccountName;
import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.util.Event;

public class AccountAdded implements Event {
    public final UserName userName;
    public final AccountRef reference;
    public final Money openingBalance;
    public final AccountName name;

    public AccountAdded(UserName userName, AccountRef reference, Money openingBalance, AccountName name) {
        this.userName = userName;
        this.reference = reference;
        this.openingBalance = openingBalance;
        this.name = name;
    }
}
