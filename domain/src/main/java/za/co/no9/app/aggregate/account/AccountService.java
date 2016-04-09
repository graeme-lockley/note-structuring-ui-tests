package za.co.no9.app.aggregate.account;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Transaction;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.UserAdded;
import za.co.no9.app.util.Either;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class AccountService {
    private Map<UserName, Set<Account>> userAccounts = new HashMap<>();
    private Map<AccountRef, Account> accounts = new HashMap<>();

    private void apply(UserAdded event) {
        userAccounts.put(event.name, new HashSet<>());
    }

    private void apply(AccountAdded event) {
        final Account account = Account.from(event.reference, event.openingBalance, event.name);

        userAccounts.get(event.userName).add(account);
        accounts.put(account.reference(), account);
    }

    public Either<AccountServiceFailure, Stream<Account>> accounts(UserName user) {
        final Set<Account> accounts = this.userAccounts.get(user);

        if (accounts == null) {
            return Either.left(AccountServiceFailure.UNKNOWN_USER);
        } else {
            return Either.right(accounts.stream());
        }
    }

    public Either<AccountServiceFailure, Stream<Transaction>> accountTransactions(AccountRef reference) {
        final Account account = accounts.get(reference);
        if (account == null) {
            return Either.left(AccountServiceFailure.UNKNOWN_ACCOUNT);
        } else {
            return Either.right(account.transactions());
        }
    }

    public enum AccountServiceFailure {
        UNKNOWN_ACCOUNT, UNKNOWN_USER
    }
}