package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.UserID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Users {
    private Map<UserID, User> users = new HashMap<>();

    public User get(UserID userID) {
        return find(userID).get();
    }

    public Optional<User> find(UserID userID) {
        return Optional.ofNullable(users.get(userID));
    }

    public void add(UserID userID, User user) {
        users.put(userID, user);
    }
}
