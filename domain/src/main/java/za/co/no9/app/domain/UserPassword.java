package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public class UserPassword {
    private final String password;

    private UserPassword(String password) {
        this.password = validate(password, "password").minimumLength(5).get();
    }

    public static UserPassword from(String password) {
        return new UserPassword(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPassword that = (UserPassword) o;

        return password.equals(that.password);

    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }
}
