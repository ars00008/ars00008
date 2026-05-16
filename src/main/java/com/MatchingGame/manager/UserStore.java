package com.MatchingGame.manager;

import com.MatchingGame.model.UserRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UserStore {
    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Path APP_DIR = Paths.get(System.getProperty("user.home"), ".matching-game");
    private static final Path USERS_FILE = APP_DIR.resolve("users.json");

    public boolean register(String username, String password) throws IOException {
        String cleanUsername = normalizeUsername(username);
        UserDatabase database = readDatabase();

        if (database.users.containsKey(cleanUsername)) {
            return false;
        }

        String salt = createSalt();
        database.users.put(cleanUsername, new UserRecord(
                cleanUsername,
                salt,
                hashPassword(password, salt),
                System.currentTimeMillis()
        ));
        writeDatabase(database);
        return true;
    }

    public boolean authenticate(String username, String password) throws IOException {
        String cleanUsername = normalizeUsername(username);
        UserRecord user = readDatabase().users.get(cleanUsername);
        if (user == null) {
            return false;
        }
        return user.passwordHash.equals(hashPassword(password, user.salt));
    }

    public String normalizeUsername(String username) {
        return username == null ? "" : username.trim();
    }

    public boolean isValidUsername(String username) {
        String cleanUsername = normalizeUsername(username);
        return cleanUsername.length() >= 3 && cleanUsername.length() <= 30;
    }

    private UserDatabase readDatabase() throws IOException {
        Files.createDirectories(APP_DIR);
        if (!Files.exists(USERS_FILE)) {
            return new UserDatabase();
        }
        UserDatabase database = MAPPER.readValue(USERS_FILE.toFile(), UserDatabase.class);
        if (database.users == null) {
            database.users = new HashMap<>();
        }
        return database;
    }

    private void writeDatabase(UserDatabase database) throws IOException {
        Files.createDirectories(APP_DIR);
        MAPPER.writeValue(USERS_FILE.toFile(), database);
    }

    private String createSalt() {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((salt + ":" + password).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available.", e);
        }
    }

    private static class UserDatabase {
        public Map<String, UserRecord> users = new HashMap<>();
    }
}
