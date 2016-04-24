package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.AccountNumber;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.AccountAdded;

public class AddAccountCommand {
    final UserName userName;
    final AccountNumber accountNumber;
    private final Money openingBalance;

    public AddAccountCommand(UserName userName, AccountNumber accountNumber, Money openingBalance) {
        this.userName = userName;
        this.accountNumber = accountNumber;
        this.openingBalance = openingBalance;
    }

    AccountAdded makeEvent() {
        return new AccountAdded(userName, accountNumber, openingBalance);
    }
}
