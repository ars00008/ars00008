package com.MatchingGame;

import com.MatchingGame.manager.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // initialize view manager and parce it through the main stage
        ViewManager.init(primaryStage);
        
        // then set the title of the game
        primaryStage.setTitle("素晴らしい Matching Game");
        
        // enter the login page
        ViewManager.getInstance().goToLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}