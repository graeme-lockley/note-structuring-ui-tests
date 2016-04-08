package za.co.no9.app.service;

import za.co.no9.app.aggregate.user.AuditItem;
import za.co.no9.app.aggregate.user.User;
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
        final Optional<User> user = DI.get(Repository.class).findUser(credential.username());

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
        final Optional<User> user = DI.get(Repository.class).findUser(userName);

        if (user.isPresent()) {
            return Either.right(user.get().auditTrail());
        } else {
            return Either.left(UNKNOWN_USER);
        }
    }

    public enum UserServiceFailures {
        INVALID_CREDENTIAL, UNKNOWN_USER
    }
}
