package za.co.no9.app.service;

import za.co.no9.app.domain.*;

import java.util.*;
import java.util.stream.Stream;

public class TestRepository implements Repository {
    private final Set<User> users;
    private final Map<UserName, Set<Account>> accounts;
    private final Map<AccountRef, Transaction[]> accountTransactions;
    private final Map<UserName, AuditItem[]> auditItems;

    private TestRepository(Set<User> users, Map<UserName, Set<Account>> accounts, Map<AccountRef, Transaction[]> accountTransactions, Map<UserName, AuditItem[]> auditItems) {
        this.users = users;
        this.accounts = accounts;
        this.accountTransactions = accountTransactions;
        this.auditItems = auditItems;
    }

    public static TestRepositoryBuilder builder() {
        return new TestRepositoryBuilder();
    }

    @Override
    public Optional<User> findUser(UserName userName) {
        return users.stream().filter(u -> u.name().equals(userName)).findFirst();
    }

    @Override
    public Stream<Account> accounts(UserName userName) {
        final Set<Account> accounts = this.accounts.get(userName);

        if (accounts == null) {
            return new HashSet<Account>().stream();
        } else {
            return accounts.stream();
        }
    }

    @Override
    public Stream<Transaction> transactions(AccountRef accountRef) {
        if (accountTransactions.containsKey(accountRef)) {
            return Stream.of(accountTransactions.get(accountRef));
        } else {
            return Collections.<Transaction>emptyList().stream();
        }
    }

    @Override
    public Optional<Account> findAccount(AccountRef accountRef) {
        for (Set<Account> accounts : this.accounts.values()) {
            final Optional<Account> accountOptional = accounts.stream().filter(x -> x.reference().equals(accountRef)).findFirst();
            if (accountOptional.isPresent()) {
                return accountOptional;
            }
        }
        return Optional.empty();
    }

    @Override
    public Stream<AuditItem> auditItems(UserName userName) {
        if (auditItems.containsKey(userName)) {
            return Stream.of(auditItems.get(userName));
        } else {
            return Collections.<AuditItem>emptyList().stream();
        }
    }

    public static class TestRepositoryBuilder {
        private final Set<User> users = new HashSet<>();
        private final Map<UserName, Set<Account>> userAccounts = new HashMap<>();
        private final Map<AccountRef, Transaction[]> accountTransactions = new HashMap<>();
        private final Map<UserName, AuditItem[]> auditItems = new HashMap<>();

        public TestRepositoryBuilder addUser(User user) {
            users.add(user);
            return this;
        }

        public TestRepositoryBuilder addAccount(UserName name, Account account) {
            Set<Account> userAccounts = this.userAccounts.get(name);
            if (userAccounts == null) {
                userAccounts = new HashSet<>();
                this.userAccounts.put(name, userAccounts);
            }
            userAccounts.add(account);
            return this;
        }

        public TestRepositoryBuilder addTransactions(AccountRef accountRef, Transaction... transactions) {
            this.accountTransactions.put(accountRef, transactions);
            return this;
        }

        public TestRepositoryBuilder addAuditTrail(UserName userName, AuditItem... auditItems) {
            this.auditItems.put(userName, auditItems);
            return this;
        }

        public Repository build() {
            return new TestRepository(users, userAccounts, accountTransactions, auditItems);
        }
    }
}
