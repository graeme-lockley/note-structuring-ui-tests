package za.co.no9.app.service;

import za.co.no9.app.domain.Account;
import za.co.no9.app.domain.User;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.util.Optional;
import java.util.stream.Stream;

public class AccountsService {
    public Either<AccountsServiceFailures, Stream<Account>> view(UserName name) {
        final Optional<User> user = DI.get(Repository.class).findUser(name);

        if (user.isPresent()) {
            return Either.right(user.get().accounts());
        } else {
            return Either.left(AccountsServiceFailures.UNKNOWN_USER);
        }
    }

    public enum AccountsServiceFailures {
        UNKNOWN_USER
    }
}
