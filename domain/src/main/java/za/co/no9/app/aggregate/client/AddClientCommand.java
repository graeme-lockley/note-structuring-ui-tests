package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.Password;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.ClientAdded;

public class AddClientCommand {
    public final UserName userName;
    public final Password password;

    public AddClientCommand(UserName userName, Password password) {
        this.userName = userName;
        this.password = password;
    }

    public ClientAdded makeEvent() {
        return new ClientAdded(userName, password);
    }
}
