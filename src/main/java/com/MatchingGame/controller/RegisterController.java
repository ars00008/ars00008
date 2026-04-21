package com.MatchingGame.controller;

import com.MatchingGame.manager.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username == null || username.isBlank()) {
            messageLabel.setText("Username required.");
            return;
        }
        if (password == null || password.isBlank()) {
            messageLabel.setText("Password required.");
            return;
        }
        if (!password.equals(confirm)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        // Placeholder: later persist to local storage (Task 2.2).
        ViewManager.getInstance().getAppContext().loginAsRegisteredUser(username.trim());
        ViewManager.getInstance().goToMainMenu();
    }

    @FXML
    public void handleBackToLogin() {
        ViewManager.getInstance().goToLogin();
    }
}

