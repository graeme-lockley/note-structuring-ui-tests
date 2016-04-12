package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Password;

import static za.co.no9.app.util.Validation.validate;

public class Credential {
    private final ClientID clientID;
    private final Password password;

    private Credential(ClientID clientID, Password password) {
        this.clientID = validate(clientID).notNull().get();
        this.password = validate(password).notNull().get();
    }

    public static Credential from(ClientID clientID, Password password) {
        return new Credential(clientID, password);
    }

    public ClientID clientID() {
        return clientID;
    }

    public boolean acceptCredential(ClientID clientID, Password password) {
        return this.clientID.equals(clientID) && this.password.equals(password);
    }
}
