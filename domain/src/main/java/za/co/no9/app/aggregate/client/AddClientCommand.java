package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.Password;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.ClientAdded;

public class AddClientCommand {
    final UserName userName;
    private final Password password;

    public AddClientCommand(UserName userName, Password password) {
        this.userName = userName;
        this.password = password;
    }

    ClientAdded makeEvent() {
        return new ClientAdded(userName, password);
    }
}
