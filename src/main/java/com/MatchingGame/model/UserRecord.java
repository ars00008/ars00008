package com.MatchingGame.model;

public class UserRecord {
    public String username;
    public String salt;
    public String passwordHash;
    public long createdAt;

    public UserRecord() {
    }

    public UserRecord(String username, String salt, String passwordHash, long createdAt) {
        this.username = username;
        this.salt = salt;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }
}
