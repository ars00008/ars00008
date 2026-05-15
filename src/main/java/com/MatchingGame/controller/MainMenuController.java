package com.MatchingGame.controller;

import com.MatchingGame.manager.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainMenuController {
    @FXML private Label userLabel;
    @FXML private Button saveSlotsButton;

    @FXML
    public void initialize() {
        var context = ViewManager.getInstance().getAppContext();
        if (!context.isLoggedIn()) {
            ViewManager.getInstance().goToLogin();
            return;
        }

        userLabel.setText(context.isGuest() ? "Guest" : context.getUsername().orElse("User"));
        saveSlotsButton.setDisable(context.isGuest());
    }

    @FXML
    public void handleStartEasy() {
        ViewManager.getInstance().goToGame("EASY");
    }

    @FXML
    public void handleStartHard() {
        ViewManager.getInstance().goToGame("HARD");
    }

    @FXML
    public void handleOpenSaveSlots() {
        ViewManager.getInstance().goToSaveSlots();
    }

    @FXML
    public void handleOpenRanks() {
        ViewManager.getInstance().goToRanks();
    }

    @FXML
    public void handleLogout() {
        ViewManager.getInstance().getAppContext().logout();
        ViewManager.getInstance().goToLogin();
    }

    @FXML
    public void handleExit() {
        System.exit(0);
    }
}
