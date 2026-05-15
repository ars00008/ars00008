package com.MatchingGame.controller;

import com.MatchingGame.logic.MapGenerator;
import com.MatchingGame.manager.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.MatchingGame.logic.AvailablePairsFounder.findAllAvailablePairs;


public class GameController {

    @FXML private GridPane gameGrid;

    @FXML private Label timeLabel;
    @FXML private Label scoreLabel;
    @FXML private Label pairsLabel;
    @FXML private Label progressLabel;
    @FXML private Label operationLabel;
    //fxml variables

    private CellPos firstSelected = null;
    private StackPane firstSelectedNode = null;
    //for interaction

    private MapGenerator mapGenerator = new MapGenerator();
    private com.MatchingGame.logic.LinkLogic linkLogic = new com.MatchingGame.logic.LinkLogic();
    private int[][] gameMap;//store the map
    private int ROWS ;
    private int COLS ;
    //for map

    private String currentMode = "HARD";
    //for different modes

    private int currentScore = 0;
    private int remainingTime = 60;
    private long lastTimeUpdate = 0;
    private javafx.animation.AnimationTimer gameTimer;
    private int totalPairsEliminated = 0;
    private int totalPairsCount = 0;
    //for timer

    @FXML
    public void initialize() {
        firstSelected = null;
        this.remainingTime = (this.currentMode == "EASY") ? 60 : 150;
        this.currentScore = 0;
        this.totalPairsEliminated = 0;//initialize score related variables

        scoreLabel.setText(String.valueOf(this.currentScore));
        timeLabel.setText(String.valueOf(this.remainingTime));
        progressLabel.setText("0");
        pairsLabel.setText("--");//initialize GUI

        if ("EASY".equals(currentMode)) {
            this.totalPairsCount = (4 * 4 * 2) / 2; // 16
        } else {
            this.totalPairsCount = (10 * 10) / 2; // 50
        }//count pairs by modes

        operationLabel.setText("Loading...");
    }

    public void setDifficulty(String difficulty) {
        this.currentMode = difficulty;
        if ("EASY".equals(difficulty)) {
            this.ROWS = 10;
            this.COLS = 10;
        } else {
            this.ROWS = 10;
            this.COLS = 10;
        }
        startNewGame();
    }

    private void startNewGame() {

        initialize();
        totalPairsCount = "EASY".equals(currentMode) ? 16 : 50;
        scoreLabel.setText(String.valueOf(currentScore));
        pairsLabel.setText(String.valueOf(this.totalPairsCount));
        progressLabel.setText("0%");
        timeLabel.setText(String.valueOf(currentScore));
        //show on labels

        this.gameMap = mapGenerator.generateMapByDifficulty(currentMode);
        renderBoard();
        //draw map

        operationLabel.setText("Mode: " + currentMode);

        startScoreTimer();//start counting
    }

