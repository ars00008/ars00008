package com.MatchingGame.controller;

import com.MatchingGame.manager.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameController {
    @FXML
    private GridPane gameGrid;

    @FXML private Label timeLabel;
    @FXML private Label scoreLabel;
    @FXML private Label pairsLabel;
    @FXML private Label progressLabel;
    @FXML private Label operationLabel;

    private CellPos firstSelected = null;
    private StackPane firstSelectedNode = null;

    @FXML
    public void initialize() {
        timeLabel.setText("--");
        scoreLabel.setText("0");
        pairsLabel.setText("--");
        progressLabel.setText("--");
        operationLabel.setText("Click two tiles to select.");
        renderBoard(8, 8);
    }

    private void renderBoard(int rows, int cols) {
        gameGrid.getChildren().clear();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                StackPane cellWrapper = new StackPane();
                cellWrapper.getStyleClass().add("game-cell");

                int type = (int) (Math.random() * 5) + 1;
                var tile = new Rectangle(46, 46, colorForType(type));
                tile.setArcWidth(10);
                tile.setArcHeight(10);

                cellWrapper.getChildren().add(tile);

                var pos = new CellPos(r, c);
                cellWrapper.setOnMouseClicked(event -> handleCellClick(pos, cellWrapper));

                gameGrid.add(cellWrapper, c, r);
            }
        }
    }

    private void handleCellClick(CellPos pos, StackPane wrapper) {
        if (firstSelected == null) {
            firstSelected = pos;
            firstSelectedNode = wrapper;
            applySelectionEffect(wrapper, true);
            operationLabel.setText("Selected: (" + pos.row + ", " + pos.col + ")");
        } else {
            if (firstSelected.equals(pos)) {
                applySelectionEffect(wrapper, false);
                firstSelected = null;
                firstSelectedNode = null;
                operationLabel.setText("Selection cleared.");
            } else {
                operationLabel.setText("Picked second: (" + pos.row + ", " + pos.col + ")");

                applySelectionEffect(firstSelectedNode, false);
                firstSelected = null;
                firstSelectedNode = null;
            }
        }
    }

    private void applySelectionEffect(StackPane wrapper, boolean selected) {
        if (selected) {
            wrapper.setStyle("-fx-border-color: #E53935; -fx-border-width: 3; -fx-border-radius: 6;");
        } else {
            wrapper.setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
        }
    }

    private static Color colorForType(int type) {
        return switch (type) {
            case 1 -> Color.web("#FF7043");
            case 2 -> Color.web("#42A5F5");
            case 3 -> Color.web("#66BB6A");
            case 4 -> Color.web("#AB47BC");
            default -> Color.web("#FFD54F");
        };
    }

    @FXML
    public void handleRestart() {
        firstSelected = null;
        firstSelectedNode = null;
        operationLabel.setText("Restarted (GUI placeholder).");
        renderBoard(8, 8);
    }

    @FXML
    public void handleBackToMenu() {
        ViewManager.getInstance().goToMainMenu();
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
