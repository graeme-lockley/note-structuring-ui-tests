package za.co.no9.app.service;

import za.co.no9.app.aggregate.user.AuditItem;
import za.co.no9.app.aggregate.user.User;
import za.co.no9.app.domain.Account;
import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Transaction;
import za.co.no9.app.domain.UserName;

import java.util.Optional;
import java.util.stream.Stream;

public interface Repository {
    Optional<User> findUser(UserName userName);

    Stream<Account> accounts(UserName userName);

    Stream<Transaction> transactions(AccountRef accountRef);

    Optional<Account> findAccount(AccountRef accountRef);

    Stream<AuditItem> auditItems(UserName userName);
}
