package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.UserName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class Clients {
    private Map<UserName, Client> clients = new HashMap<>();

    Client get(UserName userName) {
        return find(userName).get();
    }

    Optional<Client> find(UserName userName) {
        return Optional.ofNullable(clients.get(userName));
    }

    void add(UserName userName, Client client) {
        clients.put(userName, client);
    }
}
