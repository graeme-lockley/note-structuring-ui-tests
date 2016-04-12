package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Password;
import za.co.no9.app.event.ClientAdded;

public class AddClientCommand {
    public final ClientID clientID;
    public final Password password;

    public AddClientCommand(ClientID clientID, Password password) {
        this.clientID = clientID;
        this.password = password;
    }

    public ClientAdded makeEvent() {
        return new ClientAdded(clientID, password);
    }
}
