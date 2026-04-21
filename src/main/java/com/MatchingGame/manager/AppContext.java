package com.MatchingGame.manager;

import java.util.Optional;

public class AppContext {
    private boolean guest;
    private String username;

    public void loginAsRegisteredUser(String username) {
        this.guest = false;
        this.username = username;
    }

    public void loginAsGuest() {
        this.guest = true;
        this.username = null;
    }

    public void logout() {
        this.guest = false;
        this.username = null;
    }

    public boolean isLoggedIn() {
        return guest || username != null;
    }

    public boolean isGuest() {
        return guest;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }
}

