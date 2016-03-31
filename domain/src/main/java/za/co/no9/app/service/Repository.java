package za.co.no9.app.service;

import za.co.no9.app.domain.*;

import java.util.Optional;
import java.util.stream.Stream;

public interface Repository {
    Optional<User> findUser(UserName userName);

    Stream<Account> accounts(UserName userName);

    Stream<Transaction> transactions(AccountRef accountRef);

    Optional<Account> findAccount(AccountRef accountRef);

    Stream<AuditItem> auditItems(UserName userName);
}
