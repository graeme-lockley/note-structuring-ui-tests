package za.co.no9.app.read;

import za.co.no9.app.domain.AccountNumber;
import za.co.no9.app.domain.Money;
import za.co.no9.app.domain.Password;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.event.InterAccountTransferred;
import za.co.no9.app.util.Validation;

import java.util.*;
import java.util.stream.Stream;

class Client {
    private final UserName userName;
    private final Password password;

    private final Set<Account> accounts = new HashSet<>();
    private final List<AuditEntry> auditTrail = new ArrayList<>();

    Client(UserName userName, Password password) {
        this.userName = Validation.value(userName).notNull().get();
        this.password = Validation.value(password).notNull().get();
    }

    Optional<Account> findAccount(AccountNumber accountNumber) {
        return accounts.stream().filter(x -> x.reference().equals(accountNumber)).findFirst();
    }

    void addAccount(AccountNumber reference, Money openingBalance) {
        accounts.add(new Account(reference, openingBalance));
    }

    Stream<AuditEntry> auditTrail() {
        return auditTrail.stream();
    }

    boolean acceptCredential(Credential credential) {
        return credential.acceptCredential(userName, password);
    }

    private void apply(InterAccountTransferred event) {
        findAccount(event.source).get().debit(event.when, event.reference, event.description, event.amount);
        findAccount(event.destination).get().credit(event.when, event.reference, event.description, event.amount);

        auditTrail.add(new AuditEntry(event.when, event.source, event.destination, event.amount, event.reference, event.description));
    }
}
