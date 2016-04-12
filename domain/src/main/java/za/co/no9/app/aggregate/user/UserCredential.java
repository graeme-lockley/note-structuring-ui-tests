package za.co.no9.app.aggregate.user;

import za.co.no9.app.domain.UserID;
import za.co.no9.app.domain.UserPassword;

import static za.co.no9.app.util.Validation.validate;

public class UserCredential {
    private final UserID userID;
    private final UserPassword password;

    private UserCredential(UserID userID, UserPassword password) {
        this.userID = validate(userID).notNull().get();
        this.password = validate(password).notNull().get();
    }

    public static UserCredential from(UserID userID, UserPassword password) {
        return new UserCredential(userID, password);
    }

    public UserID username() {
        return userID;
    }

    public boolean acceptCredential(UserID name, UserPassword password) {
        return userID.equals(name) && this.password.equals(password);
    }
}
