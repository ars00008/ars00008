package com.MatchingGame.controller;

import com.MatchingGame.manager.UserStore;
import javafx.fxml.FXML;
import com.MatchingGame.manager.ViewManager;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    private final UserStore userStore = new UserStore();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            messageLabel.setText("Username/password required.");
            return;
        }

        try {
            String cleanUsername = userStore.normalizeUsername(username);
            if (userStore.authenticate(cleanUsername, password)) {
                ViewManager.getInstance().getAppContext().loginAsRegisteredUser(cleanUsername);
                ViewManager.getInstance().goToMainMenu();
            } else {
                messageLabel.setText("Wrong username or password.");
            }
        } catch (IOException e) {
            messageLabel.setText("Cannot read user data.");
        }
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
