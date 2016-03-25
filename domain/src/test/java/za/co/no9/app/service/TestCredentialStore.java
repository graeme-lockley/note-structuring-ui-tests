package za.co.no9.app.service;

import za.co.no9.app.domain.UserCredential;
import za.co.no9.app.domain.UserName;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TestCredentialStore implements CredentialStore {
    private final Set<UserCredential> credentials;

    public TestCredentialStore(Set<UserCredential> credentials) {
        this.credentials = credentials;
    }

    @Override
    public boolean accept(UserCredential credential) {
        final Optional<UserCredential> userCredential = find(credential.username());

        return userCredential.isPresent() && userCredential.get().acceptCredential(credential);
    }

    private Optional<UserCredential> find(UserName name) {
        return credentials.stream().filter(c -> c.username().equals(name)).findFirst();
    }

    public static TestCredentialStoreBuilder builder() {
        return new TestCredentialStoreBuilder();
    }

    public static class TestCredentialStoreBuilder {
        private Set<UserCredential> credentials = new HashSet<>();

        public TestCredentialStoreBuilder addCredential(UserCredential credential) {
            credentials.add(credential);
            return this;
        }

        public CredentialStore build() {
            return new TestCredentialStore(credentials);
        }
    }
}
