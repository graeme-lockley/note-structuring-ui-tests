package za.co.no9.app.read;

import za.co.no9.app.domain.Password;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.util.Validation;

public class Credential {
    private final UserName userName;
    private final Password password;

    private Credential(UserName userName, Password password) {
        this.userName = Validation.value(userName).notNull().get();
        this.password = Validation.value(password).notNull().get();
    }

    public static Credential from(UserName userName, Password password) {
        return new Credential(userName, password);
    }

    public UserName clientID() {
        return userName;
    }

    public boolean acceptCredential(UserName userName, Password password) {
        return this.userName.equals(userName) && this.password.equals(password);
    }
}
