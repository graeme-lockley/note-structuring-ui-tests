package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.ClientAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientService {
    private Map<UserName, Client> clients = new HashMap<>();

    public Optional<ClientServiceFailure> addClient(AddClientCommand command) {
        if (clients.containsKey(command.userName)) {
            return Optional.of(ClientServiceFailure.DUPLICATE_CLIENT_ID);
        }
        DI.get(EventStore.class).publishEvent(command.makeEvent());

        return Optional.empty();
    }

    public Optional<ClientServiceFailure> addAccount(AddAccountCommand command) {
        Optional<Client> client = findClient(command.userName);
        if (client.isPresent()) {
            return client.get().addAccount(command);
        } else {
            return Optional.of(ClientServiceFailure.UNKNOWN_CLIENT);
        }
    }

    private Optional<Client> findClient(UserName userName) {
        return Optional.ofNullable(clients.get(userName));
    }

    private void apply(ClientAdded event) {
        clients.put(event.userName, new Client());
    }

    private void apply(AccountAdded event) {
        DI.get(EventStore.class).processEvent(clients.get(event.userName), event);
    }

    public enum ClientServiceFailure {
        DUPLICATE_CLIENT_ID, UNKNOWN_CLIENT, DUPLICATE_ACCOUNT_REF
    }
}
