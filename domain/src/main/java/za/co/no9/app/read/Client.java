package za.co.no9.app.read;

import za.co.no9.app.domain.AccountRef;
import za.co.no9.app.domain.Money;
import za.co.no9.app.event.InterAccountTransferred;

import java.util.*;
import java.util.stream.Stream;

public class Client {
    private final Set<Account> accounts = new HashSet<>();
    private final List<AuditItem> auditTrail = new ArrayList<>();

    public Optional<Account> findAccount(AccountRef accountRef) {
        return accounts.stream().filter(x -> x.reference().equals(accountRef)).findFirst();
    }

    public void addAccount(AccountRef reference, Money openingBalance) {
        accounts.add(new Account(reference, openingBalance));
    }

    public Stream<AuditItem> auditTrail() {
        return auditTrail.stream();
    }

    private void apply(InterAccountTransferred event) {
        findAccount(event.source).get().debit(event.when, event.reference, event.description, event.amount);
        findAccount(event.destination).get().credit(event.when, event.reference, event.description, event.amount);

        auditTrail.add(new AuditItem(event.when, event.source, event.destination, event.amount, event.reference, event.description));
    }
}
