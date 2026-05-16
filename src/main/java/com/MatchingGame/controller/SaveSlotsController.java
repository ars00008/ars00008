package com.MatchingGame.controller;

import com.MatchingGame.manager.InvalidSaveException;
import com.MatchingGame.manager.SaveStore;
import com.MatchingGame.manager.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SaveSlotsController {
    @FXML private Label infoLabel;
    @FXML private Label slotLabel;
    @FXML private Button loadButton;

    private final SaveStore saveStore = new SaveStore();

    @FXML
    public void initialize() {
        var context = ViewManager.getInstance().getAppContext();
        if (!context.isLoggedIn()) {
            ViewManager.getInstance().goToLogin();
            return;
        }
        if (context.isGuest()) {
            infoLabel.setText("Guest mode: save/load is disabled.");
            slotLabel.setText("Slot 1: Unavailable");
            loadButton.setDisable(true);
        } else {
            String username = context.getUsername().orElse("");
            infoLabel.setText("Only your own save slot can be loaded.");
            slotLabel.setText(saveStore.describeSlot(username));
            loadButton.setDisable(!saveStore.hasSave(username));
        }
    }

    @FXML
    public void handleLoad() {
        String username = ViewManager.getInstance().getAppContext().getUsername().orElse("");
        try {
            ViewManager.getInstance().goToGame(saveStore.load(username));
        } catch (InvalidSaveException e) {
            showInvalidSaveAlert();
            ViewManager.getInstance().goToMainMenu();
        }
    }

    @FXML
    public void handleBack() {
        ViewManager.getInstance().goToMainMenu();
    }

    private void showInvalidSaveAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("存档无效");
        alert.showAndWait();
    }
}
