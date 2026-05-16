package com.MatchingGame.model;

public class GameResult {
    public String username;
    public int score;
    public String mode;
    public boolean victory;
    public long finishedAt;

    public GameResult() {
    }

    public GameResult(String username, int score, String mode, boolean victory, long finishedAt) {
        this.username = username;
        this.score = score;
        this.mode = mode;
        this.victory = victory;
        this.finishedAt = finishedAt;
    }
}
