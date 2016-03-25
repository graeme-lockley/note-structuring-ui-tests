package za.co.no9.app.service;

import za.co.no9.app.domain.UserCredential;

public interface CredentialStore {
    boolean accept(UserCredential credential);
}
