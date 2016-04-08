package za.co.no9.app.service;

import za.co.no9.app.aggregate.user.User;
import za.co.no9.app.domain.*;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PaymentService {
    public Either<Set<PaymentServiceFailures>, TransactionRef> payment(UserName userName, AccountRef source, AccountRef destination, Money amount, TransactionDescription description) {
        final Set<PaymentServiceFailures> failures = validatePayment(userName, source, destination, amount);

        if (failures.isEmpty()) {
            return Either.right(TransactionRef.from("abcdefg"));
        } else {
            return Either.left(failures);
        }
    }

    private Set<PaymentServiceFailures> validatePayment(UserName userName, AccountRef source, AccountRef destination, Money amount) {
        Set<PaymentServiceFailures> failures = new HashSet<>();

        final Optional<User> user = DI.get(Repository.class).findUser(userName);

        if (user.isPresent()) {
            final Optional<Account> sourceAccount = user.get().accounts().filter(x -> x.reference().equals(source)).findFirst();
            final Optional<Account> destinationAccount = user.get().accounts().filter(x -> x.reference().equals(destination)).findFirst();

            if (!sourceAccount.isPresent()) {
                failures.add(PaymentServiceFailures.UNKNOWN_SOURCE_ACCOUNT);
            }
            if (!destinationAccount.isPresent()) {
                failures.add(PaymentServiceFailures.UNKNOWN_DESTINATION_ACCOUNT);
            }

            if (sourceAccount.isPresent() && amount.greaterThan(sourceAccount.get().balance())) {
                failures.add(PaymentServiceFailures.INSUFFICIENT_FUNDS);
            }
        } else {
            failures.add(PaymentServiceFailures.UNKNOWN_USER);
        }

        return failures;
    }

    enum PaymentServiceFailures {
        UNKNOWN_USER, UNKNOWN_SOURCE_ACCOUNT, UNKNOWN_DESTINATION_ACCOUNT, INSUFFICIENT_FUNDS
    }
}
