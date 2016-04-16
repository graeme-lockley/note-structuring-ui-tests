package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Money;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.ClientAdded;
import za.co.no9.app.event.InterAccountTransferred;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ReadService {
    private final Clients clients = new Clients();
    private final Map<AccountRef, Account> accounts = new HashMap<>();

    private void apply(ClientAdded event) {
        clients.addClient(event.clientID);
    }

    private void apply(AccountAdded event) {
        clients.addAccount(event.clientID, event.reference, event.openingBalance);
        accounts.put(event.reference, clients.get(event.clientID).findAccount(event.reference).get());
    }

    private void apply(InterAccountTransferred event) {
        final Client client = clients.get(event.clientID);
        DI.get(EventStore.class).processEvent(client, event);
    }

    public Money accountBalance(AccountRef accountRef) {
        return accounts.get(accountRef).balance();
    }

    public Stream<Transaction> accountTransactions(AccountRef accountRef) {
        return accounts.get(accountRef).transactions();
    }

    public Stream<AuditItem> auditTrail(ClientID clientID) {
        return clients.get(clientID).auditTrail();
    }
}
