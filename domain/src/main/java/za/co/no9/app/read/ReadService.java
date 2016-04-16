package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.ClientID;
import za.co.no9.app.domain.Money;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.ClientAdded;
import za.co.no9.app.event.InterAccountTransferred;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;
import za.co.no9.app.util.EventStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ReadService {
    private final Clients clients = new Clients();
    private final Map<AccountRef, Account> accounts = new HashMap<>();

    public Either<ReadServiceFailure, Money> accountBalance(AccountRef accountRef) {
        return getAccount(accountRef).foldRight(Account::balance);
    }

    public Either<ReadServiceFailure, Stream<Transaction>> accountTransactions(AccountRef accountRef) {
        return getAccount(accountRef).foldRight(Account::transactions);
    }

    public Either<ReadServiceFailure, Stream<AuditEntry>> auditTrail(ClientID clientID) {
        return getClient(clientID).foldRight(Client::auditTrail);
    }

    public boolean login(Credential credential) {
        return getClient(credential.clientID()).fold(l -> false, c -> c.acceptCredential(credential));
    }

    private Either<ReadServiceFailure, Account> getAccount(AccountRef accountRef) {
        return Either.rightElse(Optional.ofNullable(accounts.get(accountRef)), ReadServiceFailure.UNKNOWN_ACCOUNT_REF);
    }

    private Either<ReadServiceFailure, Client> getClient(ClientID clientID) {
        return Either.rightElse(Optional.ofNullable(clients.get(clientID)), ReadServiceFailure.UNKNOWN_CLIENT_ID);
    }

    private void apply(ClientAdded event) {
        clients.addClient(event.clientID, event.password);
    }

    private void apply(AccountAdded event) {
        clients.addAccount(event.clientID, event.reference, event.openingBalance);
        accounts.put(event.reference, clients.get(event.clientID).findAccount(event.reference).get());
    }

    private void apply(InterAccountTransferred event) {
        final Client client = clients.get(event.clientID);
        DI.get(EventStore.class).processEvent(client, event);
    }

    public enum ReadServiceFailure {
        UNKNOWN_CLIENT_ID, UNKNOWN_ACCOUNT_REF
    }
}
