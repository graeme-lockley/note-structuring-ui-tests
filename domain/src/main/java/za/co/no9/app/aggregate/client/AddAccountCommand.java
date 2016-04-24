package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.AccountAdded;

public class AddAccountCommand {
    public final UserName userName;
    public final AccountRef accountRef;
    public final Money openingBalance;

    public AddAccountCommand(UserName userName, AccountRef accountRef, Money openingBalance) {
        this.userName = userName;
        this.accountRef = accountRef;
        this.openingBalance = openingBalance;
    }

    public AccountAdded makeEvent() {
        return new AccountAdded(userName, accountRef, openingBalance);
    }
}
