package com.MatchingGame.controller;

import com.MatchingGame.manager.ViewManager;
import javafx.fxml.FXML;

public class RanksController {
    @FXML
    public void initialize() {
        var context = ViewManager.getInstance().getAppContext();
        if (!context.isLoggedIn()) {
            ViewManager.getInstance().goToLogin();
        }
    }

    @FXML
    public void handleBack() {
        ViewManager.getInstance().goToMainMenu();
    }
}

