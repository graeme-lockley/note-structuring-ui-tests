package za.co.no9.app.aggregate.transfer;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.domain.*;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.UserAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class TransferServiceTest {
    public static final UserName VALID_USER = UserName.from("mary-anne");
    public static final UserPassword VALID_USER_PASSWORD = UserPassword.from("password");
    public static final UserName UNKNOWN_USER = UserName.from("joe-joe");

    public static final AccountRef ACCOUNT_1 = AccountRef.from("12345");
    public static final Money ACCOUNT_1_OPENING_BALANCE = Money.from(123.45);
    public static final AccountName ACCOUNT_1_NAME = AccountName.from("Current Account");
    public static final AccountRef ACCOUNT_2 = AccountRef.from("12346");
    public static final Money ACCOUNT_2_OPENING_BALANCE = Money.from(12345.67);
    public static final AccountName ACCOUNT_2_NAME = AccountName.from("Cheque Account");
    public static final AccountRef UNKNOWN_ACCOUNT = AccountRef.from("12347");
    public static final Money PAYMENT_AMOUNT = Money.from(12.00);
    public static final Money ILLEGAL_PAYMENT_AMOUNT = Money.from(12345.00);
    public static final TransactionDescription PAYMENT_DESCRIPTION = TransactionDescription.from("Test Payment");

    private TransferService transferService = new TransferService();
    private EventStore eventStore = new EventStore();

    @Before
    public void setup() {
        DI.reset();
        DI.register(eventStore);
        DI.register(transferService);

        eventStore.registerEventHandler(transferService);

        eventStore.publishEvent(new UserAdded(VALID_USER, VALID_USER_PASSWORD));
        eventStore.publishEvent(new AccountAdded(VALID_USER, ACCOUNT_1, ACCOUNT_1_OPENING_BALANCE, ACCOUNT_1_NAME));
        eventStore.publishEvent(new AccountAdded(VALID_USER, ACCOUNT_2, ACCOUNT_2_OPENING_BALANCE, ACCOUNT_2_NAME));
    }

    @Test
    public void given_an_unknown_user_should_fail_with_error() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(UNKNOWN_USER, ACCOUNT_1, ACCOUNT_2, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertTrue(failures.isPresent());
        assertTrue(failures.get().contains(TransferService.PaymentServiceFailure.UNKNOWN_USER));
        assertEquals(1, failures.get().size());
    }

    @Test
    public void given_an_unknown_source_account_should_fail_with_error() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(VALID_USER, UNKNOWN_ACCOUNT, ACCOUNT_2, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertTrue(failures.isPresent());
        assertTrue(failures.get().contains(TransferService.PaymentServiceFailure.UNKNOWN_SOURCE_ACCOUNT));
        assertEquals(1, failures.get().size());
    }

    @Test
    public void given_an_unknown_target_account_should_fail_with_error() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(VALID_USER, ACCOUNT_1, UNKNOWN_ACCOUNT, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertTrue(failures.isPresent());
        assertTrue(failures.get().contains(TransferService.PaymentServiceFailure.UNKNOWN_DESTINATION_ACCOUNT));
        assertEquals(1, failures.get().size());
    }

    @Test
    public void given_insufficient_funds_should_fail_with_error() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(VALID_USER, ACCOUNT_1, ACCOUNT_2, ILLEGAL_PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertTrue(failures.isPresent());
        assertTrue(failures.get().contains(TransferService.PaymentServiceFailure.INSUFFICIENT_FUNDS));
        assertEquals(1, failures.get().size());
    }

    @Test
    public void given_a_valid_interaccount_transfer_the_account_balances_are_updated() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(VALID_USER, ACCOUNT_1, ACCOUNT_2, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertFalse(failures.isPresent());
        assertEquals(ACCOUNT_1_OPENING_BALANCE.subtract(PAYMENT_AMOUNT), transferService.getUser(VALID_USER).findAccount(ACCOUNT_1).get().balance());
        assertEquals(ACCOUNT_2_OPENING_BALANCE.add(PAYMENT_AMOUNT), transferService.getUser(VALID_USER).findAccount(ACCOUNT_2).get().balance());
    }
}