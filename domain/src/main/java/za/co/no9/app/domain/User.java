package za.co.no9.app.domain;

import za.co.no9.app.service.CredentialStore;
import za.co.no9.app.util.DI;

import static za.co.no9.app.util.Validation.validate;

public class User {
    private final UserName name;

    private User(UserName name) {
        this.name = validate(name).notNull().get();
    }

    public static User from(UserName name) {
        return new User(name);
    }

    public UserName name() {
        return name;
    }

    public boolean acceptCredential(UserCredential credential) {
        return DI.get(CredentialStore.class).accept(credential);
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
}
