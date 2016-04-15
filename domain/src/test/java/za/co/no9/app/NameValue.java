package za.co.no9.app;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NameValue {
    private String name;
    private String value;

    public NameValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("value", value)
                .build();
    }
}
