package com.mad.quizapp;

public class UserScore {
    private int scoreId;
    private int userId;
    private int score;
    private int totalQuestions;
    private int correctAnswers;
    private long quizDate;

    // Constructors
    public UserScore(){

    }

    public UserScore(int userId, int score, int totalQuestions, int correctAnswers, long quizDate) {
        this.userId = userId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.quizDate = quizDate;
    }

    public int getCorrectAnswers() {return correctAnswers;}
    public long getQuizDate() {return quizDate;}
    public int getScore() {return score;}
    public int getScoreId() {return scoreId;}
    public int getTotalQuestions() {return totalQuestions;}
    public int getUserId() {return userId;}

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
    public void setQuizDate(long quizDate) {
        this.quizDate = quizDate;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
