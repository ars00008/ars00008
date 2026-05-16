package com.MatchingGame.model;

public class SaveGameData {
    public String username;
    public String difficulty;
    public int rows;
    public int cols;
    public int[][] gameMap;
    public int currentScore;
    public int remainingTime;
    public int totalPairsEliminated;
    public int totalPairsCount;
    public long savedAt;

    public SaveGameData() {
    }

    public SaveGameData(String username, String difficulty, int rows, int cols, int[][] gameMap,
                        int currentScore, int remainingTime, int totalPairsEliminated,
                        int totalPairsCount, long savedAt) {
        this.username = username;
        this.difficulty = difficulty;
        this.rows = rows;
        this.cols = cols;
        this.gameMap = gameMap;
        this.currentScore = currentScore;
        this.remainingTime = remainingTime;
        this.totalPairsEliminated = totalPairsEliminated;
        this.totalPairsCount = totalPairsCount;
        this.savedAt = savedAt;
    }
}
