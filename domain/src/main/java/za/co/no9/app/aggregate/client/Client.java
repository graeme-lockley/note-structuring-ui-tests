package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Password;
import za.co.no9.app.util.Validation;

public class Client {
    private final ClientID clientID;
    private final Password password;

    private Client(ClientID clientID, Password password) {
        this.clientID = Validation.value(clientID).notNull().get();
        this.password = Validation.value(password).notNull().get();
    }

    public static Client from(ClientID clientID, Password password) {
        return new Client(clientID, password);
    }

    public boolean acceptCredential(Credential credential) {
        return credential.acceptCredential(clientID, password);
    }
}
