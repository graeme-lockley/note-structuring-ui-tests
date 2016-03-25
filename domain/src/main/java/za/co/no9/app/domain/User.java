package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public class User {
    private final UserCredential credential;

    private User(UserCredential credential) {
        this.credential = validate(credential).notNull().get();
    }

    public static User from(UserCredential credential) {
        return new User(credential);
    }

    public UserName username() {
        return credential.username();
    }

    public boolean acceptCredential(UserCredential credential) {
        return this.credential.acceptCredential(credential);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return credential.equals(user.credential);

    }

    @Override
    public int hashCode() {
        return credential.hashCode();
    }
}
