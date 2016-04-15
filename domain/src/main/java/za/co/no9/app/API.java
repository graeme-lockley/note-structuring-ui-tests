package za.co.no9.app;

import za.co.no9.app.aggregate.client.AddAccountCommand;
import za.co.no9.app.aggregate.client.AddClientCommand;
import za.co.no9.app.aggregate.client.ClientService;
import za.co.no9.app.aggregate.client.Credential;
import za.co.no9.app.aggregate.transfer.InterAccountTransferCommand;
import za.co.no9.app.aggregate.transfer.TransferService;
import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.read.ReadService;
import za.co.no9.app.read.Transaction;
import za.co.no9.app.util.DI;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class API {
    public Optional<ClientService.ClientServiceFailure> addClient(AddClientCommand command) {
        return DI.get(ClientService.class).addClient(command);
    }

    public Optional<ClientService.ClientServiceFailure> login(Credential credential) {
        return DI.get(ClientService.class).login(credential);
    }

    public Optional<Set<TransferService.PaymentServiceFailure>> interAccountTransfer(InterAccountTransferCommand command) {
        return DI.get(TransferService.class).interAccountTransfer(command);
    }

    public Optional<ClientService.ClientServiceFailure> addAccount(AddAccountCommand command) {
        return DI.get(ClientService.class).addAccount(command);
    }

    // TODO Add an Either return type
    public Money accountBalance(AccountRef accountRef) {
        return DI.get(ReadService.class).accountBalance(accountRef);
    }

    // TODO Add an Either return type
    public Stream<Transaction> accountTransactions(AccountRef accountRef) {
        return DI.get(ReadService.class).accountTransactions(accountRef);
    }
}
