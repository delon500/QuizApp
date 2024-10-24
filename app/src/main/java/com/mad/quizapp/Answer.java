package com.mad.quizapp;

public class Answer {
    private int answerId, questionId;
    private String answerText;
    private boolean isCorrect;

    public Answer(String answerText, boolean isCorrect){
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    public Answer(int answerId, int questionId, String answerText, boolean isCorrect){
        this.answerId = answerId;
        this.questionId = questionId;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    // Getters and setters
    public int getAnswerId() { return answerId;}
    public int getQuestionId() { return questionId;}
    public String getAnswerText() { return answerText;}
    public boolean isCorrect() { return isCorrect;}
}
