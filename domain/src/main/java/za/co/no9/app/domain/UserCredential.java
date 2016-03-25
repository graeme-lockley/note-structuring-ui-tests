package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public class UserCredential {
    private final UserName username;
    private final String password;

    private UserCredential(UserName username, String password) {
        this.username = validate(username).notNull().get();
        this.password = validate(password, "password").minimumLength(5).get();
    }

    public static UserCredential from(UserName username, String password) {
        return new UserCredential(username, password);
    }

    public UserName username() {
        return username;
    }

    public boolean acceptCredential(UserCredential credential) {
        return username.equals(credential.username) && acceptPassword(credential.password);
    }

    private boolean acceptPassword(String password) {
        return this.password.equals(password);
    }
}
