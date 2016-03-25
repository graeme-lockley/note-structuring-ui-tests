package za.co.no9.app.service;

import org.junit.Test;
import za.co.no9.app.domain.User;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.util.DI;

public class AccountsServiceTest {
    @Test
    public void should_return_no_accounts_if_the_user_is_unknown() throws Exception {
        final User user = User.from(UserName.from("graeme"));
        DI.get(AccountsService.class).view(user);
    }
}