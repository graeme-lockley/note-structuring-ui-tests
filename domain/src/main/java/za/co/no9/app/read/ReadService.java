package za.co.no9.app.read;

import za.co.no9.app.domain.AccountNumber;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.UserName;
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
    private final Map<AccountNumber, Account> accounts = new HashMap<>();

    public Either<ReadServiceFailure, Money> accountBalance(AccountNumber accountNumber) {
        return getAccount(accountNumber).foldRight(Account::balance);
    }

    public Either<ReadServiceFailure, Stream<Transaction>> accountTransactions(AccountNumber accountNumber) {
        return getAccount(accountNumber).foldRight(Account::transactions);
    }

    public Either<ReadServiceFailure, Stream<AuditEntry>> auditTrail(UserName userName) {
        return getClient(userName).foldRight(Client::auditTrail);
    }

    public boolean login(Credential credential) {
        return getClient(credential.clientID()).fold(l -> false, c -> c.acceptCredential(credential));
    }

    private Either<ReadServiceFailure, Account> getAccount(AccountNumber accountNumber) {
        return Either.rightElse(Optional.ofNullable(accounts.get(accountNumber)), ReadServiceFailure.UNKNOWN_ACCOUNT_REF);
    }

    private Either<ReadServiceFailure, Client> getClient(UserName userName) {
        return Either.rightElse(Optional.ofNullable(clients.get(userName)), ReadServiceFailure.UNKNOWN_CLIENT_ID);
    }

    private void apply(ClientAdded event) {
        clients.addClient(event.userName, event.password);
    }

    private void apply(AccountAdded event) {
        clients.addAccount(event.userName, event.reference, event.openingBalance);
        accounts.put(event.reference, clients.get(event.userName).findAccount(event.reference).get());
    }

    private void apply(InterAccountTransferred event) {
        final Client client = clients.get(event.userName);
        DI.get(EventStore.class).processEvent(client, event);
    }

    public enum ReadServiceFailure {
        UNKNOWN_CLIENT_ID, UNKNOWN_ACCOUNT_REF
    }
}
