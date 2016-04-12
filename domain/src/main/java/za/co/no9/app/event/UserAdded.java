package za.co.no9.app.event;

import za.co.no9.app.domain.UserID;
import za.co.no9.app.domain.UserPassword;
import za.co.no9.app.util.Event;

public class UserAdded implements Event {
    public final UserID userID;
    public final UserPassword password;

    public UserAdded(UserID userID, UserPassword password) {
        this.userID = userID;
        this.password = password;
    }
}
