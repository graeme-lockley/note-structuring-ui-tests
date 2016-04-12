package za.co.no9.app.aggregate.user;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.aggregate.user.UserService.UserServiceFailure;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.domain.UserPassword;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.EventStore;

import java.util.Optional;

import static org.junit.Assert.*;

public class UserServiceTest {
    private static final UserName VALID_USER_NAME = UserName.from("graeme");
    private static final UserPassword VALID_USER_PASSWORD = UserPassword.from("password");
    private static final UserPassword INVALID_USER_PASSWORD = UserPassword.from("wrong-password");

    private static final UserCredential UNKNOWN_USER_CREDENTIAL = UserCredential.from(UserName.from("jimmy"), UserPassword.from("bob's your uncle"));
    private static final UserCredential VALID_USER_CREDENTIAL = UserCredential.from(VALID_USER_NAME, VALID_USER_PASSWORD);
    private static final UserCredential INVALID_USER_CREDENTIAL = UserCredential.from(UserName.from("graeme"), INVALID_USER_PASSWORD);

    private UserService userService = new UserService();
    private EventStore eventStore = new EventStore();

    @Before
    public void setup() {
        DI.reset();
        DI.register(eventStore);
        DI.register(userService);

        eventStore.registerEventHandler(userService);

        userService.addUser(new AddUserCommand(VALID_USER_CREDENTIAL.username(), VALID_USER_PASSWORD));
    }

    @Test
    public void should_throw_an_exception_when_adding_a_duplicate_user() throws Exception {
        assertEquals(UserServiceFailure.DUPLICATE_USERNAME, userService.addUser(new AddUserCommand(VALID_USER_CREDENTIAL.username(), VALID_USER_PASSWORD)).get());
    }

    @Test
    public void given_a_known_user_should_login() throws Exception {
        Optional<UserServiceFailure> loginResult = userService.login(VALID_USER_CREDENTIAL);

        assertFalse(loginResult.isPresent());
    }

    @Test
    public void given_an_invalid_user_should_not_login() throws Exception {
        Optional<UserServiceFailure> loginResult = userService.login(UNKNOWN_USER_CREDENTIAL);

        assertTrue(loginResult.isPresent());
        assertEquals(UserServiceFailure.UNKNOWN_USER, loginResult.get());
    }

    @Test
    public void should_not_login_if_the_users_password_is_invalid() throws Exception {
        Optional<UserServiceFailure> loginResult = userService.login(INVALID_USER_CREDENTIAL);

        assertTrue(loginResult.isPresent());
        assertEquals(UserServiceFailure.INVALID_CREDENTIAL, loginResult.get());
    }
}