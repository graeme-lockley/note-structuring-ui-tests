package za.co.no9.app.read;

import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.ClientAdded;
import za.co.no9.app.event.InterAccountTransferred;

public class ReadService {
    private Clients clients = new Clients();

    private void apply(ClientAdded event) {
        clients.addClient(event.clientID);
    }

    private void apply(AccountAdded event) {
        clients.addAccount(event.clientID, event.reference, event.openingBalance);
    }

    private void apply(InterAccountTransferred event) {
        final Client client = clients.get(event.clientID);
        client.getAccount(event.source).debit(event.when, event.reference, event.description, event.amount);
        client.getAccount(event.destination).credit(event.when, event.reference, event.description, event.amount);
    }
}
