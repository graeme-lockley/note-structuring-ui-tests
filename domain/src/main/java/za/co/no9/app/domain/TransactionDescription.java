package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public final class TransactionDescription {
    private final String value;

    public TransactionDescription(String value) {
        this.value = validate(value, "value").notEmpty().get();
    }
}
