package za.co.no9.app.aggregate.user;

import za.co.no9.app.domain.UserID;
import za.co.no9.app.domain.UserPassword;

import static za.co.no9.app.util.Validation.validate;

public class User {
    private final UserID userID;
    private final UserPassword password;

    private User(UserID userID, UserPassword password) {
        this.userID = validate(userID).notNull().get();
        this.password = validate(password).notNull().get();
    }

    public static User from(UserID userID, UserPassword password) {
        return new User(userID, password);
    }

    public boolean acceptCredential(UserCredential credential) {
        return credential.acceptCredential(userID, password);
    }
}
