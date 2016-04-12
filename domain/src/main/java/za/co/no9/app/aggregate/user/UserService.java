package za.co.no9.app.aggregate.user;

import za.co.no9.app.domain.UserID;
import za.co.no9.app.event.UserAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserService {
    private Map<UserID, User> users = new HashMap<>();

    public Optional<UserServiceFailure> addUser(AddUserCommand command) {
        if (users.containsKey(command.userID)) {
            return Optional.of(UserServiceFailure.DUPLICATE_USERNAME);
        }
        DI.get(EventStore.class).publishEvent(command.makeEvent());

        return Optional.empty();
    }

    private void apply(UserAdded event) {
        users.put(event.userID, User.from(event.userID, event.password));
    }

    public Optional<UserServiceFailure> login(UserCredential credential) {
        final Optional<User> user = findUser(credential.username());
        if (!user.isPresent()) {
            return Optional.of(UserServiceFailure.UNKNOWN_USER);
        } else if (user.get().acceptCredential(credential)) {
            return Optional.empty();
        } else {
            return Optional.of(UserServiceFailure.INVALID_CREDENTIAL);
        }
    }

    public Optional<User> findUser(UserID userID) {
        return Optional.ofNullable(users.get(userID));
    }

    public enum UserServiceFailure {
        DUPLICATE_USERNAME, INVALID_CREDENTIAL, UNKNOWN_USER
    }
}
