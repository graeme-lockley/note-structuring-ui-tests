package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.AccountName;
import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.AccountAdded;

public class AddAccountCommand {
    public final UserName userName;
    public final AccountRef accountRef;
    public final Money openingBalance;
    public final AccountName name;

    public AddAccountCommand(UserName userName, AccountRef accountRef, Money openingBalance, AccountName name) {
        this.userName = userName;
        this.accountRef = accountRef;
        this.openingBalance = openingBalance;
        this.name = name;
    }

    public AccountAdded makeEvent() {
        return new AccountAdded(userName, accountRef, openingBalance, name);
    }
}
