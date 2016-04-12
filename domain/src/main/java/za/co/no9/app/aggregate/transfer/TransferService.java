package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.TransactionRef;
import za.co.no9.app.domain.UserID;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.InterAccountTransferred;
import za.co.no9.app.event.UserAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TransferService {
    private Users users = new Users();
    private TransactionRef lastReference = TransactionRef.from(0);

    public Optional<Set<PaymentServiceFailure>> interAccountTransfer(InterAccountTransferCommand command) {
        final Set<PaymentServiceFailure> failures = new HashSet<>();

        final Optional<User> user = users.find(command.userID);
        if (user.isPresent()) {
            final Optional<Account> sourceAccount = user.get().findAccount(command.source);
            final Optional<Account> destinationAccount = user.get().findAccount(command.destination);

            if (!sourceAccount.isPresent()) {
                failures.add(PaymentServiceFailure.UNKNOWN_SOURCE_ACCOUNT);
            } else if (!sourceAccount.get().hasSufficientFundsToDebit(command.paymentAmount)) {
                failures.add(PaymentServiceFailure.INSUFFICIENT_FUNDS);
            }
            if (!destinationAccount.isPresent()) {
                failures.add(PaymentServiceFailure.UNKNOWN_DESTINATION_ACCOUNT);
            }
        } else {
            failures.add(PaymentServiceFailure.UNKNOWN_USER);
        }

        if (failures.isEmpty()) {
            DI.get(EventStore.class).publishEvent(command.makeEvent(getTransactionDate(), lastReference.next()));

            return Optional.empty();
        } else {
            return Optional.of(failures);
        }
    }

    protected Date getTransactionDate() {
        return new Date();
    }

    private void apply(UserAdded event) {
        users.add(event.userID, new User());
    }

    private void apply(AccountAdded event) {
        users.get(event.userID).addAccount(Account.from(event.reference, event.openingBalance));
    }

    private void apply(InterAccountTransferred event) {
        final User user = users.get(event.userID);
        user.getAccount(event.source).debit(event.amount);
        user.getAccount(event.destination).credit(event.amount);

        if (lastReference.isLessThan(event.reference)) {
            lastReference = event.reference;
        }
    }

    public User getUser(UserID userID) {
        return users.get(userID);
    }

    enum PaymentServiceFailure {
        UNKNOWN_USER, UNKNOWN_SOURCE_ACCOUNT, UNKNOWN_DESTINATION_ACCOUNT, INSUFFICIENT_FUNDS
    }
}
