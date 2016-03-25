package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public final class AccountName {
    private final String value;

    private AccountName(String value) {
        this.value = validate(value, "value").notEmpty().get();
    }

    public static AccountName from(String value) {
        return new AccountName(value);
    }
}
