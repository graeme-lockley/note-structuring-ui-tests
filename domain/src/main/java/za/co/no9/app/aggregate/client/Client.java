package za.co.no9.app.aggregate.client;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Password;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;
import za.co.no9.app.util.Validation;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Client {
    private final ClientID clientID;
    private final Password password;
    private final Set<AccountRef> accounts = new HashSet<>();

    private Client(ClientID clientID, Password password) {
        this.clientID = Validation.value(clientID).notNull().get();
        this.password = Validation.value(password).notNull().get();
    }

    public static Client from(ClientID clientID, Password password) {
        return new Client(clientID, password);
    }

    public boolean acceptCredential(Credential credential) {
        return credential.acceptCredential(clientID, password);
    }

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
