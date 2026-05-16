package com.MatchingGame.manager;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import com.MatchingGame.model.SaveGameData;

public class ViewManager {
    private static ViewManager instance;
    private Stage mainStage;
    private final AppContext appContext;

    private ViewManager(Stage stage) {
        this.mainStage = stage;
        this.appContext = new AppContext();
    }//getter of ViewManager

    public static void init(Stage stage) {
        if (instance == null) {
            instance = new ViewManager(stage);
        }
    }//initial the method, when starts up MainApp

    public static ViewManager getInstance() {
        return instance;
    }//getter of instance
    public AppContext getAppContext() { return appContext; }//getter of the status of the player

    public void goToLogin() {
        switchScene("/fxml/LoginView.fxml");
    }
    public void goToMainMenu() {
        switchScene("/fxml/MainMenuView.fxml");
    }

    public void goToGame(String difficulty) {
        showGame(difficulty, null);
    }

    public void goToGame(SaveGameData saveGameData) {
        showGame(saveGameData.difficulty, saveGameData);
    }

    private void showGame(String difficulty, SaveGameData saveGameData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
            Parent root = loader.load();

            com.MatchingGame.controller.GameController controller = loader.getController();
            if (saveGameData == null) {
                controller.setDifficulty(difficulty);
            } else {
                controller.loadSavedGame(saveGameData);
            }

            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.centerOnScreen();
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToGameOver(int score, String mode, boolean isVictory) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameOverView.fxml"));
            Parent root = loader.load();

            com.MatchingGame.controller.GameOverController controller = loader.getController();
            controller.initData(score, mode, isVictory);
            //pass in score and previous mode(for restart)


            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.centerOnScreen();
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToRegister() {
        switchScene("/fxml/RegisterView.fxml");
    }
    public void goToSaveSlots() {
        switchScene("/fxml/SaveSlotsView.fxml");
    }
    public void goToRanks() { switchScene("/fxml/RanksView.fxml"); }

    public void switchScene(String fxmlPath) {
        // use to load different function pages
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.centerOnScreen(); // put the window in the middle
            mainStage.show();
            //switch the page
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("the page load failed: " + fxmlPath);
        }
    }
}
