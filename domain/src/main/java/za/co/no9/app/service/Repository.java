package za.co.no9.app.service;

import za.co.no9.app.domain.Account;
import za.co.no9.app.domain.User;
import za.co.no9.app.domain.UserName;

import java.util.Optional;
import java.util.stream.Stream;

public interface Repository {
    Optional<User> findUser(UserName username);

    Stream<Account> accounts(UserName name);
}
