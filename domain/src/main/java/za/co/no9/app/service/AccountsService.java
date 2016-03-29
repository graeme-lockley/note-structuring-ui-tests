package za.co.no9.app.service;

import za.co.no9.app.domain.Account;
import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.User;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.util.Optional;
import java.util.stream.Stream;

public class AccountsService {
    public Either<AccountsServiceFailures, Stream<Account>> view(UserName userName) {
        final Optional<User> user = DI.get(Repository.class).findUser(userName);

        if (user.isPresent()) {
            return Either.right(user.get().accounts());
        } else {
            return Either.left(AccountsServiceFailures.UNKNOWN_USER);
        }
    }

    public Either<AccountsServiceFailures, Account> find(AccountRef accountRef) {
        final Optional<Account> account = DI.get(Repository.class).findAccount(accountRef);

        if (account.isPresent()) {
            return Either.right(account.get());
        } else {
            return Either.left(AccountsServiceFailures.UNKNOWN_ACCOUNT);
        }
    }

    public enum AccountsServiceFailures {
        UNKNOWN_ACCOUNT, UNKNOWN_USER
    }
}
