package za.co.no9.app.service;

import za.co.no9.app.domain.AuditItem;
import za.co.no9.app.domain.User;
import za.co.no9.app.domain.UserCredential;
import za.co.no9.app.domain.UserName;
import za.co.no9.app.util.DI;
import za.co.no9.app.util.Either;

import java.util.Optional;
import java.util.stream.Stream;

import static za.co.no9.app.service.UserService.UserServiceFailures.INVALID_CREDENTIAL;
import static za.co.no9.app.service.UserService.UserServiceFailures.UNKNOWN_USER;

public class UserService {
    public Either<UserServiceFailures, User> login(UserCredential credential) {
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

    public Either<UserServiceFailures, Stream<AuditItem>> auditTrial(UserName userName) {
        return Either.left(UNKNOWN_USER);
    }

    public enum UserServiceFailures {
        INVALID_CREDENTIAL, UNKNOWN_USER
    }
}
