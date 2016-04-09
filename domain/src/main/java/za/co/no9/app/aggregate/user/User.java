package za.co.no9.app.aggregate.user;

import za.co.no9.app.aggregate.account.Account;
import za.co.no9.app.domain.UserCredential;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.domain.UserPassword;
import za.co.no9.app.service.Repository;
import za.co.no9.app.util.DI;

import java.util.stream.Stream;

import static za.co.no9.app.util.Validation.validate;

public class User {
    private final UserName name;
    private final UserPassword password;

    private User(UserName name, UserPassword password) {
        this.name = validate(name).notNull().get();
        this.password = validate(password).notNull().get();
    }

    public static User from(UserName name, UserPassword password) {
        return new User(name, password);
    }

    public UserName name() {
        return name;
    }

    public boolean acceptCredential(UserCredential credential) {
        return credential.acceptCredential(name, password);
    }

    public Stream<Account> accounts() {
        return DI.get(Repository.class).accounts(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return name.equals(user.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Stream<AuditItem> auditTrail() {
        return DI.get(Repository.class).auditItems(name);
    }
}