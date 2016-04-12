package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public class UserID {
    private final String value;

    private UserID(String value) {
        this.value = validate(value, "value").minimumLength(5).get();
    }

    public static UserID from(String value) {
        return new UserID(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserID userID = (UserID) o;

        return value.equals(userID.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
