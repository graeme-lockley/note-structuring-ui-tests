package za.co.no9.app.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoneyTest {
    @Test
    public void should_parse_a_currency_with_the_currency_enum_as_a_prefix() throws Exception {
        assertEquals("ZAR123.45", Money.from("ZAR123.45").asString());
    }

    @Test
    public void should_parse_a_currency_with_the_currency_symbol_as_a_prefix() throws Exception {
        assertEquals("ZAR123.45", Money.from("R123.45").asString());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void should_fail_when_attempting_to_parse_an_unknown_currency() throws Exception {
        Money.from("XXX123.45");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void should_fail_when_attempting_to_add_currency_mismatched_amounts() throws Exception {
        Money.from("ZAR123").add(Money.from("$45.65"));
    }

    @Test
    public void should_format_a_currency_amount_as_expected() throws Exception {
        assertEquals("USD1.23", Money.from("USD1.23").asString());
        assertEquals("USD1.20", Money.from("USD1.2").asString());
        assertEquals("USD1.23", Money.from("USD1.234").asString());
        assertEquals("USD1.23", Money.from("$1.234").asString());
    }
}