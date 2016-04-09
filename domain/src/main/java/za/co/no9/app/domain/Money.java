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

    public Money subtract(Money amount) {
        return new Money(value.subtract(amount.value));
    }

    public Money add(Money amount) {
        return new Money(value.add(amount.value));
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        return value.equals(money.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
