package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.ClientID;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.ClientAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientService {
    private Map<ClientID, Client> clients = new HashMap<>();

    public Optional<ClientServiceFailure> addClient(AddClientCommand command) {
        if (clients.containsKey(command.clientID)) {
            return Optional.of(ClientServiceFailure.DUPLICATE_CLIENT_ID);
        }
        DI.get(EventStore.class).publishEvent(command.makeEvent());

        return Optional.empty();
    }

    public Optional<ClientServiceFailure> addAccount(AddAccountCommand command) {
        Optional<Client> client = findClient(command.clientID);
        if (client.isPresent()) {
            return client.get().addAccount(command);
        } else {
            return Optional.of(ClientServiceFailure.UNKNOWN_CLIENT);
        }
    }

    private Optional<Client> findClient(ClientID clientID) {
        return Optional.ofNullable(clients.get(clientID));
    }

    private void apply(ClientAdded event) {
        clients.put(event.clientID, new Client());
    }

    private void apply(AccountAdded event) {
        DI.get(EventStore.class).processEvent(clients.get(event.clientID), event);
    }

    public enum ClientServiceFailure {
        DUPLICATE_CLIENT_ID, UNKNOWN_CLIENT, DUPLICATE_ACCOUNT_REF
    }
}
