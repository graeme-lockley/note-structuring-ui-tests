package za.co.no9.app.aggregate.user;

import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.InterAccountTransferred;
import za.co.no9.app.event.UserAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserService {
    private Map<UserName, User> users = new HashMap<>();

    public Optional<UserServiceFailure> addUser(AddUserCommand command) {
        if (users.containsKey(command.name)) {
            return Optional.of(UserServiceFailure.DUPLICATE_USERNAME);
        }
        DI.get(EventStore.class).publishEvent(command.makeEvent());

        return Optional.empty();
    }

    private void apply(UserAdded event) {
        users.put(event.name, User.from(event.name, event.password));
    }

    private void apply(InterAccountTransferred event) {
        DI.get(EventStore.class).processEvent(findUser(event.user).get(), event);
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

    public Optional<User> findUser(UserName name) {
        return Optional.ofNullable(users.get(name));
    }

    public enum UserServiceFailure {
        DUPLICATE_USERNAME, INVALID_CREDENTIAL, UNKNOWN_USER
    }
}
