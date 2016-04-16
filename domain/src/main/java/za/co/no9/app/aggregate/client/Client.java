package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Client {
    private final Set<AccountRef> accounts = new HashSet<>();

    public Optional<ClientService.ClientServiceFailure> addAccount(AddAccountCommand command) {
        if (accounts.contains(command.accountRef)) {
            return Optional.of(ClientService.ClientServiceFailure.DUPLICATE_ACCOUNT_REF);
        } else {
            DI.get(EventStore.class).publishEvent(command.makeEvent());
            return Optional.empty();
        }
    }

    private void apply(AccountAdded event) {
        accounts.add(event.reference);
    }
}
