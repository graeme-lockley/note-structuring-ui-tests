package za.co.no9.app.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import za.co.no9.app.util.Validation;

public final class TransactionDescription {
    private final String value;

    public TransactionDescription(String value) {
        this.value = Validation.value(value, "value").notEmpty().get();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionDescription that = (TransactionDescription) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
