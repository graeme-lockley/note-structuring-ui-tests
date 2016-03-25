package za.co.no9.app.service;

import za.co.no9.app.domain.Account;
import za.co.no9.app.domain.User;

import java.util.HashSet;
import java.util.Set;

public class AccountsService {
    public Set<Account> view(User user){
        return new HashSet<>();
    }
}
