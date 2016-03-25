package za.co.no9.app.service;

import za.co.no9.app.domain.Account;
import za.co.no9.app.domain.User;
import za.co.no9.app.domain.UserName;

import java.util.*;
import java.util.stream.Stream;

public class TestRepository implements Repository {
    private final Set<User> users;
    private final Map<UserName, Set<Account>> accounts;

    private TestRepository(Set<User> users, Map<UserName, Set<Account>> accounts) {
        this.users = users;
        this.accounts = accounts;
    }

    public static TestRepositoryBuilder builder() {
        return new TestRepositoryBuilder();
    }

    @Override
    public Optional<User> findUser(UserName username) {
        return users.stream().filter(u -> u.name().equals(username)).findFirst();
    }

    @Override
    public Stream<Account> accounts(UserName name) {
        final Set<Account> accounts = this.accounts.get(name);

        if (accounts == null) {
            return new HashSet<Account>().stream();
        } else {
            return accounts.stream();
        }
    }

    public static class TestRepositoryBuilder {
        private final Set<User> users = new HashSet<>();
        private final Map<UserName, Set<Account>> accounts = new HashMap<>();

        public TestRepositoryBuilder addUser(User user) {
            users.add(user);
            return this;
        }

        public TestRepositoryBuilder addAccount(UserName name, Account account) {
            Set<Account> userAccounts = accounts.get(name);
            if (userAccounts == null) {
                userAccounts = new HashSet<>();
                accounts.put(name, userAccounts);
            }
            userAccounts.add(account);
            return this;
        }

        public Repository build() {
            return new TestRepository(users, accounts);
        }
    }
}
