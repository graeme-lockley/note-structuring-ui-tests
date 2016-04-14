package za.co.no9.app.domain;

import static za.co.no9.app.util.Validation.validate;

public class ClientID {
    private final String value;

    public ClientID(String value) {
        this.value = validate(value, "value").minimumLength(5).get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientID clientID = (ClientID) o;

        return value.equals(clientID.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}