package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.AccountName;
import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Money;
import za.co.no9.app.event.AccountAdded;

public class AddAccountCommand {
    public final ClientID clientID;
    public final AccountRef accountRef;
    public final Money openingBalance;
    public final AccountName name;

    public AddAccountCommand(ClientID clientID, AccountRef accountRef, Money openingBalance, AccountName name) {
        this.clientID = clientID;
        this.accountRef = accountRef;
        this.openingBalance = openingBalance;
        this.name = name;
    }

    public AccountAdded makeEvent() {
        return new AccountAdded(clientID, accountRef, openingBalance, name);
    }
}
