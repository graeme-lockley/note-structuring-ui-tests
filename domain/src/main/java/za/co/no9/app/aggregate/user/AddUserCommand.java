package za.co.no9.app.aggregate.user;

import za.co.no9.app.domain.UserName;
import za.co.no9.app.domain.UserPassword;
import za.co.no9.app.event.UserAdded;

public class AddUserCommand {
    public final UserName name;
    public final UserPassword password;


    public AddUserCommand(UserName name, UserPassword password) {
        this.name = name;
        this.password = password;
    }

    public UserAdded makeEvent() {
        return new UserAdded(name, password);
    }
}
