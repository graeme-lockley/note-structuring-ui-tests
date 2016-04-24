package za.co.no9.app.event;

import za.co.no9.app.domain.Password;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.util.Event;

public class ClientAdded implements Event {
    public final UserName userName;
    public final Password password;

    public ClientAdded(UserName userName, Password password) {
        this.userName = userName;
        this.password = password;
    }
}
