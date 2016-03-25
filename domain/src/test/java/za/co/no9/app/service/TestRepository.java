package za.co.no9.app.service;

import za.co.no9.app.domain.User;
import za.co.no9.app.domain.UserName;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TestRepository implements Repository {
    private final Set<User> users;

    private TestRepository(Set<User> users) {
        this.users = users;
    }

    public static TestRepositoryBuilder builder() {
        return new TestRepositoryBuilder();
    }

    @Override
    public Optional<User> findUser(UserName username) {
        return users.stream().filter(u -> u.name().equals(username)).findFirst();
    }

    public static class TestRepositoryBuilder {
        private Set<User> users = new HashSet<>();

        public TestRepositoryBuilder addUser(User user) {
            users.add(user);
            return this;
        }

        public Repository build() {
            return new TestRepository(users);
        }
    }
}
