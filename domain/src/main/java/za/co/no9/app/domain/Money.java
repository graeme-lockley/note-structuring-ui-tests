package za.co.no9.app.domain;

import java.math.BigDecimal;

public class Money {
    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public static Money from(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public boolean greaterThan(Money balance) {
        return value.compareTo(balance.value) > 0;
    }
}
