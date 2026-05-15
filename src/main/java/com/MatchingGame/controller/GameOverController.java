package com.MatchingGame.controller;

import com.MatchingGame.manager.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameOverController {

    @FXML private Label finalScoreLabel;
    @FXML private Label titleLabel;

    private String currentMode = "";
    private boolean currentVictory = false;

    public void initData(int score, String mode, boolean isVictory) {
        finalScoreLabel.setText(String.valueOf(score));
        currentMode = mode;
        currentVictory = isVictory;

        if (currentVictory) {
            titleLabel.setText("success！");
            titleLabel.setTextFill(javafx.scene.paint.Color.web("#2E7D32")); // 绿色
        } else {
            titleLabel.setText("fail...");
            titleLabel.setTextFill(javafx.scene.paint.Color.web("#D32F2F")); // 红色
        }
    }

    @FXML
    public void handleRestart() {//back to game, default the previous mode
        ViewManager.getInstance().goToGame(currentMode);
    }

    @FXML
    public void handleBackToMenu() {
        ViewManager.getInstance().goToMainMenu();
    }
}