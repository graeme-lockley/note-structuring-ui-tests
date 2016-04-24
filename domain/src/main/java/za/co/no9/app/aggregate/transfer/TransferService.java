package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.TransactionRef;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.ClientAdded;
import za.co.no9.app.event.InterAccountTransferred;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TransferService {
    private Clients clients = new Clients();
    private TransactionRef lastReference = new TransactionRef((long) 0);

    public Optional<Set<PaymentServiceFailure>> interAccountTransfer(InterAccountTransferCommand command) {
        final Set<PaymentServiceFailure> failures = new HashSet<>();

        final Optional<Client> client = clients.find(command.userName);
        if (client.isPresent()) {
            final Optional<Account> sourceAccount = client.get().findAccount(command.source);
            final Optional<Account> destinationAccount = client.get().findAccount(command.destination);

            if (!sourceAccount.isPresent()) {
                failures.add(PaymentServiceFailure.UNKNOWN_SOURCE_ACCOUNT);
            } else if (!sourceAccount.get().hasSufficientFundsToDebit(command.paymentAmount)) {
                failures.add(PaymentServiceFailure.INSUFFICIENT_FUNDS);
            }
            if (!destinationAccount.isPresent()) {
                failures.add(PaymentServiceFailure.UNKNOWN_DESTINATION_ACCOUNT);
            }

            if (sourceAccount.isPresent() && destinationAccount.isPresent()) {
                if (!sourceAccount.get().currency().equals(destinationAccount.get().currency())) {
                    failures.add(PaymentServiceFailure.CURRENCY_MISMATCH);
                }
            }
        } else {
            failures.add(PaymentServiceFailure.UNKNOWN_CLIENT);
        }

        if (failures.isEmpty()) {
            DI.get(EventStore.class).publishEvent(command.makeEvent(getTransactionDate(), lastReference.next()));

            return Optional.empty();
        } else {
            return Optional.of(failures);
        }
    }

    private Date getTransactionDate() {
        return new Date();
    }

    private void apply(ClientAdded event) {
        clients.add(event.userName, new Client());
    }

    private void apply(AccountAdded event) {
        clients.get(event.userName).addAccount(Account.from(event.reference, event.openingBalance));
    }

    private void apply(InterAccountTransferred event) {
        final Client client = clients.get(event.userName);
        client.getAccount(event.source).debit(event.amount);
        client.getAccount(event.destination).credit(event.amount);

        if (lastReference.isLessThan(event.reference)) {
            lastReference = event.reference;
        }
    }

    Client getClient(UserName userName) {
        return clients.get(userName);
    }

    public enum PaymentServiceFailure {
        UNKNOWN_CLIENT, UNKNOWN_SOURCE_ACCOUNT, UNKNOWN_DESTINATION_ACCOUNT, INSUFFICIENT_FUNDS, CURRENCY_MISMATCH
    }
}
