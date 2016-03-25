package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public class UserName {
    private final String value;

    private UserName(String value) {
        this.value = validate(value, "value").minimumLength(5).get();
    }

    public static UserName from(String value) {
        return new UserName(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserName userName = (UserName) o;

        return value.equals(userName.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
