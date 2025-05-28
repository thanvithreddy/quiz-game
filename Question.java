package com.quizgame;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Question {
    @SerializedName("question")  // This ensures Gson serializes 'text' as 'question'
    private String text;
    private List<String> options;
    private int correctAnswerIndex;
    private String category;
    private String difficulty;

    public Question(String text, List<String> options, int correctAnswerIndex, String category, String difficulty) {
        this.text = text;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.category = category;
        this.difficulty = difficulty;
    }

    // Getters
    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }

    // Setters
    public void setText(String text) { this.text = text; }
    public void setOptions(List<String> options) { this.options = options; }
    public void setCorrectAnswerIndex(int correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }
    public void setCategory(String category) { this.category = category; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    @Override
    public String toString() {
        return String.format("Question{text='%s', category='%s', difficulty='%s'}",
                text, category, difficulty);
    }
}