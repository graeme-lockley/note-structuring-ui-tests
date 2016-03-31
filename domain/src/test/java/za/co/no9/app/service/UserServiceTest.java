package za.co.no9.app.service;

import org.junit.Before;
import org.junit.Test;
import za.co.no9.app.domain.*;
import za.co.no9.app.service.UserService.UserServiceFailures;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserServiceTest {
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
                .addAuditTrail(VALID_USER_CREDENTIAL.username(),
                        AuditItem.from(Date.from(Instant.parse("2016-01-01T10:15:30.00Z")), TransactionRef.from("A123"), TransactionDescription.from("Tranaction 123"), Money.from(123.45), AccountRef.from("FromAc"), AccountRef.from("ToAccount")),
                        AuditItem.from(Date.from(Instant.parse("2016-01-02T10:15:30.00Z")), TransactionRef.from("A124"), TransactionDescription.from("Tranaction 124"), Money.from(123.46), AccountRef.from("FromAc"), AccountRef.from("ToAccount")))
                .build());
        DI.register(TestCredentialStore.builder()
                .addCredential(VALID_USER_CREDENTIAL)
                .build());
        DI.register(new UserService());
    }

    @Test
    public void should_login_if_a_user_does_exists() throws Exception {
        Either<UserServiceFailures, User> loginResult = DI.get(UserService.class).login(VALID_USER_CREDENTIAL);

        assertTrue(loginResult.isRight());
        assertTrue(loginResult.right().acceptCredential(VALID_USER_CREDENTIAL));
    }

    @Test
    public void should_not_login_if_a_user_does_not_exist() throws Exception {
        Either<UserServiceFailures, User> loginResult = DI.get(UserService.class).login(UNKNOWN_USER_CREDENTIAL);

        assertTrue(loginResult.isLeft());
        assertEquals(UserServiceFailures.UNKNOWN_USER, loginResult.left());
    }

    @Test
    public void should_not_login_if_the_users_password_is_invalid() throws Exception {
        Either<UserServiceFailures, User> loginResult = DI.get(UserService.class).login(INVALID_USER_CREDENTIAL);

        assertTrue(loginResult.isLeft());
        assertEquals(UserServiceFailures.INVALID_CREDENTIAL, loginResult.left());
    }

    @Test
    public void should_return_error_if_unknown_user_is_passed_to_audit_trail() throws Exception {
        final Either<UserServiceFailures, Stream<AuditItem>> auditTrial = DI.get(UserService.class).auditTrial(UNKNOWN_USER_CREDENTIAL.username());

        assertTrue(auditTrial.isLeft());
        assertEquals(UserServiceFailures.UNKNOWN_USER, auditTrial.left());
    }

    @Test
    public void should_return_audit_items_if_valid_user_is_passed() throws Exception {
        final Either<UserServiceFailures, Stream<AuditItem>> auditTrial = DI.get(UserService.class).auditTrial(VALID_USER_CREDENTIAL.username());

        assertTrue(auditTrial.isRight());
        assertEquals(2, auditTrial.right().count());
    }
}