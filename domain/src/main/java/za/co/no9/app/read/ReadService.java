package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.ClientAdded;
import za.co.no9.app.event.InterAccountTransferred;

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
        client.findAccount(event.source).get().debit(event.when, event.reference, event.description, event.amount);
        client.findAccount(event.destination).get().credit(event.when, event.reference, event.description, event.amount);
    }

    public Money accountBalance(AccountRef accountRef) {
        return accounts.get(accountRef).balance();
    }

    public Stream<Transaction> accountTransactions(AccountRef accountRef) {
        return accounts.get(accountRef).transactions();
    }
}
