package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public final class TransactionDescription {
    private final String value;

    private TransactionDescription(String value) {
        this.value = validate(value, "value").notEmpty().get();
    }

    public static TransactionDescription from(String value) {
        return new TransactionDescription(value);
    }
}
