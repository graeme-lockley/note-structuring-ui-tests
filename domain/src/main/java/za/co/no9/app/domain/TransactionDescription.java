package za.co.no9.app.domain;

import za.co.no9.app.util.Validation;

public final class TransactionDescription {
    private final String value;

    public TransactionDescription(String value) {
        this.value = Validation.value(value, "value").notEmpty().get();
    }
}
