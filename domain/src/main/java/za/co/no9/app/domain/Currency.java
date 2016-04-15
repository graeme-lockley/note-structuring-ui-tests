package za.co.no9.app.domain;

import java.math.BigDecimal;

public enum Currency {
    USD("$", 2),
    ZAR("R", 2);

    private final String symbol;
    private final int decimals;

    Currency(String symbol, int decimals) {
        this.symbol = symbol;
        this.decimals = decimals;
    }

    public int decimals() {
        return this.decimals;
    }

    public String format(BigDecimal value) {
        return this.name() + value;
    }

    public static Currency parse(String currencySymbol) {
        for (Currency value : values()) {
            if (value.name().equals(currencySymbol) || value.symbol.equals(currencySymbol)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown currency symbol " + currencySymbol);
    }
}
