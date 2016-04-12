package za.co.no9.app.aggregate.user;

import za.co.no9.app.domain.UserID;
import za.co.no9.app.domain.UserPassword;
import za.co.no9.app.event.UserAdded;

public class AddUserCommand {
    public final UserID userID;
    public final UserPassword password;


    public AddUserCommand(UserID userID, UserPassword password) {
        this.userID = userID;
        this.password = password;
    }

    public UserAdded makeEvent() {
        return new UserAdded(userID, password);
    }
}
