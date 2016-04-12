package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public final class AccountName {
    private final String value;

    public AccountName(String value) {
        this.value = validate(value, "value").notEmpty().get();
    }
}
