package za.co.no9.app.event;

import za.co.no9.app.domain.UserName;
import za.co.no9.app.domain.UserPassword;
import za.co.no9.app.util.Event;

public class UserAdded implements Event {
    public final UserName name;
    public final UserPassword password;

    public UserAdded(UserName name, UserPassword password) {
        this.name = name;
        this.password = password;
    }
}
