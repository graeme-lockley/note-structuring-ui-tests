package za.co.no9.app.aggregate.transfer;

import za.co.no9.app.domain.AccountNumber;
import za.co.no9.app.domain.Currency;
import za.co.no9.app.domain.Money;
import za.co.no9.app.util.Validation;

class Account {
    private final AccountNumber accountNumber;
    private Money balance;

    private Account(AccountNumber accountNumber, Money balance) {
        this.accountNumber = Validation.value(accountNumber).notNull().get();
        this.balance = Validation.value(balance).notNull().get();
    }

    static Account from(AccountNumber reference, Money balance) {
        return new Account(reference, balance);
    }

    AccountNumber reference() {
        return accountNumber;
    }

    boolean hasSufficientFundsToDebit(Money amount) {
        return balance.greaterThan(amount);
    }

    void debit(Money amount) {
        balance = balance.subtract(amount);
    }

    void credit(Money amount) {
        balance = balance.add(amount);
    }

    Money balance() {
        return balance;
    }

    Currency currency() {
        return balance.currency();
    }
}
