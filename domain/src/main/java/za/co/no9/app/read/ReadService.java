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

    public Either<ReadServiceFailure, Money> accountBalance(AccountRef accountRef) {
        return getAccount(accountRef).mapRight(Account::balance);
    }

    public Either<ReadServiceFailure, Stream<Transaction>> accountTransactions(AccountRef accountRef) {
        return getAccount(accountRef).mapRight(Account::transactions);
    }

    private Either<ReadServiceFailure, Account> getAccount(AccountRef accountRef) {
        final Account account = accounts.get(accountRef);
        if (account == null) {
            return Either.left(ReadServiceFailure.UNKNOWN_ACCOUNT_REF);
        } else {
            return Either.right(account);
        }
    }

    public Either<ReadServiceFailure, Stream<AuditEntry>> auditTrail(ClientID clientID) {
        return getClient(clientID).mapRight(Client::auditTrail);
    }

    private Either<ReadServiceFailure, Client> getClient(ClientID clientID) {
        final Client client = clients.get(clientID);
        if (client == null) {
            return Either.left(ReadServiceFailure.UNKNOWN_CLIENT_ID);
        } else {
            return Either.right(client);
        }
    }

    public enum ReadServiceFailure {
        UNKNOWN_CLIENT_ID, UNKNOWN_ACCOUNT_REF
    }
}
