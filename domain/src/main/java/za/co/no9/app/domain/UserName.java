package za.co.no9.app.domain;

import za.co.no9.app.util.Validation;

public class UserName {
    private final String value;

    public UserName(String value) {
        this.value = Validation.value(value, "value").minimumLength(5).get();
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
