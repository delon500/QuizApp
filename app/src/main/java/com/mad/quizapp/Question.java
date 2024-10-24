package com.mad.quizapp;

import java.util.List;

public class Question {
    private int questionId;
    private String questionText;
    private String category;
    private String difficulty;
    private List<Answer> answers;

    // Constructors
    public Question(String questionText, String category, String difficulty, List<Answer> answers){
        this.questionText = questionText;
        this.category=category;
        this.difficulty = difficulty;
        this.answers=answers;
    }

    public Question(int questionId,String questionText, String category, String difficulty, List<Answer> answers){
        this.questionId = questionId;
        this.questionText = questionText;
        this.category=category;
        this.difficulty = difficulty;
        this.answers=answers;
    }

    // Getters and setters
    public int getQuestionId() { return questionId; }
    public String getQuestionText() { return questionText; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public List<Answer> getAnswers() { return answers; }

}
