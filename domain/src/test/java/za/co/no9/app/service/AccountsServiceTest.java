package za.co.no9.app.service;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.domain.*;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountsServiceTest {
    public static final UserName USER_A = UserName.from("graeme");
    public static final UserName USER_B = UserName.from("mary-anne");
    public static final UserName UNKNOWN_USER = UserName.from("joe-joe");

    @Before
    public void before() {
        DI.reset();
        DI.register(TestRepository.builder()
                .addUser(User.from(USER_A))
                .addUser(User.from(USER_B))
                .addAccount(USER_B, Account.from(AccountRef.from("12345"), Money.from(123.45), AccountName.from("Credit Card")))
                .addAccount(USER_B, Account.from(AccountRef.from("12346"), Money.from(543.21), AccountName.from("Card Finance")))
                .build());
        DI.register(new AccountsService());
    }

    @Test
    public void should_return_no_accounts_if_the_user_has_no_accounts() throws Exception {
        final Either<AccountsService.AccountsServiceFailures, Stream<Account>> view = DI.get(AccountsService.class).view(USER_A);

        assertTrue(view.isRight());
        assertEquals(0, view.right().count());
    }

    @Test
    public void should_return_two_accounts_if_the_user_is_known() throws Exception {
        final Either<AccountsService.AccountsServiceFailures, Stream<Account>> view = DI.get(AccountsService.class).view(USER_B);

        assertTrue(view.isRight());
        assertEquals(2, view.right().count());
    }

    @Test
    public void should_return_an_error_if_user_is_unknown() throws Exception {
        final Either<AccountsService.AccountsServiceFailures, Stream<Account>> view = DI.get(AccountsService.class).view(UNKNOWN_USER);

        assertTrue(view.isLeft());
        assertEquals(AccountsService.AccountsServiceFailures.UNKNOWN_USER, view.left());
    }
}