package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public final class AccountRef {
    private final String value;

    private AccountRef(String value) {
        this.value = validate(value, "value").minimumLength(5).get();
    }

    public static AccountRef from(String value) {
        return new AccountRef(value);
    }
}
