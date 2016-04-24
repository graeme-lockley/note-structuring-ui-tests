package za.co.no9.app.read;

import za.co.no9.app.domain.AccountNumber;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.Password;
import za.co.no9.app.domain.UserName;

import java.util.HashMap;
import java.util.Map;

class Clients {
    private Map<UserName, Client> clients = new HashMap<>();

    Client get(UserName userName) {
        return clients.get(userName);
    }

    void addClient(UserName userName, Password password) {
        final Client client = new Client(userName, password);
        clients.put(userName, client);
    }

    void addAccount(UserName userName, AccountNumber reference, Money openingBalance) {
        final Client client = clients.get(userName);
        client.addAccount(reference, openingBalance);
    }
}
