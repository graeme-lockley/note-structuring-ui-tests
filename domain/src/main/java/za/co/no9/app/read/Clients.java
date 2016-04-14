package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Money;

import java.util.HashMap;
import java.util.Map;

public class Clients {
    private Map<ClientID, Client> clients = new HashMap<>();

    public Client get(ClientID clientID) {
        return clients.get(clientID);
    }

    public void addClient(ClientID clientID) {
        final Client client = new Client();
        clients.put(clientID, client);
    }

    public void addAccount(ClientID clientID, AccountRef reference, Money openingBalance) {
        final Client client = clients.get(clientID);
        client.addAccount(reference, openingBalance);
    }
}
