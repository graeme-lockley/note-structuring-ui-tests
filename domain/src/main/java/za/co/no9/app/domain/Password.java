package za.co.no9.app.domain;

import za.co.no9.app.util.Validation;

public class Password {
    private final String password;

    public Password(String password) {
        this.password = Validation.value(password, "password").minimumLength(5).get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Password that = (Password) o;

        return password.equals(that.password);

    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }
}
