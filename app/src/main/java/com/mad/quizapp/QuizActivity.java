package com.mad.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    TextView questionTextView,progressTextView;
    Button ans_A_Button, ans_B_Button;
    Button ans_C_Button,ans_D_Button;
    DatabaseHelper databaseHelper;
    List<Question> questionlist;
    int currentQuestionIndex =0;
    int totalQuestions = 0;
    int correctAnswers = 0;
    Question currentQuestion;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionQuiz);
        progressTextView = findViewById(R.id.progressTextView);
        ans_A_Button = findViewById(R.id.ans_A);
        ans_B_Button = findViewById(R.id.ans_B);
        ans_C_Button = findViewById(R.id.ans_C);
        ans_D_Button = findViewById(R.id.ans_D);

        databaseHelper = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId == -1) {
            // User is not logged in, redirect to login screen
            Intent intent = new Intent(QuizActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        loadQuestion();

        ans_A_Button.setOnClickListener(v-> checkAnswer(ans_A_Button));
        ans_B_Button.setOnClickListener(v->checkAnswer(ans_B_Button));
        ans_C_Button.setOnClickListener(v->checkAnswer(ans_C_Button));
        ans_D_Button.setOnClickListener(v->checkAnswer(ans_D_Button));

    }

    private void loadQuestion(){
        try{
            questionlist = databaseHelper.getAllQuestions();
            // Check if questions are available
            if(questionlist!=null && !questionlist.isEmpty()){
                totalQuestions = questionlist.size();
                Collections.shuffle(questionlist);

                // Display the first question
                displayQuestion();
            }else{
                // No questions available
                questionTextView.setText("No questions available.");
                disableAnswersButton();
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void displayQuestion() {
        if (currentQuestionIndex < totalQuestions) {
            currentQuestion = questionlist.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.getQuestionText());
            progressTextView.setText("Question " + (currentQuestionIndex + 1) + " of " + totalQuestions);


            List<Answer> answers = currentQuestion.getAnswers();

            if (answers == null || answers.isEmpty()) {
                Toast.makeText(this, "No answers available for this question.", Toast.LENGTH_SHORT).show();
                // Skip to the next question
                currentQuestionIndex++;
                displayQuestion();
                return;
            }

            Collections.shuffle(answers);

            // Hide all answer buttons initially
            ans_A_Button.setVisibility(View.GONE);
            ans_B_Button.setVisibility(View.GONE);
            ans_C_Button.setVisibility(View.GONE);
            ans_D_Button.setVisibility(View.GONE);

            // Display the available answers
            int answerCount = answers.size();
            if (answerCount > 0) {
                ans_A_Button.setText(answers.get(0).getAnswerText());
                ans_A_Button.setVisibility(View.VISIBLE);
            }
            if (answerCount > 1) {
                ans_B_Button.setText(answers.get(1).getAnswerText());
                ans_B_Button.setVisibility(View.VISIBLE);
            }
            if (answerCount > 2) {
                ans_C_Button.setText(answers.get(2).getAnswerText());
                ans_C_Button.setVisibility(View.VISIBLE);
            }
            if (answerCount > 3) {
                ans_D_Button.setText(answers.get(3).getAnswerText());
                ans_D_Button.setVisibility(View.VISIBLE);
            }

            // Enable buttons
            enableAnswersButtons();
        } else {
            finishQuiz();
        }
    }


    private void finishQuiz() {
        int scorePercentage = (correctAnswers*100)/totalQuestions;

        // Show the final score to the user
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Completed");
        builder.setMessage("Your Score: "+correctAnswers+" out of "+totalQuestions + "\nPercentage: " + scorePercentage + "%");
        builder.setPositiveButton("OK",(dialog,which) -> {
            saveUserScore();

            // Navigate to the score summary
            Intent intent = new Intent(QuizActivity.this, ScoreSummaryActivity.class);
            intent.putExtra("CORRECT_ANSWERS",correctAnswers);
            intent.putExtra("TOTAL_QUESTIONS",totalQuestions);
            startActivity(intent);
            finish();
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void checkAnswer(Button selectedButton){
        // Disable buttons to prevent multiple clicks
        disableAnswersButton();

        String selectedAnswerText = selectedButton.getText().toString();
        boolean isCorrect = false;

        for (Answer answer: currentQuestion.getAnswers()){
            if(answer.getAnswerText().equals(selectedAnswerText)){
                isCorrect = answer.isCorrect();
                break;
            }
        }
        if(isCorrect){
            correctAnswers++;
            selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_answer_color));
            Toast.makeText(this, "Correct",Toast.LENGTH_SHORT).show();
        }else{
            selectedButton.setBackgroundColor(ContextCompat.getColor(this,R.color.incorrect_answer_color));
            Toast.makeText(this,"Incorrect",Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(()->{
           currentQuestionIndex++;
           displayQuestion();
        },1000);
    }

    private void resetAnswerButtons() {
        // Reset button background colors to default
        ans_A_Button.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color));
        ans_B_Button.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color));
        ans_C_Button.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color));
        ans_D_Button.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color));
    }

    private void saveUserScore() {
        UserScore userScore = new UserScore();
        userScore.setUserId(userId);
        userScore.setScore(correctAnswers);
        userScore.setTotalQuestions(totalQuestions);
        userScore.setCorrectAnswers(correctAnswers);
        userScore.setQuizDate(System.currentTimeMillis());

        databaseHelper.insertUserScore(userScore);
    }

    private void disableAnswersButton() {
        ans_A_Button.setEnabled(false);
        ans_B_Button.setEnabled(false);
        ans_C_Button.setEnabled(false);
        ans_D_Button.setEnabled(false);
    }

    private void enableAnswersButtons() {
        ans_A_Button.setEnabled(true);
        ans_B_Button.setEnabled(true);
        ans_C_Button.setEnabled(true);
        ans_D_Button.setEnabled(true);
    }
}