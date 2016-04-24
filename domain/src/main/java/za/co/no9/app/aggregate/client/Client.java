package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.AccountNumber;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class Client {
    private final Set<AccountNumber> accounts = new HashSet<>();

    Optional<ClientService.ClientServiceFailure> addAccount(AddAccountCommand command) {
        if (accounts.contains(command.accountNumber)) {
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
