package za.co.no9.app.domain;

import za.co.no9.app.util.Validation;

public final class AccountRef {
    private final String value;

    public AccountRef(String value) {
        this.value = Validation.value(value, "value").minimumLength(5).get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountRef that = (AccountRef) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
