package com.MatchingGame.manager;

import com.MatchingGame.model.GameResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankStore {
    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final Path APP_DIR = Paths.get(System.getProperty("user.home"), ".matching-game");
    private static final Path RESULTS_FILE = APP_DIR.resolve("results.json");

    public void addResult(GameResult result) throws IOException {
        ResultDatabase database = readDatabase();
        database.results.add(result);
        Files.createDirectories(APP_DIR);
        MAPPER.writeValue(RESULTS_FILE.toFile(), database);
    }

    public List<GameResult> getTopResults(int limit) throws IOException {
        List<GameResult> results = new ArrayList<>(readDatabase().results);
        results.sort(Comparator.comparingInt((GameResult result) -> result.score).reversed()
                .thenComparing(result -> result.finishedAt, Comparator.reverseOrder()));
        if (results.size() <= limit) {
            return results;
        }
        return new ArrayList<>(results.subList(0, limit));
    }

    private ResultDatabase readDatabase() throws IOException {
        Files.createDirectories(APP_DIR);
        if (!Files.exists(RESULTS_FILE)) {
            return new ResultDatabase();
        }
        ResultDatabase database = MAPPER.readValue(RESULTS_FILE.toFile(), ResultDatabase.class);
        if (database.results == null) {
            database.results = new ArrayList<>();
        }
        return database;
    }

    private static class ResultDatabase {
        public List<GameResult> results = new ArrayList<>();
    }
}
