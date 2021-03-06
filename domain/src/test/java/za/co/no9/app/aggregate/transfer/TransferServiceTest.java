package za.co.no9.app.aggregate.transfer;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.domain.*;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.ClientAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static za.co.no9.app.domain.Currency.ZAR;

public class TransferServiceTest {
    public static final UserName VALID_CLIENT = new UserName("mary-anne");
    public static final Password VALID_CLIENT_PASSWORD = new Password("password");
    public static final UserName UNKNOWN_CLIENT = new UserName("joe-joe");

    public static final AccountNumber ACCOUNT_1 = new AccountNumber("12345");
    public static final Money ACCOUNT_1_OPENING_BALANCE = new Money(ZAR, 123.45);
    public static final AccountNumber ACCOUNT_2 = new AccountNumber("12346");
    public static final Money ACCOUNT_2_OPENING_BALANCE = new Money(ZAR, 12345.67);
    public static final AccountNumber UNKNOWN_ACCOUNT = new AccountNumber("12347");
    public static final Money PAYMENT_AMOUNT = new Money(ZAR, 12.00);
    public static final Money ILLEGAL_PAYMENT_AMOUNT = new Money(ZAR, 12345.00);
    public static final TransactionDescription PAYMENT_DESCRIPTION = new TransactionDescription("Test Payment");

    private TransferService transferService = new TransferService();
    private EventStore eventStore = new EventStore();

    @Before
    public void setup() {
        DI.reset();
        DI.register(eventStore);
        DI.register(transferService);

        eventStore.registerEventHandler(transferService);

        eventStore.publishEvent(new ClientAdded(VALID_CLIENT, VALID_CLIENT_PASSWORD));
        eventStore.publishEvent(new AccountAdded(VALID_CLIENT, ACCOUNT_1, ACCOUNT_1_OPENING_BALANCE));
        eventStore.publishEvent(new AccountAdded(VALID_CLIENT, ACCOUNT_2, ACCOUNT_2_OPENING_BALANCE));
    }

    @Test
    public void given_an_unknown_client_should_fail_with_error() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(UNKNOWN_CLIENT, ACCOUNT_1, ACCOUNT_2, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertTrue(failures.isPresent());
        assertTrue(failures.get().contains(TransferService.PaymentServiceFailure.UNKNOWN_CLIENT));
        assertEquals(1, failures.get().size());
    }

    @Test
    public void given_an_unknown_source_account_should_fail_with_error() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(VALID_CLIENT, UNKNOWN_ACCOUNT, ACCOUNT_2, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertTrue(failures.isPresent());
        assertTrue(failures.get().contains(TransferService.PaymentServiceFailure.UNKNOWN_SOURCE_ACCOUNT));
        assertEquals(1, failures.get().size());
    }

    @Test
    public void given_an_unknown_destination_account_should_fail_with_error() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(VALID_CLIENT, ACCOUNT_1, UNKNOWN_ACCOUNT, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertTrue(failures.isPresent());
        assertTrue(failures.get().contains(TransferService.PaymentServiceFailure.UNKNOWN_DESTINATION_ACCOUNT));
        assertEquals(1, failures.get().size());
    }

    @Test
    public void given_insufficient_funds_should_fail_with_error() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(VALID_CLIENT, ACCOUNT_1, ACCOUNT_2, ILLEGAL_PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertTrue(failures.isPresent());
        assertTrue(failures.get().contains(TransferService.PaymentServiceFailure.INSUFFICIENT_FUNDS));
        assertEquals(1, failures.get().size());
    }

    @Test
    public void given_a_valid_interaccount_transfer_the_account_balances_are_updated() throws Exception {
        final Optional<Set<TransferService.PaymentServiceFailure>> failures = transferService.interAccountTransfer(new InterAccountTransferCommand(VALID_CLIENT, ACCOUNT_1, ACCOUNT_2, PAYMENT_AMOUNT, PAYMENT_DESCRIPTION));

        assertFalse(failures.isPresent());
        assertEquals(ACCOUNT_1_OPENING_BALANCE.subtract(PAYMENT_AMOUNT), transferService.getClient(VALID_CLIENT).findAccount(ACCOUNT_1).get().balance());
        assertEquals(ACCOUNT_2_OPENING_BALANCE.add(PAYMENT_AMOUNT), transferService.getClient(VALID_CLIENT).findAccount(ACCOUNT_2).get().balance());
    }
}