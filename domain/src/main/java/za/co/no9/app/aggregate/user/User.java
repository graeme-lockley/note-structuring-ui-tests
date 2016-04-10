package za.co.no9.app.aggregate.user;

import za.co.no9.app.domain.UserName;
import za.co.no9.app.domain.UserPassword;
import za.co.no9.app.event.InterAccountTransferred;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static za.co.no9.app.util.Validation.validate;

public class User {
    private final UserName name;
    private final UserPassword password;

    private List<AuditItem> auditItems = new ArrayList<>();

    private User(UserName name, UserPassword password) {
        this.name = validate(name).notNull().get();
        this.password = validate(password).notNull().get();
    }

    public static User from(UserName name, UserPassword password) {
        return new User(name, password);
    }

    public boolean acceptCredential(UserCredential credential) {
        return credential.acceptCredential(name, password);
    }

    private void apply(InterAccountTransferred event) {
        auditItems.add(new AuditItem(event.when, event.source, event.destination, event.amount, event.reference, event.description));
    }

    public Stream<AuditItem> auditItems() {
        return auditItems.stream();
    }
}
