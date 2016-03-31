package za.co.no9.app.service;

import za.co.no9.app.domain.User;
import za.co.no9.app.domain.UserCredential;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.util.Optional;

import static za.co.no9.app.service.UserService.LoginReasonFailures.INVALID_CREDENTIAL;
import static za.co.no9.app.service.UserService.LoginReasonFailures.UNKNOWN_USER;

public class UserService {
    public Either<LoginReasonFailures, User> login(UserCredential credential) {
        Repository repository = DI.get(Repository.class);

        final Optional<User> user = repository.findUser(credential.username());

        if (user.isPresent()) {
            if (user.get().acceptCredential(credential)) {
                return Either.right(user.get());
            } else {
                return Either.left(INVALID_CREDENTIAL);
            }
        } else {
            return Either.left(UNKNOWN_USER);
        }
    }

    public enum LoginReasonFailures {
        INVALID_CREDENTIAL, UNKNOWN_USER
    }
}
