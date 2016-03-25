package za.co.no9.app.service;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.domain.User;
import za.co.no9.app.domain.UserCredential;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.domain.UserPassword;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginServiceTest {
    private static final UserPassword VALID_USER_CREDENTIAL_PASSWORD = UserPassword.from("password");
    private static final UserPassword INVALID_USER_CREDENTIAL_PASSWORD = UserPassword.from("wrong-password");

    private static final UserCredential UNKNOWN_USER_CREDENTIAL = UserCredential.from(UserName.from("jimmy"), UserPassword.from("bob's your uncle"));
    private static final UserCredential VALID_USER_CREDENTIAL = UserCredential.from(UserName.from("graeme"), VALID_USER_CREDENTIAL_PASSWORD);
    private static final UserCredential INVALID_USER_CREDENTIAL = UserCredential.from(UserName.from("graeme"), INVALID_USER_CREDENTIAL_PASSWORD);

    @Before
    public void before() {
        DI.reset();
        DI.register(TestRepository.builder()
                .addUser(User.from(VALID_USER_CREDENTIAL.username()))
                .build());
        DI.register(TestCredentialStore.builder()
                .addCredential(VALID_USER_CREDENTIAL)
                .build());
        DI.register(new LoginService());
    }

    @Test
    public void should_login_if_a_user_does_exists() throws Exception {
        Either<LoginService.LoginReasonFailures, User> loginResult = DI.get(LoginService.class).login(VALID_USER_CREDENTIAL);

        assertTrue(loginResult.isRight());
        assertTrue(loginResult.right().acceptCredential(VALID_USER_CREDENTIAL));
    }

    @Test
    public void should_not_login_if_a_user_does_not_exist() throws Exception {
        Either<LoginService.LoginReasonFailures, User> loginResult = DI.get(LoginService.class).login(UNKNOWN_USER_CREDENTIAL);

        assertTrue(loginResult.isLeft());
        assertEquals(LoginService.LoginReasonFailures.UNKNOWN_USER, loginResult.left());
    }

    @Test
    public void should_not_login_if_the_users_password_is_invalid() throws Exception {
        Either<LoginService.LoginReasonFailures, User> loginResult = DI.get(LoginService.class).login(INVALID_USER_CREDENTIAL);

        assertTrue(loginResult.isLeft());
        assertEquals(LoginService.LoginReasonFailures.INVALID_CREDENTIAL, loginResult.left());
    }
}