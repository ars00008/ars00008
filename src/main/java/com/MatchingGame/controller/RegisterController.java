package com.MatchingGame.controller;

import com.MatchingGame.manager.UserStore;
import com.MatchingGame.manager.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;
    private final UserStore userStore = new UserStore();

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username == null || username.isBlank()) {
            messageLabel.setText("Username required.");
            return;
        }
        if (!userStore.isValidUsername(username)) {
            messageLabel.setText("Username must be 3-30 characters.");
            return;
        }
        if (password == null || password.isBlank()) {
            messageLabel.setText("Password required.");
            return;
        }
        if (password.length() < 4) {
            messageLabel.setText("Password must be at least 4 characters.");
            return;
        }
        if (!password.equals(confirm)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        try {
            String cleanUsername = userStore.normalizeUsername(username);
            if (!userStore.register(cleanUsername, password)) {
                messageLabel.setText("Username already exists.");
                return;
            }
            ViewManager.getInstance().getAppContext().loginAsRegisteredUser(cleanUsername);
            ViewManager.getInstance().goToMainMenu();
        } catch (IOException e) {
            messageLabel.setText("Cannot save user data.");
        }
    }

    @FXML
    public void handleBackToLogin() {
        ViewManager.getInstance().goToLogin();
    }
}
