package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.aggregate.account.Account;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.UserAdded;

import java.util.*;

public class TransferService {
    private Map<UserName, Set<Account>> userAccounts = new HashMap<>();

    public Optional<Set<PaymentServiceFailure>> interAccountTransfer(InterAccountTransferCommand command) {
        Set<PaymentServiceFailure> failures = new HashSet<>();

        Set<Account> accounts = userAccounts.get(command.user);
        if (accounts == null) {
            failures.add(PaymentServiceFailure.UNKNOWN_USER);
        } else {
            final Optional<Account> sourceAccount = accounts.stream().filter(a -> a.reference().equals(command.source)).findFirst();
            final Optional<Account> destinationAccount = accounts.stream().filter(a -> a.reference().equals(command.destination)).findFirst();

            if (!sourceAccount.isPresent()) {
                failures.add(PaymentServiceFailure.UNKNOWN_SOURCE_ACCOUNT);
            } else if (!sourceAccount.get().hasSufficientFundsToDebit(command.paymentAmount)) {
                failures.add(PaymentServiceFailure.INSUFFICIENT_FUNDS);
            }
            if (!destinationAccount.isPresent()) {
                failures.add(PaymentServiceFailure.UNKNOWN_DESTINATION_ACCOUNT);
            }
        }

        if (failures.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(failures);
        }
    }

    private void apply(UserAdded event) {
        userAccounts.put(event.name, new HashSet<>());
    }

    private void apply(AccountAdded event) {
        userAccounts.get(event.userName).add(Account.from(event.reference, event.openingBalance, event.name));
    }

    enum PaymentServiceFailure {
        UNKNOWN_USER, UNKNOWN_SOURCE_ACCOUNT, UNKNOWN_DESTINATION_ACCOUNT, INSUFFICIENT_FUNDS
    }
}
