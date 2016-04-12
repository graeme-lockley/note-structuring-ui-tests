package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.UserName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Users {
    private Map<UserName, User> users = new HashMap<>();

    public User get(UserName name) {
        return find(name).get();
    }

    public Optional<User> find(UserName name) {
        return Optional.ofNullable(users.get(name));
    }

    public void add(UserName name, User user) {
        users.put(name, user);
    }
}
