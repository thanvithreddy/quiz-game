package com.quizgame;

import java.util.List;

public class GameSession {
    private String sessionId;
    private String playerName;
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private long startTime;
    private long endTime;

    public GameSession(String sessionId, String playerName, List<Question> questions) {
        this.sessionId = sessionId;
        this.playerName = playerName;
        this.questions = questions;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.startTime = System.currentTimeMillis();
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean submitAnswer(int answerIndex) {
        Question current = getCurrentQuestion();
        if (current != null && answerIndex == current.getCorrectAnswerIndex()) {
            score++;
            return true;
        }
        return false;
    }

    public void nextQuestion() {
        currentQuestionIndex++;
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questions.size();
    }

    public GameResult getResult() {
        endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        return new GameResult(score, questions.size(), timeTaken, playerName);
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public String getPlayerName() { return playerName; }
    public List<Question> getQuestions() { return questions; }
    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public int getScore() { return score; }
    public long getStartTime() { return startTime; }

    // Setters
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public void setCurrentQuestionIndex(int currentQuestionIndex) { this.currentQuestionIndex = currentQuestionIndex; }
    public void setScore(int score) { this.score = score; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
}