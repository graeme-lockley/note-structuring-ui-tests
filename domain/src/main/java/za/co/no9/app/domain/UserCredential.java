package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public class UserCredential {
    private final UserName username;
    private final UserPassword password;

    private UserCredential(UserName username, UserPassword password) {
        this.username = validate(username).notNull().get();
        this.password = validate(password).notNull().get();
    }

    public static UserCredential from(UserName username, UserPassword password) {
        return new UserCredential(username, password);
    }

    public UserName username() {
        return username;
    }

    public boolean acceptCredential(UserCredential credential) {
        return username.equals(credential.username) && acceptPassword(credential.password);
    }

    private boolean acceptPassword(UserPassword password) {
        return this.password.equals(password);
    }

    public boolean acceptCredential(UserName name, UserPassword password) {
        return username.equals(name) && this.password.equals(password);
    }
}
