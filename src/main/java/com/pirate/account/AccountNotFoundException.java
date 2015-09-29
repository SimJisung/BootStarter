package com.pirate.account;

/**
 * Created by simjisung on 15. 9. 16..
 */
public class AccountNotFoundException extends RuntimeException {
    Long id;

    public AccountNotFoundException(Long id) {
        this.id = id;
    }

    Long getId() {
        return id;
    }
}
