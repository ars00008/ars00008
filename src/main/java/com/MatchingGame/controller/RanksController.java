package com.MatchingGame.controller;

import com.MatchingGame.manager.RankStore;
import com.MatchingGame.manager.ViewManager;
import com.MatchingGame.model.GameResult;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RanksController {
    @FXML private Label summaryLabel;
    @FXML private VBox resultsBox;

    private final RankStore rankStore = new RankStore();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault());

    @FXML
    public void initialize() {
        var context = ViewManager.getInstance().getAppContext();
        if (!context.isLoggedIn()) {
            ViewManager.getInstance().goToLogin();
            return;
        }

        resultsBox.getChildren().clear();
        try {
            var results = rankStore.getTopResults(10);
            if (results.isEmpty()) {
                summaryLabel.setText("No registered-user results yet.");
                resultsBox.getChildren().add(new Label("Finish a game after logging in to create a record."));
                return;
            }
            summaryLabel.setText("Top registered-user results");
            for (int i = 0; i < results.size(); i++) {
                resultsBox.getChildren().add(new Label(formatResult(i + 1, results.get(i))));
            }
        } catch (IOException e) {
            summaryLabel.setText("Cannot read rank data.");
        }
    }

    @FXML
    public void handleBack() {
        ViewManager.getInstance().goToMainMenu();
    }

    private String formatResult(int rank, GameResult result) {
        String status = result.victory ? "Win" : "Lose";
        return rank + ". " + result.username + " | " + result.score + " | "
                + result.mode + " | " + status + " | " + formatter.format(Instant.ofEpochMilli(result.finishedAt));
    }
}
