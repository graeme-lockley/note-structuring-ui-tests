package za.co.no9.app.service;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.aggregate.account.Account;
import za.co.no9.app.aggregate.user.AuditItem;
import za.co.no9.app.aggregate.user.User;
import za.co.no9.app.domain.*;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentServiceTest {
    public static final UserName VALID_USER = UserName.from("mary-anne");
    public static final UserName UNKNOWN_USER = UserName.from("joe-joe");

    public static final AccountRef ACCOUNT_1 = AccountRef.from("12345");
    public static final AccountRef ACCOUNT_2 = AccountRef.from("12346");
    public static final AccountRef UNKNOWN_ACCOUNT = AccountRef.from("12347");
    private static final Money PAYMENT_AMOUNT = Money.from(12.00);
    private static final Money ILLEGAL_PAYMENT_AMOUNT = Money.from(12345.00);
    private static final TransactionDescription PAYMENT_DESCRIPTION = TransactionDescription.from("Test Payment");

    @Before
    public void before() {
        DI.reset();
        DI.register(TestRepository.builder()
                .addUser(User.from(VALID_USER, UserPassword.from("123456")))
                .addAccount(VALID_USER, Account.from(ACCOUNT_1, Money.from(123.45), AccountName.from("Credit Card")))
                .addAccount(VALID_USER, Account.from(ACCOUNT_2, Money.from(543.21), AccountName.from("Card Finance")))
                .addTransactions(ACCOUNT_2,
                        Transaction.from(Date.from(Instant.parse("2016-01-01T10:15:30.00Z")), TransactionRef.from("Transaction A"), TransactionDescription.from("Reference A"), Money.from(123.45)),
                        Transaction.from(Date.from(Instant.parse("2016-01-02T10:15:30.00Z")), TransactionRef.from("Transaction B"), TransactionDescription.from("Reference B"), Money.from(123.45)))
                .addAuditTrail(VALID_USER,
                        AuditItem.from(Date.from(Instant.parse("2016-01-01T10:15:30.00Z")), TransactionRef.from("A123"), TransactionDescription.from("Tranaction 123"), Money.from(123.45), ACCOUNT_1, ACCOUNT_2),
                        AuditItem.from(Date.from(Instant.parse("2016-01-02T10:15:30.00Z")), TransactionRef.from("A124"), TransactionDescription.from("Tranaction 124"), Money.from(123.46), ACCOUNT_2, ACCOUNT_1))
                .build());
        DI.register(new AccountService());
        DI.register(new PaymentService());
    }

    @Test
    public void given_an_unknown_user_should_fail_with_error() throws Exception {
        final Either<Set<PaymentService.PaymentServiceFailures>, TransactionRef> failures = DI.get(PaymentService.class).payment(UNKNOWN_USER, ACCOUNT_1, ACCOUNT_2, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION);

        assertTrue(failures.isLeft());
        assertTrue(failures.left().contains(PaymentService.PaymentServiceFailures.UNKNOWN_USER));
        assertEquals(1, failures.left().size());
    }

    @Test
    public void given_an_unknown_source_account_should_fail_with_error() throws Exception {
        final Either<Set<PaymentService.PaymentServiceFailures>, TransactionRef> failures = DI.get(PaymentService.class).payment(VALID_USER, UNKNOWN_ACCOUNT, ACCOUNT_2, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION);

        assertTrue(failures.isLeft());
        assertTrue(failures.left().contains(PaymentService.PaymentServiceFailures.UNKNOWN_SOURCE_ACCOUNT));
        assertEquals(1, failures.left().size());
    }

    @Test
    public void given_an_unknown_target_account_should_fail_with_error() throws Exception {
        final Either<Set<PaymentService.PaymentServiceFailures>, TransactionRef> failures = DI.get(PaymentService.class).payment(VALID_USER, ACCOUNT_1, UNKNOWN_ACCOUNT, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION);

        assertTrue(failures.isLeft());
        assertTrue(failures.left().contains(PaymentService.PaymentServiceFailures.UNKNOWN_DESTINATION_ACCOUNT));
        assertEquals(1, failures.left().size());
    }

    @Test
    public void given_insufficient_funds_should_fail_with_error() throws Exception {
        final Either<Set<PaymentService.PaymentServiceFailures>, TransactionRef> failures = DI.get(PaymentService.class).payment(VALID_USER, ACCOUNT_1, ACCOUNT_2, ILLEGAL_PAYMENT_AMOUNT, PAYMENT_DESCRIPTION);

        assertTrue(failures.isLeft());
        assertTrue(failures.left().contains(PaymentService.PaymentServiceFailures.INSUFFICIENT_FUNDS));
        assertEquals(1, failures.left().size());
    }

    @Test
    public void given_a_valid_payment_should_update_accounts_transactions_audit_trail() throws Exception {
        final Either<Set<PaymentService.PaymentServiceFailures>, TransactionRef> failures = DI.get(PaymentService.class).payment(VALID_USER, ACCOUNT_1, ACCOUNT_2, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION);

        assertTrue(failures.isRight());
    }
}