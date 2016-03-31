package za.co.no9.app.service;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.domain.*;
import za.co.no9.app.service.AccountsService.AccountsServiceFailures;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountsServiceTest {
    public static final UserName USER_A = UserName.from("graeme");
    public static final UserName USER_B = UserName.from("mary-anne");
    public static final UserName UNKNOWN_USER = UserName.from("joe-joe");

    public static final AccountRef ACCOUNT_B_1 = AccountRef.from("12345");
    public static final AccountRef ACCOUNT_B_2 = AccountRef.from("12346");
    public static final AccountRef UNKNOWN_ACCOUNT = AccountRef.from("12347");

    @Before
    public void before() {
        DI.reset();
        DI.register(TestRepository.builder()
                .addUser(User.from(USER_A))
                .addUser(User.from(USER_B))
                .addAccount(USER_B, Account.from(ACCOUNT_B_1, Money.from(123.45), AccountName.from("Credit Card")))
                .addAccount(USER_B, Account.from(ACCOUNT_B_2, Money.from(543.21), AccountName.from("Card Finance")))
                .addTransactions(ACCOUNT_B_2,
                        Transaction.from(Date.from(Instant.parse("2016-01-01T10:15:30.00Z")), TransactionRef.from("Transaction A"), TransactionDescription.from("Reference A"), Money.from(123.45)),
                        Transaction.from(Date.from(Instant.parse("2016-01-02T10:15:30.00Z")), TransactionRef.from("Transaction B"), TransactionDescription.from("Reference B"), Money.from(123.45)))
                .build());
        DI.register(new AccountsService());
    }

    @Test
    public void should_return_no_accounts_if_the_user_has_no_accounts() throws Exception {
        final Either<AccountsServiceFailures, Stream<Account>> view = DI.get(AccountsService.class).view(USER_A);

        assertTrue(view.isRight());
        assertEquals(0, view.right().count());
    }

    @Test
    public void should_return_two_accounts_if_the_user_is_known() throws Exception {
        final Either<AccountsServiceFailures, Stream<Account>> view = DI.get(AccountsService.class).view(USER_B);

        assertTrue(view.isRight());
        assertEquals(2, view.right().count());
    }

    @Test
    public void should_return_an_error_if_user_is_unknown() throws Exception {
        final Either<AccountsServiceFailures, Stream<Account>> view = DI.get(AccountsService.class).view(UNKNOWN_USER);

        assertTrue(view.isLeft());
        assertEquals(AccountsServiceFailures.UNKNOWN_USER, view.left());
    }

    @Test
    public void should_return_an_error_for_UNKNOWN_ACCOUNT() throws Exception {
        final Either<AccountsServiceFailures, Stream<Transaction>> findResult = DI.get(AccountsService.class).accountTransactions(UNKNOWN_ACCOUNT);

        assertTrue(findResult.isLeft());
        assertEquals(AccountsServiceFailures.UNKNOWN_ACCOUNT, findResult.left());
    }

    @Test
    public void should_return_no_transactions_for_ACCOUNT_B_1() throws Exception {
        final Either<AccountsServiceFailures, Stream<Transaction>> findResult = DI.get(AccountsService.class).accountTransactions(ACCOUNT_B_1);

        assertTrue(findResult.isRight());
        assertEquals(0, findResult.right().count());
    }

    @Test
    public void should_return_two_transactions_for_ACCOUNT_B_2() throws Exception {
        final Either<AccountsServiceFailures, Stream<Transaction>> findResult = DI.get(AccountsService.class).accountTransactions(ACCOUNT_B_2);

        assertTrue(findResult.isRight());
        assertEquals(2, findResult.right().count());
    }
}