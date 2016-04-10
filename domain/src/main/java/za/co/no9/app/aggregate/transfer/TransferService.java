package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.TransactionRef;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.InterAccountTransferred;
import za.co.no9.app.event.UserAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.*;

public class TransferService {
    private TransactionRef lastReference = TransactionRef.from(0);

    private Map<UserName, User> users = new HashMap<>();

    public Optional<Set<PaymentServiceFailure>> interAccountTransfer(InterAccountTransferCommand command) {
        final Set<PaymentServiceFailure> failures = new HashSet<>();

        final Optional<User> user = findUser(command.user);
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
        users.put(event.name, new User());
    }

    private void apply(AccountAdded event) {
        users.get(event.userName).addAccount(Account.from(event.reference, event.openingBalance));
    }

    private void apply(InterAccountTransferred event) {
        final User user = getUser(event.user);
        user.getAccount(event.source).debit(event.amount);
        user.getAccount(event.destination).credit(event.amount);

        if (lastReference.isLessThan(event.reference)) {
            lastReference = event.reference;
        }
    }

    public User getUser(UserName name) {
        return findUser(name).get();
    }

    private Optional<User> findUser(UserName name) {
        return Optional.ofNullable(users.get(name));
    }

    enum PaymentServiceFailure {
        UNKNOWN_USER, UNKNOWN_SOURCE_ACCOUNT, UNKNOWN_DESTINATION_ACCOUNT, INSUFFICIENT_FUNDS
    }
}
