package za.co.no9.app.service;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.domain.*;
import za.co.no9.app.service.AccountService.AccountServiceFailures;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountServiceTest {
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
        DI.register(new AccountService());
    }

    @Test
    public void given_a_user_with_no_accounts_should_return_no_accounts() throws Exception {
        final Either<AccountServiceFailures, Stream<Account>> view = DI.get(AccountService.class).view(USER_A);

        assertTrue(view.isRight());
        assertEquals(0, view.right().count());
    }

    @Test
    public void given_a_known_user_should_return_two_accounts() throws Exception {
        final Either<AccountServiceFailures, Stream<Account>> view = DI.get(AccountService.class).view(USER_B);

        assertTrue(view.isRight());
        assertEquals(2, view.right().count());
    }

    @Test
    public void given_an_unknown_user_should_return_an_error() throws Exception {
        final Either<AccountServiceFailures, Stream<Account>> view = DI.get(AccountService.class).view(UNKNOWN_USER);

        assertTrue(view.isLeft());
        assertEquals(AccountServiceFailures.UNKNOWN_USER, view.left());
    }

    @Test
    public void given_an_unknown_account_should_return_an_error() throws Exception {
        final Either<AccountServiceFailures, Stream<Transaction>> findResult = DI.get(AccountService.class).accountTransactions(UNKNOWN_ACCOUNT);

        assertTrue(findResult.isLeft());
        assertEquals(AccountServiceFailures.UNKNOWN_ACCOUNT, findResult.left());
    }

    @Test
    public void given_an_account_with_no_transactions_should_return_no_transactions() throws Exception {
        final Either<AccountServiceFailures, Stream<Transaction>> findResult = DI.get(AccountService.class).accountTransactions(ACCOUNT_B_1);

        assertTrue(findResult.isRight());
        assertEquals(0, findResult.right().count());
    }

    @Test
    public void given_an_account_with_trasctions_should_return_these_transactions() throws Exception {
        final Either<AccountServiceFailures, Stream<Transaction>> findResult = DI.get(AccountService.class).accountTransactions(ACCOUNT_B_2);

        assertTrue(findResult.isRight());
        assertEquals(2, findResult.right().count());
    }
}