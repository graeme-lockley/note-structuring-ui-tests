package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.AccountNumber;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class Client {
    private Set<Account> accounts = new HashSet<>();

    Account getAccount(AccountNumber accountNumber) {
        return findAccount(accountNumber).get();
    }

    Optional<Account> findAccount(AccountNumber accountNumber) {
        return accounts.stream().filter(a -> a.reference().equals(accountNumber)).findFirst();
    }

    void addAccount(Account account) {
        accounts.add(account);
    }
}