    private void startScoreTimer() {
        if (gameTimer != null) gameTimer.stop();

        lastTimeUpdate = System.nanoTime();

        gameTimer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                // every second minus a point
                if (now - lastTimeUpdate >= 1_000_000_000L) {
                    if (remainingTime > 0) {

                        remainingTime--;
                        currentScore = totalPairsEliminated*5 + remainingTime/3;

                        if (remainingTime < 20) {
                            timeLabel.setTextFill(javafx.scene.paint.Color.RED);//score less than 20, emergency
                        }else {
                            timeLabel.setTextFill(javafx.scene.paint.Color.BLACK);
                        }
                        scoreLabel.setText(String.valueOf(currentScore));
                        timeLabel.setText(remainingTime + "s");
                    }else{
                        gameTimer.stop();
                        timeLabel.setText("TIME UP!");
                        handleGameFailure();//time is up!
                    }
                    lastTimeUpdate = now;
                }
            }
        };
        gameTimer.start();
    }

    private void renderBoard() {

        gameGrid.getChildren().clear();

        double cellSize = 40.0;

        for (int r = 1; r <= ROWS; r++) {
            for (int c = 1; c <= COLS; c++) {
                int type = gameMap[r][c];

                StackPane cellWrapper = new StackPane();
                cellWrapper.setPrefSize(cellSize, cellSize);
                cellWrapper.setMinSize(cellSize, cellSize);
                cellWrapper.setMaxSize(cellSize, cellSize);//no matter empty or not, filled with a new cell object

                if (type > 0) {//not empty
                    var tile = new Rectangle(cellSize - 4, cellSize - 4, colorForType(type));
                    tile.setArcWidth(10);
                    tile.setArcHeight(10);

                    cellWrapper.getStyleClass().add("game-cell-active");
                    cellWrapper.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-cursor: hand;");
                    cellWrapper.getChildren().add(tile);

                    final int finalR = r;
                    final int finalC = c;
                    cellWrapper.setOnMouseClicked(event -> handleCellClick(new CellPos(finalR, finalC), cellWrapper));
                }else {//empty
                    cellWrapper.setStyle("-fx-background-color: transparent;");
                    cellWrapper.setMouseTransparent(true);
                }

                gameGrid.add(cellWrapper, c - 1, r - 1);
            }
        }
    }

    private boolean checkIfGameFinished() {
        for (int r = 1; r <= ROWS; r++) {
            for (int c = 1; c <= COLS; c++) {
                if (gameMap[r][c] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void handleCellClick(CellPos pos, StackPane wrapper) {

        if (firstSelected == null) {
            //first select mustn't be empty
            if (gameMap[pos.row][pos.col] == 0) return;

            firstSelected = pos;
            firstSelectedNode = wrapper;
            applySelectionEffect(wrapper, true);
            operationLabel.setText("Selected: (" + pos.row + ", " + pos.col + ")");

        } else {

            if (firstSelected.equals(pos)) {
                //same position, cancel selection
                applySelectionEffect(wrapper, false);
                firstSelected = null;
                firstSelectedNode = null;
                operationLabel.setText("Selection cleared.");

            } else {
                operationLabel.setText("Picked second: (" + pos.row + ", " + pos.col + ")");

                int type1 = gameMap[firstSelected.row][firstSelected.col];
                int type2 = gameMap[pos.row][pos.col];

                var p1 = new com.MatchingGame.logic.GridPoint(firstSelected.col, firstSelected.row);
                var p2 = new com.MatchingGame.logic.GridPoint(pos.col, pos.row);

                if (type1 == type2 && linkLogic.judge(p1, p2, gameMap)) {//color, route, match found

                    gameMap[firstSelected.row][firstSelected.col] = 0;
                    gameMap[pos.row][pos.col] = 0;
                    //erase

                    currentScore += 5;
                    totalPairsEliminated++;
                    //updateScore
                    double progress = (double) totalPairsEliminated / totalPairsCount * 100;
                    //updateProgress
                    scoreLabel.setText(String.valueOf(currentScore));
                    pairsLabel.setText(String.valueOf(totalPairsCount - totalPairsEliminated));
                    progressLabel.setText(String.format("%.1f%%", progress));
                    //update GUI

                    if (!checkIfGameFinished()) {
                        autoShuffleIfDeadEnd();
                    } else{//game end
                        gameTimer.stop();
                        handleGameVictory();
                    }//gameover

                    renderBoard();
                    //repaint

                    operationLabel.setText("Match found!");

                } else {//not found
                    operationLabel.setText("No path or different types!");
                    applySelectionEffect(firstSelectedNode, false);
                }
                firstSelected = null;
                firstSelectedNode = null;//reset the select point
            }
        }
    }

    private void autoShuffleIfDeadEnd() {
        int retryLimit = 20;
        while (findAllAvailablePairs(gameMap).isEmpty() && !checkIfGameFinished() && retryLimit > 0) {
            System.out.println("Detect a dead end, shuffling...");
            mapGenerator.shuffleSpecificMode(gameMap);
            retryLimit--;
        }//prevent dead lock
        if (retryLimit == 0) {
            System.err.println("could not found a valid solution, please restart");
        }
    }

    private void applySelectionEffect(StackPane wrapper, boolean selected) {
        if (selected) {
            wrapper.setStyle("-fx-border-color: #E53935; -fx-border-width: 3; -fx-border-radius: 6;");
        } else {
            wrapper.setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
        }
    }

    private Color colorForType(int type) {
        return switch (type) {
            case 1 -> Color.RED;
            case 2 -> Color.BLUE;
            case 3 -> Color.GREEN;
            case 4 -> Color.ORANGE;
            case 5 -> Color.PURPLE;
            case 6 -> Color.BROWN;
            case 7 -> Color.PINK;
            case 8 -> Color.CYAN;
            case 9 -> Color.DARKGOLDENROD;
            case 10 -> Color.MAGENTA;
            case 11 -> Color.LIME;
            case 12 -> Color.TEAL;
            default -> Color.GRAY;
        };
    }

    @FXML
    public void handleRestart() {
        if (gameTimer != null) gameTimer.stop();
        firstSelected = null;
        firstSelectedNode = null;
        operationLabel.setText("New perfect map generated!");
        startNewGame();
    }

    @FXML
    public void handleBackToMenu() {
        if (gameTimer != null) gameTimer.stop();
        ViewManager.getInstance().goToMainMenu();
    }

    @FXML
    private void handleGameFailure() {
        if (gameTimer != null) gameTimer.stop();
        gameGrid.setDisable(true);
        operationLabel.setText("Time is up, game over!");
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            ViewManager.getInstance().goToGameOver(currentScore, currentMode,false);
        });
        pause.play();
    }

    @FXML
    private void handleGameVictory() {
        operationLabel.setText("Congratulates...");
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        pause.setOnFinished(event -> {
            ViewManager.getInstance().goToGameOver(currentScore, currentMode, true);
        });
        pause.play();
    }

    private static final class CellPos {
        final int row;
        final int col;

        private CellPos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof CellPos that)) return false;
            return row == that.row && col == that.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }
}
