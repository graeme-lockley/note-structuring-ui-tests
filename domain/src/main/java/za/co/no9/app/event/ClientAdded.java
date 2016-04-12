package za.co.no9.app.event;

import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Password;
import za.co.no9.app.util.Event;

public class ClientAdded implements Event {
    public final ClientID clientID;
    public final Password password;

    public ClientAdded(ClientID clientID, Password password) {
        this.clientID = clientID;
        this.password = password;
    }
}
