package com.quizgame;

public class GameResult {
    private int score;
    private int totalQuestions;
    private long timeTaken;
    private String playerName;
    private double percentage;

    public GameResult(int score, int totalQuestions, long timeTaken, String playerName) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timeTaken = timeTaken;
        this.playerName = playerName;
        this.percentage = totalQuestions > 0 ? (double) score / totalQuestions * 100 : 0;
    }

    // Getters
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public long getTimeTaken() { return timeTaken; }
    public String getPlayerName() { return playerName; }
    public double getPercentage() { return percentage; }

    // Setters
    public void setScore(int score) {
        this.score = score;
        this.percentage = totalQuestions > 0 ? (double) score / totalQuestions * 100 : 0;
    }
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
        this.percentage = totalQuestions > 0 ? (double) score / totalQuestions * 100 : 0;
    }
    public void setTimeTaken(long timeTaken) { this.timeTaken = timeTaken; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    @Override
    public String toString() {
        return String.format("GameResult{player='%s', score=%d/%d (%.1f%%), time=%dms}",
                playerName, score, totalQuestions, percentage, timeTaken);
    }
}