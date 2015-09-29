package com.pirate.account;

/**
 * Created by simjisung on 15. 9. 16..
 */
public class UserDuplicatedException extends RuntimeException {
    String username;

    public UserDuplicatedException(String username) {
        this.username = username;
    }

    public String getUserName() {
        return username;
    }
}
