package za.co.no9.app.aggregate.user;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.aggregate.user.UserService.UserServiceFailure;
import za.co.no9.app.domain.UserCredential;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.domain.UserPassword;
import za.co.no9.app.event.UserAdded;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;
import za.co.no9.app.util.EventStore;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class UserServiceTest {
    private static final UserPassword VALID_USER_CREDENTIAL_PASSWORD = UserPassword.from("password");
    private static final UserPassword INVALID_USER_CREDENTIAL_PASSWORD = UserPassword.from("wrong-password");

    private static final UserCredential UNKNOWN_USER_CREDENTIAL = UserCredential.from(UserName.from("jimmy"), UserPassword.from("bob's your uncle"));
    private static final UserCredential VALID_USER_CREDENTIAL = UserCredential.from(UserName.from("graeme"), VALID_USER_CREDENTIAL_PASSWORD);
    private static final UserCredential INVALID_USER_CREDENTIAL = UserCredential.from(UserName.from("graeme"), INVALID_USER_CREDENTIAL_PASSWORD);

    private UserService userService = new UserService();
    private EventStore eventStore = new EventStore();

    @Before
    public void setup() {
        DI.reset();
        DI.register(eventStore);
        DI.register(userService);

        eventStore.registerEventHandler(userService);

        eventStore.publishEvent(new UserAdded(VALID_USER_CREDENTIAL.username(), VALID_USER_CREDENTIAL_PASSWORD));
    }

    @Test
    public void should_throw_an_exception_when_adding_a_duplicate_user() throws Exception {
        assertEquals(UserServiceFailure.DUPLICATE_USERNAME, userService.addUser(new AddUserCommand(VALID_USER_CREDENTIAL.username(), VALID_USER_CREDENTIAL_PASSWORD)).get());
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

    @Test
    public void given_an_unknown_user_when_requesting_users_audit_trail_should_return_error() throws Exception {
        final Either<UserServiceFailure, Stream<AuditItem>> auditTrial = userService.auditTrial(UNKNOWN_USER_CREDENTIAL.username());

        assertTrue(auditTrial.isLeft());
        assertEquals(UserServiceFailure.UNKNOWN_USER, auditTrial.left());
    }

//    @Test
//    public void given_a_valid_user_when_requesting_audit_trail_should_return_audit_items() throws Exception {
//        final Either<za.co.no9.app.service.UserService.UserServiceFailures, Stream<AuditItem>> auditTrial = DI.get(za.co.no9.app.service.UserService.class).auditTrial(VALID_USER_CREDENTIAL.username());
//
//        assertTrue(auditTrial.isRight());
//        assertEquals(2, auditTrial.right().count());
//    }
}