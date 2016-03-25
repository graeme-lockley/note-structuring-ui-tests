package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public final class TransactionRef {
    private final String value;

    private TransactionRef(String value) {
        this.value = validate(value, "value").minimumLength(1).get();
    }
    
    public static TransactionRef from(String value) {
        return new TransactionRef(value);
    }
}
