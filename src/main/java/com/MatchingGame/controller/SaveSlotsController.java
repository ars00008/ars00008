package com.MatchingGame.controller;

import com.MatchingGame.manager.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SaveSlotsController {
    @FXML private Label infoLabel;

    @FXML
    public void initialize() {
        var context = ViewManager.getInstance().getAppContext();
        if (!context.isLoggedIn()) {
            ViewManager.getInstance().goToLogin();
            return;
        }
        if (context.isGuest()) {
            infoLabel.setText("Guest mode: save/load is disabled.");
        } else {
            infoLabel.setText("Save slots (placeholder). Later: slot list + Save/Load buttons.");
        }
    }

    @FXML
    public void handleBack() {
        ViewManager.getInstance().goToMainMenu();
    }
}

