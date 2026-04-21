package com.MatchingGame.controller;

import javafx.fxml.FXML;
import com.MatchingGame.manager.ViewManager;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            messageLabel.setText("Username/password required.");
            return;
        }

        ViewManager.getInstance().getAppContext().loginAsRegisteredUser(username.trim());
        ViewManager.getInstance().goToMainMenu();
    }

    @FXML
    public void handleGuest() {
        ViewManager.getInstance().getAppContext().loginAsGuest();
        ViewManager.getInstance().goToMainMenu();
    }

    @FXML
    public void handleRegister() {
        ViewManager.getInstance().goToRegister();
    }

    @FXML
    public void handleExit() {
        System.exit(0);
    }//the end
}
