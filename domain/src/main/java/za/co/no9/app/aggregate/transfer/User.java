package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.AccountRef;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class User {
    private Set<Account> accounts = new HashSet<>();

    public Account getAccount(AccountRef accountRef) {
        return findAccount(accountRef).get();
    }

    public Optional<Account> findAccount(AccountRef accountRef) {
        return accounts.stream().filter(a -> a.reference().equals(accountRef)).findFirst();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}
