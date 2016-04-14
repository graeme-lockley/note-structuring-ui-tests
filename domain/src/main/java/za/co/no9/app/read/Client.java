package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;

import java.util.HashSet;
import java.util.Set;

public class Client {
    private final Set<Account> accounts = new HashSet<>();

    public Account getAccount(AccountRef accountRef) {
        return null;
    }

    public void addAccount(AccountRef reference, Money openingBalance) {
        accounts.add(new Account(reference, openingBalance));
    }
}
