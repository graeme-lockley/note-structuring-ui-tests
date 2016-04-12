package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.ClientID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Clients {
    private Map<ClientID, Client> clients = new HashMap<>();

    public Client get(ClientID clientID) {
        return find(clientID).get();
    }

    public Optional<Client> find(ClientID clientID) {
        return Optional.ofNullable(clients.get(clientID));
    }

    public void add(ClientID clientID, Client client) {
        clients.put(clientID, client);
    }
}
