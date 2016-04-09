package za.co.no9.app.aggregate.account;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.aggregate.account.AccountService.AccountServiceFailure;
import za.co.no9.app.domain.*;
import za.co.no9.app.event.AccountAdded;
import za.co.no9.app.event.UserAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;
import za.co.no9.app.util.EventStore;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountServiceTest {
    public static final UserName USER_A = UserName.from("graeme");
    public static final UserPassword USER_A_PASSWORD = UserPassword.from("password");
    public static final UserName USER_B = UserName.from("mary-anne");
    public static final UserPassword USER_B_PASSWORD = UserPassword.from("password");
    public static final UserName UNKNOWN_USER = UserName.from("joe-joe");

    public static final AccountRef ACCOUNT_B_1 = AccountRef.from("12345");
    public static final AccountName ACCOUNT_B_1_NAME = AccountName.from("Account 12345");
    public static final AccountRef ACCOUNT_B_2 = AccountRef.from("12346");
    public static final AccountName ACCOUNT_B_2_NAME = AccountName.from("Account 12346");
    public static final AccountRef UNKNOWN_ACCOUNT = AccountRef.from("12347");

    private AccountService accountService = new AccountService();
    private EventStore eventStore = new EventStore();

    @Before
    public void setup() {
        DI.reset();
        DI.register(eventStore);
        DI.register(accountService);

        eventStore.registerEventHandler(accountService);

        eventStore.publishEvent(new UserAdded(USER_A, USER_A_PASSWORD));
        eventStore.publishEvent(new UserAdded(USER_B, USER_B_PASSWORD));
        eventStore.publishEvent(new AccountAdded(USER_B, ACCOUNT_B_1, Money.from(0.0), ACCOUNT_B_1_NAME));
        eventStore.publishEvent(new AccountAdded(USER_B, ACCOUNT_B_2, Money.from(0.0), ACCOUNT_B_2_NAME));
    }

    @Test
    public void given_a_user_with_no_accounts_should_return_no_accounts() throws Exception {
        final Either<AccountService.AccountServiceFailure, Stream<Account>> view = accountService.accounts(USER_A);

        assertTrue(view.isRight());
        assertEquals(0, view.right().count());
    }

    @Test
    public void given_a_known_user_should_return_two_accounts() throws Exception {
        final Either<AccountService.AccountServiceFailure, Stream<Account>> view = accountService.accounts(USER_B);

        assertTrue(view.isRight());
        assertEquals(2, view.right().count());
    }

    @Test
    public void given_an_unknown_user_should_return_an_error() throws Exception {
        final Either<AccountServiceFailure, Stream<Account>> view = accountService.accounts(UNKNOWN_USER);

        assertTrue(view.isLeft());
        assertEquals(AccountServiceFailure.UNKNOWN_USER, view.left());
    }

    @Test
    public void given_an_unknown_account_should_return_an_error() throws Exception {
        final Either<AccountService.AccountServiceFailure, Stream<Transaction>> findResult = accountService.accountTransactions(UNKNOWN_ACCOUNT);

        assertTrue(findResult.isLeft());
        assertEquals(AccountService.AccountServiceFailure.UNKNOWN_ACCOUNT, findResult.left());
    }

    @Test
    public void given_an_account_with_no_transactions_should_return_no_transactions() throws Exception {
        final Either<AccountService.AccountServiceFailure, Stream<Transaction>> findResult = accountService.accountTransactions(ACCOUNT_B_1);

        assertTrue(findResult.isRight());
        assertEquals(0, findResult.right().count());
    }
}