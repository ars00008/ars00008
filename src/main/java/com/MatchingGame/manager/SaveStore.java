package com.MatchingGame.manager;

import com.MatchingGame.model.SaveGameData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class SaveStore {
    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final Path APP_DIR = Paths.get(System.getProperty("user.home"), ".matching-game");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault());

    public void save(SaveGameData data) throws IOException, InvalidSaveException {
        if (data == null) {
            throw new InvalidSaveException("Missing save data.");
        }
        validate(data, data.username);
        Path slotFile = getSlotFile(data.username);
        Files.createDirectories(slotFile.getParent());
        MAPPER.writeValue(slotFile.toFile(), data);
    }

    public SaveGameData load(String username) throws InvalidSaveException {
        Path slotFile = getSlotFile(username);
        if (!Files.exists(slotFile)) {
            throw new InvalidSaveException("Save slot is empty.");
        }

        try {
            SaveGameData data = MAPPER.readValue(slotFile.toFile(), SaveGameData.class);
            validate(data, username);
            data.gameMap = copyMap(data.gameMap);
            return data;
        } catch (IOException | RuntimeException e) {
            throw new InvalidSaveException("Save file is invalid.", e);
        }
    }

    public boolean hasSave(String username) {
        return Files.exists(getSlotFile(username));
    }

    public String describeSlot(String username) {
        if (!hasSave(username)) {
            return "Slot 1: Empty";
        }

        try {
            SaveGameData data = load(username);
            return "Slot 1: " + data.difficulty + " | Score " + data.currentScore
                    + " | Time " + data.remainingTime + "s | " + FORMATTER.format(Instant.ofEpochMilli(data.savedAt));
        } catch (InvalidSaveException e) {
            return "Slot 1: Invalid save";
        }
    }

    private Path getSlotFile(String username) {
        return APP_DIR.resolve("saves").resolve(encodeUsername(username)).resolve("slot1.json");
    }

    private String encodeUsername(String username) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(username.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    private void validate(SaveGameData data, String expectedUsername) throws InvalidSaveException {
        if (data == null || expectedUsername == null || expectedUsername.isBlank()) {
            throw new InvalidSaveException("Missing save data.");
        }
        if (!expectedUsername.equals(data.username)) {
            throw new InvalidSaveException("Save owner does not match current user.");
        }
        if (!"EASY".equals(data.difficulty) && !"HARD".equals(data.difficulty)) {
            throw new InvalidSaveException("Unknown difficulty.");
        }
        if (data.rows <= 0 || data.cols <= 0 || data.gameMap == null) {
            throw new InvalidSaveException("Missing board data.");
        }
        if (data.gameMap.length != data.rows + 2) {
            throw new InvalidSaveException("Board row count is wrong.");
        }
        for (int[] row : data.gameMap) {
            if (row == null || row.length != data.cols + 2) {
                throw new InvalidSaveException("Board column count is wrong.");
            }
            for (int value : row) {
                if (value < 0 || value > 12) {
                    throw new InvalidSaveException("Board value is out of range.");
                }
            }
        }
        if (data.currentScore < 0 || data.remainingTime < 0 || data.totalPairsEliminated < 0
                || data.totalPairsCount <= 0 || data.totalPairsEliminated > data.totalPairsCount) {
            throw new InvalidSaveException("Score or progress is invalid.");
        }
        int remainingTiles = 0;
        for (int r = 1; r <= data.rows; r++) {
            for (int c = 1; c <= data.cols; c++) {
                if (data.gameMap[r][c] != 0) {
                    remainingTiles++;
                }
            }
        }
        if (remainingTiles % 2 != 0 || data.totalPairsEliminated + remainingTiles / 2 != data.totalPairsCount) {
            throw new InvalidSaveException("Progress does not match board data.");
        }
    }

    private int[][] copyMap(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new int[original[i].length];
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }
}
