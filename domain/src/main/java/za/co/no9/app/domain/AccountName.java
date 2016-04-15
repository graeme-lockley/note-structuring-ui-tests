package za.co.no9.app.domain;

import za.co.no9.app.util.Validation;

public final class AccountName {
    private final String value;

    public AccountName(String value) {
        this.value = Validation.value(value, "value").notEmpty().get();
    }
}
