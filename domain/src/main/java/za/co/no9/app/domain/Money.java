package za.co.no9.app.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

import static za.co.no9.app.util.Validation.validate;
import static za.co.no9.app.util.Validation.value;

public class Money {
    private final Currency currency;
    private final BigDecimal value;

    private Money(Currency currency, BigDecimal value) {
        this.currency = value(currency).notNull().get();
        this.value = value(value).notNull().get().setScale(this.currency.decimals(), BigDecimal.ROUND_DOWN);
    }

    public Money(Currency currency, double value) {
        this(currency, BigDecimal.valueOf(value));
    }

    public boolean greaterThan(Money amount) {
        validate(currency == amount.currency, "Currency mismatch");

        return value.compareTo(amount.value) > 0;
    }

    public Money subtract(Money amount) {
        validate(currency == amount.currency, "Currency mismatch");

        return new Money(currency, value.subtract(amount.value));
    }

    public Money add(Money amount) {
        validate(currency == amount.currency, "Currency mismatch");

        return new Money(currency, value.add(amount.value));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("currency", currency)
                .append("value", value)
                .build();
    }

    public static Money from(String openBalance) {
        StringBuilder currencyString = new StringBuilder();
        int idx = 0;
        int lengthOfOpenBalance = openBalance.length();
        while (true) {
            if (idx == lengthOfOpenBalance) {
                break;
            }
            if (openBalance.charAt(idx) == '.' || Character.isDigit(openBalance.charAt(idx))) {
                break;
            }
            currencyString.append(openBalance.charAt(idx));
            idx += 1;
        }

        return new Money(Currency.parse(currencyString.toString()), new BigDecimal(openBalance.substring(idx)));
    }

    public String asString() {
        return currency.format(value);
    }

    public Currency currency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        return currency == money.currency && value.equals(money.value);
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
